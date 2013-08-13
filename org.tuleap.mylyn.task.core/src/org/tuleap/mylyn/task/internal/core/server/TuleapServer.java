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
package org.tuleap.mylyn.task.internal.core.server;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.net.TuleapAttachmentDescriptor;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

/**
 * This class will be used to communicate with the server with a higher level of abstraction than raw HTTP
 * connections. This class will not have any relation with the framework used to parse the JSON nor with the
 * framework used to communicate with the server in order to be easily testable.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapServer {

	/**
	 * The version number of the best api supported.
	 */
	public static final String BEST_SUPPORTED_API_VERSION = ITuleapAPIVersions.V1_0;

	/**
	 * Utility class used to communicate using HTTP with the server.
	 */
	private final TuleapRestConnector tuleapRestConnector;

	/**
	 * The JSON parser.
	 */
	private final TuleapJsonParser tuleapJsonParser;

	/**
	 * The JSON serializer.
	 */
	private final TuleapJsonSerializer tuleapJsonSerializer;

	/**
	 * The task repository.
	 */
	private final TaskRepository taskRepository;

	/**
	 * The version of the API to use.
	 */
	private String apiVersion = BEST_SUPPORTED_API_VERSION;

	/**
	 * The constructor.
	 * 
	 * @param tuleapRestConnector
	 *            The connector is used for HTTP communication
	 * @param tuleapJsonParser
	 *            The Tuleap JSON parser
	 * @param tuleapJsonSerializer
	 *            The Tuleap JSON serializer
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapServer(TuleapRestConnector tuleapRestConnector, TuleapJsonParser tuleapJsonParser,
			TuleapJsonSerializer tuleapJsonSerializer, TaskRepository taskRepository) {
		this.tuleapRestConnector = tuleapRestConnector;
		this.tuleapJsonParser = tuleapJsonParser;
		this.tuleapJsonSerializer = tuleapJsonSerializer;
		this.taskRepository = taskRepository;
	}

	/**
	 * Tests that the connection with the server is functional by using sending a request to the server. The
	 * request will be an OPTIONS to the root of the server to ensure that a compatible version of the API is
	 * available and working.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return A status indicating if the server supports a version of the API compatible with the one
	 *         expected by the connector
	 */
	protected IStatus testConnection(IProgressMonitor monitor) {
		IStatus status = null;

		// Try to send a request to the best supported version of the API
		ServerResponse serverResponse = this.tuleapRestConnector.sendRequest(BEST_SUPPORTED_API_VERSION,
				Resource.API__OPTIONS, Maps.<String, String> newHashMap(), null);

		switch (serverResponse.getStatus()) {
			case ITuleapServerStatus.OK:
				// If we receive a 200 OK, the connection is good
				status = Status.OK_STATUS;
				break;
			case ITuleapServerStatus.MOVED:
				// If we receive a 301 Moved Permanently, a new compatible version of the API is available
				Map<String, String> headers = serverResponse.getHeaders();
				String newApiLocation = headers.get(ITuleapHeaders.LOCATION);

				// Parse the new location to get the api version segment.
				if (newApiLocation != null) {
					int indexOfAPIPrefix = newApiLocation.indexOf(ITuleapAPIVersions.API_PREFIX);
					if (indexOfAPIPrefix != -1) {
						String newApiVersion = newApiLocation.substring(indexOfAPIPrefix
								+ ITuleapAPIVersions.API_PREFIX.length());
						this.apiVersion = newApiVersion;
					} else {
						// Error, invalid behavior of the server, invalid location
						status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
								TuleapMylynTasksMessages.getString("TuleapServer.InvalidAPILocation", //$NON-NLS-1$
										newApiLocation));
					}
				} else {
					// Error, invalid behavior of the server, no new location
					status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
							TuleapMylynTasksMessages.getString("TuleapServer.MissingAPILocation")); //$NON-NLS-1$
				}
				break;
			case ITuleapServerStatus.GONE:
				// If we receive a 410 Gone, the server does not support the required API, the connector
				// cannot work with this server
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString("TuleapServer.MissingCompatibleAPI", BEST_SUPPORTED_API_VERSION)); //$NON-NLS-1$
				break;
			case ITuleapServerStatus.NOT_FOUND:
				// If we receive a 404 Not Found, the URL of server is invalid or the server is offline
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString("TuleapServer.APINotFound")); //$NON-NLS-1$
				break;
			default:
				// Unknown error, invalid behavior of the server?
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString("TuleapServer.InvalidBehavior", Integer //$NON-NLS-1$
								.valueOf(serverResponse.getStatus())));
				break;
		}

		return status;
	}

	/**
	 * Tests that we can use the repository by validating that the server provides a compatible version of the
	 * API using {@link TuleapServer#testConnection(IProgressMonitor)} then we try to log in using the
	 * credentials from the task repository, we check the user session and we try to log out.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return A status indicating if the server support a version of the API compatible with the one expected
	 *         by the connector and if we can successfully log in and log out from the server
	 */
	public IStatus validateConnection(IProgressMonitor monitor) {
		// Test the connection first
		IStatus testConnectionStatus = this.testConnection(monitor);
		if (testConnectionStatus.isOK()) {
			String sessionHash = this.login(monitor);
			if (sessionHash != null) {
				IStatus logoutStatus = this.logout(sessionHash, monitor);
				return logoutStatus;
			}
		}

		// If the connection status is not ok, do not try to go further and return the error directly
		return testConnectionStatus;
	}

	/**
	 * Logs the user in the server and returns the session hash used for the other operations.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The session hash
	 */
	protected String login(IProgressMonitor monitor) {
		String sessionHash = null;

		// Send a request with OPTIONS to ensure that we can log in and that we have the right to log in.
		ServerResponse loginOptionsServerResponse = this.tuleapRestConnector.sendRequest(this.apiVersion,
				Resource.LOGIN__OPTIONS, Maps.<String, String> newHashMap(), null);

		// Check the available operations
		Map<String, String> headers = loginOptionsServerResponse.getHeaders();
		String allowHeaderEntry = headers.get(ITuleapHeaders.ALLOW);
		String corsAllowMethodsHeaderEntry = headers.get(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS);

		boolean canLogin = allowHeaderEntry != null
				&& allowHeaderEntry.contains(Resource.Operation.GET.toString());
		boolean isAuthorizedToLogin = corsAllowMethodsHeaderEntry != null
				&& corsAllowMethodsHeaderEntry.contains(Resource.Operation.GET.toString());

		if (ITuleapServerStatus.OK == loginOptionsServerResponse.getStatus() && canLogin
				&& isAuthorizedToLogin) {
			// Try to log in with the credentials of the repository
			String username = this.taskRepository.getCredentials(AuthenticationType.REPOSITORY).getUserName();
			String password = this.taskRepository.getCredentials(AuthenticationType.REPOSITORY).getPassword();

			// Create the JSON representation of the login to send to the server
			String serializedLogin = this.tuleapJsonSerializer.serializeLogin(username, password);

			ServerResponse loginGetServerResponse = this.tuleapRestConnector.sendRequest(this.apiVersion,
					Resource.LOGIN__GET, Maps.<String, String> newHashMap(), serializedLogin);

			if (ITuleapServerStatus.OK == loginGetServerResponse.getStatus()) {
				// If we succeed in logging the user in, return the session hash
				String loginResponseBody = loginGetServerResponse.getBody();
				sessionHash = this.tuleapJsonParser.getSessionHash(loginResponseBody);
			} else {
				// TODO Invalid login? server error?
			}
		} else {
			// TODO cannot login? not authorized to login? server error?
		}

		return sessionHash;
	}

	/**
	 * Logs the user out of the server.
	 * 
	 * @param sessionHash
	 *            The hash of the session
	 * @param monitor
	 *            Used to monitor the progress
	 * @return A status indicating if we succeed in logging out of the server
	 */
	protected IStatus logout(String sessionHash, IProgressMonitor monitor) {
		// Send a request with OPTIONS to ensure that we can log out and that we have the right to log out

		// Try to log our using the session hash

		// If we succeed in logging the user out, return an OK status
		return null;
	}

	/**
	 * Returns the configuration of the Tuleap server.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The configuration of the server
	 */
	public TuleapInstanceConfiguration getTuleapServerConfiguration(IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to get the projects

		// Retrieve the projects and create the configuration of each project

		// For each project that has the tracker service

		// Send a request with OPTIONS to ensure that we can and have the right to get the trackers

		// Retrieve the trackers and create the configuration of each tracker

		// Put the configuration of the trackers in their containing project's configuration

		// Try to log out
		return null;
	}

	// get user groups of a project

	// get users of a group

	/**
	 * Retrieves the artifact from the server with the given artifact id.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The task data of the artifact
	 */
	public TaskData getArtifact(int artifactId, IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to retrieve the artifact

		// Retrieve the artifact

		// Create the task data

		// Populate the task data using task mappers

		// Try to log out
		return null;
	}

	/**
	 * Updates the artifact represented by the given task data with the given task data.
	 * 
	 * @param taskData
	 *            The task data
	 * @param monitor
	 *            Used to monitor the progress
	 * @return A status indicating if the artifact was successfully updated
	 */
	public IStatus updateArtifact(TaskData taskData, IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to update the artifact

		// Compute the change to send

		// Send the update to the server for the attribute of the task

		// See if we need to update additional tasks (cardwall, planning, etc)

		// Send the update of the other artifacts

		// Try to log out
		return null;
	}

	/**
	 * Creates the artifact on the server.
	 * 
	 * @param taskData
	 *            The task data of the artifact to create
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The identifier of the artifact created
	 */
	public int createArtifact(TaskData taskData, IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to create the artifact

		// Create the artifact

		// See if we need to update additional tasks (cardwall, planning, etc)

		// Send the update of the other artifacts

		// Try to log out
		return -1;
	}

	/**
	 * Returns all the reports of the tracker with the given tracker identifier.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The list of all the reports of the tracker
	 */
	public List<TuleapTrackerReport> getReports(int trackerId, IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to retrieve the reports

		// Retrieve the reports

		// Try to log out
		return null;
	}

	/**
	 * Runs the report on the server for the tracker with the given tracker identifier.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param reportId
	 *            The identifier of the report
	 * @param collector
	 *            The task data collector
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The number of artifact retrieved
	 */
	public int executeReport(int trackerId, int reportId, TaskDataCollector collector,
			IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to run a report

		// Run the report

		// Create the task data from the result

		// Put them in the task data collector

		// Try to log out
		return -1;
	}

	/**
	 * Runs the query on the server for the tracker with the given tracker identifier.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param criteras
	 *            The criteras of the query
	 * @param collector
	 *            The task data collector
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The number of artifact retrieved
	 */
	public int executeQuery(int trackerId, Map<String, String> criteras, TaskDataCollector collector,
			IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to run a query

		// Run the report

		// Create the task data from the result

		// Put them in the task data collector

		// Try to log out
		return -1;
	}

	/**
	 * Retrieve the content of the attachment with the given attachment identifier.
	 * 
	 * @param attachmentId
	 *            The identifier of the attachment
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The content of the attachment
	 */
	public byte[] getAttachmentContent(int attachmentId, IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to retrieve the file

		// Retrieve the details of the file from the OPTIONS request (size)

		// Download a chunk of the file until completion

		// Assemble the whole file in an array of byte

		// Try to log out

		return null;
	}

	/**
	 * Uploads an attachment to the server for the given artifact.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 * @param attachmentFieldId
	 *            The identifier of the file field in the artifact
	 * @param tuleapAttachmentDescriptor
	 *            The descriptor of the attachment
	 * @param comment
	 *            The comment
	 * @param monitor
	 *            Used to monitor the progress
	 * @return A status indicating if the attachment was successfully uploaded
	 */
	public IStatus uploadAttachment(int artifactId, int attachmentFieldId,
			TuleapAttachmentDescriptor tuleapAttachmentDescriptor, String comment, IProgressMonitor monitor) {
		// Test the connection

		// Try to log in

		// Send a request with OPTIONS to ensure that we can and have the right to upload a file

		// Send the first chunk of data to create a temporary file

		// Retrieve the upload id as a response

		// Send the remaining chunk using the upload id

		// Send a request with POST in order to complete the upload of the temporary file

		// Send a request with OPTIONS to ensure that we can and have the right to update the artifact

		// Update the artifact with a new attachment

		// Try to log out
		return null;
	}
}
