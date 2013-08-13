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

import java.util.ArrayList;
import java.util.List;

/**
 * This enumeration contains all the available resources on the server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public enum Resource {
	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;.
	 */
	API__OPTIONS(Operation.OPTIONS, URL.API),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;.
	 */
	API__GET(Operation.GET, URL.API),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/login.
	 */
	LOGIN__OPTIONS(Operation.OPTIONS, URL.LOGIN),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/login.
	 */
	LOGIN__GET(Operation.GET, URL.LOGIN),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/logout.
	 */
	LOGOUT__OPTIONS(Operation.OPTIONS, URL.LOGIN),

	/**
	 * POST &lt;domain&gt;/api/&lt;api_version&gt;/logout.
	 */
	LOGOUT__POST(Operation.POST, URL.LOGIN),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/session.
	 */
	SESSION__OPTIONS(Operation.OPTIONS, URL.SESSION),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/session.
	 */
	SESSION__GET(Operation.GET, URL.SESSION),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/projects.
	 */
	PROJECTS__OPTIONS(Operation.OPTIONS, URL.PROJECTS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/projects.
	 */
	PROJECTS__GET(Operation.GET, URL.PROJECTS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/plannings.
	 */
	PROJECTS_PLANNINGS__OPTIONS(Operation.OPTIONS, URL.PROJECTS, URL.PLANNINGS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/plannings.
	 */
	PROJECTS_PLANNINGS__GET(Operation.GET, URL.PROJECTS, URL.PLANNINGS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/plannings.
	 */
	PLANNINGS__OPTIONS(Operation.OPTIONS, URL.PLANNINGS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/plannings.
	 */
	PLANNINGS__GET(Operation.GET, URL.PLANNINGS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/plannings/&lt;planning_id&gt;/milestones.
	 */
	PLANNINGS_MILESTONES__OPTIONS(Operation.OPTIONS, URL.PLANNINGS, URL.MILESTONES),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/plannings/&lt;planning_id&gt;/milestones.
	 */
	PLANNINGS_MILESTONES__GET(Operation.GET, URL.PLANNINGS, URL.MILESTONES),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/milestones.
	 */
	MILESTONES__OPTIONS(Operation.OPTIONS, URL.MILESTONES),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/milestones.
	 */
	MILESTONES__GET(Operation.GET, URL.MILESTONES),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/milestones/&lt;milestones_id&gt;/submilestones.
	 */
	MILESTONES_SUBMILESTONES__OPTIONS(Operation.OPTIONS, URL.MILESTONES, URL.SUBMILESTONES),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/milestones/&lt;milestones_id&gt;/submilestones.
	 */
	MILESTONES_SUBMILESTONES__GET(Operation.GET, URL.MILESTONES, URL.SUBMILESTONES),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/milestones/&lt;milestones_id&gt;/backlog_items.
	 */
	MILESTONES_BACKLOG_ITEMS__OPTIONS(Operation.OPTIONS, URL.MILESTONES, URL.BACKLOG_ITEMS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/milestones/&lt;milestones_id&gt;/backlog_items.
	 */
	MILESTONES_BACKLOG_ITEMS__GET(Operation.GET, URL.MILESTONES, URL.BACKLOG_ITEMS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/trackers.
	 */
	PROJECTS_TRACKERS__OPTIONS(Operation.OPTIONS, URL.PROJECTS, URL.TRACKERS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/trackers.
	 */
	PROJECTS_TRACKERS__GET(Operation.GET, URL.PROJECTS, URL.TRACKERS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/trackers.
	 */
	TRACKERS__OPTIONS(Operation.OPTIONS, URL.TRACKERS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/trackers.
	 */
	TRACKERS__GET(Operation.GET, URL.TRACKERS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/groups.
	 */
	PROJECTS_GROUP__OPTIONS(Operation.OPTIONS, URL.PROJECTS, URL.GROUPS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/groups.
	 */
	PROJECTS_GROUP__GET(Operation.GET, URL.PROJECTS, URL.GROUPS),

	/**
	 * OPTIONS
	 * &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/groups/&lt;group_id&gt;/members.
	 */
	PROJECTS_GROUPS_MEMBERS__OPTIONS(Operation.OPTIONS, URL.PROJECTS, URL.GROUPS, URL.MEMBERS),

	/**
	 * GEt &lt;domain&gt;/api/&lt;api_version&gt;/projects/&lt;project_id&gt;/groups/&lt;group_id&gt;/members.
	 */
	PROJECTS_GROUPS_MEMBERS__GET(Operation.GET, URL.PROJECTS, URL.GROUPS, URL.MEMBERS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/users.
	 */
	USERS__OPTIONS(Operation.OPTIONS, URL.USERS),

	/**
	 * GEt &lt;domain&gt;/api/&lt;api_version&gt;/users.
	 */
	USERS__GET(Operation.GET, URL.USERS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/reports.
	 */
	TRACKERS_REPORTS__OPTIONS(Operation.OPTIONS, URL.TRACKERS, URL.REPORTS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/reports.
	 */
	TRACKERS_REPORTS__GET(Operation.GET, URL.TRACKERS, URL.REPORTS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/query.
	 */
	TRACKERS_QUERY__OPTIONS(Operation.OPTIONS, URL.TRACKERS, URL.QUERY),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/query.
	 */
	TRACKERS_QUERY__GET(Operation.GET, URL.TRACKERS, URL.QUERY),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/artifacts.
	 */
	TRACKERS_ARTIFACTS__OPTIONS(Operation.OPTIONS, URL.TRACKERS, URL.ARTIFACTS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/artifacts.
	 */
	TRACKERS_ARTIFACTS__GET(Operation.GET, URL.TRACKERS, URL.ARTIFACTS),

	/**
	 * POST &lt;domain&gt;/api/&lt;api_version&gt;/trackers/&lt;tracker_id&gt;/artifacts.
	 */
	TRACKERS_ARTIFACTS__POST(Operation.POST, URL.TRACKERS, URL.ARTIFACTS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/artifacts.
	 */
	ARTIFACTS__OPTIONS(Operation.OPTIONS, URL.ARTIFACTS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/artifacts.
	 */
	ARTIFACTS__GET(Operation.GET, URL.ARTIFACTS),

	/**
	 * PUT &lt;domain&gt;/api/&lt;api_version&gt;/artifacts.
	 */
	ARTIFACTS__PUT(Operation.PUT, URL.ARTIFACTS),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/files/chunked_upload.
	 */
	FILES_CHUNKED_UPLOAD__OPTIONS(Operation.OPTIONS, URL.FILES, URL.CHUNKED_UPLOAD),

	/**
	 * PUT &lt;domain&gt;/api/&lt;api_version&gt;/files/chunked_upload.
	 */
	FILES_CHUNKED_UPLOAD__PUT(Operation.PUT, URL.FILES, URL.CHUNKED_UPLOAD),

	/**
	 * POST &lt;domain&gt;/api/&lt;api_version&gt;/files/chunked_upload.
	 */
	FILES_CHUNKED_UPLOAD__POST(Operation.POST, URL.FILES, URL.CHUNKED_UPLOAD),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/files.
	 */
	FILES__OPTIONS(Operation.OPTIONS, URL.FILES),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/files.
	 */
	FILES__GET(Operation.GET, URL.FILES);

	/**
	 * The segments of the URL of the resource.
	 */
	private final String[] segments;

	/**
	 * The operation of the resource.
	 */
	private final Operation operation;

	/**
	 * The path variables of the URL.
	 */
	private final List<String> pathVariables = new ArrayList<String>();

	/**
	 * the constructor.
	 * 
	 * @param operation
	 *            The operation
	 * @param segments
	 *            The segments
	 */
	private Resource(Operation operation, String... segments) {
		this.segments = segments;
		this.operation = operation;
	}

	/**
	 * Adds the variable to the list of path variables.
	 * 
	 * @param variable
	 *            The variable
	 */
	public void addPathVariable(String variable) {
		this.pathVariables.add(variable);
	}

	/**
	 * Returns the url of the resource.
	 * 
	 * @return The url of the resource
	 */
	public String getUrl() {
		StringBuilder stringBuilder = new StringBuilder();

		int i = 0;
		for (String segment : this.segments) {
			stringBuilder.append(segment);

			if (pathVariables.size() > i) {
				stringBuilder.append('/');
				stringBuilder.append(pathVariables.get(i));
			}

			i++;
		}

		return stringBuilder.toString();
	}

	/**
	 * Returns the operation of the resource.
	 * 
	 * @return The operation of the resource
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * This interface contains the list of all the available URL in the REST api.
	 * 
	 * @noextend This class is not intended to be subclassed by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 */
	public interface URL {

		/**
		 * "".
		 */
		String API = ""; //$NON-NLS-1$

		/**
		 * "/login".
		 */
		String LOGIN = "/login"; //$NON-NLS-1$

		/**
		 * "/logout".
		 */
		String LOGOUT = "/logout"; //$NON-NLS-1$

		/**
		 * "/session".
		 */
		String SESSION = "/session"; //$NON-NLS-1$

		/**
		 * "/projects".
		 */
		String PROJECTS = "/projects"; //$NON-NLS-1$

		/**
		 * "/plannings".
		 */
		String PLANNINGS = "/plannings"; //$NON-NLS-1$

		/**
		 * "/milestones".
		 */
		String MILESTONES = "/milestones"; //$NON-NLS-1$

		/**
		 * "/submilestones".
		 */
		String SUBMILESTONES = "/submilestones"; //$NON-NLS-1$

		/**
		 * "/backlog_items".
		 */
		String BACKLOG_ITEMS = "/backlog_items"; //$NON-NLS-1$

		/**
		 * "/trackers".
		 */
		String TRACKERS = "/trackers"; //$NON-NLS-1$

		/**
		 * "/groups".
		 */
		String GROUPS = "/groups"; //$NON-NLS-1$

		/**
		 * "/members".
		 */
		String MEMBERS = "/members"; //$NON-NLS-1$

		/**
		 * "/users".
		 */
		String USERS = "/users"; //$NON-NLS-1$

		/**
		 * "/reports".
		 */
		String REPORTS = "/reports"; //$NON-NLS-1$

		/**
		 * "/query".
		 */
		String QUERY = "/query"; //$NON-NLS-1$

		/**
		 * "/artifacts".
		 */
		String ARTIFACTS = "/artifacts"; //$NON-NLS-1$

		/**
		 * "/files".
		 */
		String FILES = "/files"; //$NON-NLS-1$

		/**
		 * "/chunked_upload".
		 */
		String CHUNKED_UPLOAD = "chunked_upload"; //$NON-NLS-1$
	}

	/**
	 * This enumeration contains the list of all the available operations in the REST api.
	 * 
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 */
	public enum Operation {
		/**
		 * Request the description of the resource.
		 */
		OPTIONS,

		/**
		 * Request a representation of the resource.
		 */
		GET,

		/**
		 * Create the resource.
		 */
		POST,

		/**
		 * Replace the representation of the resource with a new one.
		 */
		PUT
	}
}
