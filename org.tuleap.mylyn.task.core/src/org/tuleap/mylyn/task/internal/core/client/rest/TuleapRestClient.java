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
package org.tuleap.mylyn.task.internal.core.client.rest;

import com.google.common.collect.Lists;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.restlet.data.Method;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.TuleapDebugPart;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorMessage;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorPart;
import org.tuleap.mylyn.task.internal.core.model.TuleapToken;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapAttachmentDescriptor;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapBacklogItemSerializer;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapCardSerializer;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * This class will be used to communicate with the server with a higher level of abstraction than raw HTTP
 * connections. This class will not have any relation with the framework used to parse the JSON nor with the
 * framework used to communicate with the server in order to be easily testable.
 * <p>
 * All the operations that can be used from the editor will throw {@link CoreException} in order to let the
 * editor catch these exceptions. All the operations that cannot be used from an editor will instead log their
 * errors using the logger.
 * </p>
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapRestClient {

	/**
	 * The JSON parser.
	 */
	private final TuleapJsonParser jsonParser;

	/**
	 * The JSON serializer.
	 */
	private final TuleapJsonSerializer jsonSerializer;

	/**
	 * The task repository.
	 */
	private final TaskRepository taskRepository;

	/**
	 * The logger.
	 */
	private ILog logger;

	/**
	 * Factory of RESt resources.
	 */
	private RestResourceFactory restResourceFactory;

	/**
	 * The current authentication token.
	 */
	private TuleapToken token;

	/**
	 * The constructor.
	 * 
	 * @param resourceFactory
	 *            The RESt resource factory to use
	 * @param jsonParser
	 *            The Tuleap JSON parser
	 * @param jsonSerializer
	 *            The Tuleap JSON serializer
	 * @param taskRepository
	 *            The task repository
	 * @param logger
	 *            The logger
	 */
	public TuleapRestClient(RestResourceFactory resourceFactory, TuleapJsonParser jsonParser,
			TuleapJsonSerializer jsonSerializer, TaskRepository taskRepository, ILog logger) {
		this.restResourceFactory = resourceFactory;
		this.jsonParser = jsonParser;
		this.jsonSerializer = jsonSerializer;
		this.taskRepository = taskRepository;
		this.logger = logger;
	}

	/**
	 * Tests that we can use the repository by validating that the server provides a compatible version of the
	 * API and then trying to log in using the credentials from the task repository, checking the user
	 * session, and logging out.
	 * 
	 * @param monitor
	 *            Used to monitor the progress
	 * @return A status indicating if the server support a version of the API compatible with the one expected
	 *         by the connector and if we can successfully log in and log out from the server
	 * @throws CoreException
	 *             In case of error during the log in or log out process
	 */
	public IStatus validateConnection(IProgressMonitor monitor) throws CoreException {
		// FIXME when Tuleap has a convenient route
		// resourceFactory.user().get().checkedRun();
		return Status.OK_STATUS;
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
	public TuleapServer getServer(IProgressMonitor monitor) throws CoreException {
		TuleapServer tuleapServer = new TuleapServer(this.taskRepository.getRepositoryUrl());
		tuleapServer.setLastUpdate(new Date().getTime());

		RestResource restProjects = restResourceFactory.projects().withToken(token);

		// Retrieve the projects and create the configuration of each project
		ServerResponse projectsGetServerResponse = restProjects.get().run();

		checkServerError(restProjects, Method.GET.toString(), projectsGetServerResponse);

		String projectsGetResponseBody = projectsGetServerResponse.getBody();
		List<TuleapProject> projects = this.jsonParser.parseProjectConfigurations(projectsGetResponseBody);

		// For each project that has the tracker service
		for (TuleapProject project : projects) {
			tuleapServer.addProject(project);

			loadPlanningsInto(project);

			// TODO SBE Restore this code!

			// if (projectConfig.hasService(ITuleapProjectServices.TRACKERS)) {
			// // Check that we can get the list of trackers for this project
			// RestProjectsTrackers restTrackers = restResources.projectsTrackers(projectConfig
			// .getIdentifier());
			//
			// // Retrieve the trackers and create the configuration of each tracker
			// ServerResponse projectTrackersGetServerResponse = restTrackers.get(Collections
			// .<String, String> emptyMap());
			// // TODO Pagination on trackers?
			// if (ITuleapServerStatus.OK == projectTrackersGetServerResponse.getStatus()) {
			// String projectTrackersGetResponseBody = projectTrackersGetServerResponse.getBody();
			// List<TuleapTrackerConfiguration> trackerConfigurations = this.jsonParser
			// .getTrackerConfigurations(projectTrackersGetResponseBody);
			// // Put the configuration of the trackers in their containing project's
			// // configuration
			// for (TuleapTrackerConfiguration trackerConfig : trackerConfigurations) {
			// projectConfig.addTracker(trackerConfig);
			// }
			//
			// // adding the properties parent/children to trackers
			// JsonParser trackersParser = new JsonParser();
			// JsonArray trackersArray = trackersParser.parse(projectTrackersGetResponseBody)
			// .getAsJsonArray();
			//
			// for (int i = 0; i < trackersArray.size(); i++) {
			// JsonObject tracker = (JsonObject)trackersArray.get(i);
			//							int trackerId = tracker.get("id").getAsInt(); //$NON-NLS-1$
			//							JsonObject hierarchy = tracker.get("hierarchy").getAsJsonObject(); //$NON-NLS-1$
			//							int parentTrackerId = hierarchy.get("parent_tracker_id").getAsInt(); //$NON-NLS-1$
			//
			// projectConfig.getTrackerConfiguration(trackerId).setParentTracker(
			// projectConfig.getTrackerConfiguration(parentTrackerId));
			// }
			//
			// } else {
			// // Invalid login? server error?
			// String message = this.jsonParser.getErrorMessage(projectTrackersGetServerResponse
			// .getBody());
			// throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
			// message));
			// }
			// }
		}

		return tuleapServer;
	}

	/**
	 * Loads the planning into a given project configuration after fetching them from the remote server via
	 * the REST API.
	 * 
	 * @param project
	 *            The project in which plannings must be loaded
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public void loadPlanningsInto(TuleapProject project) throws CoreException {
		// Retrieve the plannings of the project
		RestResource plannings = restResourceFactory.projectPlannings(project.getIdentifier()).withToken(
				token);
		RestOperation operation = plannings.get();
		ServerResponse planningsResponse = operation.checkedRun();
		List<TuleapPlanning> planningList = jsonParser.parsePlanningList(planningsResponse.getBody());
		for (TuleapPlanning planning : planningList) {
			project.addPlanning(planning);
		}
	}

	/**
	 * Throws a CoreException that encapsulates useful info about a server error.
	 * 
	 * @param restResource
	 *            The restResource that returned the given response.
	 * @param method
	 *            The HTTP method invoked
	 * @param response
	 *            The error response received from the server.
	 * @throws CoreException
	 *             If the given response does not have a status OK (200).
	 */
	private void checkServerError(RestResource restResource, String method, ServerResponse response)
			throws CoreException {
		if (!response.isOk()) {
			TuleapErrorMessage message = jsonParser.getErrorMessage(response.getBody());
			TuleapErrorPart errorPart = message.getError();
			TuleapDebugPart debugPart = message.getDebug();
			String msg;
			if (errorPart == null) {
				msg = response.getBody();
			} else {
				if (debugPart != null) {
					msg = TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.errorReturnedByServer, restResource.getUrl(),
							method, Integer.valueOf(errorPart.getCode()), errorPart.getMessage(), debugPart
									.getSource());
				} else {
					msg = TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.errorReturnedByServer, restResource.getUrl(),
							method, Integer.valueOf(errorPart.getCode()), errorPart.getMessage(),
							"no debug information provided"); //$NON-NLS-1$
				}
			}
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, msg));
		}
	}

	/**
	 * Throws a CoreException that encapsulates useful info about a server error.
	 * 
	 * @param restOperation
	 *            The RESt operation that returned the given response.
	 * @param response
	 *            The error response received from the server.
	 * @throws CoreException
	 *             If the given response does not have a status OK (200).
	 */
	private void checkServerError(RestOperation restOperation, ServerResponse response) throws CoreException {
		if (!response.isOk()) {
			TuleapErrorMessage message = jsonParser.getErrorMessage(response.getBody());
			TuleapErrorPart errorPart = message.getError();
			TuleapDebugPart debugPart = message.getDebug();
			String msg;
			if (errorPart == null) {
				msg = response.getBody();
			} else {
				if (debugPart != null) {
					msg = TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.errorReturnedByServer, restOperation.getUrl(),
							restOperation.getMethodName(), Integer.valueOf(errorPart.getCode()), errorPart
									.getMessage(), debugPart.getSource());
				} else {
					msg = TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.errorReturnedByServer, restOperation.getUrl(),
							restOperation.getMethodName(), Integer.valueOf(errorPart.getCode()), errorPart
									.getMessage(), "no debug information provided"); //$NON-NLS-1$
				}
			}
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, msg));
		}
	}

	// TODO get user groups of a project

	// TODO get users of a group

	/**
	 * Creates an authorization token on the server and stores it so that it can be used.
	 * 
	 * @param monitor
	 *            The progress monitor.
	 * @throws CoreException
	 *             In case of error during the authentication.
	 */
	public void login(IProgressMonitor monitor) throws CoreException {
		RestResource restBacklogItem = restResourceFactory.tokens();

		AuthenticationCredentials credentials = taskRepository.getCredentials(AuthenticationType.REPOSITORY);
		// Credentials can be null?
		if (credentials != null) {
			String credentialsToPost = getCredentials(credentials);
			// Send the POST request
			RestOperation postOperation = restBacklogItem.post().withBody(credentialsToPost);
			ServerResponse response = postOperation.run();
			checkServerError(postOperation, response);
			GsonBuilder builder = new GsonBuilder()
					.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
			Gson gson = builder.create();
			this.token = gson.fromJson(response.getBody(), TuleapToken.class);
		}
	}

	/**
	 * Deletes the current authentication token.
	 * 
	 * @param monitor
	 *            The progress monitor to use.
	 */
	public void logout(IProgressMonitor monitor) {
		// TODO
	}

	/**
	 * Create the POST token body.
	 * 
	 * @param credentials
	 *            The task repository credentials
	 * @return The POST token body
	 */
	private String getCredentials(AuthenticationCredentials credentials) {
		JsonObject tokenObject = new JsonObject();
		tokenObject.add("username", new JsonPrimitive(credentials.getUserName())); //$NON-NLS-1$
		tokenObject.add("password", new JsonPrimitive(credentials.getPassword())); //$NON-NLS-1$
		return tokenObject.toString();
	}

	/**
	 * Retrieves the artifact from the server with the given artifact id.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 * @param connector
	 *            The tuleap repository connector
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The task data of the artifact
	 * @throws CoreException
	 *             In case of error during the retrieval of the artifact
	 */
	public TuleapArtifact getArtifact(int artifactId, ITuleapRepositoryConnector connector,
			IProgressMonitor monitor) throws CoreException {
		// // TODO [SBE] See if the parameter connector should be a class attribute instead of a parameter
		// // Test the connection
		// RestResources restResources = tuleapRestConnector.getResourceFactory();
		// // Send a request with OPTIONS to ensure that we can and have the right to retrieve the artifact
		// RestArtifacts restArtifacts = restResources.artifacts(artifactId);
		// restArtifacts.checkGet(Collections.<String, String> emptyMap());
		// // Retrieve the artifact
		// ServerResponse response = restArtifacts.get(Collections.<String, String> emptyMap());
		// if (ITuleapServerStatus.OK != response.getStatus()) {
		// // Invalid login? server error?
		// String message = this.jsonParser.getErrorMessage(response.getBody());
		// throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, message));
		// }
		// // Create the task data
		// String json = response.getBody();
		return null;
	}

	/**
	 * Updates the artifact represented by the given task data with the given task data.
	 * 
	 * @param taskData
	 *            The task data
	 * @param monitor
	 *            Used to monitor the progress
	 * @throws CoreException
	 *             In case of error during the update of the artifact
	 */

	public void updateArtifact(TaskData taskData, IProgressMonitor monitor) throws CoreException {
		// Test the connection
		// Try to log in
		// Send a request with OPTIONS to ensure that we can and have the right to update the artifact
		// Compute the change to send
		// Send the update to the server for the attribute of the task
		// See if we need to update additional tasks (cardwall, planning, etc)
		// Send the update of the other artifacts
		// Try to log out
	}

	/**
	 * Creates the artifact on the server.
	 * 
	 * @param taskData
	 *            The task data of the artifact to create
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The identifier of the artifact created
	 * @throws CoreException
	 *             In case of error during the creation of the artifact
	 */
	public int createArtifact(TaskData taskData, IProgressMonitor monitor) throws CoreException {
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
	 * @throws CoreException
	 *             In case of error during the retrieval of the reports
	 */
	public List<TuleapTrackerReport> getReports(int trackerId, IProgressMonitor monitor) throws CoreException {
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
	 * @throws CoreException
	 *             In case of error during the report execution
	 */
	public int executeReport(int trackerId, int reportId, TaskDataCollector collector,
			IProgressMonitor monitor) throws CoreException {
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
	 * @throws CoreException
	 *             In case of error during the query execution
	 */
	public int executeQuery(int trackerId, Map<String, String> criteras, TaskDataCollector collector,
			IProgressMonitor monitor) throws CoreException {
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
	 * @throws CoreException
	 *             In case of error during the attachment content retrieval
	 */
	public byte[] getAttachmentContent(int attachmentId, IProgressMonitor monitor) throws CoreException {
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
	 * @throws CoreException
	 *             In case of error during the attachment upload
	 */
	public void uploadAttachment(int artifactId, int attachmentFieldId,
			TuleapAttachmentDescriptor tuleapAttachmentDescriptor, String comment, IProgressMonitor monitor)
			throws CoreException {
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
	}

	/**
	 * Retrieves a milestone from its id.
	 * 
	 * @param milestoneId
	 *            milestone id
	 * @param monitor
	 *            progress monitor to use
	 * @return a milestone
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public TuleapMilestone getMilestone(int milestoneId, IProgressMonitor monitor) throws CoreException {
		RestResource milestoneResource = restResourceFactory.milestone(milestoneId).withToken(token);
		ServerResponse response = milestoneResource.get().run();
		checkServerError(milestoneResource, Method.GET.toString(), response);
		TuleapMilestone milestone = this.jsonParser.parseMilestone(response.getBody());
		return milestone;
	}

	/**
	 * Retrieve the backlog items of a given milestone.
	 * 
	 * @param milestoneId
	 *            The milestone id
	 * @param monitor
	 *            The monitor to use
	 * @return A list of backlog items of the milestone.
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public List<TuleapBacklogItem> getBacklogItems(int milestoneId, IProgressMonitor monitor)
			throws CoreException {
		RestResource backlogItemsResource = restResourceFactory.milestonesBacklogItems(milestoneId)
				.withToken(token);
		RestOperation operation = backlogItemsResource.get();
		RestOperationIterable jsonElements = new RestOperationIterable(operation);
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		for (JsonElement e : jsonElements) {
			backlogItems.add(jsonParser.parseBacklogItem(e));
		}
		return backlogItems;
	}

	/**
	 * Retrieve the backlog items of a given milestone.
	 * 
	 * @param milestoneId
	 *            The milestone id
	 * @param monitor
	 *            The monitor to use
	 * @return A list of backlog items of the milestone.
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public List<TuleapMilestone> getSubMilestones(int milestoneId, IProgressMonitor monitor)
			throws CoreException {
		RestResource subMilestonesResource = restResourceFactory.milestonesSubmilestones(milestoneId)
				.withToken(token);
		RestOperation operation = subMilestonesResource.get();
		RestOperationIterable jsonElements = new RestOperationIterable(operation);
		List<TuleapMilestone> milestones = Lists.newArrayList();
		for (JsonElement e : jsonElements) {
			milestones.add(jsonParser.parseMilestone(e));
		}
		return milestones;
	}

	/**
	 * Retrieves the cardwall of a milestone, if there is one.
	 * 
	 * @param milestoneId
	 *            Id of the milestone
	 * @param monitor
	 *            Progress monitor to use
	 * @return The milestone's cardwall
	 * @throws CoreException
	 *             If a problem occurs.
	 */
	public TuleapCardwall getCardwall(int milestoneId, IProgressMonitor monitor) throws CoreException {
		RestResource restCardwall = restResourceFactory.milestonesCardwall(milestoneId).withToken(token);
		ServerResponse cardwallResponse = restCardwall.get().run();
		checkServerError(restCardwall, Method.GET.toString(), cardwallResponse);
		TuleapCardwall cardwall = jsonParser.parseCardwall(cardwallResponse.getBody());
		return cardwall;
	}

	/**
	 * Updates a cardwall by sending its local state to the server.
	 * 
	 * @param cardwall
	 *            Cardwall to commit.
	 * @param monitor
	 *            Progress monitor to use.
	 * @throws CoreException
	 *             In case of error during the update of the artifact.
	 */
	public void updateCardwall(TuleapCardwall cardwall, IProgressMonitor monitor) throws CoreException {
		for (TuleapSwimlane tuleapSwimlane : cardwall.getSwimlanes()) {
			for (TuleapCard tuleapCard : tuleapSwimlane.getCards()) {
				this.updateCard(tuleapCard, monitor);
			}
		}
	}

	/**
	 * Updates the milestone BacklogItems on the server.
	 * 
	 * @param tuleapBacklogItem
	 *            The backlogItemto update.
	 * @param monitor
	 *            The progress monitor.
	 * @throws CoreException
	 *             In case of error during the update of the artifact.
	 */

	public void updateBacklogItem(TuleapBacklogItem tuleapBacklogItem, IProgressMonitor monitor)
			throws CoreException {
		RestResource restBacklogItem = restResourceFactory.backlogItem(tuleapBacklogItem.getId()).withToken(
				token);

		// from POJO to JSON
		JsonElement backlogItem = new TuleapBacklogItemSerializer().serialize(tuleapBacklogItem, null, null);

		String changesToPut = backlogItem.toString();

		// Send the PUT request
		RestOperation op = restBacklogItem.put().withBody(changesToPut);
		ServerResponse response = op.run();
		checkServerError(op, response);
	}

	/**
	 * Updates the milestone BacklogItems on the server.
	 * 
	 * @param milestoneId
	 *            the id of the milestone containing the backlog items
	 * @param backlogItems
	 *            The backlog items to update.
	 * @param monitor
	 *            The progress monitor.
	 * @throws CoreException
	 *             In case of error during the update of the artifact.
	 */

	public void updateMilestoneBacklogItems(int milestoneId, List<TuleapBacklogItem> backlogItems,
			IProgressMonitor monitor) throws CoreException {
		RestResource restMilestonesBacklogItems = restResourceFactory.milestonesBacklogItems(milestoneId)
				.withToken(token);

		// from POJO to JSON
		String changesToPut = backlogItems.toString();

		// Send the PUT request
		ServerResponse response = restMilestonesBacklogItems.put().withBody(changesToPut).run();

		if (!response.isOk()) {
			// Invalid login? server error?
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, response
					.getBody()));
		}
	}

	/**
	 * Updates the milestone card on the server.
	 * 
	 * @param tuleapCard
	 *            The card to update
	 * @param monitor
	 *            The progress monitor.
	 * @throws CoreException
	 *             In case of error during the update of the artifact.
	 */

	private void updateCard(TuleapCard tuleapCard, IProgressMonitor monitor) throws CoreException {
		RestResource restCards = restResourceFactory.cards(tuleapCard.getId()).withToken(token);

		// from POJO to JSON
		JsonElement card = new TuleapCardSerializer().serialize(tuleapCard, null, null);
		String changesToPut = card.toString();

		// Send the PUT request
		ServerResponse response = restCards.put().withBody(changesToPut).run();

		if (!response.isOk()) {
			// Invalid login? server error?
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, response
					.getBody()));
		}
	}

	/**
	 * Creates the Tuleap milestone on the server.
	 * 
	 * @param tuleapMilestone
	 *            The tuleap milestone
	 * @param monitor
	 *            The progress monitor
	 * @return The identifier of the Tuleap milestone created
	 */
	public String createMilestone(TuleapMilestone tuleapMilestone, IProgressMonitor monitor) {
		int projectId = -1;
		int configurationId = -1;
		// TODO int configurationId = tuleapMilestone.getConfigurationId();

		// TODO SBE create the milestone on the server

		int milestoneId = -1;
		return TuleapTaskIdentityUtil.getTaskDataId(projectId, configurationId, milestoneId);
	}

	/**
	 * Returns the Tuleap backlog item with the given identifier from the server.
	 * 
	 * @param backlogItemId
	 *            The identifier of the backlog item
	 * @param monitor
	 *            The progress monitor
	 * @return The Tuleap backlog item with the given identifier from the server
	 * @throws CoreException
	 *             In case of error during the retrieval of the BacklogItem
	 */
	public TuleapBacklogItem getBacklogItem(int backlogItemId, IProgressMonitor monitor) throws CoreException {
		RestResource restBacklogItem = restResourceFactory.backlogItem(backlogItemId).withToken(token);

		ServerResponse backlogItemResponse = restBacklogItem.get().run();

		checkServerError(restBacklogItem, Method.GET.toString(), backlogItemResponse);

		TuleapBacklogItem backlogItem = this.jsonParser.parseBacklogItem(backlogItemResponse.getBody());

		return backlogItem;
	}

	/**
	 * Retrieve the top planning(s) of a given project.
	 * 
	 * @param projectId
	 *            The project id
	 * @param monitor
	 *            The monitor to use
	 * @return A list of the top plannings of the project.
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public List<TuleapTopPlanning> getTopPlannings(int projectId, IProgressMonitor monitor)
			throws CoreException {
		// 1- Retrieve the list of top plannings
		RestResource restProjectTopPlannings = restResourceFactory.projectsTopPlannings(projectId).withToken(
				token);
		ServerResponse response = restProjectTopPlannings.get().run();

		checkServerError(restProjectTopPlannings, Method.GET.toString(), response);

		List<TuleapTopPlanning> topPlannings = this.jsonParser.parseTopPlannings(response.getBody());
		for (TuleapTopPlanning topPlanning : topPlannings) {
			loadTopPlanningElements(topPlanning);
		}

		return topPlannings;
	}

	/**
	 * Retrieve the top planning(s) of a given project.
	 * 
	 * @param topPlanningId
	 *            The top planning id
	 * @param monitor
	 *            The monitor to use
	 * @return A list of the top plannings of the project.
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public TuleapTopPlanning getTopPlanning(int topPlanningId, IProgressMonitor monitor) throws CoreException {
		RestResource restTopPlannings = restResourceFactory.topPlannings(topPlanningId).withToken(token);
		ServerResponse response = restTopPlannings.get().run();

		checkServerError(restTopPlannings, Method.GET.toString(), response);

		TuleapTopPlanning topPlanning = this.jsonParser.parseTopPlanning(response.getBody());
		return loadTopPlanningElements(topPlanning);
	}

	/**
	 * Loads the sub-milestones and the backlog items of a top-planning.
	 * 
	 * @param topPlanning
	 *            The top planning to load, which will be modified by this method.
	 * @return The given top planning, just for fluency of the PIA.
	 * @throws CoreException
	 *             If something goes wrong.
	 */
	private TuleapTopPlanning loadTopPlanningElements(TuleapTopPlanning topPlanning) throws CoreException {
		// 2- Retrieve the milestones of this top planning
		RestResource restMilestones = restResourceFactory.topPlanningsMilestones(topPlanning.getId())
				.withToken(token);
		ServerResponse milestonesResponse = restMilestones.get().run();

		checkServerError(restMilestones, Method.GET.toString(), milestonesResponse);

		// TODO Pagination
		String jsonMilestones = milestonesResponse.getBody();

		List<TuleapMilestone> tuleapMilestones = this.jsonParser.parseMilestones(jsonMilestones);
		for (TuleapMilestone tuleapMilestone : tuleapMilestones) {
			topPlanning.addSubMilestone(tuleapMilestone);
		}

		// 3- Retrieve the backlog items of this top planning
		RestResource restBacklogItems = restResourceFactory.topPlanningsBacklogItems(topPlanning.getId())
				.withToken(token);
		ServerResponse backlogItemsResponse = restBacklogItems.get().run();

		checkServerError(restBacklogItems, Method.GET.toString(), backlogItemsResponse);

		// TODO Pagination
		String jsonBacklogItems = backlogItemsResponse.getBody();

		List<TuleapBacklogItem> backlogItems = this.jsonParser.parseBacklogItems(jsonBacklogItems);
		for (TuleapBacklogItem tuleapBacklogItem : backlogItems) {
			topPlanning.addBacklogItem(tuleapBacklogItem);
		}
		return topPlanning;
	}
}
