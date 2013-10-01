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
package org.tuleap.mylyn.task.internal.core.client;

/**
 * Utility interface used to store all the constants related to the creation and manipulation of queries.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ITuleapQueryConstants {
	/**
	 * The key used to specify the kind of the query.
	 */
	String QUERY_KIND = "tuleap_query_kind"; //$NON-NLS-1$

	/**
	 * The key used to specify that the query is looking for all artifacts from a specific tracker.
	 */
	String QUERY_KIND_ALL_FROM_TRACKER = "tuleap_query_all_from_tracker"; //$NON-NLS-1$

	/**
	 * The key used to specify that the query is a custom query.
	 */
	String QUERY_KIND_CUSTOM = "tuleap_query_custom"; //$NON-NLS-1$

	/**
	 * The key used to specify that the query will execute a report.
	 */
	String QUERY_KIND_REPORT = "tuleap_query_report"; //$NON-NLS-1$

	/**
	 * The key used to specify that the query will execute a top level planning.
	 */
	String QUERY_KIND_TOP_LEVEL_PLANNING = "tuleap_query_top_level_planning"; //$NON-NLS-1$

	/**
	 * The id of the report to run.
	 */
	String QUERY_REPORT_ID = "tuleap_query_report_id"; //$NON-NLS-1$

	/**
	 * The key used to identify the configuration id on which the query will be run.
	 */
	String QUERY_CONFIGURATION_ID = "tuleap_query_configuration_id"; //$NON-NLS-1$

	/**
	 * The key used to identify the project id on which the query will be run.
	 */
	String QUERY_PROJECT_ID = "tuleap_query_project_id"; //$NON-NLS-1$
}
