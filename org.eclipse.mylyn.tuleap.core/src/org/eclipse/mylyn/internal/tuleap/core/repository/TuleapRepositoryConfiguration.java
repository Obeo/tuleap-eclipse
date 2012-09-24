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
package org.eclipse.mylyn.internal.tuleap.core.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapFormElement;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapCannedResponse;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapColumn;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapFieldSet;

/**
 * The repository configuration will hold the latest known configuration of a given repository. This
 * configuration can dramatically evolve over time and it should be refreshed automatically or manually.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapRepositoryConfiguration implements Serializable {
	/**
	 * The serialization version ID.
	 */
	private static final long serialVersionUID = 1132263181253677627L;

	/**
	 * The last time this configuration was updated.
	 */
	private long lastUpdate;

	/**
	 * The url of the repository configuration.
	 */
	private String url;

	/**
	 * The name of the Tuleap tracker.
	 */
	private String name;

	/**
	 * The item name.
	 */
	private String itemName;

	/**
	 * The description of the Tuleap tracker.
	 */
	private String description;

	/**
	 * The list of canned responses.
	 */
	private List<TuleapCannedResponse> cannedResponses = new ArrayList<TuleapCannedResponse>();

	/**
	 * The structural elements of the Tuleap tracker.
	 */
	private List<AbstractTuleapFormElement> formElements = new ArrayList<AbstractTuleapFormElement>();

	/**
	 * The default constructor.
	 * 
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public TuleapRepositoryConfiguration(String repositoryURL) {
		this.url = repositoryURL;
	}

	/**
	 * The constructor.
	 * 
	 * @param repositoryURL
	 *            The URL of the repository.
	 * @param repositoryName
	 *            The name of the repository
	 * @param repositoryItemName
	 *            The item name of the repository
	 * @param repositoryDescription
	 *            The description of the repository
	 */
	public TuleapRepositoryConfiguration(String repositoryURL, String repositoryName,
			String repositoryItemName, String repositoryDescription) {
		this.url = repositoryURL;
		this.name = repositoryName;
		this.itemName = repositoryItemName;
	}

	/**
	 * Set the time at which the configuration was last updated.
	 * 
	 * @param time
	 *            The time at which the configuration was last updated.
	 */
	public void setLastUpdate(long time) {
		this.lastUpdate = time;
	}

	/**
	 * Returns the time at which the configuration was last updated.
	 * 
	 * @return The time at which the configuration was last updated.
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * Returns the url of the repository.
	 * 
	 * @return The url of the repository.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Returns the name of the repository.
	 * 
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the item name of the repository.
	 * 
	 * @return The item name of the repository.
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * Returns the description of the repository.
	 * 
	 * @return The description of the repository.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the form elements of the repository.
	 * 
	 * @return The form elements of the repository.
	 */
	public List<AbstractTuleapFormElement> getFormElements() {
		return this.formElements;
	}

	/**
	 * Returns the list of the canned responses.
	 * 
	 * @return The list of the canned respones.
	 */
	public List<TuleapCannedResponse> getCannedResponses() {
		return this.cannedResponses;
	}

	/**
	 * Returns the list of fields recursively contained in a given form element.
	 * 
	 * @param formElement
	 *            A structural form element
	 * @return The list of fields recursively contained in a given form element.
	 */
	public static List<AbstractTuleapField> getFields(AbstractTuleapFormElement formElement) {
		List<AbstractTuleapField> fields = new ArrayList<AbstractTuleapField>();
		if (formElement instanceof TuleapFieldSet) {
			TuleapFieldSet tuleapFieldSet = (TuleapFieldSet)formElement;
			List<AbstractTuleapFormElement> formElements = tuleapFieldSet.getFormElements();
			for (AbstractTuleapFormElement abstractTuleapFormElement : formElements) {
				if (abstractTuleapFormElement instanceof TuleapFieldSet) {
					TuleapFieldSet fieldSet = (TuleapFieldSet)abstractTuleapFormElement;
					fields.addAll(TuleapRepositoryConfiguration.getFields(fieldSet));
				} else if (abstractTuleapFormElement instanceof TuleapColumn) {
					TuleapColumn column = (TuleapColumn)abstractTuleapFormElement;
					fields.addAll(TuleapRepositoryConfiguration.getFields(column));
				} else if (abstractTuleapFormElement instanceof AbstractTuleapField) {
					AbstractTuleapField field = (AbstractTuleapField)abstractTuleapFormElement;
					fields.add(field);

				}
			}
		} else if (formElement instanceof TuleapColumn) {
			TuleapColumn tuleapColumn = (TuleapColumn)formElement;
			fields.addAll(tuleapColumn.getFormElements());
		}
		return fields;
	}
}
