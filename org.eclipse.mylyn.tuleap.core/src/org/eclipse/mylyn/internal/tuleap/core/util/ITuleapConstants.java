/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.internal.tuleap.core.util;

/**
 * This interface is a container of constants used accross the Mylyn tasks Tuleap connector.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapConstants {
	/**
	 * The kind of Mylyn tasks connector.
	 */
	String CONNECTOR_KIND = "tuleap"; //$NON-NLS-1$

	/**
	 * The descriptor file.
	 */
	String TULEAP_DESCRIPTOR_FILE = "tuleap.desciptorFile"; //$NON-NLS-1$

	/**
	 * The task separator.
	 */
	String REPOSITORY_TASK_URL_SEPARATOR = "&aid="; //$NON-NLS-1$

	/**
	 * The tracker separator.
	 */
	String REPOSITORY_TRACKER_URL_SEPARATOR = "&tracker="; //$NON-NLS-1$

	/**
	 * The group id separator.
	 */
	String REPOSITORY_GROUP_ID_URL_SEPARATOR = "&group_id="; //$NON-NLS-1$

	/**
	 * The ID representing the UTC timezone.
	 */
	String TIMEZONE_UTC = "UTC"; //$NON-NLS-1$

	/**
	 * The ID of the default static binding.
	 */
	String TULEAP_STATIC_BINDING_ID = "static"; //$NON-NLS-1$

	/**
	 * The ID of the default dynamic binding.
	 */
	String TULEAP_DYNAMIC_BINDING_USERS = "users"; //$NON-NLS-1$

	/**
	 * Part of the Tuleap repository URL. The URL format is :
	 * "https://<domainName>/plugins/tracker/?groupd_id=<groupdId>"
	 */
	String TULEAP_REPOSITORY_URL_STRUCTURE = "/plugins/tracker/?group_id="; //$NON-NLS-1$

	/**
	 * The suffix for the task data "leave" operation.
	 */
	String LEAVE_OPERATION = "leave"; //$NON-NLS-1$

	/**
	 * The url used to invoke the soap v1 services.
	 */
	String SOAP_V1_URL = "/soap/"; //$NON-NLS-1$

	/**
	 * The url used to invoke the soap v2 services.
	 */
	String SOAP_V2_URL = "/plugins/tracker/soap/"; //$NON-NLS-1$

	/**
	 * The separator used in the id of the task data.
	 */
	String TASK_DATA_ID_SEPARATOR = " #"; //$NON-NLS-1$

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
	 * The key used to identify the tracker id on which the query will be run.
	 */
	String QUERY_TRACKER_ID = "tuleap_query_tracker_id"; //$NON-NLS-1$

	/**
	 * Indicates that the user can read the field.
	 */
	String PERMISSION_READ = "read"; //$NON-NLS-1$

	/**
	 * Indicates that the user can update the field.
	 */
	String PERMISSION_UPDATE = "update"; //$NON-NLS-1$

	/**
	 * Indicates that the user can submit a newly created artifact with the field set.
	 */
	String PERMISSION_SUBMIT = "submit"; //$NON-NLS-1$

	/**
	 * The UTF-8 constants.
	 */
	String UTF8 = "UTF-8"; //$NON-NLS-1$

	/**
	 * The select box "none" value.
	 */
	String SELECT_BOX_NONE_VALUE = "None"; //$NON-NLS-1$

	/**
	 * The attributes query separator.
	 */
	String QUERY_ATTRIBUTES_SEPARATOR = ","; //$NON-NLS-1$
}
