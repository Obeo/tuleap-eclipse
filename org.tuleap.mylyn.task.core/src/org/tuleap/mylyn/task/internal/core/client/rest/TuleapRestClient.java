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

import com.google.gson.JsonElement;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.restlet.data.Method;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.TuleapAttachmentDescriptor;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapBacklogItemSerializer;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapCardSerializer;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapMilestoneSerializer;
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
	 * The version number of the best api supported.
	 */
	public static final String BEST_SUPPORTED_API_VERSION = ITuleapAPIVersions.V1_0;

	/**
	 * Utility class used to communicate using HTTP with the server.
	 */
	private final IRestConnector tuleapRestConnector;

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
	 * The constructor.
	 * 
	 * @param tuleapRestConnector
	 *            The connector is used for HTTP communication
	 * @param jsonParser
	 *            The Tuleap JSON parser
	 * @param jsonSerializer
	 *            The Tuleap JSON serializer
	 * @param taskRepository
	 *            The task repository
	 * @param logger
	 *            The logger
	 */
	public TuleapRestClient(IRestConnector tuleapRestConnector, TuleapJsonParser jsonParser,
			TuleapJsonSerializer jsonSerializer, TaskRepository taskRepository, ILog logger) {
		this.tuleapRestConnector = tuleapRestConnector;
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
		// Test the connection first
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();

		restResourceFactory.user();

		// If we made it here without exception, everything is fine since all the potential errors in the
		// validation process would be considered as critical, they would throw a CoreException
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
	public TuleapServerConfiguration getTuleapServerConfiguration(IProgressMonitor monitor)
			throws CoreException {
		// Test the connection
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();

		TuleapServerConfiguration tuleapServerConfiguration = new TuleapServerConfiguration(
				this.taskRepository.getRepositoryUrl());
		tuleapServerConfiguration.setLastUpdate(new Date().getTime());

		// Check that we can get the list of projects
		RestResource restProjects = restResourceFactory.projects();

		// Retrieve the projects and create the configuration of each project
		ServerResponse projectsGetServerResponse = restProjects.get().run();

		checkServerError(restProjects, Method.GET.toString(), projectsGetServerResponse);

		String projectsGetResponseBody = projectsGetServerResponse.getBody();
		List<TuleapProjectConfiguration> projectConfigurations = this.jsonParser
				.parseProjectConfigurations(projectsGetResponseBody);

		// For each project that has the tracker service
		for (TuleapProjectConfiguration projectConfiguration : projectConfigurations) {
			tuleapServerConfiguration.addProject(projectConfiguration);

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

		return tuleapServerConfiguration;
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
			String message = TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.errorReturnedByServer, restResource.getUrl(), method,
					jsonParser.getErrorMessage(response.getBody()));
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, message));
		}
	}

	// TODO get user groups of a project

	// TODO get users of a group

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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();
		RestResource milestoneResource = restResourceFactory.milestone(milestoneId);
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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();
		RestResource backlogItemsResource = restResourceFactory.milestonesBacklogItems(milestoneId);
		ServerResponse response = backlogItemsResource.get().run();
		checkServerError(backlogItemsResource, Method.GET.toString(), response);
		return this.jsonParser.parseBacklogItems(response.getBody());
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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();
		RestResource subMilestonesResource = restResourceFactory.milestonesSubmilestones(milestoneId);
		ServerResponse response = subMilestonesResource.get().run();
		checkServerError(subMilestonesResource, Method.GET.toString(), response);
		return this.jsonParser.parseMilestones(response.getBody());
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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();
		RestResource restCardwall = restResourceFactory.milestonesCardwall(milestoneId);
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
		// Test the connection
		RestResourceFactory restResources = tuleapRestConnector.getResourceFactory();
		RestResource restBacklogItem = restResources.backlogItem(tuleapBacklogItem.getId());

		// from POJO to JSON
		JsonElement backlogItem = new TuleapBacklogItemSerializer().serialize(tuleapBacklogItem, null, null);

		String changesToPut = backlogItem.toString();

		// Send the PUT request
		ServerResponse response = restBacklogItem.put().withBody(changesToPut).run();
		if (!response.isOk()) {
			// Invalid login? server error?
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, response
					.getBody()));
		}
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
		// Test the connection

		RestResourceFactory restResources = tuleapRestConnector.getResourceFactory();
		RestResource restMilestonesBacklogItems = restResources.milestonesBacklogItems(milestoneId);

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
	 * Updates the milestone fields on the server.
	 * 
	 * @param tuleapMilestone
	 *            The milestone to update.
	 * @param monitor
	 *            The progress monitor.
	 * @throws CoreException
	 *             In case of error during the update of the artifact.
	 */
	// TODO Remove this, replace by updateArtifact
	private void updateMilestoneFields(TuleapMilestone tuleapMilestone, IProgressMonitor monitor)
			throws CoreException {
		// Test the connection
		RestResourceFactory restResources = tuleapRestConnector.getResourceFactory();
		RestResource restMilestone = restResources.milestone(tuleapMilestone.getId());

		// from POJO to JSON
		JsonElement milestoneObject = new TuleapMilestoneSerializer().serialize(tuleapMilestone, null, null);
		String changesToPut = milestoneObject.toString();

		// Send the PUT request
		ServerResponse response = restMilestone.put().withBody(changesToPut).run();

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
		// Test the connection
		RestResourceFactory restResources = tuleapRestConnector.getResourceFactory();
		RestResource restCards = restResources.cards(tuleapCard.getId());

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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();
		RestResource restBacklogItem = restResourceFactory.backlogItem(backlogItemId);

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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();

		// 1- Retrieve the list of top plannings
		RestResource restProjectTopPlannings = restResourceFactory.projectsTopPlannings(projectId);
		ServerResponse response = restProjectTopPlannings.get().run();

		checkServerError(restProjectTopPlannings, Method.GET.toString(), response);

		List<TuleapTopPlanning> topPlannings = this.jsonParser.parseTopPlannings(response.getBody());
		for (TuleapTopPlanning topPlanning : topPlannings) {
			loadTopPlanningElements(restResourceFactory, topPlanning);
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
		RestResourceFactory restResourceFactory = tuleapRestConnector.getResourceFactory();

		RestResource restTopPlannings = restResourceFactory.topPlannings(topPlanningId);
		ServerResponse response = restTopPlannings.get().run();

		checkServerError(restTopPlannings, Method.GET.toString(), response);

		TuleapTopPlanning topPlanning = this.jsonParser.parseTopPlanning(response.getBody());
		return loadTopPlanningElements(restResourceFactory, topPlanning);
	}

	/**
	 * Loads the sub-milestones and the backlog items of a top-planning.
	 * 
	 * @param restResourceFactory
	 *            The {@link RestResourceFactory} to use.
	 * @param topPlanning
	 *            The top planning to load, which will be modified by this method.
	 * @return The given top planning, just for fluency of the PIA.
	 * @throws CoreException
	 *             If something goes wrong.
	 */
	private TuleapTopPlanning loadTopPlanningElements(RestResourceFactory restResourceFactory,
			TuleapTopPlanning topPlanning) throws CoreException {
		// 2- Retrieve the milestones of this top planning
		RestResource restMilestones = restResourceFactory.topPlanningsMilestones(topPlanning.getId());
		ServerResponse milestonesResponse = restMilestones.get().run();

		checkServerError(restMilestones, Method.GET.toString(), milestonesResponse);

		// TODO Pagination
		String jsonMilestones = milestonesResponse.getBody();

		List<TuleapMilestone> tuleapMilestones = this.jsonParser.parseMilestones(jsonMilestones);
		for (TuleapMilestone tuleapMilestone : tuleapMilestones) {
			topPlanning.addSubMilestone(tuleapMilestone);
		}

		// 3- Retrieve the backlog items of this top planning
		RestResource restBacklogItems = restResourceFactory.topPlanningsBacklogItems(topPlanning.getId());
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
