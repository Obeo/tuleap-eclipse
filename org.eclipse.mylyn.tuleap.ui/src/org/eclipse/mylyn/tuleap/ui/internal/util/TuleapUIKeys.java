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
package org.eclipse.mylyn.tuleap.ui.internal.util;

import org.eclipse.osgi.util.NLS;

/**
 * The list of all the possible keys to use for internationalization.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapUIKeys extends NLS {
	/** Qualified path to the properties file in which to seek the keys. */
	private static final String BUNDLE_NAME = "org.eclipse.mylyn.tuleap.ui.internal.util.tuleap_ui_messages"; //$NON-NLS-1$

	// CHECKSTYLE:OFF (Javadoc is __still__ required!)

	/**
	 * Element not found.
	 */
	public static String elementNotFound;

	/**
	 * Log a null status.
	 */
	public static String logNullStatus;

	/**
	 * Log a null exception.
	 */
	public static String logNullException;

	/**
	 * An unexpected problem has occurred while logging an issue.
	 */
	public static String unexpectedException;

	/**
	 * The repository connector is invalid.
	 */
	public static String invalidRepositoryConnector;

	/**
	 * The credentials are missing.
	 */
	public static String missingCredentials;

	/**
	 * The message displayed while the validation of the connection is in progress.
	 */
	public static String validateConnection;

	/**
	 * The name of the repository setting page.
	 */
	public static String repositorySettingsPageName;

	/**
	 * The description of the repository setting page.
	 */
	public static String repositorySettingsPageDescription;

	/**
	 * The label of the task kind.
	 */
	public static String taskKindLabel;

	/**
	 * The name of the page used to edit the properties of a Tuleap artifact.
	 */
	public static String tuleapTabName;

	/**
	 * The name of the report page.
	 */
	public static String tuleapReportPageName;

	/**
	 * The title of the report page.
	 */
	public static String tuleapReportPageTitle;

	/**
	 * The description of the report page.
	 */
	public static String tuleapReportPageDescription;

	/**
	 * The name of the custom query page.
	 */
	public static String tuleapCustomQueryPageName;

	/**
	 * The title of the custom query page.
	 */
	public static String tuleapCustomQueryPageTitle;

	/**
	 * The description of the custom query page.
	 */
	public static String tuleapCustomQueryPageDescription;

	/**
	 * The type of the field used is not supported. {0} The type of the field.
	 */
	public static String unsupportedTrackerFieldType;

	/**
	 * Invalid value for a query criterion. Uses 2 parameters:
	 * <ul>
	 * <li>The value</li>
	 * <li>The expected type</li>
	 * </ul>
	 */
	public static String tuleapQueryInvalidValue;

	/**
	 * Message for invalid date input for "between" operator.
	 */
	public static String tuleapQueryDatesMandatoryForBetween;

	/**
	 * Message on top of wizard page for custome queries.
	 */
	public static String tuleapQueryInvalidCriteria;

	/**
	 * Message for invalid input in integer query criterion field.
	 */
	public static String tuleapQueryIntegerFieldDecoratorText;

	/**
	 * Message for invalid input in floating-point query criterion field.
	 */
	public static String tuleapQueryDoubleFieldDecoratorText;

	/**
	 * Title of the new Tuleap wizard window.
	 */
	public static String newTuleapWizardWindowTitle;

	/**
	 * The name of the tracker page.
	 */
	public static String tuleapTrackerPageName;

	/**
	 * The title of the tracker page.
	 */
	public static String tuleapTrackerPageTitle;

	/**
	 * The description of the tracker page.
	 */
	public static String tuleapTrackerPageDescription;

	/**
	 * Error message used to indicate that a tracker is required.
	 */
	public static String tuleapTrackerPageSelectATracker;

	/**
	 * Description of the tracker selected. {0} The description of the tracker.
	 */
	public static String tuleapTrackerPageDescriptionLabel;

	/**
	 * The name of the Tuleap project page.
	 */
	public static String tuleapProjectPageName;

	/**
	 * The title of the Tuleap project page.
	 */
	public static String tuleapProjectPageTitle;

	/**
	 * The description of the Tuleap project page.
	 */
	public static String tuleapProjectPageDescription;

	/**
	 * Error message used to indicate that a project is required.
	 */
	public static String tuleapProjectPageSelectAProject;

	/**
	 * Label of the update project list button.
	 */
	public static String tuleapProjectPageUpdateProjectsList;

	/**
	 * The label of the combo used to select the field to which the attachment will be added.
	 */
	public static String tuleapTaskAttachmentPageSelectionLabel;

	/**
	 * The label used to select a report.
	 */
	public static String tuleapQueryProjectPageReportButtonLabel;

	/**
	 * The label used to select a custom query.
	 */
	public static String tuleapQueryProjectPageCustomQueryButtonLabel;

	/**
	 * The label used to select a query of top level plannings.
	 */
	public static String tuleapQueryProjectPageTopLevelPlanningButtonLabel;

	/**
	 * Label used for missing agile connector when attempting to create a TaskDataModel.
	 */
	public static String agileConnectorRequired;

	/**
	 * The title of the Tuleap preferences page.
	 */
	public static String tuleapPreferencesPageTitle;

	/**
	 * Activate the debug mode of the connector.
	 */
	public static String activateDebugModeLabel;

	static {
		// load message values from bundle file
		reloadMessages();
	}

	/**
	 * Reloads the messages from the properties file.
	 */
	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, TuleapUIKeys.class);
	}
}
