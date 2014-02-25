/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.server.soap2rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.WebLocation;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.TuleapSoapServiceLocator;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.TuleapTrackerV5APILocatorImpl;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Group;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Session;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UGroupMember;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Ugroup;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.AgileDashBoardSemanticInitialEffort;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactComments;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactQueryResult;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Criteria;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValueFileInfo;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Tracker;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerField;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerFieldBindType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemantic;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticContributor;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticStatus;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticTitle;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerStructure;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APILocator;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.UGroup;

/**
 * Utility class used to initialize the REST server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("restriction")
public class Soap2RestInitializer {
	/**
	 * The SOAP configuration file.
	 */
	private static final String CONFIG_FILE = "org/tuleap/mylyn/task/internal/core/wsdl/soap/client-config.wsdd"; //$NON-NLS-1$

	/**
	 * The URL of the SOAP server.
	 */
	private String soapServerUrl;

	/**
	 * The URL of the REST server.
	 */
	private String restServerUrl;

	/**
	 * The core SOAP API.
	 */
	private CodendiAPIPortType codendiAPIPort;

	/**
	 * The tracker SOAP API.
	 */
	private TuleapTrackerV5APIPortType tuleapTrackerV5APIPort;

	/**
	 * The session hash.
	 */
	private String sessionHash;

	/**
	 * The login of the REST server.
	 */
	private String restLogin;

	/**
	 * The password of the REST server
	 */
	private String restPassword;

	/**
	 * The entry point of the initialization.
	 * 
	 * @param args
	 *            The arguments of the initializer
	 */
	public static void main(String[] args) {
		Soap2RestInitializer soap2RestInitializer = new Soap2RestInitializer(args[0], args[1]);
		soap2RestInitializer.login(args[2], args[3], args[4], args[5]);

		soap2RestInitializer.populateRestProjects();

		soap2RestInitializer.logout();

	}

	/**
	 * The constructor.
	 * 
	 * @param soapServerUrl
	 *            The URL of the SOAP server
	 * @param restServerUrl
	 *            The URL of the REST server
	 */
	public Soap2RestInitializer(String soapServerUrl, String restServerUrl) {
		this.soapServerUrl = soapServerUrl;
		this.restServerUrl = restServerUrl + "/api/v3.14"; //$NON-NLS-1$

		AbstractWebLocation location = new WebLocation(soapServerUrl);
		EngineConfiguration config = new FileProvider(TuleapSoapConnector.class.getClassLoader()
				.getResourceAsStream(CONFIG_FILE));
		TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, location);

		config = new FileProvider(TuleapSoapConnector.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config, location);

		try {
			codendiAPIPort = locator.getCodendiAPIPort(new URL(soapServerUrl + "/soap/")); //$NON-NLS-1$
			tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(new URL(soapServerUrl
					+ "/plugins/tracker/soap/")); //$NON-NLS-1$
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Logs in.
	 * 
	 * @param soapLogin
	 *            The SOAP login
	 * @param soapPassword
	 *            The SOAP password
	 * @param restLogin
	 *            The REST login
	 * @param restPassword
	 *            The REST password
	 */
	@SuppressWarnings("hiding")
	private void login(String soapLogin, String soapPassword, String restLogin, String restPassword) {
		this.restLogin = restLogin;
		this.restPassword = restPassword;

		try {
			Session session = codendiAPIPort.login(soapLogin, soapPassword);
			sessionHash = session.getSession_hash();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the request to the REST server.
	 * 
	 * @param method
	 *            The method (GET, POST, PUT, OPTIONS)
	 * @param url
	 *            The url
	 * @param body
	 *            The JSON body
	 * @return The JSON response
	 */
	private String sendRequest(Method method, String url, String body) {
		if (1 == 1) {
			return ""; //$NON-NLS-1$
		}

		Request request = new Request(method, url);

		Preference<CharacterSet> preferenceCharset = new Preference<CharacterSet>(CharacterSet.UTF_8);
		request.getClientInfo().getAcceptedCharacterSets().add(preferenceCharset);

		Preference<MediaType> preferenceMediaType = new Preference<MediaType>(MediaType.APPLICATION_JSON);
		request.getClientInfo().getAcceptedMediaTypes().add(preferenceMediaType);

		ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, restLogin,
				restPassword);
		request.setChallengeResponse(challengeResponse);

		Representation entity = new StringRepresentation(body, MediaType.APPLICATION_JSON,
				Language.ENGLISH_US, CharacterSet.UTF_8);
		request.setEntity(entity);

		Client c = new Client(Protocol.HTTP);
		Object object = request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
		if (object == null) {
			object = new Form();
			request.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, object);
		}

		Response response = c.handle(request);
		String responseBody = null;
		try {
			if (response.getEntity() != null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				response.getEntity().write(byteArrayOutputStream);
				responseBody = new String(byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseBody;
	}

	/**
	 * Populates the projects of the REST server from the projects of the SOAP server.
	 */
	private void populateRestProjects() {
		String deleteResponse = this.sendRequest(Method.DELETE, this.restServerUrl + "/projects", ""); //$NON-NLS-1$//$NON-NLS-2$
		System.out.println(deleteResponse);

		try {
			Group[] myProjects = codendiAPIPort.getMyProjects(sessionHash);
			for (Group project : myProjects) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("{\n"); //$NON-NLS-1$
				stringBuffer.append("  \"id\": ").append(project.getGroup_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"label\": \"").append(project.getGroup_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer
						.append("  \"unix_name\": \"").append(project.getUnix_group_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"description\": \"").append(project.getDescription()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$

				if (project.getGroup_name().contains("Agile")) { //$NON-NLS-1$
					stringBuffer.append("  \"services\": \"['tracker', 'agile_dashboard']\",\n"); //$NON-NLS-1$
				} else {
					stringBuffer.append("  \"services\": \"['tracker']\",\n"); //$NON-NLS-1$ 
				}

				stringBuffer.append("  \"is_member\": \"true\"\n"); //$NON-NLS-1$
				stringBuffer.append("}"); //$NON-NLS-1$

				String json = stringBuffer.toString();

				System.out.println(json);

				String response = this.sendRequest(Method.POST, this.restServerUrl + "/projects", json); //$NON-NLS-1$
				System.out.println(response);

				this.populateRestUserGroups(project);
				this.populateRestTrackers(project);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populate the user groups available for the given project.
	 * 
	 * @param project
	 *            The project on tuleap
	 */
	private void populateRestUserGroups(Group project) {
		String deleteResponse = this.sendRequest(Method.DELETE, this.restServerUrl
				+ "/projects/" + project.getGroup_id() + "/user_groups", ""); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		System.out.println(deleteResponse);

		try {
			Ugroup[] ugroups = codendiAPIPort.getGroupUgroups(sessionHash, project.getGroup_id());
			for (Ugroup ugroup : ugroups) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("{\n"); //$NON-NLS-1$
				stringBuffer.append("  \"id\": ").append(ugroup.getUgroup_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"name\": \"").append(ugroup.getName()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("}"); //$NON-NLS-1$

				String json = stringBuffer.toString();

				System.out.println(json);

				String response = this.sendRequest(Method.POST, this.restServerUrl
						+ "/projects/" + project.getGroup_id() + "/user_groups", json); //$NON-NLS-1$ //$NON-NLS-2$
				System.out.println(response);

				this.populateRestUsers(ugroup);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populates the users from the given group.
	 * 
	 * @param ugroup
	 *            The user group on tuleap
	 */
	private void populateRestUsers(Ugroup ugroup) {
		String deleteResponse = this.sendRequest(Method.DELETE, this.restServerUrl + "/user_groups/" //$NON-NLS-1$
				+ ugroup.getUgroup_id() + "/users", ""); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(deleteResponse);
		try {
			UGroupMember[] members = ugroup.getMembers();
			for (UGroupMember uGroupMember : members) {
				UserInfo userInfo = codendiAPIPort.getUserInfo(sessionHash, uGroupMember.getUser_id());
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("{\n"); //$NON-NLS-1$
				stringBuffer.append("  \"id\": ").append(userInfo.getId()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"email\": \"").append(userInfo.getEmail()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"identifier\": \"").append(userInfo.getIdentifier()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"real_name\": \"").append(userInfo.getReal_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"username\": \"").append(userInfo.getUsername()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("  \"ldap_id\": \"").append(userInfo.getLdap_id()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("}"); //$NON-NLS-1$

				String json = stringBuffer.toString();

				System.out.println(json);

				String response = this.sendRequest(Method.POST, this.restServerUrl
						+ "/user_groups/" + ugroup.getUgroup_id() + "/users", json); //$NON-NLS-1$ //$NON-NLS-2$
				System.out.println(response);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populate the trackers of a given project.
	 * 
	 * @param project
	 *            The project to use
	 */
	private void populateRestTrackers(Group project) {
		String deleteResponse = this.sendRequest(Method.DELETE, this.restServerUrl + "/projects/" //$NON-NLS-1$
				+ project.getGroup_id() + "/trackers", ""); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(deleteResponse);

		try {
			Tracker[] trackerList = tuleapTrackerV5APIPort.getTrackerList(sessionHash, project.getGroup_id());
			for (Tracker tracker : trackerList) {
				String json = getJsonConfiguration(project, tracker, "trackers"); //$NON-NLS-1$

				String response = this.sendRequest(Method.POST, this.restServerUrl
						+ "/projects/" + project.getGroup_id() + "/trackers", json); //$NON-NLS-1$ //$NON-NLS-2$
				System.out.println(response);

				// Handle Agile configurations
				if (tracker.getName().contains("Sprint")) { //$NON-NLS-1$
					// Milestone
				} else if (tracker.getName().contains("Release")) { //$NON-NLS-1$
					// Milestone
				} else if (tracker.getName().contains("Stories")) { //$NON-NLS-1$
					// Backlog Item && card
				} else if (tracker.getName().contains("Epics")) { //$NON-NLS-1$
					// Backlog Item
				} else if (tracker.getName().contains("Tasks")) { //$NON-NLS-1$
					// Card
				}

				this.populateRestArtifacts(project, tracker);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populates the artifacts from the given tracker.
	 * 
	 * @param project
	 *            The project
	 * @param tracker
	 *            The tracker
	 */
	private void populateRestArtifacts(Group project, Tracker tracker) {
		String deleteResponse = this.sendRequest(Method.DELETE, this.restServerUrl + "/trackers/" //$NON-NLS-1$
				+ tracker.getTracker_id() + "/artifacts", ""); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(deleteResponse);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$

			ArtifactQueryResult artifactQueryResult = tuleapTrackerV5APIPort.getArtifacts(sessionHash,
					project.getGroup_id(), tracker.getTracker_id(), new Criteria[0], 0, 9999);

			Artifact[] artifacts = artifactQueryResult.getArtifacts();
			for (Artifact artifact : artifacts) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("{\n"); //$NON-NLS-1$
				stringBuffer.append("  \"id\": ").append(artifact.getArtifact_id()).append(",\n"); //$NON-NLS-1$//$NON-NLS-2$
				stringBuffer.append("  \"kind\": \"").append(tracker.getItem_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer
						.append("  \"url\": \"").append(restServerUrl).append("/artifacts/").append(artifact.getArtifact_id()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				stringBuffer
						.append("  \"html_url\": \"").append(soapServerUrl).append("/plugins/tracker/?aid=").append(artifact.getArtifact_id()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				stringBuffer.append("  \"tracker_id\": ").append(tracker.getTracker_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer
						.append("  \"submitted_by\": \"").append(artifact.getSubmitted_by()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.valueOf(artifact.getSubmitted_on()).longValue() * 1000);
				Date date = calendar.getTime();

				stringBuffer.append("  \"submitted_on\": \"").append(dateFormat.format(date)).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$

				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.valueOf(artifact.getLast_update_date()).longValue() * 1000);
				date = calendar.getTime();

				stringBuffer.append("  \"last_modified_date\": \"").append(artifact).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$

				ArtifactFieldValue[] values = artifact.getValue();
				if (values != null && values.length > 0) {
					for (ArtifactFieldValue artifactFieldValue : values) {
						FieldValue field_value = artifactFieldValue.getField_value();

						String value = field_value.getValue();

						TrackerFieldBindValue[] bind_value = field_value.getBind_value();

						FieldValueFileInfo[] file_info = field_value.getFile_info();
					}
				}
				stringBuffer.append("  \"values\": {\n"); //$NON-NLS-1$

				stringBuffer.append("  }\n"); //$NON-NLS-1$

				ArtifactComments[] artifactComments = tuleapTrackerV5APIPort.getArtifactComments(sessionHash,
						artifact.getArtifact_id());
				if (artifactComments != null && artifactComments.length > 0) {
					stringBuffer.append("  \"changesets\": {\n"); //$NON-NLS-1$

					int cptComments = 0;
					for (ArtifactComments artifactComment : artifactComments) {
						calendar = Calendar.getInstance();
						calendar.setTimeInMillis(Long.valueOf(artifactComment.getSubmitted_on()).longValue() * 1000);
						date = calendar.getTime();

						cptComments++;
						stringBuffer.append("    {\n"); //$NON-NLS-1$
						stringBuffer
								.append("      \"submitted_by\": \"").append(artifactComment.getSubmitted_by()).append("\"\n"); //$NON-NLS-1$ //$NON-NLS-2$
						stringBuffer
								.append("      \"submitted_on\": \"").append(dateFormat.format(date)).append("\"\n"); //$NON-NLS-1$ //$NON-NLS-2$
						stringBuffer
								.append("      \"body\": \"").append(artifactComment.getBody()).append("\"\n"); //$NON-NLS-1$ //$NON-NLS-2$

						if (artifactComments.length > cptComments) {
							stringBuffer.append("    },\n"); //$NON-NLS-1$
						} else {
							stringBuffer.append("    }\n"); //$NON-NLS-1$
						}
					}

					stringBuffer.append("  }\n"); //$NON-NLS-1$
				}

				stringBuffer.append("}"); //$NON-NLS-1$

				String json = stringBuffer.toString();

				System.out.println(json);

				String response = this.sendRequest(Method.POST, this.restServerUrl + "/trackers/" //$NON-NLS-1$
						+ tracker.getTracker_id() + "/artifacts", json); //$NON-NLS-1$
				System.out.println(response);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the JSON configuration of the given tracker.
	 * 
	 * @param project
	 *            The project containing the tracker
	 * @param tracker
	 *            The tracker
	 * @param kind
	 *            The kind of configuration created
	 * @return The JSON configuration of the given tracker
	 * @throws RemoteException
	 *             In case of issue with the SOAP connection
	 */
	private String getJsonConfiguration(Group project, Tracker tracker, String kind) throws RemoteException {
		TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionHash, project
				.getGroup_id(), tracker.getTracker_id());

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{\n"); //$NON-NLS-1$
		stringBuffer.append("  \"id\": ").append(tracker.getTracker_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
		stringBuffer.append("  \"name\": \"").append(tracker.getItem_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
		stringBuffer.append("  \"item_name\": \"").append(tracker.getItem_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
		stringBuffer.append("  \"description\": \"").append(tracker.getDescription()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
		stringBuffer
				.append("  \"url\": \"").append(restServerUrl).append("/").append(kind).append("/").append(tracker.getTracker_id()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		stringBuffer
				.append("  \"html_url\": \"").append(soapServerUrl).append("/plugins/tracker/?tracker=").append(tracker.getTracker_id()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		stringBuffer.append("  \"project_id\": ").append(tracker.getGroup_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
		stringBuffer.append("  \"fields\": [\n"); //$NON-NLS-1$

		// Fields
		int cpt = 0;
		for (TrackerField trackerField : trackerFields) {
			stringBuffer.append("    {\n"); //$NON-NLS-1$
			stringBuffer.append("      \"field_id\": ").append(trackerField.getField_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
			stringBuffer
					.append("      \"short_name\": \"").append(trackerField.getShort_name()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
			stringBuffer.append("      \"label\": \"").append(trackerField.getLabel()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
			stringBuffer.append("      \"type\": \"").append(trackerField.getType()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$

			TrackerFieldBindType binding = trackerField.getBinding();
			if ("users".equals(binding.getBind_type())) { //$NON-NLS-1$
				UGroup[] bind_list = binding.getBind_list();
				stringBuffer.append("      \"binding\": {\n"); //$NON-NLS-1$
				stringBuffer
						.append("        \"bind_type\": \"").append(binding.getBind_type()).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("        \"bind_list\": [\n"); //$NON-NLS-1$

				int cptBinding = 0;
				for (UGroup uGroup : bind_list) {
					stringBuffer.append("          {\n"); //$NON-NLS-1$
					stringBuffer
							.append("            \"user_group_id\": ").append(uGroup.getUgroup_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
					stringBuffer
							.append("            \"user_group_name\": \"").append(uGroup.getName()).append("\"\n"); //$NON-NLS-1$ //$NON-NLS-2$
					cptBinding++;
					if (bind_list.length > cptBinding) {
						stringBuffer.append("          },\n"); //$NON-NLS-1$
					} else {
						stringBuffer.append("          }\n"); //$NON-NLS-1$
					}

				}

				stringBuffer.append("        ]\n"); //$NON-NLS-1$

				stringBuffer.append("      },\n"); //$NON-NLS-1$
			} else {
				TrackerFieldBindValue[] values = trackerField.getValues();
				if (values != null && values.length > 0) {
					stringBuffer.append("      \"values\": [\n"); //$NON-NLS-1$
					int cptValues = 0;
					for (TrackerFieldBindValue trackerFieldBindValue : values) {
						stringBuffer.append("        {\n"); //$NON-NLS-1$ 
						stringBuffer
								.append("          \"field_value_id\": ").append(trackerFieldBindValue.getBind_value_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
						stringBuffer
								.append("          \"field_value_label\": \"").append(trackerFieldBindValue.getBind_value_label()).append("\"\n"); //$NON-NLS-1$ //$NON-NLS-2$

						if (values.length > cptValues + 1) {
							stringBuffer.append("        },\n"); //$NON-NLS-1$ 
						} else {
							stringBuffer.append("        }\n"); //$NON-NLS-1$
						}
						cptValues++;
					}

					stringBuffer.append("      ],\n"); //$NON-NLS-1$
				}
			}

			stringBuffer.append("      \"permissions\": [\n"); //$NON-NLS-1$ 
			String[] permissions = trackerField.getPermissions();
			int cptPerm = 0;
			for (String permission : permissions) {
				if (permissions.length > cptPerm + 1) {
					stringBuffer.append("        \"").append(permission).append("\",\n"); //$NON-NLS-1$ //$NON-NLS-2$							
				} else {
					stringBuffer.append("        \"").append(permission).append("\"\n"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				cptPerm++;
			}
			stringBuffer.append("      ]\n"); //$NON-NLS-1$ 

			cpt++;

			if (trackerFields.length > cpt) {
				stringBuffer.append("    },\n"); //$NON-NLS-1$
			} else {
				stringBuffer.append("    }\n"); //$NON-NLS-1$
			}
		}

		stringBuffer.append("  ],\n"); //$NON-NLS-1$

		// Semantic
		TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(sessionHash, project
				.getGroup_id(), tracker.getTracker_id());
		TrackerSemantic semantic = trackerStructure.getSemantic();

		TrackerSemanticTitle title = semantic.getTitle();
		TrackerSemanticStatus status = semantic.getStatus();
		TrackerSemanticContributor contributor = semantic.getContributor();
		AgileDashBoardSemanticInitialEffort initial_effort = semantic.getInitial_effort();

		stringBuffer.append("  \"semantic\": {\n"); //$NON-NLS-1$

		for (TrackerField field : trackerFields) {
			// Title
			if (title != null && field.getShort_name().equals(title.getField_name())) {
				stringBuffer.append("    \"title\": {\n"); //$NON-NLS-1$
				stringBuffer.append("      \"field_id\": ").append(field.getField_id()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
				if (status != null || contributor != null || initial_effort != null) {
					stringBuffer.append("    },\n"); //$NON-NLS-1$
				} else {
					stringBuffer.append("    }\n"); //$NON-NLS-1$
				}
			}
		}

		for (TrackerField field : trackerFields) {
			// Status
			if (status != null && field.getShort_name().equals(status.getField_name())) {
				stringBuffer.append("    \"status\": {\n"); //$NON-NLS-1$
				stringBuffer.append("      \"field_id\": ").append(field.getField_id()).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("      \"open_status_field_value_ids\": [\n"); //$NON-NLS-1$

				int[] values = status.getValues();
				int cptStatus = 0;
				for (int value : values) {
					cptStatus++;
					if (values.length > cptStatus) {
						stringBuffer.append("        ").append(value).append(",\n"); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						stringBuffer.append("        ").append(value).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}

				stringBuffer.append("      ]\n"); //$NON-NLS-1$
				if (contributor != null || initial_effort != null) {
					stringBuffer.append("    },\n"); //$NON-NLS-1$
				} else {
					stringBuffer.append("    }\n"); //$NON-NLS-1$
				}
			}
		}

		for (TrackerField field : trackerFields) {
			// Contributor
			if (contributor != null && field.getShort_name().equals(contributor.getField_name())) {
				stringBuffer.append("    \"contributors\": {\n"); //$NON-NLS-1$
				stringBuffer.append("      \"field_id\": ").append(field.getField_id()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
				if (initial_effort != null) {
					stringBuffer.append("    },\n"); //$NON-NLS-1$
				} else {
					stringBuffer.append("    }\n"); //$NON-NLS-1$
				}
			}
		}

		for (TrackerField field : trackerFields) {
			// Initial Effort
			if (initial_effort != null && field.getShort_name().equals(initial_effort.getField_name())) {
				stringBuffer.append("    \"initial_effort\": {\n"); //$NON-NLS-1$
				stringBuffer.append("      \"field_id\": ").append(field.getField_id()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
				stringBuffer.append("    }\n"); //$NON-NLS-1$
			}
		}

		stringBuffer.append("  }\n"); //$NON-NLS-1$

		stringBuffer.append("}"); //$NON-NLS-1$

		String json = stringBuffer.toString();

		System.out.println(json);
		return json;
	}

	/**
	 * Logs out.
	 */
	private void logout() {
		try {
			codendiAPIPort.logout(sessionHash);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
