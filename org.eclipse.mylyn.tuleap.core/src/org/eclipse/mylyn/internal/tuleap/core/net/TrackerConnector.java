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

import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapPermission;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapInteger;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapText;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapFieldSet;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConfiguration;
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
		// TODO Returns the real repository configuration from the tracker
		TuleapRepositoryConfiguration configuration = new TuleapRepositoryConfiguration(this.trackerLocation
				.getUrl(), "Super Cook", "Super Cook Item Name",
				"This is a restaurant leaded by a super cook");

		// The meal group
		TuleapFieldSet mealElement = new TuleapFieldSet("Meal", "Meal", "The details of the meal", true,
				TuleapPermission.USER_GROUP_ANONYMOUS, "__cook_meal");
		TuleapString mealName = new TuleapString("Name", "Name", "The name of the meal", true,
				TuleapPermission.USER_GROUP_ANONYMOUS, "__cook_meal_name");
		mealElement.getFormElements().add(mealName);

		TuleapInteger mealPrice = new TuleapInteger("Price", "Price", "The price of the meal", true,
				TuleapPermission.USER_GROUP_PROJECT_MEMBERS, "__cook_meal_price");
		mealElement.getFormElements().add(mealPrice);

		TuleapSelectBox mealAwesomeness = new TuleapSelectBox("Awesomeness", "Awesomness",
				"The awesomness of the meal", true, TuleapPermission.USER_GROUP_ANONYMOUS,
				"__cook_meal_awesomness");
		mealAwesomeness.getItems().add("MAGNIFICENT");
		mealAwesomeness.getItems().add("Amazing");
		mealAwesomeness.getItems().add("Why not");
		mealAwesomeness.getItems().add("Not that good");
		mealAwesomeness.getItems().add("HORRIBLE");
		mealElement.getFormElements().add(mealAwesomeness);

		configuration.getFormElements().add(mealElement);

		// The persons group
		TuleapFieldSet personElement = new TuleapFieldSet("Persons Management", "Persons Management",
				"Management of the persons eating the meal", true,
				TuleapPermission.USER_GROUP_PROJECT_MEMBERS, "__persons");

		TuleapMultiSelectBox persons = new TuleapMultiSelectBox("Persons", "Persons",
				"The persons eating the meal", true, TuleapPermission.USER_GROUP_PROJECT_MEMBERS,
				"__persons_persons");
		persons.getItems().add("Stephane Begaudeau");
		persons.getItems().add("Laurent Goubet");
		persons.getItems().add("Cedric Notot");
		persons.getItems().add("Nathalie Lepine");
		persons.getItems().add("Alex Morel");
		persons.getItems().add("Cedric Brun");
		persons.getItems().add("Etienne Juliot");
		persons.getItems().add("Stephane Lacrampe");
		personElement.getFormElements().add(persons);

		TuleapDate beginDate = new TuleapDate("Begin Date", "Begin Date",
				"The date of the beginning of the meal", true, TuleapPermission.USER_GROUP_PROJECT_MEMBERS,
				"__persons_begin_date");
		personElement.getFormElements().add(beginDate);

		TuleapDate endDate = new TuleapDate("End Date", "End Date", "The date of the beginning of the meal",
				true, TuleapPermission.USER_GROUP_PROJECT_MEMBERS, "__persons_begin_date");
		personElement.getFormElements().add(endDate);

		configuration.getFormElements().add(personElement);

		// The additional data group
		TuleapFieldSet additionalElement = new TuleapFieldSet("Additional Information",
				"Additional Information", "Additional information for the meal", false,
				TuleapPermission.USER_GROUP_ADMIN, "__additional");

		TuleapText additionalText = new TuleapText("Description", "Description",
				"The description of the additional information", false, TuleapPermission.USER_GROUP_ADMIN,
				"__additional_description");
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
