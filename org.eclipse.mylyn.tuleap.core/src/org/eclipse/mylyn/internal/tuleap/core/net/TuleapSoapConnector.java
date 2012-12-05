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

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.config.ITuleapConfigurationConstants;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapArtifactLink;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFloat;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapInteger;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapOpenList;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapPermissionOnArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapText;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapArtifactId;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapBurndownChart;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapComputedValue;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapCrossReferences;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapLastUpdateDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedBy;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedOn;
import org.eclipse.mylyn.internal.tuleap.core.model.workflow.TuleapWorkflow;
import org.eclipse.mylyn.internal.tuleap.core.model.workflow.TuleapWorkflowTransition;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapTaskDataHandler;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.TuleapSoapServiceLocator;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.TuleapTrackerV5APILocatorImpl;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.CodendiAPIPortType;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Group;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Session;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.CriteriaValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.CriteriaValueDate;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemantic;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticContributor;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticStatus;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticTitle;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflow;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowTransition;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APILocator;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * This class will be used to connect Mylyn to the SOAP services provided by the Tuleap instance.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 1.0
 */
public class TuleapSoapConnector {
	/**
	 * The location of the configuration file.
	 */
	private static final String CONFIG_FILE = "org/eclipse/mylyn/internal/tuleap/core/wsdl/soap/client-config.wsdd"; //$NON-NLS-1$

	/**
	 * The login message.
	 */
	private static final String LOGIN_MESSAGE = TuleapMylynTasksMessages
			.getString("TuleapSoapConnector.Login"); //$NON-NLS-1$

	/**
	 * The validate connection message.
	 */
	private static final String VALIDATE_CONNECTION_MESSAGE = TuleapMylynTasksMessages
			.getString("TuleapSoapConnector.ValidateConnection"); //$NON-NLS-1$

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
		monitor.beginTask(VALIDATE_CONNECTION_MESSAGE, 100);
		IStatus status = Status.OK_STATUS;

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		this.ensureProxySettingsRegistration();

		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
			soapv1url = soapv1url + ITuleapConstants.SOAP_V1_URL;
		}

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			final int fifty = 50;
			monitor.worked(fifty);
			monitor.subTask(LOGIN_MESSAGE);

			URL url = new URL(soapv1url);
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
	 * Ensures that the settings of the proxy are correctly registered in the Axis properties.
	 */
	private void ensureProxySettingsRegistration() {
		// FIXME Register the setting of the proxy

		String url = this.trackerLocation.getUrl();
		Proxy proxy = this.trackerLocation.getProxyForHost(url, "HTTP"); //$NON-NLS-1$
		if (proxy == null) {
			proxy = this.trackerLocation.getProxyForHost(url, "HTTPS"); //$NON-NLS-1$
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

		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
			soapv1url = soapv1url + ITuleapConstants.SOAP_V1_URL;
		}

		String soapv2url = trackerLocation.getUrl();
		index = soapv2url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv2url = soapv2url.substring(0, index);
			soapv2url = soapv2url + ITuleapConstants.SOAP_V2_URL;
		}

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			monitor.worked(1);
			monitor.subTask(LOGIN_MESSAGE);

			URL url = new URL(soapv1url);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();
			monitor.worked(5);

			int groupId = TuleapUtil.getGroupId(this.trackerLocation.getUrl());

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
					this.trackerLocation);
			url = new URL(soapv2url);

			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);

			monitor.worked(1);
			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.RetrievingTrackersList")); //$NON-NLS-1$
			Tracker[] trackers = tuleapTrackerV5APIPort.getTrackerList(sessionHash, groupId);

			for (Tracker tracker : trackers) {
				monitor.worked(5);
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
			monitor.worked(5);

			TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash,
					tracker.getGroup_id(), tracker.getTracker_id());
			for (TrackerField trackerField : trackerFields) {
				AbstractTuleapField tuleapField = getTuleapTrackerField(trackerStructure, trackerField,
						sessionHash, monitor);
				monitor.worked(1);
				if (tuleapField != null) {
					tuleapField.setName(trackerField.getShort_name());
					tuleapField.setLabel(trackerField.getLabel());
					tuleapField.setPermissions(trackerField.getPermissions());
					tuleapTrackerConfiguration.getFields().add(tuleapField);
				}
			}
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}

		return tuleapTrackerConfiguration;
	}

	/**
	 * Returns the tuleap tracker field for the given tracker field.
	 * 
	 * @param trackerStructure
	 *            The structure of the tracker
	 * @param trackerField
	 *            The tracker field
	 * @param sessionHash
	 *            The session hash
	 * @param monitor
	 *            The progress monitor
	 * @return The tuleap tracker field
	 */
	private AbstractTuleapField getTuleapTrackerField(TrackerStructure trackerStructure,
			TrackerField trackerField, String sessionHash, IProgressMonitor monitor) {
		AbstractTuleapField tuleapField = null;

		monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.AnalyzeTuleapTrackerField", //$NON-NLS-1$
				trackerField.getLabel()));

		TrackerSemantic trackerSemantic = trackerStructure.getSemantic();
		TrackerSemanticTitle trackerSemanticTitle = trackerSemantic.getTitle();
		String trackerSemanticTitleFieldName = trackerSemanticTitle.getField_name();
		TrackerSemanticStatus trackerSemanticStatus = trackerSemantic.getStatus();
		int[] trackerSemanticStatusOpenValues = trackerSemanticStatus.getValues();

		TrackerSemanticContributor trackerSemanticContributor = trackerSemantic.getContributor();
		String trackerSemanticContributorFieldName = trackerSemanticContributor.getField_name();

		TrackerWorkflow trackerWorkflow = trackerStructure.getWorkflow();
		TrackerWorkflowTransition[] trackerWorkflowTransitions = trackerWorkflow.getTransitions();

		String type = trackerField.getType();
		int fieldIdentifier = trackerField.getField_id();
		if (ITuleapConfigurationConstants.STRING.equals(type)) {
			tuleapField = new TuleapString(fieldIdentifier);
			if (trackerSemanticTitleFieldName.equals(trackerField.getShort_name())) {
				((TuleapString)tuleapField).setSemanticTitle(true);
			}
		} else if (ITuleapConfigurationConstants.TEXT.equals(type)) {
			tuleapField = new TuleapText(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.AID.equals(type)) {
			tuleapField = new TuleapArtifactId(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.LUD.equals(type)) {
			tuleapField = new TuleapLastUpdateDate(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.SB.equals(type)) {
			tuleapField = new TuleapSelectBox(fieldIdentifier);
			if (trackerWorkflow.getField_id() == fieldIdentifier) {
				// Workflow
				TuleapWorkflow tuleapWorkflow = ((TuleapSelectBox)tuleapField).getWorkflow();
				for (TrackerWorkflowTransition trackerTransition : trackerWorkflowTransitions) {
					TuleapWorkflowTransition tuleapTransition = new TuleapWorkflowTransition();
					tuleapTransition.setFrom(trackerTransition.getFrom_id());
					tuleapTransition.setTo(trackerTransition.getTo_id());
					tuleapWorkflow.getTransitions().add(tuleapTransition);
				}
			}

			for (TrackerFieldBindValue trackerFieldBindValue : trackerField.getValues()) {
				TuleapSelectBoxItem tuleapSelectBoxItem = new TuleapSelectBoxItem(trackerFieldBindValue
						.getBind_value_id());
				tuleapSelectBoxItem.setLabel(trackerFieldBindValue.getBind_value_label());
				((TuleapSelectBox)tuleapField).getItems().add(tuleapSelectBoxItem);

				// Semantic status
				if (trackerField.getShort_name().equals(trackerSemanticStatus.getField_name())) {
					if (ArrayUtils.contains(trackerSemanticStatusOpenValues, trackerFieldBindValue
							.getBind_value_id())) {
						((TuleapSelectBox)tuleapField).getOpenStatus().add(tuleapSelectBoxItem);
					}
				}
			}
		} else if (ITuleapConfigurationConstants.MSB.equals(type)) {
			tuleapField = new TuleapMultiSelectBox(fieldIdentifier);
			if (trackerSemanticContributorFieldName.equals(trackerField.getShort_name())) {
				((TuleapMultiSelectBox)tuleapField).setSemanticContributor(true);
			}
			for (TrackerFieldBindValue trackerFieldBindValue : trackerField.getValues()) {
				TuleapSelectBoxItem tuleapSelectBoxItem = new TuleapSelectBoxItem(trackerFieldBindValue
						.getBind_value_id());
				tuleapSelectBoxItem.setLabel(trackerFieldBindValue.getBind_value_label());
				((TuleapMultiSelectBox)tuleapField).getItems().add(tuleapSelectBoxItem);
				if (ArrayUtils.contains(trackerSemanticStatusOpenValues, trackerFieldBindValue
						.getBind_value_id())) {
					((TuleapSelectBox)tuleapField).getOpenStatus().add(tuleapSelectBoxItem);
				}
			}
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
		} else if (ITuleapConfigurationConstants.FLOAT.equals(type)) {
			tuleapField = new TuleapFloat(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.COMPUTED.equals(type)) {
			tuleapField = new TuleapComputedValue(fieldIdentifier);
		} else {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
					"TuleapSoapConnector.UnsupportedTrackerFieldType", type), true); //$NON-NLS-1$
		}

		monitor.worked(1);

		return tuleapField;
	}

	/**
	 * Performs the given query on the Tuleap tracker, collects the task data resulting from the evaluation of
	 * the query and return the number of tasks found.
	 * 
	 * @param query
	 *            The query to run
	 * @param collector
	 *            The task data collector
	 * @param mapper
	 *            The task attribute mapper
	 * @param taskDataHandler
	 *            The task data handler
	 * @param tuleapClient
	 *            The Tuleap client
	 * @param maxHits
	 *            The maximum number of tasks that should be processed
	 * @param monitor
	 *            The progress monitor
	 * @return The number of tasks processed
	 */
	public int performQuery(IRepositoryQuery query, TaskDataCollector collector, TaskAttributeMapper mapper,
			TuleapTaskDataHandler taskDataHandler, ITuleapClient tuleapClient, int maxHits,
			IProgressMonitor monitor) {
		ArtifactQueryResult artifactQueryResult = null;

		monitor.beginTask(VALIDATE_CONNECTION_MESSAGE, 100);

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
			soapv1url = soapv1url + ITuleapConstants.SOAP_V1_URL;
		}

		String soapv2url = trackerLocation.getUrl();
		index = soapv2url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv2url = soapv2url.substring(0, index);
			soapv2url = soapv2url + ITuleapConstants.SOAP_V2_URL;
		}

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			final int fifty = 50;
			monitor.worked(fifty);
			monitor.subTask(LOGIN_MESSAGE);

			URL url = new URL(soapv1url);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();

			monitor.worked(5);

			int groupId = TuleapUtil.getGroupId(this.trackerLocation.getUrl());

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
					this.trackerLocation);
			url = new URL(soapv2url);

			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.ExecutingQuery")); //$NON-NLS-1$
			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);

			List<Criteria> criterias = new ArrayList<Criteria>();
			if (ITuleapConstants.QUERY_KIND_ALL_FROM_TRACKER.equals(query
					.getAttribute(ITuleapConstants.QUERY_KIND))
					&& query.getAttribute(ITuleapConstants.QUERY_TRACKER_ID) != null) {
				// Download all artifacts from the given tracker -> no criteria
			} else if (ITuleapConstants.QUERY_KIND_CUSTOM.equals(query
					.getAttribute(ITuleapConstants.QUERY_KIND))
					&& query.getAttribute(ITuleapConstants.QUERY_TRACKER_ID) != null) {
				// Custom query
				criterias.addAll(this.getCriterias(query));
			}

			try {
				String queryTrackerId = query.getAttribute(ITuleapConstants.QUERY_TRACKER_ID);
				int trackerId = Integer.valueOf(queryTrackerId).intValue();
				artifactQueryResult = tuleapTrackerV5APIPort.getArtifacts(sessionHash, groupId, trackerId,
						criterias.toArray(new Criteria[criterias.size()]), 0, maxHits);
				Artifact[] artifacts = artifactQueryResult.getArtifacts();

				monitor.worked(fifty);

				TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, groupId,
						trackerId);

				Group group = codendiAPIPort.getGroupById(sessionHash, groupId);
				String projectName = group.getGroup_name();
				String trackerName = null;
				Tracker[] trackers = tuleapTrackerV5APIPort.getTrackerList(sessionHash, groupId);
				for (Tracker tracker : trackers) {
					if (trackerId == tracker.getTracker_id()) {
						trackerName = tracker.getName();
						break;
					}
				}

				for (Artifact artifact : artifacts) {
					TuleapArtifact tuleapArtifact = new TuleapArtifact(artifact, trackerName, projectName);
					ArtifactFieldValue[] value = artifact.getValue();

					for (TrackerField trackerField : trackerFields) {
						boolean found = false;
						for (ArtifactFieldValue artifactFieldValue : value) {
							if (artifactFieldValue.getField_name().equals(trackerField.getShort_name())
									&& artifactFieldValue.getField_label().equals(trackerField.getLabel())) {
								tuleapArtifact.putValue(artifactFieldValue.getField_name(),
										artifactFieldValue.getField_value());
								monitor.worked(1);
								found = true;
							}
						}

						if (!found) {
							// The value is not set in the artifact
							// Let's create an empty entry in the artifact

							tuleapArtifact.putValue(trackerField.getShort_name(), null);
							monitor.worked(1);
						}
					}
					monitor.worked(5);

					TaskData taskData = taskDataHandler.createTaskDataFromArtifact(tuleapClient, tuleapClient
							.getTaskRepository(), tuleapArtifact, monitor);
					collector.accept(taskData);
				}
			} catch (NumberFormatException e) {
				TuleapCoreActivator.log(e, true);
			}

			monitor.worked(fifty);

			codendiAPIPort.logout(sessionHash);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}

		if (artifactQueryResult != null) {
			return artifactQueryResult.getTotal_artifacts_number();
		}

		return 0;
	}

	/**
	 * Returns the list of the criteria to take into account for a custom query.
	 * 
	 * @param query
	 *            The query from which we have to extract the criteria
	 * @return The list of the criteria to take into account for a custom query.
	 */
	private List<Criteria> getCriterias(IRepositoryQuery query) {
		List<Criteria> criterias = new ArrayList<Criteria>();
		for (Entry<String, String> entry : query.getAttributes().entrySet()) {
			if (!(entry.getKey().equals(ITuleapConstants.QUERY_KIND) || entry.getKey().equals(
					ITuleapConstants.QUERY_TRACKER_ID))) {
				String[] attributes = this.getAttributes(entry.getValue());
				CriteriaValueDate criteriaValueDate = null;
				// First attribute is the value
				String value = attributes[0];

				// If it exists a second attribute it means that we have to do a query on date and have
				// to get the operation
				if (attributes.length == 2) {
					String operation = attributes[1];
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy"); //$NON-NLS-1$
					try {
						criteriaValueDate = new CriteriaValueDate(operation, Long.valueOf(
								TuleapUtil.parseDate(dateFormat.parse(value))).intValue());
					} catch (ParseException e) {
						TuleapCoreActivator.log(e, true);
					}
				}

				CriteriaValue attributeValue = new CriteriaValue(value, criteriaValueDate, null);
				Criteria criteria = new Criteria(entry.getKey(), attributeValue);
				criterias.add(criteria);
			}
		}
		return criterias;
	}

	/**
	 * Get attributes.
	 * 
	 * @param attributes
	 *            Attributes
	 * @return Array of attributes values
	 */
	private String[] getAttributes(String attributes) {
		return attributes.split(ITuleapConstants.QUERY_ATTRIBUTES_SEPARATOR);
	}

	/**
	 * Returns the Tuleap Artifact with the given artifact id on the tracker with the given tracker id.
	 * 
	 * @param trackerId
	 *            The tracer id
	 * @param artifactId
	 *            The artifact id
	 * @param monitor
	 *            The progress monitor
	 * @return The Tuleap Artifact
	 */
	public TuleapArtifact getArtifact(int trackerId, int artifactId, IProgressMonitor monitor) {
		TuleapArtifact tuleapArtifact = null;

		monitor.beginTask(VALIDATE_CONNECTION_MESSAGE, 100);

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
			soapv1url = soapv1url + ITuleapConstants.SOAP_V1_URL;
		}

		String soapv2url = trackerLocation.getUrl();
		index = soapv2url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv2url = soapv2url.substring(0, index);
			soapv2url = soapv2url + ITuleapConstants.SOAP_V2_URL;
		}

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			final int fifty = 50;
			monitor.worked(fifty);
			monitor.subTask(LOGIN_MESSAGE);

			URL url = new URL(soapv1url);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();

			monitor.worked(5);

			int groupId = TuleapUtil.getGroupId(this.trackerLocation.getUrl());

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
					this.trackerLocation);
			url = new URL(soapv2url);

			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);
			Artifact artifact = tuleapTrackerV5APIPort.getArtifact(sessionHash, groupId, trackerId,
					artifactId);

			Group group = codendiAPIPort.getGroupById(sessionHash, groupId);
			String projectName = group.getGroup_name();

			String trackerName = null;
			Tracker[] trackers = tuleapTrackerV5APIPort.getTrackerList(sessionHash, groupId);
			for (Tracker tracker : trackers) {
				if (trackerId == tracker.getTracker_id()) {
					trackerName = tracker.getName();
					break;
				}
			}

			tuleapArtifact = new TuleapArtifact(artifact, trackerName, projectName);

			ArtifactFieldValue[] value = artifact.getValue();

			TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, groupId,
					trackerId);
			for (TrackerField trackerField : trackerFields) {
				boolean found = false;
				for (ArtifactFieldValue artifactFieldValue : value) {
					if (artifactFieldValue.getField_name().equals(trackerField.getShort_name())
							&& artifactFieldValue.getField_label().equals(trackerField.getLabel())) {
						tuleapArtifact.putValue(artifactFieldValue.getField_name(), artifactFieldValue
								.getField_value());
						monitor.worked(1);
						found = true;
					}
				}

				if (!found) {
					// The value is not set in the artifact
					// Let's create an empty entry in the artifact

					tuleapArtifact.putValue(trackerField.getShort_name(), null);
					monitor.worked(1);
				}
			}

			monitor.worked(fifty);

			codendiAPIPort.logout(sessionHash);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}

		return tuleapArtifact;
	}

	/**
	 * Creates a Tuleap artifact on the tracker matching the description of the Tuleap artifact.
	 * 
	 * @param artifact
	 *            The description of the artifact to create
	 * @param monitor
	 *            The progress monitor
	 * @return The id of the task data that will represent the Tuleap artifact created
	 */
	public String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) {
		String taskDataId = null;

		monitor.beginTask(VALIDATE_CONNECTION_MESSAGE, 100);

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
			soapv1url = soapv1url + ITuleapConstants.SOAP_V1_URL;
		}

		String soapv2url = trackerLocation.getUrl();
		index = soapv2url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv2url = soapv2url.substring(0, index);
			soapv2url = soapv2url + ITuleapConstants.SOAP_V2_URL;
		}

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			final int fifty = 50;
			monitor.worked(fifty);
			monitor.subTask(LOGIN_MESSAGE);

			URL url = new URL(soapv1url);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();

			monitor.worked(5);

			int groupId = TuleapUtil.getGroupId(this.trackerLocation.getUrl());

			Group group = codendiAPIPort.getGroupById(sessionHash, groupId);
			String projectName = group.getGroup_name();

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
					this.trackerLocation);
			url = new URL(soapv2url);

			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);

			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.RetrievingTrackerFields")); //$NON-NLS-1$

			List<ArtifactFieldValue> valuesList = new ArrayList<ArtifactFieldValue>();
			TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, groupId,
					artifact.getTrackerId());
			monitor.worked(10);

			monitor.subTask(TuleapMylynTasksMessages
					.getString("TuleapSoapConnector.RetrievingTrackerSemantic")); //$NON-NLS-1$
			TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash,
					groupId, artifact.getTrackerId());
			monitor.worked(10);
			for (TrackerField trackerField : trackerFields) {
				if (trackerStructure != null) {
					ArtifactFieldValue artifactFieldValue = getArtifactFieldValue(trackerStructure,
							trackerField, artifact, ITuleapConstants.PERMISSION_SUBMIT);
					if (artifactFieldValue != null) {
						valuesList.add(artifactFieldValue);
					}
				}
				monitor.worked(1);
			}
			int artifactId = tuleapTrackerV5APIPort.addArtifact(sessionHash, groupId,
					artifact.getTrackerId(), valuesList.toArray(new ArtifactFieldValue[valuesList.size()]));

			String trackerName = ""; //$NON-NLS-1$
			Tracker[] trackerList = tuleapTrackerV5APIPort.getTrackerList(sessionHash, groupId);
			for (Tracker tracker : trackerList) {
				if (tracker.getTracker_id() == artifact.getTrackerId()) {
					trackerName = tracker.getName();
					break;
				}
			}
			String trackerId = TuleapUtil.getTrackerId(trackerName, artifact.getTrackerId());
			taskDataId = TuleapUtil.getTaskDataId(projectName, trackerId, artifactId);

			monitor.worked(fifty);

			codendiAPIPort.logout(sessionHash);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}

		return taskDataId;
	}

	/**
	 * Updates the artifact on the Tuleap tracker with the information from the given Tuleap artifact.
	 * 
	 * @param artifact
	 *            The Tuleap artifact
	 * @param monitor
	 *            the progress monitor
	 */
	public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor) {
		monitor.beginTask(VALIDATE_CONNECTION_MESSAGE, 100);

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
			soapv1url = soapv1url + ITuleapConstants.SOAP_V1_URL;
		}

		String soapv2url = trackerLocation.getUrl();
		index = soapv2url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv2url = soapv2url.substring(0, index);
			soapv2url = soapv2url + ITuleapConstants.SOAP_V2_URL;
		}

		try {
			EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
					CONFIG_FILE));
			TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);

			final int fifty = 50;
			monitor.worked(fifty);
			monitor.subTask(LOGIN_MESSAGE);

			URL url = new URL(soapv1url);
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(username, password);
			String sessionHash = session.getSession_hash();

			monitor.worked(5);

			int groupId = TuleapUtil.getGroupId(this.trackerLocation.getUrl());

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
					this.trackerLocation);
			url = new URL(soapv2url);

			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);

			monitor.subTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.RetrievingTrackerFields")); //$NON-NLS-1$
			List<ArtifactFieldValue> valuesList = new ArrayList<ArtifactFieldValue>();
			TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, groupId,
					artifact.getTrackerId());
			monitor.worked(10);

			monitor.subTask(TuleapMylynTasksMessages
					.getString("TuleapSoapConnector.RetrievingTrackerSemantic")); //$NON-NLS-1$
			TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash,
					groupId, artifact.getTrackerId());
			monitor.worked(10);
			for (TrackerField trackerField : trackerFields) {
				if (trackerStructure != null) {
					ArtifactFieldValue artifactFieldValue = getArtifactFieldValue(trackerStructure,
							trackerField, artifact, ITuleapConstants.PERMISSION_UPDATE);
					if (artifactFieldValue != null) {
						valuesList.add(artifactFieldValue);
					}
				}
				monitor.worked(1);
			}

			String newComment = artifact.getValue(TaskAttribute.COMMENT_NEW);
			if (newComment == null) {
				newComment = TuleapMylynTasksMessages.getString("TuleapSoapConnector.DefaultComment"); //$NON-NLS-1$
			}
			tuleapTrackerV5APIPort.updateArtifact(sessionHash, groupId, artifact.getTrackerId(), artifact
					.getId(), valuesList.toArray(new ArtifactFieldValue[valuesList.size()]), newComment,
					ITuleapConstants.UTF8);

			monitor.worked(fifty);

			codendiAPIPort.logout(sessionHash);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		}
	}

	/**
	 * Creates the artifact field value from the given tuleap artifact, the tracker structure and the tracker
	 * field.
	 * 
	 * @param trackerStructure
	 *            The structure of the tracker holding the semantic and the workflow of the tracker.
	 * @param trackerField
	 *            The field of the tracker.
	 * @param artifact
	 *            The artifact that should be submitted
	 * @param permission
	 *            The permission that should be checked.
	 * @return The artifact field value
	 */
	private ArtifactFieldValue getArtifactFieldValue(TrackerStructure trackerStructure,
			TrackerField trackerField, TuleapArtifact artifact, String permission) {
		ArtifactFieldValue artifactFieldValue = null;
		if (trackerStructure.getSemantic() != null
				&& trackerStructure.getSemantic().getTitle() != null
				&& trackerField.getShort_name().equals(
						trackerStructure.getSemantic().getTitle().getField_name())) {
			// The title of the artifact
			artifactFieldValue = new ArtifactFieldValue(trackerField.getShort_name(),
					trackerField.getLabel(), artifact.getValue(TaskAttribute.SUMMARY));
		} else if (trackerStructure.getSemantic() != null
				&& trackerStructure.getSemantic().getStatus() != null
				&& trackerField.getShort_name().equals(
						trackerStructure.getSemantic().getStatus().getField_name())) {
			// The status of the artifact
			artifactFieldValue = new ArtifactFieldValue(trackerField.getShort_name(),
					trackerField.getLabel(), artifact.getValue(TaskAttribute.STATUS));
		} else if (ITuleapConfigurationConstants.DATE.equals(trackerField.getType())) {
			// Convert the date into a valid timestamp
			String value = artifact.getValue(Integer.valueOf(trackerField.getField_id()).toString());
			if (value != null && value.length() > 0) {
				// FIXME Bug date creation / upload
				// int date = Long.valueOf(Long.valueOf(value).longValue() / 1000).intValue();
				// artifactFieldValue = new ArtifactFieldValue(trackerField.getShort_name(), trackerField
				// .getLabel(), Integer.valueOf(date).toString());
			}
		} else if (this.shouldConsider(trackerField.getType())) {
			// Any other value
			String fieldId = Integer.valueOf(trackerField.getField_id()).toString();
			boolean hasKey = artifact.getKeys().contains(fieldId);
			String value = artifact.getValue(fieldId);

			if ((ITuleapConfigurationConstants.SB.equals(trackerField.getType()) || ITuleapConfigurationConstants.MSB
					.equals(trackerField.getType()))
					&& (value == null || "".equals(value))) { //$NON-NLS-1$
				value = ITuleapConstants.SELECT_BOX_NONE_VALUE;
			}

			if (Arrays.asList(trackerField.getPermissions()).contains(permission) && hasKey
					&& canSubmitValue(trackerField.getType(), value)) {
				artifactFieldValue = new ArtifactFieldValue(trackerField.getShort_name(), trackerField
						.getLabel(), value);
			}
		}

		return artifactFieldValue;
	}

	/**
	 * Indicates if we can submit the value for the given type of field.
	 * 
	 * @param fieldType
	 *            The type of the field
	 * @param value
	 *            The value to submit
	 * @return <code>true</code> if we can submit the value, <code>false</code> otherwise
	 */
	private boolean canSubmitValue(String fieldType, String value) {
		if (value == null || "".equals(value.trim())) { //$NON-NLS-1$			
			boolean canSubmitEmptyValue = false;
			canSubmitEmptyValue = canSubmitEmptyValue
					|| ITuleapConfigurationConstants.STRING.equals(fieldType);
			canSubmitEmptyValue = canSubmitEmptyValue || ITuleapConfigurationConstants.TEXT.equals(fieldType);
			return canSubmitEmptyValue;
		}
		return true;
	}

	/**
	 * Indicates if we should consider the given tracker field type for the creation or update or an artifact.
	 * 
	 * @param trackerFieldType
	 *            The type of the field of the tracker
	 * @return <code>true</code> if we should consider it, <code>false</code> otherwise
	 */
	private boolean shouldConsider(String trackerFieldType) {
		boolean shouldConsider = true;
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.AID.equals(trackerFieldType);
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.ARTLINK.equals(trackerFieldType);
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.SUBON.equals(trackerFieldType);
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.SUBBY.equals(trackerFieldType);
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.LUD.equals(trackerFieldType);
		return shouldConsider;
	}
}
