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
package org.tuleap.mylyn.task.internal.core.util;

import org.eclipse.osgi.util.NLS;

/**
 * The list of all possible keys to use for internationalization.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapMylynTasksMessagesKeys extends NLS {
	/** Qualified path to the properties file in which to seek the keys. */
	private static final String BUNDLE_NAME = "org.tuleap.mylyn.task.internal.core.util.tuleap_mylyn_tasks_messages"; //$NON-NLS-1$

	// CHECKSTYLE:OFF (Javadoc is __still__ required!)

	/**
	 * The label of the repository connector used in the wizard where the user select the kind of repository
	 * to create.
	 */
	public static String tuleapRepositoryConnectorLabel;

	/**
	 * Error message for a logging issue.
	 */
	public static String elementNotFound;

	/**
	 * Error message for a logging issue.
	 */
	public static String logNullStatus;

	/**
	 * Error message for a logging issue.
	 */
	public static String logNullException;

	/**
	 * Error message for a logging issue.
	 */
	public static String unexpectedException;

	/**
	 * The default name of a configuration.
	 */
	public static String defaultConfigurationName;

	/**
	 * The status label.
	 */
	public static String statusLabel;

	/**
	 * The assigned to label.
	 */
	public static String assignedToLabel;

	/**
	 * The creation date label.
	 */
	public static String creationDateLabel;

	/**
	 * The last modification label.
	 */
	public static String lastModificationDateLabel;

	/**
	 * The completion date label.
	 */
	public static String completionDateLabel;

	/**
	 * The leave as label.
	 */
	public static String leaveAsLabel;

	/**
	 * The new comment label.
	 */
	public static String newCommentLabel;

	/**
	 * The mark as label.
	 */
	public static String markAsLabel;

	/**
	 * The default new title.
	 */
	public static String defaultNewTitle;

	/**
	 * Error message for issues with the creation of the temporary file used to download attachments.
	 */
	public static String problemWithTempFileCreation;

	/**
	 * Unknow host.
	 */
	public static String unknownHost;

	/**
	 * Error during the parsing of the configuration retrieved.
	 */
	public static String repositoryConfigurationParseError;

	/**
	 * Failure to retrieve the configuration.
	 */
	public static String failToRetrieveTheConfiguration;

	/**
	 * Validate the connection.
	 */
	public static String validateConnection;

	/**
	 * Login.
	 */
	public static String login;

	/**
	 * Retrieve the Tuleap instance configuration.
	 */
	public static String retrieveTuleapInstanceConfiguration;

	/**
	 * Retrieve the list of trackers.
	 */
	public static String retrievingTrackersList;

	/**
	 * Analyze the tracker.
	 */
	public static String analyzingTracker;

	/**
	 * Retrieve the description of the fields from the tracker.
	 */
	public static String retrievingFieldsDescriptionFromTracker;

	/**
	 * Unsupported tracker field type.
	 */
	public static String unsupportedTrackerFieldType;

	/**
	 * Analyze the tracker field.
	 */
	public static String analyzeTuleapTrackerField;

	/**
	 * Executing a query.
	 */
	public static String executingQuery;

	/**
	 * Retrieving the tracker fields.
	 */
	public static String retrievingTrackerFields;

	/**
	 * Retrieving the tracker semantic.
	 */
	public static String retrievingTrackerSemantic;

	/**
	 * Retrieving the comments.
	 */
	public static String retrieveComments;

	/**
	 * Retrieving the reports.
	 */
	public static String retrievingTheReports;

	/**
	 * Retrieving the content of the attachments.
	 */
	public static String retrievingAttachmentContent;

	/**
	 * Downloading the attachments.
	 */
	public static String retrievingAttachmentContentFor;

	/**
	 * Uploading an attachment.
	 */
	public static String uploadingAttachmentContent;

	/**
	 * The default comment.
	 */
	public static String defaultComment;

	/**
	 * Missing API location.
	 */
	public static String missingAPILocation;

	/**
	 * Invalid API location.
	 */
	public static String invalidAPILocation;

	/**
	 * Invalid behavior.
	 */
	public static String invalidBehavior;

	/**
	 * Missing compatible API.
	 */
	public static String missingCompatibleAPI;

	/**
	 * API not found.
	 */
	public static String aPINotFound;

	/**
	 * Cannot perform operation. 2 parameters:
	 * <ol>
	 * <li>URL</li>
	 * <li>HTTP method</li>
	 * </ol>
	 */
	public static String cannotPerformOperation;

	/**
	 * Not authorized to perform operation. 2 parameters:
	 * <ol>
	 * <li>URL</li>
	 * <li>HTTP method</li>
	 * </ol>
	 */
	public static String notAuthorizedToPerformOperation;

	/**
	 * The server returned an error. 4 parameters:
	 * <ol>
	 * <li>URL</li>
	 * <li>HTTP method</li>
	 * <li>Error code</li>
	 * <li>Error message returned by the server</li>
	 * </ol>
	 */
	public static String errorReturnedByServer;

	/**
	 * The server returned an error. 5 parameters:
	 * <ol>
	 * <li>URL</li>
	 * <li>HTTP method</li>
	 * <li>Error code</li>
	 * <li>Error message returned by the server</li>
	 * <li>Debug source returned by the server</li>
	 * </ol>
	 */
	public static String errorReturnedByServerWithDebug;

	/**
	 * Invalid pagination header. No parameter.
	 */
	public static String invalidPaginationHeader;

	/**
	 * Invalid credentials. No parameter.
	 */
	public static String invalidCredentials;

	/**
	 * Method &lt;METHOD> is not supported for resource &lt;URL>. 2 parameters:
	 * <ol>
	 * <li>HTTP method</li>
	 * <li>resource URL</li>
	 * </ol>
	 */
	public static String operationNotAllowedOnResource;

	/**
	 * Retrieve the list of projects.
	 */
	public static String retrievingProjectsList;

	/**
	 * Retrieve a milestone.
	 */
	public static String retrievingMilestone;

	/**
	 * Retrieve backlogItems of a specific milestone.
	 */
	public static String retrievingBacklogItems;

	/**
	 * Retrieve submilestones of a specific milestone.
	 */
	public static String retrievingSubMilestones;

	/**
	 * Retrieve cardwall of a specific milestone.
	 */
	public static String retrievingCardwall;

	/**
	 * Updating the cardwall.
	 */
	public static String updatingCardwall;

	/**
	 * Updating a backlogItem.
	 */
	public static String updatingBacklogItem;

	/**
	 * Update backlogItems of a specific milestone.
	 */
	public static String updatingBacklogItems;

	/**
	 * Updating a card.
	 */
	public static String updatingCard;

	/**
	 * Create a milestone.
	 */
	public static String creatingMilestone;

	/**
	 * Retrieve a backlogItem.
	 */
	public static String retrievingBacklogItem;

	/**
	 * Retrieving topPlanning of a specific project.
	 */
	public static String retrievingTopPlannings;

	/**
	 * Retrieving a topPlanning.
	 */
	public static String retrievingTopPlanning;

	static {
		// load message values from bundle file
		reloadMessages();
	}

	/**
	 * Reloads the messages from the properties file.
	 */
	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, TuleapMylynTasksMessagesKeys.class);
	}
}
