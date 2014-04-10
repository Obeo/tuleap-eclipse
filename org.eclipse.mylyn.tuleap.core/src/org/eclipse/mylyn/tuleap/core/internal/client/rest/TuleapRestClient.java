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
package org.eclipse.mylyn.tuleap.core.internal.client.rest;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.client.ITuleapQueryConstants;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.TuleapToken;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapPlanning;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTrackerReport;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUserGroup;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.data.ArtifactReference;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithAttachment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapFile;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapBacklogItem;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapBurndown;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapCard;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapCardwall;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapMilestone;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreKeys;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreMessages;

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
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapRestClient implements IAuthenticator {

	/**
	 * The JSON parser.
	 */
	private final Gson gson;

	/**
	 * The task repository.
	 */
	private final TaskRepository taskRepository;

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
	 * @param gson
	 *            The JSON parser
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapRestClient(RestResourceFactory resourceFactory, Gson gson, TaskRepository taskRepository) {
		this.restResourceFactory = resourceFactory;
		this.gson = gson;
		this.taskRepository = taskRepository;
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
	 */
	public IStatus validateConnection(IProgressMonitor monitor) {
		if (monitor != null) {
			monitor.beginTask(TuleapCoreMessages.getString(TuleapCoreKeys.validateConnection), 10);
		}
		try {
			login();
		} catch (CoreException e) {
			return new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage());
		}
		return Status.OK_STATUS;
	}

	/**
	 * Loads the planning into a given project after fetching them from the remote server via the REST API.
	 *
	 * @param project
	 *            The project in which plannings must be loaded
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public void loadPlanningsInto(TuleapProject project) throws CoreException {
		// Retrieve the plannings of the project
		RestResource plannings = restResourceFactory.projectPlannings(project.getIdentifier())
				.withAuthenticator(this);
		RestOperation operation = plannings.get();
		for (JsonElement element : operation.iterable()) {
			TuleapPlanning planning = gson.fromJson(element, TuleapPlanning.class);
			project.addPlanning(planning);
		}
	}

	/**
	 * Creates an authorization token on the server and stores it so that it can be used.
	 *
	 * @throws CoreException
	 *             In case of error during the authentication.
	 */
	public void login() throws CoreException {
		RestResource restBacklogItem = restResourceFactory.tokens();
		AuthenticationCredentials credentials = taskRepository.getCredentials(AuthenticationType.REPOSITORY);
		// Credentials can be null?
		if (credentials != null) {
			String credentialsToPost = getCredentials(credentials);
			// Send the POST request
			// It is on purpose that there is no authenticator here!
			RestOperation postOperation = restBacklogItem.post().withBody(credentialsToPost);
			ServerResponse response = postOperation.checkedRun();
			this.token = gson.fromJson(response.getBody(), TuleapToken.class);
		} else {
			token = null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.client.rest.IAuthenticator#getToken()
	 */
	public TuleapToken getToken() {
		return token;
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
	 * @param server
	 *            The server configuration
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The task data of the artifact
	 * @throws CoreException
	 *             In case of error during the retrieval of the artifact
	 */
	public TuleapArtifact getArtifact(int artifactId, TuleapServer server, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingArtifact, Integer
					.valueOf(artifactId)));
		}
		RestResource artifactResource = restResourceFactory.artifact(artifactId).withAuthenticator(this);
		ServerResponse response = artifactResource.get().checkedRun();
		TuleapArtifact artifact = gson.fromJson(response.getBody(), TuleapArtifact.class);
		return artifact;
	}

	/**
	 * Updates the artifact and its comment (but not its attachments).
	 *
	 * @param artifact
	 *            The artifact to submit
	 * @param monitor
	 *            Used to monitor the progress
	 * @throws CoreException
	 *             In case of error during the update of the artifact
	 */
	public void updateArtifact(TuleapArtifactWithComment artifact, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingArtifact, artifact.getId()));
		}
		RestResource artifactResource = restResourceFactory.artifact(artifact.getId().intValue())
				.withAuthenticator(this);
		artifactResource.put().withBody(gson.toJson(artifact, TuleapArtifactWithComment.class)).checkedRun();
	}

	/**
	 * Updates the artifact's attachment represented by the given object.
	 *
	 * @param artifact
	 *            The artifact to submit
	 * @param monitor
	 *            Used to monitor the progress
	 * @throws CoreException
	 *             In case of error during the update of the artifact
	 */
	public void updateArtifactAttachments(TuleapArtifactWithAttachment artifact, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingArtifact, artifact.getId()));
		}
		RestResource artifactResource = restResourceFactory.artifact(artifact.getId().intValue())
				.withAuthenticator(this);
		artifactResource.put().withBody(gson.toJson(artifact, TuleapArtifactWithAttachment.class))
				.checkedRun();
	}

	/**
	 * Creates the artifact on the server.
	 *
	 * @param artifact
	 *            The artifact to create
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The identifier of the artifact created
	 * @throws CoreException
	 *             In case of error during the creation of the artifact
	 */
	public TuleapTaskId createArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.creatingArtifact));
		}
		RestResource artifactResource = restResourceFactory.artifacts().withAuthenticator(this);
		ServerResponse response = artifactResource.post().withBody(
				gson.toJson(artifact, TuleapArtifact.class)).checkedRun();
		ArtifactReference ref = gson.fromJson(response.getBody(), ArtifactReference.class);
		return TuleapTaskId.forArtifact(artifact.getProject().getId(), ref.getTracker().getId(), ref.getId());
	}

	/**
	 * Retrieve an artifact file attachment that is characterized by the data it contains.
	 *
	 * @param fileId
	 *            The file identifier
	 * @param offset
	 *            the offset Where to start reading the file
	 * @param limit
	 *            Max number of bytes to read from the file
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The file
	 * @throws CoreException
	 *             In case of error during the attachment content retrieval
	 */
	public TuleapFile getArtifactFile(int fileId, int offset, int limit, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingFile, Integer
					.valueOf(fileId)));
		}
		RestResource fileResource = restResourceFactory.artifactFile(fileId).withAuthenticator(this);
		ServerResponse response = fileResource.get().withQueryParameter(RestResource.OFFSET,
				Integer.toString(offset)).withQueryParameter(RestResource.LIMIT, Integer.toString(limit))
				.checkedRun();
		TuleapFile file = gson.fromJson(response.getBody(), TuleapFile.class);
		if (file == null || file.getData() == null) {
			file = new TuleapFile(""); //$NON-NLS-1$
		}
		return file;
	}

	/**
	 * Create a new file attachment that is not yet attached to an artifact. If parameters are right, the
	 * server returns the identifier and the URI of the created file.
	 *
	 * @param content
	 *            the file data
	 * @param type
	 *            the file type
	 * @param name
	 *            the file name
	 * @param description
	 *            the file description
	 * @param monitor
	 *            Used to monitor the progress
	 * @return The tuleap reference
	 * @throws CoreException
	 *             In case of error during the attachment content retrieval
	 */
	public TuleapReference createArtifactFile(String content, String type, String name, String description,
			IProgressMonitor monitor) throws CoreException {
		RestResource restFiles = restResourceFactory.artifactTemporaryFiles().withAuthenticator(this);

		JsonObject json = new JsonObject();
		json.add(ITuleapConstants.NAME, new JsonPrimitive(name));
		json.add(ITuleapConstants.DESCRIPTION, new JsonPrimitive(description));
		json.add(ITuleapConstants.MIME_TYPE, new JsonPrimitive(type));
		json.add(ITuleapConstants.CONTENT, new JsonPrimitive(content));

		String changesToPost = json.toString();
		RestOperation operation = restFiles.post().withBody(changesToPost);
		ServerResponse response = operation.checkedRun();
		TuleapReference ref = gson.fromJson(response.getBody(), TuleapReference.class);
		return ref;
	}

	/**
	 * Update the file attachment.
	 *
	 * @param fileId
	 *            The file identifier
	 * @param data
	 *            the file data
	 * @param offset
	 *            the offset
	 * @param monitor
	 *            Used to monitor the progress
	 * @throws CoreException
	 *             In case of error during the file update
	 */
	public void updateArtifactFile(int fileId, String data, int offset, IProgressMonitor monitor)
			throws CoreException {
		RestResource restFile = restResourceFactory.artifactTemporaryFile(fileId).withAuthenticator(this);

		JsonObject json = new JsonObject();
		json.add(ITuleapConstants.CONTENT, new JsonPrimitive(data));
		json.add(ITuleapConstants.OFFSET, new JsonPrimitive(Integer.valueOf(offset)));

		String changesToPut = json.toString();
		RestOperation operation = restFile.put().withBody(changesToPut);
		operation.checkedRun();
	}

	/**
	 * Delete the file attachment.
	 *
	 * @param fileId
	 *            The file identifier
	 * @param monitor
	 *            Used to monitor the progress
	 * @throws CoreException
	 *             In case of error during the file deletion
	 */
	public void deleteArtifactFile(int fileId, IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages
					.getString(TuleapCoreKeys.deletingFile, Integer.valueOf(fileId)));
		}
		RestResource milestoneResource = restResourceFactory.artifactTemporaryFile(fileId).withAuthenticator(
				this);
		milestoneResource.delete().checkedRun();
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
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingMilestone, Integer
					.valueOf(milestoneId)));
		}
		RestResource milestoneResource = restResourceFactory.milestone(milestoneId).withAuthenticator(this);
		ServerResponse response = milestoneResource.get().checkedRun();
		TuleapMilestone milestone = gson.fromJson(response.getBody(), TuleapMilestone.class);
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
	public List<TuleapBacklogItem> getMilestoneBacklog(int milestoneId, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingBacklogItems, Integer
					.valueOf(milestoneId)));
		}
		// The backlog BacklogItems
		RestResource backlogResource = restResourceFactory.milestoneBacklog(milestoneId).withAuthenticator(
				this);
		RestOperation operation = backlogResource.get();
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			backlogItems.add(gson.fromJson(e, TuleapBacklogItem.class));
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
	public List<TuleapBacklogItem> getMilestoneContent(int milestoneId, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingBacklogItems, Integer
					.valueOf(milestoneId)));
		}
		// The backlog BacklogItems
		RestResource backlogResource = restResourceFactory.milestoneContent(milestoneId).withAuthenticator(
				this);
		RestOperation operation = backlogResource.get();
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			backlogItems.add(gson.fromJson(e, TuleapBacklogItem.class));
		}

		return backlogItems;
	}

	/**
	 * Retrieve burndown of a given milestone.
	 *
	 * @param milestoneId
	 *            The milestone id
	 * @param monitor
	 *            The monitor to use
	 * @return The burndown of the milestone.
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public TuleapBurndown getMilestoneBurndown(int milestoneId, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingBurndown, Integer
					.valueOf(milestoneId)));
		}
		RestResource restBurndown = restResourceFactory.milestoneBurndown(milestoneId)
				.withAuthenticator(this);
		ServerResponse burnDownResponse = restBurndown.get().checkedRun();
		TuleapBurndown burndown = gson.fromJson(burnDownResponse.getBody(), TuleapBurndown.class);
		return burndown;
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
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingSubMilestones, Integer
					.valueOf(milestoneId)));
		}
		RestResource subMilestonesResource = restResourceFactory.milestoneSubmilestones(milestoneId)
				.withAuthenticator(this);
		RestOperation operation = subMilestonesResource.get();
		List<TuleapMilestone> milestones = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			milestones.add(gson.fromJson(e, TuleapMilestone.class));
		}
		return milestones;
	}

	/**
	 * Retrieve a project's milestones.
	 *
	 * @param projectId
	 *            ID of the project
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the project's top-level milestones.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapMilestone> getProjectMilestones(int projectId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.projectMilestones(projectId).withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapMilestone> milestones = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			milestones.add(gson.fromJson(e, TuleapMilestone.class));
		}
		return milestones;
	}

	/**
	 * Retrieve a project's backlog.
	 *
	 * @param projectId
	 *            ID of the project
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the project's top-level milestones.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapBacklogItem> getProjectBacklog(int projectId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.projectBacklog(projectId).withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			backlogItems.add(gson.fromJson(e, TuleapBacklogItem.class));
		}
		return backlogItems;
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
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingCardwall, Integer
					.valueOf(milestoneId)));
		}
		RestResource restCardwall = restResourceFactory.milestoneCardwall(milestoneId)
				.withAuthenticator(this);
		ServerResponse cardwallResponse = restCardwall.get().checkedRun();
		TuleapCardwall cardwall = gson.fromJson(cardwallResponse.getBody(), TuleapCardwall.class);
		return cardwall;
	}

	/**
	 * Retrieve a project's user groups.
	 *
	 * @param projectId
	 *            ID of the project
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the project's user groups.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapUserGroup> getProjectUserGroups(int projectId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.projectUserGroups(projectId).withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapUserGroup> userGroups = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			userGroups.add(gson.fromJson(e, TuleapUserGroup.class));
		}
		return userGroups;
	}

	/**
	 * Retrieve a user group users.
	 *
	 * @param userGroupId
	 *            ID of the user group
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the user groups's users.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapUser> getUserGroupUsers(String userGroupId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.userGroupUsers(userGroupId).withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapUser> users = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			users.add(gson.fromJson(e, TuleapUser.class));
		}
		return users;
	}

	/**
	 * Retrieve a tracker reports.
	 *
	 * @param trackerId
	 *            ID of the tracker
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the tracker reports.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapTrackerReport> getTrackerReports(int trackerId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.trackerReports(trackerId).withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapTrackerReport> reports = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			reports.add(gson.fromJson(e, TuleapTrackerReport.class));
		}
		return reports;
	}

	/**
	 * Retrieve the projects.
	 *
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the projects.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapProject> getProjects(IProgressMonitor monitor) throws CoreException {
		RestResource r = restResourceFactory.projects().withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapProject> projects = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			projects.add(gson.fromJson(e, TuleapProject.class));
		}
		return projects;
	}

	/**
	 * Retrieve a project trackers.
	 *
	 * @param projectId
	 *            The project id
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the project trackers.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapTracker> getProjectTrackers(int projectId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.projectsTrackers(projectId).withAuthenticator(this);
		RestOperation operation = r.get();
		List<TuleapTracker> trackers = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			trackers.add(gson.fromJson(e, TuleapTracker.class));
		}
		return trackers;
	}

	/**
	 * Retrieve a tracker report artifacts.
	 *
	 * @param trackerReportId
	 *            ID of the tracker report
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the tracker report artifacts.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapArtifact> getTrackerReportArtifacts(int trackerReportId, IProgressMonitor monitor)
			throws CoreException {
		RestResource r = restResourceFactory.trackerReportArtifacts(trackerReportId).withAuthenticator(this);
		// The /tracker_reports/:id/artifacts returns no values by default
		// So it's necessary to add the parameter ?values=all
		RestOperation operation = r.get().withQueryParameter("values", "all"); //$NON-NLS-1$//$NON-NLS-2$
		List<TuleapArtifact> artifacts = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			artifacts.add(gson.fromJson(e, TuleapArtifact.class));
		}
		return artifacts;
	}

	/**
	 * Retrieves the {@link TuleapArtifact} from a query run on the server.
	 *
	 * @param query
	 *            The query to run
	 * @param tracker
	 *            The configuration used to analyze the data from the SOAP responses
	 * @param monitor
	 *            the progress monitor
	 * @return The list of the Tuleap artifact
	 * @throws CoreException
	 *             If communication fails.
	 */
	public List<TuleapArtifact> getArtifactsFromQuery(IRepositoryQuery query, TuleapTracker tracker,
			IProgressMonitor monitor) throws CoreException {
		RestResource r = restResourceFactory.trackerArtifacts(tracker.getIdentifier())
				.withAuthenticator(this);
		RestOperation op = r.get().withQueryParameter("values", "all"); //$NON-NLS-1$//$NON-NLS-2$
		Map<String, String> attributes = query.getAttributes();
		String jsonCriteria = attributes.get(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA);
		if (jsonCriteria == null) {
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapCoreMessages.getString(TuleapCoreKeys.oldQueryNotCompatible, query.getSummary())));
		}
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(jsonCriteria);
		JsonObject criteria;
		if (jsonElement.isJsonObject()) {
			criteria = jsonElement.getAsJsonObject();
		} else {
			criteria = new JsonObject();
		}
		// The JSON in the query is not exactly the same as the one we need to send
		// because for Combo-boxes, Tuleap expects int[] values where we have String[] values
		// We need String[] values for other tools to import easily queries via Mylyn import mechanism
		JsonObject criteriaToSend = new JsonObject();
		for (AbstractTuleapField field : tracker.getFields()) {
			String fieldName = field.getName();
			if (criteria.has(fieldName)) {
				JsonElement json = criteria.get(fieldName);
				// TODO Use a visitor instead of ifs
				if (field instanceof AbstractTuleapSelectBox) {
					JsonObject criterionToSend = convertBoundFieldCriterion((AbstractTuleapSelectBox)field,
							json);
					criteriaToSend.add(fieldName, criterionToSend);
				} else {
					// Other kinds of fields are stored right in the query
					criteriaToSend.add(fieldName, json);
				}
			}
		}

		List<TuleapArtifact> artifacts = Lists.newArrayList();
		for (JsonElement e : op.iterable()) {
			artifacts.add(gson.fromJson(e, TuleapArtifact.class));
		}
		return artifacts;
	}

	/**
	 * Convert a locally stored criterion for bound field (that uses labels for keys) into a criterion
	 * sendable to Tuleap (that uses IDs for keys).
	 *
	 * @param sb
	 *            The select box field
	 * @param json
	 *            The json criterion for this field in the query
	 * @return The JSON that represents the criterion to send to Tuleap for the given field.
	 */
	private JsonObject convertBoundFieldCriterion(AbstractTuleapSelectBox sb, JsonElement json) {
		JsonObject criterionToSend = new JsonObject();
		criterionToSend.add("operator", new JsonPrimitive(IQueryCriterion.OP_CONTAINS)); //$NON-NLS-1$
		JsonArray critValue = new JsonArray();
		criterionToSend.add("value", critValue); //$NON-NLS-1$
		JsonElement jsonValue = json.getAsJsonObject().get("value"); //$NON-NLS-1$
		if (jsonValue.isJsonArray()) {
			JsonArray jsonValues = jsonValue.getAsJsonArray();
			for (JsonElement critItem : jsonValues) {
				String critStr = critItem.getAsString();
				for (TuleapSelectBoxItem item : sb.getItems()) {
					if (critStr.equals(item.getLabel())) {
						critValue.add(new JsonPrimitive(Integer.valueOf(item.getIdentifier())));
						break;
					}
				}
			}
		}
		return criterionToSend;
	}

	/**
	 * Retrieve an artifact comments.
	 *
	 * @param artifactId
	 *            ID of the artifact
	 * @param server
	 *            The server configuration
	 * @param monitor
	 *            Progress monitor to use
	 * @return A list, never null but possibly empty, containing the artifact comments.
	 * @throws CoreException
	 *             If the server returns a status code different from 200 OK.
	 */
	public List<TuleapElementComment> getArtifactComments(int artifactId, TuleapServer server,
			IProgressMonitor monitor) throws CoreException {
		RestResource r = restResourceFactory.artifactChangesets(artifactId).withAuthenticator(this);
		// Only retrieve comments, not full changes
		RestOperation operation = r.get().withHeader("fields", "comments"); //$NON-NLS-1$//$NON-NLS-2$
		List<TuleapElementComment> comments = Lists.newArrayList();
		for (JsonElement e : operation.iterable()) {
			TuleapElementComment comment = gson.fromJson(e, TuleapElementComment.class);
			int submitterId = comment.getSubmitter().getId();
			TuleapUser submitter = server.getUser(submitterId);
			comment.setSubmitter(submitter);
			if (comment.getBody() != null && !comment.getBody().isEmpty()) {
				comments.add(comment);
			}
		}
		return comments;
	}

	/**
	 * Updates the backlog of a given milestone.
	 *
	 * @param milestoneId
	 *            The milestone id
	 * @param backlogItems
	 *            The backlogItems list to update.
	 * @param monitor
	 *            The monitor to use
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public void updateMilestoneBacklog(int milestoneId, List<TuleapBacklogItem> backlogItems,
			IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingBacklog, Integer
					.valueOf(milestoneId)));
		}
		RestResource backlogResource = restResourceFactory.milestoneBacklog(milestoneId).withAuthenticator(
				this);
		// from POJO to JSON
		JsonArray backlogItemsArray = new JsonArray();
		for (TuleapBacklogItem item : backlogItems) {
			backlogItemsArray.add(new JsonPrimitive(item.getId()));
		}
		String changesToPut = backlogItemsArray.toString();
		RestOperation operation = backlogResource.put().withBody(changesToPut);
		operation.checkedRun();
	}

	/**
	 * Updates the backlog of a given milestone.
	 *
	 * @param projectId
	 *            The project id
	 * @param backlogItems
	 *            The backlogItems list to update.
	 * @param monitor
	 *            The monitor to use
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public void updateTopPlanningBacklog(int projectId, List<TuleapBacklogItem> backlogItems,
			IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingProjectBacklog, Integer
					.valueOf(projectId)));
		}
		RestResource backlogResource = restResourceFactory.projectBacklog(projectId).withAuthenticator(this);
		// from POJO to JSON
		JsonArray backlogItemsArray = new JsonArray();
		for (TuleapBacklogItem item : backlogItems) {
			backlogItemsArray.add(new JsonPrimitive(item.getId()));
		}
		String changesToPut = backlogItemsArray.toString();
		RestOperation operation = backlogResource.put().withBody(changesToPut);
		operation.checkedRun();
	}

	/**
	 * Updates the content of a given milestone.
	 *
	 * @param milestoneId
	 *            The milestone id
	 * @param backlogItems
	 *            The backlogItems list to update.
	 * @param monitor
	 *            The monitor to use
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public void updateMilestoneContent(int milestoneId, List<TuleapBacklogItem> backlogItems,
			IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingContent, Integer
					.valueOf(milestoneId)));
		}
		RestResource backlogResource = restResourceFactory.milestoneContent(milestoneId).withAuthenticator(
				this);
		// from POJO to JSON
		JsonArray backlogItemsArray = new JsonArray();
		for (TuleapBacklogItem item : backlogItems) {
			backlogItemsArray.add(new JsonPrimitive(item.getId()));
		}
		String changesToPut = backlogItemsArray.toString();
		RestOperation operation = backlogResource.put().withBody(changesToPut);
		operation.checkedRun();
	}

	/**
	 * Updates the submilestones list of a given milestone.
	 *
	 * @param milestoneId
	 *            The milestone id
	 * @param submilestones
	 *            The submilestones list to update.
	 * @param monitor
	 *            The monitor to use
	 * @throws CoreException
	 *             If anything goes wrong.
	 */
	public void updateMilestoneSubmilestones(int milestoneId, List<TuleapMilestone> submilestones,
			IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingSubmilestones, Integer
					.valueOf(milestoneId)));
		}
		RestResource backlogResource = restResourceFactory.milestoneSubmilestones(milestoneId)
				.withAuthenticator(this);
		// from POJO to JSON
		JsonArray backlogItemsArray = new JsonArray();
		for (TuleapMilestone milestone : submilestones) {
			backlogItemsArray.add(new JsonPrimitive(milestone.getId()));
		}
		String changesToPut = backlogItemsArray.toString();
		RestOperation operation = backlogResource.put().withBody(changesToPut);
		operation.checkedRun();
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

	public void updateCard(TuleapCard tuleapCard, IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.updatingCard, tuleapCard.getId()));
		}
		RestResource restCards = restResourceFactory.card(tuleapCard.getId()).withAuthenticator(this);

		// from POJO to JSON
		JsonElement card = gson.toJsonTree(tuleapCard);
		String changesToPut = card.toString();

		// Send the PUT request
		RestOperation operation = restCards.put().withBody(changesToPut);
		operation.checkedRun();
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
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingBacklogItem, Integer
					.valueOf(backlogItemId)));
		}
		RestResource restBacklogItem = restResourceFactory.backlogItem(backlogItemId).withAuthenticator(this);

		RestOperation operation = restBacklogItem.get();
		ServerResponse response = operation.checkedRun();

		TuleapBacklogItem backlogItem = gson.fromJson(response.getBody(), TuleapBacklogItem.class);

		return backlogItem;
	}

	/**
	 * Returns the Tuleap tracker with the given identifier from the server.
	 *
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param monitor
	 *            The progress monitor
	 * @return The Tuleap tracker with the given identifier from the server
	 * @throws CoreException
	 *             In case of error during the retrieval of the tracker
	 */
	public TuleapTracker getTracker(int trackerId, IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrievingTracker, Integer
					.valueOf(trackerId)));
		}
		RestResource restTracker = restResourceFactory.tracker(trackerId).withAuthenticator(this);
		RestOperation operation = restTracker.get();
		ServerResponse response = operation.checkedRun();
		TuleapTracker tracker = gson.fromJson(response.getBody(), TuleapTracker.class);
		return tracker;
	}
}
