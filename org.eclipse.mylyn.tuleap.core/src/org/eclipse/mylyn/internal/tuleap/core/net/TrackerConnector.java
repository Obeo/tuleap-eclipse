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
package org.eclipse.mylyn.internal.tuleap.core.net;

import java.io.File;
import java.io.IOException;

import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapInteger;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapText;
import org.eclipse.mylyn.internal.tuleap.core.model.permission.ITuleapDefaultPermissionGroups;
import org.eclipse.mylyn.internal.tuleap.core.model.permission.TuleapAccessPermission;
import org.eclipse.mylyn.internal.tuleap.core.model.permission.TuleapPermissions;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapFieldSet;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * This utility class will encapsulate all calls to the Tuleap tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TrackerConnector {
	/**
	 * The name of the temporary file used to store the configuration.
	 */
	private static final String TEMP_FILE_NAME = "temp"; //$NON-NLS-1$

	/**
	 * The suffix of the temporary file used to store the configuration.
	 */
	private static final String TEMP_FILE_SUFFIX = "tuleap_suffix"; //$NON-NLS-1$

	/**
	 * The location of the tracker.
	 */
	private AbstractWebLocation trackerLocation;

	/**
	 * The constructor.
	 * 
	 * @param location
	 *            The location of the tracker.
	 */
	public TrackerConnector(AbstractWebLocation location) {
		this.trackerLocation = location;
	}

	/**
	 * Returns the repository configuration of the tracker.
	 * 
	 * @return The repository configuration of the tracker.
	 */
	public TuleapRepositoryConfiguration getTuleapRepositoryConfiguration() {
		TuleapRepositoryConfiguration configuration = reloadRepositoryConfiguration();
		return configuration;
	}

	/**
	 * Reload the repository configuration from the server.
	 * 
	 * @return Repository configuration
	 */
	private TuleapRepositoryConfiguration reloadRepositoryConfiguration() {
		TuleapRepositoryConfiguration tuleapRepositoryConfiguration = null;
		// Download the repository configuration file from the tracker
		File tempConfigurationFile;
		try {
			tempConfigurationFile = File.createTempFile(TEMP_FILE_NAME, TEMP_FILE_SUFFIX);
			TuleapUtil.download(this.trackerLocation.getUrl(), tempConfigurationFile);

			// Parse repository configuration
			tuleapRepositoryConfiguration = parseRepositoryConfiguration(tempConfigurationFile);
		} catch (IOException e) {
			TuleapCoreActivator.log(TuleapMylynTasksMessages
					.getString("TrackerConnector.ProblemWithTempFileCreation"), true); //$NON-NLS-1$
		}

		return tuleapRepositoryConfiguration;
	}

	/**
	 * Parse the repository configuration.
	 * 
	 * @param tempConfigurationFile
	 *            Configuration file
	 * @return Configuration file transformed to Mylyn repository configuration
	 */
	private TuleapRepositoryConfiguration parseRepositoryConfiguration(File tempConfigurationFile) {
		// TODO Returns the real repository configuration from the tracker
		TuleapRepositoryConfiguration configuration = new TuleapRepositoryConfiguration(this.trackerLocation
				.getUrl(), "Super Cook", "Super Cook Item Name",
				"This is a restaurant leaded by a super cook");

		// The meal group
		TuleapFieldSet mealElement = new TuleapFieldSet("Meal", "Meal", "__cook_meal");
		mealElement.setDescription("The details of the meal");
		mealElement.setRequired(true);
		TuleapPermissions permissions = new TuleapPermissions();
		permissions.put(ITuleapDefaultPermissionGroups.ALL_USERS, TuleapAccessPermission.UPDATE, true);
		mealElement.setPermissions(permissions);

		TuleapString mealName = new TuleapString("Name", "Name", "__cook_meal_name");
		mealName.setDescription("The name of the meal");
		mealName.setRequired(true);
		mealName.setPermissions(permissions);
		mealElement.getFormElements().add(mealName);

		TuleapInteger mealPrice = new TuleapInteger("Price", "Price", "__cook_meal_price");
		mealPrice.setDescription("The price of the meal");
		mealPrice.setRequired(true);
		mealPrice.setPermissions(permissions);
		mealElement.getFormElements().add(mealPrice);

		TuleapSelectBox mealAwesomeness = new TuleapSelectBox("Awesomeness", "Awesomness",
				"__cook_meal_awesomness");
		mealAwesomeness.setDescription("The awesomness of the meal");
		mealAwesomeness.setPermissions(permissions);
		mealAwesomeness.getItems().add("MAGNIFICENT");
		mealAwesomeness.getItems().add("Amazing");
		mealAwesomeness.getItems().add("Why not");
		mealAwesomeness.getItems().add("Not that good");
		mealAwesomeness.getItems().add("HORRIBLE");
		mealElement.getFormElements().add(mealAwesomeness);

		configuration.getFormElements().add(mealElement);

		// The persons group
		TuleapFieldSet personElement = new TuleapFieldSet("Persons Management", "Persons Management",
				"__persons");
		personElement.setDescription("Management of the persons eating the meal");

		TuleapMultiSelectBox persons = new TuleapMultiSelectBox("Persons", "Persons", "__persons_persons");
		persons.setDescription("The persons eating the meal");
		persons.setPermissions(permissions);

		persons.getItems().add("Stephane Begaudeau");
		persons.getItems().add("Laurent Goubet");
		persons.getItems().add("Cedric Notot");
		persons.getItems().add("Nathalie Lepine");
		persons.getItems().add("Alex Morel");
		persons.getItems().add("Cedric Brun");
		persons.getItems().add("Etienne Juliot");
		persons.getItems().add("Stephane Lacrampe");
		personElement.getFormElements().add(persons);

		TuleapDate beginDate = new TuleapDate("Begin Date", "Begin Date", "__persons_begin_date");
		beginDate.setDescription("The date of the beginning of the meal");
		beginDate.setPermissions(permissions);
		personElement.getFormElements().add(beginDate);

		TuleapDate endDate = new TuleapDate("End Date", "End Date", "__persons_begin_date");
		endDate.setDescription("The date of the beginning of the meal");
		endDate.setPermissions(permissions);
		personElement.getFormElements().add(endDate);

		configuration.getFormElements().add(personElement);

		// The additional data group
		TuleapFieldSet additionalElement = new TuleapFieldSet("Additional Information",
				"Additional Information", "__additional");
		additionalElement.setDescription("Additional information for the meal");
		additionalElement.setPermissions(permissions);

		TuleapText additionalText = new TuleapText("Description", "Description", "__additional_description");
		additionalText.setDescription("The description of the additional information");
		additionalText.setPermissions(permissions);
		additionalElement.getFormElements().add(additionalText);

		configuration.getFormElements().add(additionalElement);

		return configuration;
	}

	/**
	 * Performs the given query on the Tuleap tracker, collects the task data resulting from the evaluation of
	 * the query and return the number of tasks found.
	 * 
	 * @param collector
	 *            The task data collector
	 * @param mapper
	 *            The task attribute mapper
	 * @param maxHits
	 *            The maximum number of tasks that should be processed
	 * @return The number of tasks processed
	 */
	public int performQuery(TaskDataCollector collector, TaskAttributeMapper mapper, int maxHits) {
		// TODO Evaluate the tasks on the server

		return 0;
	}
}
