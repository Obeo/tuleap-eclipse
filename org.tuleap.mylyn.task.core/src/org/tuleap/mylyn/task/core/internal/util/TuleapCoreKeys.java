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
package org.tuleap.mylyn.task.core.internal.util;

import org.eclipse.osgi.util.NLS;

/**
 * The list of all possible keys to use for internationalization.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapCoreKeys extends NLS {
	/** Qualified path to the properties file in which to seek the keys. */
	private static final String BUNDLE_NAME = "org.tuleap.mylyn.task.core.internal.util.tuleap_core_messages"; //$NON-NLS-1$

	// CHECKSTYLE:OFF (Javadoc is __still__ required!)
	/**
	 * The message to log when parsing card status encounter a problem.
	 */
	public static String gettingStatusValueLogMessage;

	/**
	 * The message to log when parsing date encounter a problem.
	 */
	public static String dateParsingLogMessage;

	/**
	 * The label of the tracker.
	 */
	public static String trackerLabel;

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
	 * The default name of a tracker.
	 */
	public static String defaultTrackerName;

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
	 * The label used to indicate milestone parent id.
	 */
	public static String parentLabel;

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
	 * Failed to perform query because tracker config is missing. 2 parameters :
	 * <ul>
	 * <li>query summary</li>
	 * <li>tracker ID</li> </lu>
	 */
	public static String queryFailedBecauseMissingTracker;

	/**
	 * Validate the connection.
	 */
	public static String validateConnection;

	/**
	 * Login.
	 */
	public static String login;

	/**
	 * Retrieve the Tuleap server.
	 */
	public static String retrieveTuleapServer;

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
	public static String retrievingTracker;

	/**
	 * Retrieving the reports.
	 */
	public static String retrievingReports;

	/**
	 * Retrieving the content of the attachments.
	 */
	public static String retrievingAttachmentContent;

	/**
	 * Downloading the attachments.
	 */
	public static String retrievingAttachmentContentFor;

	/**
	 * Downloading the file attachment.
	 */
	public static String retrievingFile;

	/**
	 * Deleting the file attachment.
	 */
	public static String deletingFile;

	/**
	 * Creating the file attachment.
	 */
	public static String creatingFile;

	/**
	 * Updating the file attachment.
	 */
	public static String updatingFile;

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
	 * Server error (HTTP Code 5xx).
	 */
	public static String internalServerError;

	/**
	 * No message.
	 */
	public static String noMessage;

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
	 * Retrieve burndown of a specific milestone.
	 */
	public static String retrievingBurndown;

	/**
	 * Retrieve an artifact.
	 */
	public static String retrievingArtifact;

	/**
	 * Create an artifact.
	 */
	public static String creatingArtifact;

	/**
	 * Update an artifact.
	 */
	public static String updatingArtifact;

	/**
	 * Updating a backlogItem.
	 */
	public static String updatingBacklogItem;

	/**
	 * Update backlog of a milestone.
	 */
	public static String updatingBacklog;

	/**
	 * Update content of a milestone.
	 */
	public static String updatingContent;

	/**
	 * Update milestone submilestones.
	 */
	public static String updatingSubmilestones;

	/**
	 * Update backlog of project.
	 */
	public static String updatingProjectBacklog;

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
	 * Retrieving a topPlanning.
	 */
	public static String retrievingTopPlanning;

	/**
	 * Retrieving an artifact. 1 parameter:
	 * <ol>
	 * <li>The artifact id</li>
	 * </ol>
	 */
	public static String retrieveArtifact;

	/**
	 * Error message regarding ISO-8601 dates. 1 argument, the invalid date as a string.
	 */
	public static String invalidDate;

	/**
	 * Display id template, uses 2 parameters:
	 * <ol>
	 * <li>The tracker label</li>
	 * <li>The card's artifact ID</li>
	 * </ol>
	 */
	public static String cardDisplayId;

	/**
	 * Message when the tracker of a card cannot be found.
	 * <ol>
	 * <li>The tracker ID</li>
	 * </ol>
	 */
	public static String cardTrackerNotAvailable;

	/**
	 * Message when then tracker config of a card needs to be refreshed.
	 * <ol>
	 * <li>The tracker ID</li>
	 * </ol>
	 */
	public static String cardTrackerConfigNeedsUpdate;

	/**
	 * Message for errors during a HTTP request, uses 2 parameters:
	 * <ol>
	 * <li>The method</li>
	 * <li>The error message</li>
	 * </ol>
	 */
	public static String ioError;

	/**
	 * Communication error, probably a loss of connection. A parameter, the error code/message.
	 */
	public static String communicationError;

	/**
	 * Label used for a select box entry that represents "unselected".
	 */
	public static String selectBoxNone;

	/**
	 * Message for invalid state when attempting to submit the creation of a milestone, which should never
	 * happen.
	 */
	public static String attemptToSubmitAgileDataOnNewTask;

	/**
	 * Message for invalid state when attempting to submit the creation of a top-planning, which should never
	 * happen.
	 */
	public static String attemptToCreateNewTopPlanning;

	/**
	 * Message indicating that problems have occurred during submission. 1 parameter:
	 * <ol>
	 * <li>The number of problems (integer)</li>
	 * </ol>
	 */
	public static String problemsOccurredDuringSubmit;

	/**
	 * Message indicating that problems have occurred during retrieval of the task. 1 parameter:
	 * <ol>
	 * <li>The number of problems (integer)</li>
	 * </ol>
	 */
	public static String problemsOccurredDuringRetrieve;

	/**
	 * Message indicating that UTF-8 is not supported for REST communication.
	 */
	public static String encodingUtf8NotSupported;

	/**
	 * Message for missing attachment field when trying to upload attachment. 1 parameter, the task key.
	 */
	public static String missingAttachmentField;

	/**
	 * Message for removing temp attachment files, no parameter.
	 */
	public static String removingTempFileOnFailure;

	/**
	 * Impossible to read file content for attachment upload. No parameter.
	 */
	public static String cannotReadFileContent;

	/**
	 * Sending chunk 1/23. 2 parameters, current index, total chunk number.
	 */
	public static String uploadingAttachment;

	/**
	 * Retrieving chunk 1/23. 2 parameters, current index, total chunk number.
	 */
	public static String downloadingAttachment;

	/**
	 * Upload of attachment failed (miserably).
	 */
	public static String uploadAttachmentFailed;

	/**
	 * Message when local repository configuration cannot be deserialized.
	 */
	public static String cannotLoadOldConfiguration;

	/**
	 * An update of the task repository config is required.
	 */
	public static String configUpdateNeeded;

	/**
	 * Query created with old version. 1 parameter : the query summary.
	 */
	public static String oldQueryNotCompatible;

	/**
	 * You're not allowed to upload an attachment. 1 parameter, the task (label or id).
	 */
	public static String notAllowedToUploadAttachment;

	/**
	 * Add a backlogItem to the backlog of a milestone.
	 */
	public static String addBacklogItemToMilestone;

	/**
	 * Invoking addSubmilestone can only be made on a TaskData that can have sub-milestones.
	 */
	public static String notAllowedAddSubmilestones;

	static {
		// load message values from bundle file
		reloadMessages();
	}

	/**
	 * Reloads the messages from the properties file.
	 */
	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, TuleapCoreKeys.class);
	}
}
