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

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
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
	 * The SOAP connector.
	 */
	private final TuleapSoapConnector soapConnector;

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	/**
	 * The SOAP parser.
	 */
	private TuleapSoapParser tuleapSoapParser;

	/**
	 * The SOAP serializer.
	 */
	private TuleapSoapSerializer tuleapSoapSerializer;

	/**
	 * The logger.
	 */
	private ILog logger;

	/**
	 * The constructor.
	 * 
	 * @param repository
	 *            The task repository
	 * @param tuleapSoapConnector
	 *            The Tuleap SOAP connector
	 * @param tuleapSoapParser
	 *            The Tuleap SOAP parser
	 * @param tuleapSoapSerializer
	 *            The Tuleap SOAP serializer
	 * @param logger
	 *            the logger
	 */
	public TuleapSoapClient(TaskRepository repository, TuleapSoapConnector tuleapSoapConnector,
			TuleapSoapParser tuleapSoapParser, TuleapSoapSerializer tuleapSoapSerializer, ILog logger) {
		this.soapConnector = tuleapSoapConnector;
		this.taskRepository = repository;
		this.tuleapSoapParser = tuleapSoapParser;
		this.tuleapSoapSerializer = tuleapSoapSerializer;
		this.logger = logger;
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
			IStatus status = soapConnector.validateConnection(monitor);
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
	 * Returns the configuration of the Tuleap server.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The configuration of the server
	 * @throws CoreException
	 *             In case of error during the retrieval of the configuration
	 */
	public TuleapServer getTuleapServerConfiguration(IProgressMonitor monitor)
			throws CoreException {
		return soapConnector.getTuleapServerConfiguration(monitor);
	}

	/**
	 * Returns the configuration of the tracker with the given identifier.
	 * 
	 * @param projectConfiguration
	 *            The configuration of the project
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param monitor
	 *            The progress monitor
	 * @return The configuration of the tracker with the given identifier
	 */
	public TuleapTracker getTuleapTrackerConfiguration(
			TuleapProject projectConfiguration, int trackerId, IProgressMonitor monitor) {
		return this.soapConnector.getTuleapTrackerConfiguration(projectConfiguration.getIdentifier(),
				trackerId, monitor);
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
			TuleapServer serverConfiguration,
			TuleapTracker tuleapTracker, IProgressMonitor monitor) {
		List<TuleapArtifact> artifacts = new ArrayList<TuleapArtifact>();

		List<CommentedArtifact> artifactsToConvert = soapConnector.performQuery(query, serverConfiguration,
				TaskDataCollector.MAX_HITS, monitor);
		for (CommentedArtifact artifactToParse : artifactsToConvert) {
			TuleapArtifact tuleapArtifact = this.tuleapSoapParser.parseArtifact(tuleapTracker,
					artifactToParse);
			artifacts.add(tuleapArtifact);
		}

		return artifacts;
	}

	/**
	 * Retrieves the artifact with the given id.
	 * 
	 * @param taskId
	 *            The identifier of the artifact
	 * @param serverConfiguration
	 *            The configuration of the Tuleap server
	 * @param monitor
	 *            The progress monitor
	 * @return The Tuleap Artifact with the data from the server
	 * @throws CoreException
	 *             In case of issue during the retrieval of the artifact
	 */
	public TuleapArtifact getArtifact(String taskId, TuleapServer serverConfiguration,
			IProgressMonitor monitor) throws CoreException {
		int artifactId = TuleapTaskIdentityUtil.getElementIdFromTaskDataId(taskId);

		if (artifactId != TuleapTaskIdentityUtil.IRRELEVANT_ID) {
			TuleapArtifact tuleapArtifact;
			try {
				CommentedArtifact artifact = soapConnector.getArtifact(artifactId, serverConfiguration,
						monitor);

				tuleapArtifact = this.tuleapSoapParser.parseArtifact(serverConfiguration
						.getTracker(artifact.getArtifact().getTracker_id()), artifact);
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
			return tuleapArtifact;
		}
		return null;
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
	public String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws CoreException {
		String taskDataId;
		try {
			taskDataId = soapConnector.createArtifact(artifact, monitor);
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
		return taskDataId;
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
			soapConnector.updateArtifact(artifact, monitor);
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
