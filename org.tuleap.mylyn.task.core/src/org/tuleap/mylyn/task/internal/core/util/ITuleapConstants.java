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
	 * The key used for the project id of the POJO.
	 */
	String PROJECT_ID = "project_id"; //$NON-NLS-1$

	/**
	 * The key used for the label of the POJO.
	 */
	String LABEL = "label"; //$NON-NLS-1$

	/**
	 * The key used for the URL of the POJO.
	 */
	String URL = "url"; //$NON-NLS-1$

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
	String DURATION = "duration"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the capacity of the milestone.
	 */
	String CAPACITY = "capacity"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the type id of the milestone.
	 */
	String MILESTONE_TYPE_ID = "milestone_type_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the sub-milestones.
	 */
	String SUBMILESTONES = "submilestones"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the type id of the backlogItem.
	 */
	String BACKLOGITEM_TYPE_ID = "backlog_item_type_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the initial effort of the backlogItem.
	 */
	String INITIAL_EFFORT = "initial_effort"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the backlogItem assigned milestone identifier.
	 */
	String ASSIGNED_MILESTONE_ID = "assigned_milestone_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the type id of the card.
	 */
	String CARD_TYPE_ID = "card_type_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the color of the card.
	 */
	String COLOR = "color"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the status identifier of the card.
	 */
	String STATUS_ID = "status_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the status list of the cardwall.
	 */
	String STATUS_LIST = "status_list"; //$NON-NLS-1$

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
	String JSON_PROJECT_ID = "project_id"; //$NON-NLS-1$
}
