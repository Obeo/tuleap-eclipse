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
	 * The identifier of the none binding.
	 */
	int CONFIGURABLE_FIELD_NONE_BINDING_ID = 100;

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

	/**
	 * The key used for the id of the POJO.
	 */
	String ID = "id"; //$NON-NLS-1$

	/**
	 * The key used for the label of the POJO.
	 */
	String LABEL = "label"; //$NON-NLS-1$

	/**
	 * The key used for the URL of the POJO.
	 * 
	 * @deprecated
	 */
	@Deprecated
	String URL = "url"; //$NON-NLS-1$

	/**
	 * The key used for the REST URI of the POJO.
	 */
	String URI = "uri"; //$NON-NLS-1$

	/**
	 * The key used for the HTML URL of the POJO.
	 */
	String HTML_URL = "html_url"; //$NON-NLS-1$

	/**
	 * The key used for the field values of the POJO.
	 */
	String VALUES = "values"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the field identifier of the POJO.
	 */
	String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the field value of the POJO.
	 */
	String FIELD_VALUE = "value"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the bind value identifier.
	 */
	String FIELD_BIND_VALUE_ID = "bind_value_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the list of bind value identifiers.
	 */
	String FIELD_BIND_VALUE_IDS = "bind_value_ids"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the list of file descriptions.
	 */
	String FILE_DESCRIPTIONS = "file_descriptions"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file identifier.
	 */
	String FILE_ID = "file_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the person that submit the file.
	 */
	String SUBMITTED_BY = "submitted_by"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the date of creation.
	 */
	String SUBMITTED_ON = "submitted_on"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the date of last modification.
	 */
	String LAST_UPDATED_ON = "last_updated_on"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file description.
	 */
	String DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file name.
	 */
	String NAME = "name"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file size.
	 */
	String SIZE = "size"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file type.
	 */
	String TYPE = "type"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the start date of the milestone.
	 */
	String START_DATE = "start_date"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the duration of the milestone.
	 */
	String END_DATE = "end_date"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the capacity of the milestone.
	 */
	String CAPACITY = "capacity"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the initial effort of the backlogItem.
	 */
	String INITIAL_EFFORT = "initial_effort"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the color of a cardwall column.
	 */
	String COLOR = "color"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the accent color of a card.
	 */
	String ACCENT_COLOR = "accent_color"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the column identifier of the card.
	 */
	String COLUMN_ID = "column_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the columns of the cardwall.
	 */
	String COLUMNS = "columns"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the swimlanes of the cardwall.
	 */
	String SWIMLANES = "swimlanes"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the backlogItem of the swimlane.
	 */
	String BACKLOG_ITEM = "backlog_item"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the cards of the swimlane.
	 */
	String CARDS = "cards"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the comment of the milestone.
	 */
	String COMMENT = "comment"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the comment body of the milestone.
	 */

	String BODY = "body"; //$NON-NLS-1$

	/**
	 * The key used for the title of milestones in a top-planning for instance.
	 */
	String JSON_MILESTONES_TITLE = "milestones_title"; //$NON-NLS-1$

	/**
	 * The key used for the title of backlog items in a top-planning for instance.
	 */
	String JSON_BACKLOG_ITEMS_TITLE = "backlog_items_title"; //$NON-NLS-1$

	/**
	 * The key used for the project ID of an element possessed by a project.
	 */
	String JSON_TRACKER = "tracker"; //$NON-NLS-1$

	/**
	 * The key used for the project ref of an element possessed by a project.
	 */
	String JSON_PROJECT = "project"; //$NON-NLS-1$

	/**
	 * The key used for the artifact ref of an element possessed by a project.
	 */
	String JSON_ARTIFACT = "artifact"; //$NON-NLS-1$

	/**
	 * The key used for the status label of a milestone.
	 */
	String JSON_STATUS_VALUE = "status_value"; //$NON-NLS-1$

	/**
	 * The key used for the sub-milestones URI of a milestone.
	 */
	String JSON_SUB_MILESTONES_URI = "sub_milestones_uri"; //$NON-NLS-1$

	/**
	 * The key used for the backlog items URI of a milestone.
	 */
	String JSON_BACKLOG_ITEMS_URI = "backlog_items_uri"; //$NON-NLS-1$

	/**
	 * The key used for the parent of a milestone.
	 */
	String JSON_PARENT = "parent"; //$NON-NLS-1$

	/**
	 * The key used for the list of backlog item trackers in a planning.
	 */
	String JSON_BACKLOG_TRACKERS = "backlog_trackers"; //$NON-NLS-1$

	/**
	 * The key used for the status of a card ("Open" or "Closed").
	 */
	String JSON_STATUS = "status"; //$NON-NLS-1$

	/**
	 * The key used for the allowed column ids of a card.
	 */
	String JSON_ALLOWED_COLUMNS = "allowed_column_ids"; //$NON-NLS-1$

	/**
	 * The key used to retrieve assigned milestone reference for a backlog item.
	 */
	String JSON_ASSIGNED_MILESTONE = "assigned_milestone"; //$NON-NLS-1$

	/**
	 * The identifier of the Tuleap preference node.
	 */
	String TULEAP_PREFERENCE_NODE = "tuleap_preference_node"; //$NON-NLS-1$

	/**
	 * The identifier of the Tuleap preference for the activation of the debug mode.
	 */
	String TULEAP_PREFERENCE_DEBUG_MODE = "tuleap_preference_debug_mode"; //$NON-NLS-1$

}
