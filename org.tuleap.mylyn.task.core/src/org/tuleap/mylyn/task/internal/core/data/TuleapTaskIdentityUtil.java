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
package org.tuleap.mylyn.task.internal.core.data;

import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;

/**
 * Utility class containing various simple static utility methods.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public final class TuleapTaskIdentityUtil {

	/**
	 * The identifier to use for irrelevant ids. This will be used for the configuration id of Tuleap top
	 * plannings
	 */
	public static final int IRRELEVANT_ID = -1;

	/**
	 * The value used to indicate that the identifier is irrelevant.
	 */
	private static final String IRRELEVANT_ID_VALUE = "N/A"; //$NON-NLS-1$

	/**
	 * The separator between the project name or id and the configuration name of id.
	 */
	private static final String CONFIGURATION_SEPARATOR = ":"; //$NON-NLS-1$

	/**
	 * The separator used in the key of the task data.
	 */
	private static final String ELEMENT_NAME_SEPARATOR = "-"; //$NON-NLS-1$

	/**
	 * The separator used in the id of the task data.
	 */
	private static final String ELEMENT_ID_SEPARATOR = "#"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	private TuleapTaskIdentityUtil() {
		// prevent instantiation
	}

	/**
	 * Returns the task data key from the project name, the configuration name (tracker name) and the given
	 * artifact id.
	 * 
	 * @param projectName
	 *            The name of the project
	 * @param configurationName
	 *            The name of the configuration.
	 * @param artifactId
	 *            The id of the artifact
	 * @return The task data id
	 */
	public static String getTaskDataKey(String projectName, String configurationName, int artifactId) {
		return projectName + CONFIGURATION_SEPARATOR + configurationName + ELEMENT_NAME_SEPARATOR
				+ Integer.valueOf(artifactId).toString();
	}

	/**
	 * Returns the task data id.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 * @param configurationId
	 *            The identifier of the configuration
	 * @param elementId
	 *            The identifier of the element
	 * @return The task data id
	 */
	public static String getTaskDataId(int projectId, int configurationId, int elementId) {
		String projectIdentifier = IRRELEVANT_ID_VALUE;
		if (IRRELEVANT_ID != projectId) {
			projectIdentifier = String.valueOf(projectId);
		}

		String configurationIdentifier = IRRELEVANT_ID_VALUE;
		if (IRRELEVANT_ID != configurationId) {
			configurationIdentifier = String.valueOf(configurationId);
		}

		String elementIdentifier = IRRELEVANT_ID_VALUE;
		if (IRRELEVANT_ID != elementId) {
			elementIdentifier = String.valueOf(elementId);
		}

		return projectIdentifier + CONFIGURATION_SEPARATOR + configurationIdentifier + ELEMENT_ID_SEPARATOR
				+ elementIdentifier;
	}

	/**
	 * Returns the identifier of the Tuleap project from the task data id.
	 * 
	 * @param taskDataId
	 *            The task data id
	 * @return The identifier of the Tuleap project from the task data id
	 */
	public static int getProjectIdFromTaskDataId(String taskDataId) {
		int indexOf = taskDataId.indexOf(CONFIGURATION_SEPARATOR);
		if (indexOf != -1) {
			String projectId = taskDataId.substring(0, indexOf);

			if (!IRRELEVANT_ID_VALUE.equals(projectId)) {
				int identifier = IRRELEVANT_ID;

				try {
					identifier = Integer.parseInt(projectId);
				} catch (NumberFormatException e) {
					TuleapCoreActivator.log(e, true);
				}

				return identifier;
			}
		}
		return IRRELEVANT_ID;
	}

	/**
	 * Returns the identifier of the configuration from the task data id.
	 * 
	 * @param taskDataId
	 *            The task data id
	 * @return The identifier of the configuration from the task data id
	 */
	public static int getConfigurationIdFromTaskDataId(String taskDataId) {
		int indexOfConfiguration = taskDataId.indexOf(CONFIGURATION_SEPARATOR);
		int indexOfElement = taskDataId.indexOf(ELEMENT_ID_SEPARATOR);
		if (indexOfConfiguration != -1 && indexOfElement != -1) {
			String configurationId = taskDataId.substring(indexOfConfiguration
					+ CONFIGURATION_SEPARATOR.length(), indexOfElement);

			if (!IRRELEVANT_ID_VALUE.equals(configurationId)) {
				int identifier = IRRELEVANT_ID;

				try {
					identifier = Integer.parseInt(configurationId);
				} catch (NumberFormatException e) {
					TuleapCoreActivator.log(e, true);
				}

				return identifier;
			}
		}
		return IRRELEVANT_ID;
	}

	/**
	 * Returns the identifier of the element from the task data id.
	 * 
	 * @param taskDataId
	 *            The task data id
	 * @return The identifier of the element from the task data id
	 */
	public static int getElementIdFromTaskDataId(String taskDataId) {
		int indexOf = taskDataId.indexOf(ELEMENT_ID_SEPARATOR);
		if (indexOf != -1) {
			String elementId = taskDataId.substring(indexOf + ELEMENT_ID_SEPARATOR.length());

			if (!IRRELEVANT_ID_VALUE.equals(elementId)) {
				int identifier = IRRELEVANT_ID;

				try {
					identifier = Integer.parseInt(elementId);
				} catch (NumberFormatException e) {
					TuleapCoreActivator.log(e, true);
				}

				return identifier;
			}
		}
		return IRRELEVANT_ID;
	}
}
