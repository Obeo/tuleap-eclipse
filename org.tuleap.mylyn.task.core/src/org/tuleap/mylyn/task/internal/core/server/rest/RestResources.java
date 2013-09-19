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
package org.tuleap.mylyn.task.internal.core.server.rest;

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.server.ITuleapAPIVersions;
import org.tuleap.mylyn.task.internal.core.server.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.server.ITuleapServerStatus;
import org.tuleap.mylyn.task.internal.core.server.ServerResponse;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

/**
 * Builder class that instantiates the accessible JSON services.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class RestResources {

	/**
	 * The serverUrl of the REST API on the server, for example {@code http://localhost:3001}.
	 */
	private final String serverUrl;

	/**
	 * The version of the API to use.
	 */
	private String apiVersion;

	/**
	 * The credentials to use for authentication.
	 */
	private ICredentials credentials;

	/**
	 * Constructor.
	 * 
	 * @param serverUrl
	 *            URL of the rest API on the server.
	 * @param apiVersion
	 *            the API version to use.
	 * @param credentials
	 *            The credentials to use.
	 */
	private RestResources(String serverUrl, String apiVersion, ICredentials credentials) {
		this.serverUrl = serverUrl;
		this.apiVersion = apiVersion;
		this.credentials = credentials;
	}

	/**
	 * Factory method to get the right instance of RestResources.
	 * 
	 * @param serverUrl
	 *            The rest API url root on the server.
	 * @param bestSupportedApiVersion
	 *            The best known API version supported at development time.
	 * @param credentials
	 *            The credentials to use.
	 * @return A new instance of RestResources to access the different server APIs.
	 * @throws CoreException
	 *             if any connection problem occurs.
	 */
	protected static RestResources builder(String serverUrl, String bestSupportedApiVersion,
			ICredentials credentials) throws CoreException {
		IStatus status = null;

		// Try to send a request to the best supported version of the API
		ServerResponse serverResponse = new RestApi(serverUrl, bestSupportedApiVersion).options(Maps
				.<String, String> newHashMap());

		RestResources result = null;
		switch (serverResponse.getStatus()) {
			case ITuleapServerStatus.OK:
				// If we receive a 200 OK, the connection is good
				result = new RestResources(serverUrl, bestSupportedApiVersion, credentials);
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
						// TODO Send an OPTIONS request to this new URL to check the response
						result = new RestResources(serverUrl, newApiVersion, credentials);
					} else {
						// Error, invalid behavior of the server, invalid location
						status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
								TuleapMylynTasksMessages.getString("TuleapServer.InvalidAPILocation", //$NON-NLS-1$
										bestSupportedApiVersion));
					}
				} else {
					// Error, invalid behavior of the server, no new location
					status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
							TuleapMylynTasksMessages.getString("TuleapServer.MissingAPILocation", //$NON-NLS-1$
									bestSupportedApiVersion));
				}
				break;
			case ITuleapServerStatus.GONE:
				// If we receive a 410 Gone, the server does not support the required API, the connector
				// cannot work with this server
				// TODO How to deserialize the body here?
				String goneErrorMessage = serverResponse.getBody();
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString("TuleapServer.MissingCompatibleAPI", bestSupportedApiVersion, //$NON-NLS-1$
								goneErrorMessage));
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
		if (status != null) {
			throw new CoreException(status);
		}

		return result;
	}

	/**
	 * Provides access to the {code /login} HTTP resource.
	 * 
	 * @return A resource that gives access to the {code /login} HTTP resource.
	 */
	public RestUser user() {
		return new RestUser(serverUrl, apiVersion, credentials);
	}

	/**
	 * Provides access to the {code /projects} HTTP resource.
	 * 
	 * @return A resource that gives access to the {code /projects} HTTP resource.
	 */
	public RestProjects projects() {
		return new RestProjects(serverUrl, apiVersion, credentials);
	}

	/**
	 * Provides access to the {code /projects} HTTP resource.
	 * 
	 * @return A resource that gives access to the {code /projects} HTTP resource.
	 */
	public RestArtifacts artifacts() {
		return new RestArtifacts(serverUrl, apiVersion, credentials);
	}

	/**
	 * Provides access to the {code /projects} HTTP resource.
	 * 
	 * @param artifactId
	 *            Id of the artifact.
	 * @return A resource that gives access to the {code /projects} HTTP resource.
	 */
	public RestArtifacts artifacts(int artifactId) {
		return new RestArtifacts(serverUrl, apiVersion, credentials, artifactId);
	}

	/**
	 * Provides access to the {code /projects} HTTP resource.
	 * 
	 * @param milestoneId
	 *            Id of the milestone.
	 * @return A resource that gives access to the {code /milestones} HTTP resource.
	 */
	public RestMilestones milestone(int milestoneId) {
		return new RestMilestones(serverUrl, apiVersion, credentials, milestoneId);
	}

	/**
	 * Provides access to the {code /projects/:projectId/trackers} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/trackers} HTTP resource.
	 */
	public RestProjectsTrackers projectsTrackers(int projectId) {
		return new RestProjectsTrackers(serverUrl, apiVersion, credentials, projectId);
	}

	/**
	 * Provides access to the {code /projects/:projectId/top_plannings} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/top_plannings} HTTP resource.
	 */
	public RestProjectsTopPlannings projectsTopPlannings(int projectId) {
		return new RestProjectsTopPlannings(serverUrl, apiVersion, credentials, projectId);
	}

	/**
	 * Provides access to the {code /top_plannings/:id/milestones} HTTP resource.
	 * 
	 * @param topPlanningId
	 *            the top planning id
	 * @return A resource that gives access to the {code /top_plannings/:id/milestones} HTTP resource.
	 */
	public RestTopPlanningsMilestones topPlanningsMilestones(int topPlanningId) {
		return new RestTopPlanningsMilestones(serverUrl, apiVersion, credentials, topPlanningId);
	}

	/**
	 * Provides access to the {code /top_plannings/:id/backlog_items} HTTP resource.
	 * 
	 * @param topPlanningId
	 *            the top planning id
	 * @return A resource that gives access to the {code /top_plannings/:id/backlog_items} HTTP resource.
	 */
	public RestTopPlanningsBacklogItems topPlanningsBacklogItems(int topPlanningId) {
		return new RestTopPlanningsBacklogItems(serverUrl, apiVersion, credentials, topPlanningId);
	}

	/**
	 * Provides access to the {code /projects/:projectId/backlog_item_types} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/backlog_item_types} HTTP
	 *         resource.
	 */
	public RestProjectsBacklogItemTypes projectsBacklogItemTypes(int projectId) {
		return new RestProjectsBacklogItemTypes(serverUrl, apiVersion, credentials, projectId);
	}

	/**
	 * Provides access to the {code /projects/:projectId/milestone_types} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/milestone_types} HTTP resource.
	 */
	public RestProjectsMilestoneTypes projectsMilestoneTypes(int projectId) {
		return new RestProjectsMilestoneTypes(serverUrl, apiVersion, credentials, projectId);
	}

	/**
	 * Provides access to the {code /milestones/:id/backlog_items} HTTP resource.
	 * 
	 * @param milestoneId
	 *            the top planning id
	 * @return A resource that gives access to the {code /top_plannings/:id/backlog_items} HTTP resource.
	 */
	public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
		return new RestMilestonesBacklogItems(serverUrl, apiVersion, credentials, milestoneId);
	}
}
