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

import java.io.IOException;
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
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.internal.core.config.ITuleapConfigurationConstants;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapAttachmentDescriptor;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapGroup;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapArtifactLink;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapComputedValue;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapText;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflow;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.TuleapSoapServiceLocator;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.TuleapTrackerV5APILocatorImpl;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Group;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Session;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UGroupMember;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Ugroup;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactComments;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactQueryResult;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Criteria;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDate;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValueFileInfo;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Tracker;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerField;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerReport;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemantic;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticContributor;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticStatus;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticTitle;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerStructure;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflow;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APILocator;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;

/**
 * This class will be used to connect Mylyn to the SOAP services provided by the Tuleap instance.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class TuleapSoapConnector {
	/**
	 * The url used to invoke the soap v1 services.
	 */
	private static final String SOAP_V1_URL = "/soap/"; //$NON-NLS-1$

	/**
	 * The url used to invoke the soap v2 services.
	 */
	private static final String SOAP_V2_URL = "/plugins/tracker/soap/"; //$NON-NLS-1$

	/**
	 * The location of the configuration file.
	 */
	private static final String CONFIG_FILE = "org/tuleap/mylyn/task/internal/core/wsdl/soap/client-config.wsdd"; //$NON-NLS-1$

	/**
	 * The login message.
	 */
	private static final String LOGIN_MESSAGE = TuleapMylynTasksMessages
			.getString(TuleapMylynTasksMessagesKeys.login);

	/**
	 * Default session hash.
	 */
	private static final String DEFAULT_SESSION_HASH = "dummy"; //$NON-NLS-1$

	/**
	 * The location of the tracker.
	 */
	private final AbstractWebLocation trackerLocation;

	/**
	 * The common SOAP API.
	 */
	private CodendiAPIPortType codendiAPIPort;

	/**
	 * The SOAP entry point.
	 */
	private TuleapTrackerV5APIPortType tuleapTrackerV5APIPort;

	/**
	 * The session hash.
	 */
	private String sessionHash = DEFAULT_SESSION_HASH;

	/**
	 * The session.
	 */
	private Session session;

	/**
	 * The constructor.
	 * 
	 * @param location
	 *            The location of the tracker.
	 */
	public TuleapSoapConnector(AbstractWebLocation location) {
		this.trackerLocation = location;
		this.ensureProxySettingsRegistration();
	}

	/**
	 * Logs the user in the Tuleap server.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return A status indicating if everything went right.
	 * @throws MalformedURLException
	 *             If the URL is invalid
	 * @throws ServiceException
	 *             If an error appears with the SOAP API
	 * @throws RemoteException
	 *             If the server returns an error
	 */
	private IStatus login(IProgressMonitor monitor) throws MalformedURLException, ServiceException,
			RemoteException {
		IStatus status = Status.OK_STATUS;

		EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
				CONFIG_FILE));
		TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, this.trackerLocation);
		String soapv1url = trackerLocation.getUrl();
		int index = soapv1url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv1url = soapv1url.substring(0, index);
		}
		soapv1url = soapv1url + SOAP_V1_URL;

		String soapv2url = trackerLocation.getUrl();
		index = soapv2url.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (index != -1) {
			soapv2url = soapv2url.substring(0, index);
		}
		soapv2url = soapv2url + SOAP_V2_URL;

		String username = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = this.trackerLocation.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		URL url = new URL(soapv1url);
		codendiAPIPort = locator.getCodendiAPIPort(url);

		session = codendiAPIPort.login(username, password);
		sessionHash = session.getSession_hash();

		monitor.worked(10);

		config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
		TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config,
				this.trackerLocation);
		url = new URL(soapv2url);
		tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);

		return status;
	}

	/**
	 * Logs the user out of the Tuleap server.
	 * 
	 * @return A status indicating if everything went right.
	 */
	private IStatus logout() {
		IStatus status = Status.OK_STATUS;
		if (!DEFAULT_SESSION_HASH.equals(sessionHash)) {
			try {
				codendiAPIPort.logout(sessionHash);
			} catch (RemoteException e) {
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage());
				TuleapCoreActivator.log(e, true);
			}

			// Reinitializing everything
			this.codendiAPIPort = null;
			this.tuleapTrackerV5APIPort = null;
			this.sessionHash = null;
		}
		return status;
	}

	/**
	 * Ensures that the settings of the proxy are correctly registered in the Axis properties.
	 */
	private void ensureProxySettingsRegistration() {
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
	 * Validates the connection.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return A status indicating if everything went well
	 * @throws ServiceException
	 *             If a problem appears with the client
	 * @throws RemoteException
	 *             If a problem appears with the server
	 * @throws MalformedURLException
	 *             If the URL is invalid
	 */
	public IStatus validateConnection(IProgressMonitor monitor) throws MalformedURLException,
			RemoteException, ServiceException {
		IStatus status = this.login(monitor);
		this.logout();
		return status;
	}

	/**
	 * Returns the configuration of the Tuleap instance.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return The configuration of the Tuleap instance.
	 */
	public TuleapServerConfiguration getTuleapServerConfiguration(IProgressMonitor monitor) {
		TuleapServerConfiguration serverConfiguration = new TuleapServerConfiguration(this.trackerLocation
				.getUrl());
		serverConfiguration.setLastUpdate(new Date().getTime());

		try {
			this.login(monitor);

			monitor.beginTask(TuleapMylynTasksMessages
					.getString(TuleapMylynTasksMessagesKeys.retrieveTuleapInstanceConfiguration), 100);

			monitor.worked(1);
			monitor.subTask(TuleapMylynTasksMessages
					.getString(TuleapMylynTasksMessagesKeys.retrievingTrackersList));
			Group[] groups = codendiAPIPort.getMyProjects(sessionHash);
			for (Group group : groups) {
				int groupId = group.getGroup_id();

				Tracker[] trackers = new Tracker[0];
				try {
					trackers = tuleapTrackerV5APIPort.getTrackerList(sessionHash, groupId);
				} catch (RemoteException e) {
					// https://tuleap.net/plugins/tracker/?aid=4470
					// The project does not have any trackers, we won't log the error so we catch it
				}

				TuleapProjectConfiguration projectConfiguration = new TuleapProjectConfiguration(group
						.getGroup_name(), groupId);
				serverConfiguration.addProject(projectConfiguration);

				if (trackers.length > 0) {
					for (Tracker tracker : trackers) {
						monitor.worked(5);
						monitor.setTaskName(TuleapMylynTasksMessages.getString(
								TuleapMylynTasksMessagesKeys.analyzingTracker, tracker.getName()));
						TuleapTrackerConfiguration tuleapTrackerConfiguration = this
								.getTuleapTrackerConfiguration(tracker, monitor);
						projectConfiguration.addTracker(tuleapTrackerConfiguration);
					}
				}

				// Retrieve all the user groups we are allowed to see
				Ugroup[] ugroups = codendiAPIPort.getProjectGroupsAndUsers(sessionHash, groupId);
				for (Ugroup ugroup : ugroups) {
					TuleapGroup tuleapGroup = new TuleapGroup(ugroup.getUgroup_id(), ugroup.getName());
					UGroupMember[] members = ugroup.getMembers();
					for (UGroupMember member : members) {
						int userId = member.getUser_id();
						UserInfo userInfo;
						if (userId == ITuleapConstants.ANONYMOUS_USER_INFO_IDENTIFIER) {
							userInfo = new UserInfo(Integer.valueOf(userId).toString(),
									ITuleapConstants.ANONYMOUS_USER_INFO_USERNAME, Integer.valueOf(
											ITuleapConstants.ANONYMOUS_USER_INFO_IDENTIFIER).toString(),
									ITuleapConstants.ANONYMOUS_USER_INFO_REAL_NAME,
									ITuleapConstants.ANONYMOUS_USER_INFO_EMAIL,
									ITuleapConstants.ANONYMOUS_USER_INFO_LDAP_IDENTIFIER);
						} else {
							userInfo = codendiAPIPort.getUserInfo(sessionHash, userId);
						}
						projectConfiguration.addUserToUserGroup(tuleapGroup, new TuleapPerson(userInfo
								.getUsername(), userInfo.getReal_name(), userId, userInfo.getEmail()));
					}
				}
			}
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		}

		this.logout();
		return serverConfiguration;
	}

	/**
	 * Returns the configuration of the given tracker.
	 * 
	 * @param tracker
	 *            The Tuleap tracker
	 * @param monitor
	 *            The progress monitor
	 * @return The configuration of the Tuleap tracker.
	 */
	private TuleapTrackerConfiguration getTuleapTrackerConfiguration(Tracker tracker, IProgressMonitor monitor) {
		String trackerURL = this.trackerLocation.getUrl() + ITuleapConstants.REPOSITORY_TRACKER_URL_SEPARATOR
				+ Integer.valueOf(tracker.getTracker_id()).toString();

		TuleapTrackerConfiguration tuleapTrackerConfiguration = new TuleapTrackerConfiguration(tracker
				.getTracker_id(), trackerURL, tracker.getName(), tracker.getItem_name(), tracker
				.getDescription(), System.currentTimeMillis());

		try {
			monitor.subTask(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.retrievingFieldsDescriptionFromTracker, tracker.getName()));

			TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, tracker
					.getGroup_id(), tracker.getTracker_id());
			monitor.worked(5);

			TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash,
					tracker.getGroup_id(), tracker.getTracker_id());
			for (TrackerField trackerField : trackerFields) {
				AbstractTuleapField tuleapField = getTuleapTrackerField(tracker.getGroup_id(),
						trackerStructure, trackerField, monitor);
				monitor.worked(1);
				if (tuleapField != null) {
					tuleapField.setName(trackerField.getShort_name());
					tuleapField.setLabel(trackerField.getLabel());
					tuleapField.setPermissions(trackerField.getPermissions());
					tuleapTrackerConfiguration.addField(tuleapField);
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
	 * @param groupId
	 *            The group ID.
	 * @param trackerStructure
	 *            The structure of the tracker
	 * @param trackerField
	 *            The tracker field
	 * @param monitor
	 *            The progress monitor
	 * @return The tuleap tracker field
	 */
	private AbstractTuleapField getTuleapTrackerField(int groupId, TrackerStructure trackerStructure,
			TrackerField trackerField, IProgressMonitor monitor) {
		AbstractTuleapField tuleapField = null;

		monitor.subTask(TuleapMylynTasksMessages.getString(
				TuleapMylynTasksMessagesKeys.analyzeTuleapTrackerField, trackerField.getLabel()));

		TrackerSemantic trackerSemantic = trackerStructure.getSemantic();

		String type = trackerField.getType();
		int fieldIdentifier = trackerField.getField_id();
		if (ITuleapConfigurationConstants.STRING.equals(type)) {
			tuleapField = new TuleapString(fieldIdentifier);

			TrackerSemanticTitle trackerSemanticTitle = trackerSemantic.getTitle();
			String trackerSemanticTitleFieldName = trackerSemanticTitle.getField_name();
			if (trackerSemanticTitleFieldName.equals(trackerField.getShort_name())) {
				((TuleapString)tuleapField).setSemanticTitle(true);
			}
		} else if (ITuleapConfigurationConstants.TEXT.equals(type)) {
			tuleapField = new TuleapText(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.SB.equals(type)) {
			tuleapField = this.getTuleapSelectBox(trackerStructure, trackerField);
		} else if (ITuleapConfigurationConstants.MSB.equals(type)
				|| ITuleapConfigurationConstants.CB.equals(type)) {
			tuleapField = this.getTuleapMultiSelectBox(groupId, trackerStructure, trackerField);
		} else if (ITuleapConfigurationConstants.DATE.equals(type)) {
			tuleapField = new TuleapDate(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.FILE.equals(type)) {
			tuleapField = new TuleapFileUpload(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.ARTLINK.equals(type)) {
			tuleapField = new TuleapArtifactLink(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.INT.equals(type)) {
			tuleapField = new TuleapInteger(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.TBL.equals(type)) {
			tuleapField = new TuleapOpenList(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.FLOAT.equals(type)) {
			tuleapField = new TuleapFloat(fieldIdentifier);
		} else if (ITuleapConfigurationConstants.COMPUTED.equals(type)) {
			tuleapField = new TuleapComputedValue(fieldIdentifier);
		} else {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.unsupportedTrackerFieldType, type), true);
		}

		monitor.worked(1);

		return tuleapField;
	}

	/**
	 * Creates a Tuleap Multi select box from the description of the tracker field.
	 * 
	 * @param groupId
	 *            The identifier of the group
	 * @param trackerStructure
	 *            The structure of the tracker
	 * @param trackerField
	 *            The description of the tracker field
	 * @return The newly created Tuleap Multi select box
	 */
	private TuleapMultiSelectBox getTuleapMultiSelectBox(int groupId, TrackerStructure trackerStructure,
			TrackerField trackerField) {
		TuleapMultiSelectBox tuleapField = new TuleapMultiSelectBox(trackerField.getField_id());
		// Is semantic contributor?
		TrackerSemantic trackerSemantic = trackerStructure.getSemantic();
		TrackerSemanticStatus trackerSemanticStatus = trackerSemantic.getStatus();
		int[] trackerSemanticStatusOpenValues = trackerSemanticStatus.getValues();

		TrackerSemanticContributor trackerSemanticContributor = trackerSemantic.getContributor();
		String trackerSemanticContributorFieldName = trackerSemanticContributor.getField_name();
		if (trackerSemanticContributorFieldName.equals(trackerField.getShort_name())) {
			tuleapField.setSemanticContributor(true);
		}

		// If one binding, let's check if it's a dynamic one
		if (trackerField.getValues().length == 1) {
			TrackerFieldBindValue trackerFieldBindValue = trackerField.getValues()[0];
			try {
				Ugroup[] projectGroupsAndUsers = codendiAPIPort
						.getProjectGroupsAndUsers(sessionHash, groupId);
				for (Ugroup ugroup : projectGroupsAndUsers) {
					// FIXME Waiting for a fix from Tuleap -> https://tuleap.net/plugins/tracker/?aid=2234

					// TODO Check and remove
					// LDE Commented after changing user management caching mechanism
					// String uGroupName = ugroup.getName();
					//					String groupMembers = "group_members"; //$NON-NLS-1$
					//					String groupAdmin = "group_admins"; //$NON-NLS-1$
					//					if ("project_members".equals(uGroupName) //$NON-NLS-1$
					// && (trackerFieldBindValue.getBind_value_label().contains(uGroupName) ||
					// trackerFieldBindValue
					// .getBind_value_label().contains(groupMembers))) {
					// addUsersToMultiSelectBox(tuleapField, ugroup);
					//					} else if ("project_admins".equals(uGroupName) //$NON-NLS-1$
					// && (trackerFieldBindValue.getBind_value_label().contains(uGroupName) ||
					// trackerFieldBindValue
					// .getBind_value_label().contains(groupAdmin))) {
					// addUsersToMultiSelectBox(tuleapField, ugroup);
					// } else if (trackerFieldBindValue.getBind_value_id() == ugroup.getUgroup_id()) {
					// addUsersToMultiSelectBox(tuleapField, ugroup);
					// }
					if (trackerFieldBindValue.getBind_value_id() == ugroup.getUgroup_id()) {
						addUsersToMultiSelectBox(tuleapField, ugroup);
					}
				}
			} catch (RemoteException e) {
				TuleapCoreActivator.log(e, true);
			}
		} else {
			// More than one binding -> not dynamic
			for (TrackerFieldBindValue trackerFieldBindValue : trackerField.getValues()) {
				TuleapSelectBoxItem tuleapSelectBoxItem = new TuleapSelectBoxItem(trackerFieldBindValue
						.getBind_value_id());
				tuleapSelectBoxItem.setLabel(trackerFieldBindValue.getBind_value_label());
				tuleapField.addItem(tuleapSelectBoxItem);
				if (ArrayUtils.contains(trackerSemanticStatusOpenValues, trackerFieldBindValue
						.getBind_value_id())) {
					tuleapField.getOpenStatus().add(tuleapSelectBoxItem);
				}
			}
		}
		return tuleapField;
	}

	/**
	 * Iterates on the content of the ugroup to add all the elements as items in the multi select box.
	 * 
	 * @param tuleapSelectBox
	 *            The Tuleap multi select box in which we will add the new items.
	 * @param ugroup
	 *            The group from which we should retrieve the members.
	 * @throws RemoteException
	 *             In case of problems with the connection
	 */
	private void addUsersToMultiSelectBox(TuleapMultiSelectBox tuleapSelectBox, Ugroup ugroup)
			throws RemoteException {
		UGroupMember[] members = ugroup.getMembers();
		for (UGroupMember uGroupMember : members) {
			int userId = uGroupMember.getUser_id();
			UserInfo userInfo = null;
			// if the user is anonymous, do not ask the server
			if (userId == ITuleapConstants.ANONYMOUS_USER_INFO_IDENTIFIER) {
				userInfo = new UserInfo(Integer.valueOf(userId).toString(),
						ITuleapConstants.ANONYMOUS_USER_INFO_USERNAME, Integer.valueOf(
								ITuleapConstants.ANONYMOUS_USER_INFO_IDENTIFIER).toString(),
						ITuleapConstants.ANONYMOUS_USER_INFO_REAL_NAME,
						ITuleapConstants.ANONYMOUS_USER_INFO_EMAIL,
						ITuleapConstants.ANONYMOUS_USER_INFO_LDAP_IDENTIFIER);
			} else {
				userInfo = codendiAPIPort.getUserInfo(sessionHash, userId);
			}
			String label = userInfo.getReal_name() + " (" + userInfo.getUsername() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			TuleapSelectBoxItem tuleapSelectBoxItem = new TuleapSelectBoxItem(userId);
			tuleapSelectBoxItem.setLabel(label);
			tuleapSelectBox.addItem(tuleapSelectBoxItem);
		}
	}

	/**
	 * Creates a Tuleap select box from the description of the tracker field.
	 * 
	 * @param trackerStructure
	 *            The structure of the tracker
	 * @param trackerField
	 *            The description of the tracker field
	 * @return The newly created Tuleap select box
	 */
	private TuleapSelectBox getTuleapSelectBox(TrackerStructure trackerStructure, TrackerField trackerField) {
		TuleapSelectBox tuleapField = new TuleapSelectBox(trackerField.getField_id());

		TrackerWorkflow trackerWorkflow = trackerStructure.getWorkflow();
		if (trackerWorkflow.getField_id() == trackerField.getField_id()) {
			// Workflow
			TrackerWorkflowTransition[] trackerWorkflowTransitions = trackerWorkflow.getTransitions();
			TuleapWorkflow tuleapWorkflow = tuleapField.getWorkflow();
			for (TrackerWorkflowTransition trackerTransition : trackerWorkflowTransitions) {
				TuleapWorkflowTransition tuleapTransition = new TuleapWorkflowTransition();
				tuleapTransition.setFrom(trackerTransition.getFrom_id());
				tuleapTransition.setTo(trackerTransition.getTo_id());
				tuleapWorkflow.addTransition(tuleapTransition);
			}
		}

		TrackerSemanticStatus trackerSemanticStatus = trackerStructure.getSemantic().getStatus();
		int[] trackerSemanticStatusOpenValues = trackerSemanticStatus.getValues();

		TrackerSemanticContributor trackerSemanticContributor = trackerStructure.getSemantic()
				.getContributor();
		String trackerSemanticContributorFieldName = trackerSemanticContributor.getField_name();
		for (TrackerFieldBindValue trackerFieldBindValue : trackerField.getValues()) {
			TuleapSelectBoxItem tuleapSelectBoxItem = new TuleapSelectBoxItem(trackerFieldBindValue
					.getBind_value_id());
			tuleapSelectBoxItem.setLabel(trackerFieldBindValue.getBind_value_label());
			tuleapField.addItem(tuleapSelectBoxItem);

			// Semantic status
			if (trackerField.getShort_name().equals(trackerSemanticStatus.getField_name())) {
				if (ArrayUtils.contains(trackerSemanticStatusOpenValues, trackerFieldBindValue
						.getBind_value_id())) {
					tuleapField.getOpenStatus().add(tuleapSelectBoxItem);
				}
			}

			// Contributor
			if (trackerSemanticContributorFieldName.equals(trackerField.getShort_name())) {
				tuleapField.setSemanticContributor(true);
			}
		}
		return tuleapField;
	}

	/**
	 * Performs the given query on the Tuleap tracker and return the tasks found.
	 * 
	 * @param query
	 *            The query to run
	 * @param serverConfiguration
	 *            The user registry
	 * @param maxHits
	 *            The maximum number of tasks that should be processed
	 * @param monitor
	 *            The progress monitor
	 * @return The tasks found
	 */
	public List<CommentedArtifact> performQuery(IRepositoryQuery query,
			TuleapServerConfiguration serverConfiguration, int maxHits, IProgressMonitor monitor) {
		List<CommentedArtifact> artifactsFound = new ArrayList<CommentedArtifact>();

		int trackerId = -1;

		try {
			this.login(monitor);

			monitor.subTask(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.executingQuery));

			String queryTrackerId = query.getAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID);
			trackerId = Integer.valueOf(queryTrackerId).intValue();

			ArtifactQueryResult artifactQueryResult = null;
			if (ITuleapQueryConstants.QUERY_KIND_REPORT.equals(query
					.getAttribute(ITuleapQueryConstants.QUERY_KIND))) {
				// Run the report on the server
				String queryReportId = query.getAttribute(ITuleapQueryConstants.QUERY_REPORT_ID);
				int reportId = Integer.valueOf(queryReportId).intValue();
				artifactQueryResult = tuleapTrackerV5APIPort.getArtifactsFromReport(sessionHash, reportId, 0,
						maxHits);
			} else {
				List<Criteria> criterias = new ArrayList<Criteria>();
				if (ITuleapQueryConstants.QUERY_KIND_ALL_FROM_TRACKER.equals(query
						.getAttribute(ITuleapQueryConstants.QUERY_KIND))
						&& query.getAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID) != null) {
					// Download all artifacts from the given tracker -> no criteria
				} else if (ITuleapQueryConstants.QUERY_KIND_CUSTOM.equals(query
						.getAttribute(ITuleapQueryConstants.QUERY_KIND))
						&& query.getAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID) != null) {
					// Custom query
					criterias.addAll(this.getCriterias(query));
				}
				artifactQueryResult = tuleapTrackerV5APIPort.getArtifacts(sessionHash, -1, trackerId,
						criterias.toArray(new Criteria[criterias.size()]), 0, maxHits);
			}

			try {
				Artifact[] artifacts = artifactQueryResult.getArtifacts();
				for (Artifact artifact : artifacts) {
					// Retrieve comments
					int artifactId = artifact.getArtifact_id();
					List<TuleapElementComment> comments = getArtifactCommentsWhileLoggedIn(artifactId,
							serverConfiguration, monitor);
					artifactsFound.add(new CommentedArtifact(artifact, comments));
				}
			} catch (NumberFormatException e) {
				TuleapCoreActivator.log(e, true);
			}

			final int fifty = 50;
			monitor.worked(fifty);
		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		}

		this.logout();

		return artifactsFound;
	}

	/**
	 * Retrieve an artifact's comments. This method must be called during a valid SOAP session.
	 * 
	 * @param artifactId
	 *            Id of the artifact.
	 * @param serverConfiguration
	 *            The user registry
	 * @param monitor
	 *            To use for progress reporting, can be null.
	 * @return The list of comments of the artifact
	 * @throws RemoteException
	 *             In case a remote exception occurs
	 */
	private List<TuleapElementComment> getArtifactCommentsWhileLoggedIn(int artifactId,
			TuleapServerConfiguration serverConfiguration, IProgressMonitor monitor) throws RemoteException {
		List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();
		if (monitor != null) {
			monitor.subTask(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.retrieveComments,
					Integer.valueOf(artifactId)));
		}
		ArtifactComments[] artifactComments = tuleapTrackerV5APIPort.getArtifactComments(sessionHash,
				artifactId);
		for (ArtifactComments artifactComment : artifactComments) {
			int submitterId = artifactComment.getSubmitted_by();
			TuleapPerson submitter = serverConfiguration.getUser(submitterId);
			TuleapElementComment comment = new TuleapElementComment(artifactComment.getBody(), submitter,
					artifactComment.getSubmitted_on());
			comments.add(comment);
		}
		return comments;
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
			if (!(entry.getKey().equals(ITuleapQueryConstants.QUERY_KIND) || entry.getKey().equals(
					ITuleapQueryConstants.QUERY_CONFIGURATION_ID))) {
				String[] attributes = this.getAttributes(entry.getValue());
				CriteriaValueDate criteriaValueDate = null;
				// First attribute is the value
				String values = attributes[0];
				if (attributes.length > 1) {
					// If it exists a second attribute it means that we have to do a query on date and have
					// to get the operation
					// Check if value is a date
					if (values.matches("[0-9]*-[0-9]*-[0-9]*")) { //$NON-NLS-1$
						String operation = attributes[1];
						DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy"); //$NON-NLS-1$
						try {
							criteriaValueDate = new CriteriaValueDate(operation, Long.valueOf(
									dateFormat.parse(values).getTime() / 1000L).intValue());
						} catch (ParseException e) {
							TuleapCoreActivator.log(e, true);
						}
					} else {
						for (int i = 1; i < attributes.length; i++) {
							values += ITuleapConstants.QUERY_ATTRIBUTES_SEPARATOR + attributes[i];
						}
					}
				}

				CriteriaValue attributeValue = new CriteriaValue(values, criteriaValueDate, null);
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
	 * @param artifactId
	 *            The artifact id
	 * @param serverConfiguration
	 *            The user registry
	 * @param monitor
	 *            The progress monitor
	 * @return The Tuleap Artifact
	 * @throws ServiceException
	 *             If a problem appears with the client
	 * @throws RemoteException
	 *             If a problem appears with the server
	 * @throws MalformedURLException
	 *             If the URL is invalid
	 */
	public CommentedArtifact getArtifact(int artifactId, TuleapServerConfiguration serverConfiguration,
			IProgressMonitor monitor) throws MalformedURLException, RemoteException, ServiceException {
		this.login(monitor);

		Artifact artifact = tuleapTrackerV5APIPort.getArtifact(sessionHash, -1, -1, artifactId);

		List<TuleapElementComment> comments = getArtifactCommentsWhileLoggedIn(artifactId,
				serverConfiguration, monitor);

		this.logout();

		return new CommentedArtifact(artifact, comments);
	}

	/**
	 * Creates a Tuleap artifact on the tracker matching the description of the Tuleap artifact.
	 * 
	 * @param artifact
	 *            The description of the artifact to create
	 * @param monitor
	 *            The progress monitor
	 * @return The id of the task data that will represent the Tuleap artifact created
	 * @throws RemoteException
	 *             In case of connection problems
	 * @throws ServiceException
	 *             In case of errors from the client
	 * @throws MalformedURLException
	 *             If the URL is invalid
	 */
	public String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws RemoteException,
			MalformedURLException, ServiceException {
		this.login(monitor);

		String taskDataId = null;
		final int fifty = 50;

		int groupId = -1;

		Group[] myProjects = codendiAPIPort.getMyProjects(sessionHash);
		for (Group group : myProjects) {
			if (groupId == -1) {
				Tracker[] trackerList = tuleapTrackerV5APIPort.getTrackerList(sessionHash, group
						.getGroup_id());
				for (Tracker tracker : trackerList) {
					if (artifact.getConfigurationId() == tracker.getTracker_id()) {
						groupId = group.getGroup_id();
						break;
					}
				}
			}
		}

		monitor.subTask(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.retrievingTrackerFields));

		List<ArtifactFieldValue> valuesList = new ArrayList<ArtifactFieldValue>();
		TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, groupId, artifact
				.getConfigurationId());
		monitor.worked(10);

		monitor.subTask(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.retrievingTrackerSemantic));
		TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash, groupId,
				artifact.getConfigurationId());
		monitor.worked(10);
		for (TrackerField trackerField : trackerFields) {
			if (trackerStructure != null) {
				ArtifactFieldValue artifactFieldValue = getArtifactFieldValue(trackerStructure, trackerField,
						artifact, ITuleapConstants.PERMISSION_SUBMIT);
				if (artifactFieldValue != null) {
					valuesList.add(artifactFieldValue);
				}
			}
			monitor.worked(1);
		}
		int artifactId = tuleapTrackerV5APIPort.addArtifact(sessionHash, groupId, artifact
				.getConfigurationId(), valuesList.toArray(new ArtifactFieldValue[valuesList.size()]));

		taskDataId = String.valueOf(artifactId);
		monitor.worked(fifty);

		this.logout();

		return taskDataId;
	}

	/**
	 * Updates the artifact on the Tuleap tracker with the information from the given Tuleap artifact.
	 * 
	 * @param artifact
	 *            The Tuleap artifact
	 * @param monitor
	 *            the progress monitor
	 * @throws ServiceException
	 *             If the client has a problem
	 * @throws RemoteException
	 *             If the server returns an error
	 * @throws MalformedURLException
	 *             If the URL if invalid
	 */
	public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
			throws MalformedURLException, RemoteException, ServiceException {

		this.login(monitor);

		final int fifty = 50;

		int groupId = -1;

		Group[] myProjects = codendiAPIPort.getMyProjects(sessionHash);
		for (Group group : myProjects) {
			if (groupId == -1) {
				Tracker[] trackerList = tuleapTrackerV5APIPort.getTrackerList(sessionHash, group
						.getGroup_id());
				for (Tracker tracker : trackerList) {
					if (artifact.getConfigurationId() == tracker.getTracker_id()) {
						groupId = group.getGroup_id();
						break;
					}
				}
			}
		}

		monitor.subTask(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.retrievingTrackerFields));
		List<ArtifactFieldValue> valuesList = new ArrayList<ArtifactFieldValue>();
		TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, groupId, artifact
				.getConfigurationId());
		monitor.worked(10);

		monitor.subTask(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.retrievingTrackerSemantic));
		TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash, groupId,
				artifact.getConfigurationId());
		monitor.worked(10);
		for (TrackerField trackerField : trackerFields) {
			if (trackerStructure != null) {
				ArtifactFieldValue artifactFieldValue = getArtifactFieldValue(trackerStructure, trackerField,
						artifact, ITuleapConstants.PERMISSION_UPDATE);
				if (artifactFieldValue != null) {
					valuesList.add(artifactFieldValue);
				}
			}
			monitor.worked(1);
		}

		String newComment = artifact.getNewComment();
		if (newComment == null) {
			newComment = TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.defaultComment);
		}
		tuleapTrackerV5APIPort.updateArtifact(sessionHash, groupId, artifact.getConfigurationId(), artifact
				.getId(), valuesList.toArray(new ArtifactFieldValue[valuesList.size()]), newComment,
				ITuleapConstants.UTF8);

		monitor.worked(fifty);

		this.logout();
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

		if (!Arrays.asList(trackerField.getPermissions()).contains(permission)) {
			return artifactFieldValue;
		}

		if (ITuleapConfigurationConstants.MSB.equals(trackerField.getType())
				|| ITuleapConfigurationConstants.SB.equals(trackerField.getType())
				|| ITuleapConfigurationConstants.CB.equals(trackerField.getType())) {
			// Regular select box, multi-select box or checkbox
			AbstractFieldValue abstractFieldValue = artifact.getFieldValue(trackerField.getField_id());
			if (abstractFieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)abstractFieldValue;
				List<TrackerFieldBindValue> bindValues = new ArrayList<TrackerFieldBindValue>();
				for (Integer value : boundFieldValue.getValueIds()) {
					if (ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID == value.intValue()) {
						bindValues.add(new TrackerFieldBindValue(
								ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID, "")); //$NON-NLS-1$
					} else {
						TrackerFieldBindValue[] trackerFieldBindValues = trackerField.getValues();
						for (TrackerFieldBindValue trackerFieldBindValue : trackerFieldBindValues) {
							bindValues.add(new TrackerFieldBindValue(value.intValue(), trackerFieldBindValue
									.getBind_value_label()));
						}
					}
				}
				FieldValue fieldValue = new FieldValue(
						"", new FieldValueFileInfo[] {}, bindValues.toArray(new TrackerFieldBindValue[bindValues.size()])); //$NON-NLS-1$
				artifactFieldValue = new ArtifactFieldValue(trackerField.getShort_name(), trackerField
						.getLabel(), fieldValue);
			}
		} else if (this.shouldConsider(trackerField.getType())) {
			// Any other value (string, text, integer, float)
			AbstractFieldValue abstractFieldValue = artifact.getFieldValue(trackerField.getField_id());
			if (abstractFieldValue instanceof LiteralFieldValue) {
				LiteralFieldValue literalFieldValue = (LiteralFieldValue)abstractFieldValue;
				String composedValue = literalFieldValue.getFieldValue();

				if (composedValue != null && composedValue.length() > 0
						&& canSubmitValue(trackerField.getType(), composedValue)) {
					FieldValue fieldValue = new FieldValue(composedValue, new FieldValueFileInfo[] {},
							new TrackerFieldBindValue[] {});
					artifactFieldValue = new ArtifactFieldValue(trackerField.getShort_name(), trackerField
							.getLabel(), fieldValue);
				}
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
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.SUBON.equals(trackerFieldType);
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.SUBBY.equals(trackerFieldType);
		shouldConsider = shouldConsider && !ITuleapConfigurationConstants.LUD.equals(trackerFieldType);
		return shouldConsider;
	}

	/**
	 * Returns the list of Tuleap tracker reports available for the given tracker id.
	 * 
	 * @param trackerId
	 *            The tracker id
	 * @param monitor
	 *            The progress monitor
	 * @return The list of Tuleap tracker reports available
	 */
	public List<TuleapTrackerReport> getReports(int trackerId, IProgressMonitor monitor) {
		List<TuleapTrackerReport> reports = new ArrayList<TuleapTrackerReport>();
		try {
			this.login(monitor);

			monitor.subTask(TuleapMylynTasksMessages
					.getString(TuleapMylynTasksMessagesKeys.retrievingTheReports));

			TrackerReport[] trackerReports = tuleapTrackerV5APIPort.getTrackerReports(sessionHash, -1,
					trackerId);
			for (TrackerReport trackerReport : trackerReports) {
				TuleapTrackerReport tuleapTrackerReport = new TuleapTrackerReport(trackerReport.getId(),
						trackerReport.getName());
				reports.add(tuleapTrackerReport);
			}

		} catch (RemoteException e) {
			TuleapCoreActivator.log(e, true);
		} catch (MalformedURLException e) {
			TuleapCoreActivator.log(e, true);
		} catch (ServiceException e) {
			TuleapCoreActivator.log(e, true);
		}

		this.logout();

		return reports;
	}

	/**
	 * Returns the content of the attachment.
	 * 
	 * @param artifactId
	 *            The id of the artifact
	 * @param attachmentId
	 *            The id of the attachment
	 * @param filename
	 *            The name of the file
	 * @param size
	 *            The size of the attachment
	 * @param monitor
	 *            The progress monitor
	 * @return The content of the attachment.
	 * @throws ServiceException
	 *             If a problem appears with the client
	 * @throws RemoteException
	 *             If a problem appears with the server
	 * @throws MalformedURLException
	 *             If the URL is invalid
	 */
	public byte[] getAttachmentContent(int artifactId, int attachmentId, String filename, int size,
			IProgressMonitor monitor) throws MalformedURLException, RemoteException, ServiceException {
		monitor.subTask(TuleapMylynTasksMessages.getString(
				TuleapMylynTasksMessagesKeys.retrievingAttachmentContentFor, filename));

		this.login(monitor);
		monitor.subTask(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.retrievingAttachmentContent));

		String artifactAttachmentChunk = ""; //$NON-NLS-1$
		int downloadedBytes = 0;

		// 5 mega
		final int bufferSize = 5000000;
		while (downloadedBytes < size) {
			int bytesToDownload = bufferSize;
			if (downloadedBytes + bytesToDownload > size) {
				// The buffer is to big, let's scale down
				bytesToDownload = size - downloadedBytes;
			}
			artifactAttachmentChunk += tuleapTrackerV5APIPort.getArtifactAttachmentChunk(sessionHash,
					artifactId, attachmentId, downloadedBytes, bytesToDownload);
			downloadedBytes = downloadedBytes + bytesToDownload;
		}
		byte[] attachmentContent = artifactAttachmentChunk.getBytes();

		this.logout();

		return attachmentContent;
	}

	/**
	 * Uploads the attachment represented by the given descriptor on the tracker.
	 * 
	 * @param trackerId
	 *            The id of the tracker
	 * @param artifactId
	 *            The id of the artifact
	 * @param tuleapAttachmentDescriptor
	 *            The descriptor
	 * @param comment
	 *            The comment
	 * @param monitor
	 *            The progress monitor
	 * @throws ServiceException
	 *             If a problem appears with the client
	 * @throws IOException
	 *             If a problem appears with the read of the file.
	 */
	public void uploadAttachment(int trackerId, int artifactId,
			TuleapAttachmentDescriptor tuleapAttachmentDescriptor, String comment, IProgressMonitor monitor)
			throws ServiceException, IOException {
		this.login(monitor);

		monitor.subTask(TuleapMylynTasksMessages.getString(
				TuleapMylynTasksMessagesKeys.uploadingAttachmentContent, tuleapAttachmentDescriptor
						.getFileName()));
		monitor.subTask(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.retrievingAttachmentContent));

		tuleapTrackerV5APIPort.purgeAllTemporaryAttachments(sessionHash);

		int size = tuleapAttachmentDescriptor.getLength().intValue();

		byte[] bytes = new byte[size];
		tuleapAttachmentDescriptor.getInputStream().read(bytes);
		tuleapAttachmentDescriptor.getInputStream().close();

		int length = bytes.length;
		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		String content = new String(encodeBase64);
		int uploaded = 0;
		int contentSize = content.length();
		// 5 mega
		final int bufferSize = 5000000;

		String attachmentName = tuleapTrackerV5APIPort.createTemporaryAttachment(sessionHash);
		while (uploaded < contentSize) {
			int toUpload = bufferSize;
			if (toUpload > contentSize - uploaded) {
				toUpload = contentSize - uploaded;
			}
			String contentToUpload = content.substring(uploaded, toUpload);
			tuleapTrackerV5APIPort.appendTemporaryAttachmentChunk(sessionHash, attachmentName,
					contentToUpload);

			uploaded = uploaded + contentToUpload.length();
		}

		int submittedBy = session.getUser_id();

		FieldValueFileInfo fi = new FieldValueFileInfo(attachmentName, submittedBy,
				tuleapAttachmentDescriptor.getDescription(), tuleapAttachmentDescriptor.getFileName(),
				length, tuleapAttachmentDescriptor.getFileType(), ""); //$NON-NLS-1$
		FieldValueFileInfo[] fieldValueFileInfos = new FieldValueFileInfo[] {fi };
		FieldValue fieldValue = new FieldValue(null, fieldValueFileInfos, new TrackerFieldBindValue[] {});
		ArtifactFieldValue fv = new ArtifactFieldValue(tuleapAttachmentDescriptor.getFieldName(),
				tuleapAttachmentDescriptor.getFieldLabel(), fieldValue);
		ArtifactFieldValue[] fieldValues = new ArtifactFieldValue[] {fv };
		tuleapTrackerV5APIPort.updateArtifact(sessionHash, -1, -1, artifactId, fieldValues, comment, "UTF-8"); //$NON-NLS-1$

		this.logout();
	}
}
