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
package org.eclipse.mylyn.internal.tuleap.core.net;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.config.ITuleapConfigurationConstants;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapArtifactLink;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapInteger;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapOpenList;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapPermissionOnArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapText;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapArtifactId;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapBurndownChart;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapCrossReferences;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapLastUpdateDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedBy;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedOn;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.TuleapSoapServiceLocator;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.TuleapTrackerV5APILocatorImpl;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.CodendiAPIPortType;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Session;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APILocator;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * This class will be used to connect Mylyn to the SOAP services provided by the Tuleap instance.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapSoapConnector {
	/**
	 * The location of the configuration file.
	 */
	private static final String CONFIG_FILE = "org/eclipse/mylyn/internal/tuleap/core/wsdl/soap/client-config.wsdd"; //$NON-NLS-1$

	/**
	 * The location of the tracker.
	 */
	private AbstractWebLocation trackerLocation;

	/**
	 * The constructor.
	 * 
	 * @param location
	 *            The location of the tracker.
	 */
	public TuleapSoapConnector(AbstractWebLocation location) {
		this.trackerLocation = location;
	}

	/**
	 * Validates the connection to the repository.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return A status indicating if the connection has been successfully tested.
	 */
	public IStatus validateConnection(IProgressMonitor monitor) {
		monitor.beginTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.ValidateConnection"), 100); //$NON-NLS-1$
		IStatus status = Status.OK_STATUS;

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			final int fifty = 50;
			monitor.worked(fifty);
			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.Login")); //$NON-NLS-1$

			URL url = new URL(ITuleapConstants.SOAP_V1_URL);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();

			monitor.worked(fifty);

			codendiAPIPort.logout(sessionHash);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
			status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
			status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
			status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
		}

		return status;
	}

	/**
	 * Returns the configuration of the Tuleap instance.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return The configuration of the Tuleap instance.
	 */
	public TuleapInstanceConfiguration getTuleapInstanceConfiguration(IProgressMonitor monitor) {
		TuleapInstanceConfiguration tuleapInstanceConfiguration = new TuleapInstanceConfiguration(
				this.trackerLocation.getUrl());
		monitor.beginTask(TuleapMylynTasksMessages
				.getString("TuleapSoapConnector.RetrieveTuleapInstanceConfiguration"), 100); //$NON-NLS-1$

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			monitor.worked(1);
			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.Login")); //$NON-NLS-1$

			URL url = new URL(ITuleapConstants.SOAP_V1_URL);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();
			monitor.worked(5);

			int groupId = TuleapUtil.getGroupId(this.trackerLocation.getUrl());

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
					this.trackerLocation);
			url = new URL(ITuleapConstants.SOAP_V2_URL);

			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);

			monitor.worked(1);
			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.RetrievingTrackersList")); //$NON-NLS-1$
			Tracker[] trackers = tuleapTrackerV5APIPort.getTrackerList(sessionHash, groupId);

			for (Tracker tracker : trackers) {
				monitor.worked(10);
				monitor.setTaskName(TuleapMylynTasksMessages.getString(
						"TuleapSoapConnector.AnalyzingTracker", tracker.getName())); //$NON-NLS-1$
				TuleapTrackerConfiguration tuleapTrackerConfiguration = this.getTuleapTrackerConfiguration(
						tuleapTrackerV5APIPort, sessionHash, tracker, monitor);
				tuleapInstanceConfiguration.addTracker(Integer.valueOf(tracker.getTracker_id()),
						tuleapTrackerConfiguration);
			}

			codendiAPIPort.logout(sessionHash);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}
		return tuleapInstanceConfiguration;
	}

	/**
	 * Returns the configuration of the given tracker.
	 * 
	 * @param tuleapTrackerV5APIPort
	 *            The SOAP entry point
	 * @param sessionHash
	 *            The hash of the current session
	 * @param tracker
	 *            The Tuleap tracker
	 * @param monitor
	 *            The progress monitor
	 * @return The configuration of the Tuleap tracker.
	 */
	private TuleapTrackerConfiguration getTuleapTrackerConfiguration(
			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort, String sessionHash, Tracker tracker,
			IProgressMonitor monitor) {
		String trackerURL = this.trackerLocation.getUrl() + ITuleapConstants.REPOSITORY_TRACKER_URL_SEPARATOR
				+ Integer.valueOf(tracker.getTracker_id()).toString();
		TuleapTrackerConfiguration tuleapTrackerConfiguration = new TuleapTrackerConfiguration(tracker
				.getTracker_id(), trackerURL);

		tuleapTrackerConfiguration.setName(tracker.getName());
		tuleapTrackerConfiguration.setItemName(tracker.getItem_name());
		tuleapTrackerConfiguration.setDescription(tracker.getDescription());
		tuleapTrackerConfiguration.setLastUpdate(System.currentTimeMillis());

		try {
			monitor.subTask(TuleapMylynTasksMessages.getString(
					"TuleapSoapConnector.RetrievingFieldsDescriptionFromTracker", tracker.getName())); //$NON-NLS-1$
			TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, tracker
					.getGroup_id(), tracker.getTracker_id());
			monitor.worked(10);

			for (TrackerField trackerField : trackerFields) {
				AbstractTuleapField tuleapField = null;

				String type = trackerField.getType();
				int fieldIdentifier = trackerField.getField_id();
				if (ITuleapConfigurationConstants.STRING.equals(type)) {
					tuleapField = new TuleapString(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.TEXT.equals(type)) {
					tuleapField = new TuleapText(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.AID.equals(type)) {
					tuleapField = new TuleapArtifactId(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.LUD.equals(type)) {
					tuleapField = new TuleapLastUpdateDate(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.SB.equals(type)) {
					tuleapField = new TuleapSelectBox(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.MSB.equals(type)) {
					tuleapField = new TuleapMultiSelectBox(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.DATE.equals(type)) {
					tuleapField = new TuleapDate(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.FILE.equals(type)) {
					tuleapField = new TuleapFileUpload(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.CROSS.equals(type)) {
					tuleapField = new TuleapCrossReferences(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.ARTLINK.equals(type)) {
					tuleapField = new TuleapArtifactLink(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.SUBBY.equals(type)) {
					tuleapField = new TuleapSubmittedBy(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.SUBON.equals(type)) {
					tuleapField = new TuleapSubmittedOn(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.INT.equals(type)) {
					tuleapField = new TuleapInteger(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.BURNDOWN.equals(type)) {
					tuleapField = new TuleapBurndownChart(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.PERM.equals(type)) {
					tuleapField = new TuleapPermissionOnArtifact(fieldIdentifier);
				} else if (ITuleapConfigurationConstants.TBL.equals(type)) {
					tuleapField = new TuleapOpenList(fieldIdentifier);
				} else {
					TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
							"TuleapSoapConnector.UnsupportedTrackerFieldType", type), true); //$NON-NLS-1$
				}
				if (tuleapField != null) {
					tuleapField.setName(trackerField.getShort_name());
					tuleapField.setLabel(trackerField.getLabel());
					tuleapTrackerConfiguration.getFields().add(tuleapField);
				}
			}
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}

		return tuleapTrackerConfiguration;
	}

	/**
	 * Performs the given query on the Tuleap tracker, collects the task data resulting from the evaluation of
	 * the query and return the number of tasks found.
	 * 
	 * @param collector
	 *            The task data collector
	 * @param mapper
	 *            The task attribute mapper
	 * @param maxHits
	 *            The maximum number of tasks that should be processed
	 * @return The number of tasks processed
	 */
	public int performQuery(TaskDataCollector collector, TaskAttributeMapper mapper, int maxHits) {
		// TODO Evaluate the tasks on the server

		return 0;
	}
}
