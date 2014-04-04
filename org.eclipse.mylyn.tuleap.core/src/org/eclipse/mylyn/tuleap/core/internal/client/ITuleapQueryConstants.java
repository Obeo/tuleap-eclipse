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
package org.eclipse.mylyn.tuleap.core.internal.client;

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
	 * The key used to specify that the query is a custom query.
	 */
	String QUERY_KIND_CUSTOM = "tuleap_query_custom"; //$NON-NLS-1$

	/**
	 * The key used to specify that the query will execute a report.
	 */
	String QUERY_KIND_REPORT = "tuleap_query_report"; //$NON-NLS-1$

	/**
	 * The id of the report to run.
	 */
	String QUERY_REPORT_ID = "tuleap_query_report_id"; //$NON-NLS-1$

	/**
	 * The key used to identify the tracker id on which the query will be run.
	 */
	String QUERY_TRACKER_ID = "tuleap_query_tracker_id"; //$NON-NLS-1$

	/**
	 * The key used to identify the project id on which the query will be run.
	 */
	String QUERY_PROJECT_ID = "tuleap_query_project_id"; //$NON-NLS-1$

	/**
	 * The key used to identify the project id on which the query will be run. The value associated to this
	 * key must be the JSON serialization of a Map<String, Criterion> where Criterion has an operator and a
	 * value (String, Number, String[]). This JSON is NOT exactly the same as the JSON expected by Tuleap. We
	 * use field names instead of field_ids (which is ok with Tuleap) but alos value instead of IDs for bound
	 * fields (combo-boxes).
	 */
	String QUERY_CUSTOM_CRITERIA = "tuleap_query_custom_criteria"; //$NON-NLS-1$
}
