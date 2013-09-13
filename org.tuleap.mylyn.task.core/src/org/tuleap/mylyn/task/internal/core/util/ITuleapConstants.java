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
package org.tuleap.mylyn.task.internal.core.util;

/**
 * This interface is a container of constants used accross the Mylyn tasks Tuleap connector.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
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
	 * The separator between the project name and the tracker id.
	 */
	String TRACKER_ID_SEPARATOR = ":"; //$NON-NLS-1$

	/**
	 * The separator used in the id of the task data.
	 */
	String TASK_DATA_ID_SEPARATOR = "-"; //$NON-NLS-1$

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
	String QUERY_GROUP_ID = "tuleap_query_group_id"; //$NON-NLS-1$

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
	 * The start delimiter of the tracker id.
	 */
	String TRACKER_ID_START_DELIMITER = "["; //$NON-NLS-1$

	/**
	 * The end delimiter of the tracker id.
	 */
	String TRACKER_ID_END_DELIMITER = "]"; //$NON-NLS-1$

	/**
	 * The identifier of the none binding.
	 */
	int TRACKER_FIELD_NONE_BINDING_ID = 100;

	/**
	 * The attributes query separator.
	 */
	String QUERY_ATTRIBUTES_SEPARATOR = ","; //$NON-NLS-1$

	/**
	 * The constant used to specify the name of the field to use to upload new files.
	 */
	String ATTACHMENT_FIELD_NAME = "tuleap_attachment_field_name"; //$NON-NLS-1$

	/**
	 * The constant used to specify the label of the field to use to upload new files.
	 */
	String ATTACHMENT_FIELD_LABEL = "tuleap_attachment_field_label"; //$NON-NLS-1$

	/**
	 * The constant used to specify the id of the field with the "initial effort" semantic.
	 */
	String INITIAL_EFFORT_FIELD_ID = "tuleap_initial_effort"; //$NON-NLS-1$

	/**
	 * The identifier of an anonymous user.
	 */
	int ANONYMOUS_USER_INFO_IDENTIFIER = 0;

	/**
	 * The username of an anonymous user.
	 */
	String ANONYMOUS_USER_INFO_USERNAME = "anonymous"; //$NON-NLS-1$

	/**
	 * The real name of an anonymous user.
	 */
	String ANONYMOUS_USER_INFO_REAL_NAME = "Anonymous User"; //$NON-NLS-1$

	/**
	 * The email address of an anonymous user.
	 */
	String ANONYMOUS_USER_INFO_EMAIL = "anonymous@tuleap.net"; //$NON-NLS-1$

	/**
	 * The LDAP identifier of an anonymous user.
	 */
	String ANONYMOUS_USER_INFO_LDAP_IDENTIFIER = ""; //$NON-NLS-1$
}
