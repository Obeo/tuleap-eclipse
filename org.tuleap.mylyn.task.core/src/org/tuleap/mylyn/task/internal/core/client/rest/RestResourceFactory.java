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

import org.eclipse.core.runtime.Assert;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResource.URL;

/**
 * Builder class that instantiates the accessible JSON services.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class RestResourceFactory {

	/**
	 * The serverUrl of the REST API on the server, for example {@code http://localhost:3001}.
	 */
	private final String serverUrl;

	/**
	 * The version of the API to use.
	 */
	private final String apiVersion;

	/**
	 * The connector to use.
	 */
	private final IRestConnector connector;

	/**
	 * Constructor.
	 * 
	 * @param serverUrl
	 *            URL of the rest API on the server.
	 * @param apiVersion
	 *            the API version to use.
	 * @param connector
	 *            The connector to use.
	 */
	public RestResourceFactory(String serverUrl, String apiVersion, IRestConnector connector) {
		this.serverUrl = serverUrl;
		this.apiVersion = apiVersion;
		Assert.isNotNull(connector);
		this.connector = connector;
	}

	/**
	 * Provides access to the {code /login} HTTP resource.
	 * 
	 * @return A resource that gives access to the {code /login} HTTP resource.
	 */
	public RestResource user() {
		return resource(RestResource.GET, URL.USER);
	}

	/**
	 * Provides access to the {code /projects} HTTP resource.
	 * 
	 * @return A resource that gives access to the {code /projects} HTTP resource.
	 */
	public RestResource projects() {
		return resource(RestResource.GET, URL.PROJECTS);
	}

	/**
	 * Provides access to the {code /artifacts} HTTP resource.
	 * 
	 * @return A resource that gives access to the {code /artifacts} HTTP resource.
	 */
	public RestResource artifacts() {
		return resource(RestResource.GET, URL.ARTIFACTS);
	}

	/**
	 * Provides access to the {code /artifacts/:id} HTTP resource.
	 * 
	 * @param artifactId
	 *            Id of the artifact.
	 * @return A resource that gives access to the {code /artifacts/:id} HTTP resource.
	 */
	public RestResource artifacts(int artifactId) {
		return resource(RestResource.GET, URL.ARTIFACTS, Integer.toString(artifactId));
	}

	/**
	 * Provides access to the {code /milestones/:id} HTTP resource.
	 * 
	 * @param milestoneId
	 *            Id of the milestone.
	 * @return A resource that gives access to the {code /milestones/:id} HTTP resource.
	 */
	public RestResource milestone(int milestoneId) {
		return resource(RestResource.GET | RestResource.PUT, URL.MILESTONES, Integer.toString(milestoneId));
	}

	/**
	 * Provides access to the {code /projects/:projectId/trackers} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/trackers} HTTP resource.
	 */
	public RestResource projectsTrackers(int projectId) {
		return resource(RestResource.GET, URL.PROJECTS, Integer.toString(projectId), URL.TRACKERS);
	}

	/**
	 * Provides access to the {code /projects/:projectId/top_plannings} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/top_plannings} HTTP resource.
	 */
	public RestResource projectsTopPlannings(int projectId) {
		return resource(RestResource.GET, URL.PROJECTS, Integer.toString(projectId), URL.TOP_PLANNINGS);
	}

	/**
	 * Provides access to the {code /top_plannings/:id} HTTP resource.
	 * 
	 * @param topPlanningId
	 *            the top planning id
	 * @return A resource that gives access to the {code /top_plannings/:id} HTTP resource.
	 */
	public RestResource topPlannings(int topPlanningId) {
		return resource(RestResource.GET, URL.TOP_PLANNINGS, Integer.toString(topPlanningId));
	}

	/**
	 * Provides access to the {code /top_plannings/:id/milestones} HTTP resource.
	 * 
	 * @param topPlanningId
	 *            the top planning id
	 * @return A resource that gives access to the {code /top_plannings/:id/milestones} HTTP resource.
	 */
	public RestResource topPlanningsMilestones(int topPlanningId) {
		return resource(RestResource.GET, URL.TOP_PLANNINGS, Integer.toString(topPlanningId), URL.MILESTONES);
	}

	/**
	 * Provides access to the {code /top_plannings/:id/backlog_items} HTTP resource.
	 * 
	 * @param topPlanningId
	 *            the top planning id
	 * @return A resource that gives access to the {code /top_plannings/:id/backlog_items} HTTP resource.
	 */
	public RestResource topPlanningsBacklogItems(int topPlanningId) {
		return resource(RestResource.GET, URL.TOP_PLANNINGS, Integer.toString(topPlanningId),
				URL.BACKLOG_ITEMS);
	}

	/**
	 * Provides access to the {code /projects/:projectId/backlog_item_types} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/backlog_item_types} HTTP
	 *         resource.
	 */
	public RestResource projectsBacklogItemTypes(int projectId) {
		return resource(RestResource.GET, URL.PROJECTS, Integer.toString(projectId), URL.BACKLOG_ITEM_TYPES);
	}

	/**
	 * Provides access to the {code /backlog_item_types/:backlogItemTypeId} HTTP resource.
	 * 
	 * @param backlogItemId
	 *            The identifier of the backlog item type
	 * @return A resource that gives access to the {code /backlog_item_types/:backlogItemTypeId} HTTP
	 *         resource.
	 */
	public RestResource backlogItemType(int backlogItemId) {
		return resource(RestResource.GET, URL.BACKLOG_ITEM_TYPES, Integer.toString(backlogItemId));
	}

	/**
	 * Provides access to the {code /projects/:projectId/milestone_types} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/milestone_types} HTTP resource.
	 */
	public RestResource projectsMilestoneTypes(int projectId) {
		return resource(RestResource.GET, URL.PROJECTS, Integer.toString(projectId), URL.MILESTONE_TYPES);
	}

	/**
	 * Provides access to the {code /milestone_types/:milestoneTypeId} HTTP resource.
	 * 
	 * @param milestoneTypeId
	 *            The identifier of the milestone type
	 * @return A resource that gives access to the {code /milestone_types/:milestoneTypeId} HTTP resource
	 */
	public RestResource milestoneType(int milestoneTypeId) {
		return resource(RestResource.GET, URL.MILESTONE_TYPES, Integer.toString(milestoneTypeId));
	}

	/**
	 * Provides access to the {code /projects/:projectId/card_types} HTTP resource.
	 * 
	 * @param projectId
	 *            the project id
	 * @return A resource that gives access to the {code /projects/:projectId/card_types} HTTP resource.
	 */
	public RestResource projectsCardTypes(int projectId) {
		return resource(RestResource.GET, URL.PROJECTS, Integer.toString(projectId), URL.CARD_TYPES);
	}

	/**
	 * Provides access to the {code /card_types/:cardTypeId} HTTP resource.
	 * 
	 * @param cardTypeId
	 *            The identifier of the card type
	 * @return A resource that gives access to the {code /card_types/:cardTypeId} HTTP resource.
	 */
	public RestResource cardType(int cardTypeId) {
		return resource(RestResource.GET, URL.CARD_TYPES, Integer.toString(cardTypeId));
	}

	/**
	 * Provides access to the {code /milestones/:id/submilestones} HTTP resource.
	 * 
	 * @param milestoneId
	 *            the milestone id
	 * @return A resource that gives access to the {code /milestones/:id/submilestones} HTTP resource.
	 */
	public RestResource milestonesSubmilestones(int milestoneId) {
		return resource(RestResource.GET, URL.MILESTONES, Integer.toString(milestoneId), URL.SUBMILESTONES);
	}

	/**
	 * Provides access to the {code /milestones/:id/backlog_items} HTTP resource.
	 * 
	 * @param milestoneId
	 *            the milestone id
	 * @return A resource that gives access to the {code /milestones/:id/backlog_items} HTTP resource.
	 */
	public RestResource milestonesBacklogItems(int milestoneId) {
		return resource(RestResource.GET | RestResource.PUT, URL.MILESTONES, Integer.toString(milestoneId),
				URL.BACKLOG_ITEMS);
	}

	/**
	 * Provides access to the {code /milestones/:id/cardwall} HTTP resource.
	 * 
	 * @param milestoneId
	 *            the milestone id
	 * @return A resource that gives access to the {code /milestones/:id/cardwall} HTTP resource.
	 */
	public RestResource milestonesCardwall(int milestoneId) {
		return resource(RestResource.GET, URL.MILESTONES, Integer.toString(milestoneId), URL.CARDWALL);
	}

	/**
	 * Provides access to the {code /cards/:id} HTTP resource.
	 * 
	 * @param cardId
	 *            the card id
	 * @return A resource that gives access to the {code /cards/:id} HTTP resource.
	 */
	public RestResource cards(int cardId) {
		return resource(RestResource.GET | RestResource.PUT, URL.CARDS, Integer.toString(cardId));
	}

	/**
	 * Obtain a resource from its URL fragments.
	 * 
	 * @param supportedMethods
	 *            Flags for supported methods.
	 * @param urlFragments
	 *            Fragments that will be concatenated to build the resource URL.
	 * @return A new resource with the URL built from the given fragments, and a serverURL and apiVersion
	 *         equal to those of this {@link RestResourceFactory} instance.
	 */
	public RestResource resource(int supportedMethods, String... urlFragments) {
		final String url = url(urlFragments);
		return new RestResource(serverUrl, apiVersion, url, supportedMethods, connector);
	}

	/**
	 * Build a URL by concatenating the given fragments.
	 * 
	 * @param urlFragments
	 *            Fragments to concatenate.
	 * @return The URL built by concatenating the given fragments, separated by a slash ('/') character.
	 */
	private String url(String... urlFragments) {
		Assert.isNotNull(urlFragments);
		Assert.isTrue(urlFragments.length > 0);
		StringBuilder b = new StringBuilder();
		b.append(urlFragments[0]);
		int i = 1;
		while (i < urlFragments.length) {
			b.append('/').append(urlFragments[i++]);
		}
		return b.toString();
	}
}
