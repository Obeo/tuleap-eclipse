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

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.net.TuleapAttachmentDescriptor;

/**
 * This class will be used to communicate with the server with a higher level of abstraction than raw HTTP
 * connections.
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
	private TuleapRestConnector tuleapRestConnector;

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	/**
	 * The constructor.
	 * 
	 * @param tuleapRestConnector
	 *            The connector is used for HTTP communication
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapServer(TuleapRestConnector tuleapRestConnector, TaskRepository taskRepository) {
		this.tuleapRestConnector = tuleapRestConnector;
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
	private IStatus testConnection(IProgressMonitor monitor) {
		// Try to send a request to the best supported version of the API

		// If we receive a 200 OK, the connection is good

		// If we receive a 301 Moved Permanently, a new compatible version of the API is available, use it

		// If we receive a 410 Gone, the server does not support the required API, the connector cannot work

		// If we receive a 404 Not Found, the URL of server is invalid or the server is offline

		return null;
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

		// If the connection is valid, try to login with the credentials of the repository

		// Retrieve the session to ensure that we are correctly logged in

		// Try to logout
		return null;
	}

	/**
	 * Logs the user in the server and returns the session hash used for the other operations.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The session hash
	 */
	private String login(IProgressMonitor monitor) {
		// Send a request with OPTIONS to ensure that we can log in and that we have the right to log in.

		// Try to log in using the credentials of the repository
		this.taskRepository.getCredentials(AuthenticationType.HTTP);

		// If we succeed in logging the user in, return the session hash
		return null;
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
	private IStatus logout(String sessionHash, IProgressMonitor monitor) {
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
