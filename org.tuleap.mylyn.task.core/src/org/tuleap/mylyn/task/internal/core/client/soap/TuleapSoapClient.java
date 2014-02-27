/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.client.soap;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;

/**
 * The Mylyn Tuleap client is in charge of the connection with the repository and it will realize the request
 * in order to obtain and publish the tasks.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapSoapClient {

	/**
	 * The repository location.
	 */
	private final AbstractWebLocation webLocation;

	/**
	 * The SOAP parser.
	 */
	private TuleapSoapParser tuleapSoapParser;

	/**
	 * The constructor.
	 * 
	 * @param webLocation
	 *            The repository location
	 * @param tuleapSoapParser
	 *            The SOAP parser
	 */
	public TuleapSoapClient(AbstractWebLocation webLocation, TuleapSoapParser tuleapSoapParser) {
		this.webLocation = webLocation;
		this.tuleapSoapParser = tuleapSoapParser;
		ensureProxySettingsRegistration();
	}

	/**
	 * Creates and return a new connector. This is necessary because the {@link TuleapSoapConnector} class is
	 * not thread-safe.
	 * 
	 * @return A new instance of {@link TuleapSoapConnector}.
	 */
	private TuleapSoapConnector newConnector() {
		return new TuleapSoapConnector(webLocation);
	}

	/**
	 * Ensures that the settings of the proxy are correctly registered in the Axis properties.
	 */
	private void ensureProxySettingsRegistration() {
		String url = webLocation.getUrl();
		Proxy proxy = webLocation.getProxyForHost(url, "HTTP"); //$NON-NLS-1$
		if (proxy == null) {
			proxy = webLocation.getProxyForHost(url, "HTTPS"); //$NON-NLS-1$
		}
		if (proxy != null) {
			SocketAddress address = proxy.address();
			if (address instanceof InetSocketAddress) {
				InetSocketAddress inetSocketAddress = (InetSocketAddress)address;
				int port = inetSocketAddress.getPort();
				String hostName = inetSocketAddress.getHostName();
				AxisProperties.setProperty("http.proxyHost", hostName); //$NON-NLS-1$
				AxisProperties.setProperty("http.proxyPort", Integer.valueOf(port).toString()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Validate the credentials with the server.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return <code>true</code> if the credentials are valid and if the URL is valid, <code>false</code>
	 *         otherwise
	 * @throws CoreException
	 *             In case of error during the validation of the connection
	 */
	public IStatus validateConnection(IProgressMonitor monitor) throws CoreException {
		try {
			IStatus status = newConnector().validateConnection(monitor);
			return status;
		} catch (MalformedURLException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (RemoteException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}

	/**
	 * Retrieves the {@link TuleapArtifact} from a query run on the server.
	 * 
	 * @param query
	 *            The query to run
	 * @param serverConfiguration
	 *            The server configuration
	 * @param tuleapTracker
	 *            The configuration used to analyze the data from the SOAP responses
	 * @param monitor
	 *            the progress monitor
	 * @return The list of the Tuleap artifact
	 */
	public List<TuleapArtifact> getArtifactsFromQuery(IRepositoryQuery query,
			TuleapServer serverConfiguration, TuleapTracker tuleapTracker, IProgressMonitor monitor) {
		List<TuleapArtifact> artifacts = new ArrayList<TuleapArtifact>();

		List<CommentedArtifact> artifactsToConvert = newConnector().performQuery(query, serverConfiguration,
				TaskDataCollector.MAX_HITS, monitor);
		for (CommentedArtifact artifactToParse : artifactsToConvert) {
			TuleapArtifact tuleapArtifact = this.tuleapSoapParser.parseArtifact(tuleapTracker,
					artifactToParse);
			artifacts.add(tuleapArtifact);
		}

		return artifacts;
	}

	/**
	 * Creates the Tuleap artifact.
	 * 
	 * @param artifact
	 *            The Tuleap artifact
	 * @param monitor
	 *            The monitor
	 * @return The identifier of the artifact
	 * @throws CoreException
	 *             In case of issue during the creation of the artifact
	 */
	public TuleapTaskId createArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
			throws CoreException {
		try {
			return newConnector().createArtifact(artifact, monitor);
		} catch (RemoteException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (MalformedURLException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}

	/**
	 * Updates the artifact.
	 * 
	 * @param artifact
	 *            The artifact to update
	 * @param monitor
	 *            The progress monitor
	 * @throws CoreException
	 *             In case of issue during the update
	 */
	public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws CoreException {
		try {
			newConnector().updateArtifact(artifact, monitor);
		} catch (MalformedURLException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (RemoteException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}
}
