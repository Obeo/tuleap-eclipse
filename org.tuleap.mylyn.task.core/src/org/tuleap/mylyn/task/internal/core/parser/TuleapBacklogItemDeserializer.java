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
package org.tuleap.mylyn.task.internal.core.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;

/**
 * This class is used to deserialize the JSON representation of a project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapBacklogItemDeserializer implements JsonDeserializer<TuleapBacklogItem> {

	/**
	 * The key used for the name of the project.
	 */
	private static final String LABEL = "label"; //$NON-NLS-1$

	/**
	 * The key used for the identifier of the project.
	 */
	private static final String ID = "id"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String START_DATE = "start_date"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String DURATION = "duration"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String CAPACITY = "capacity"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String URL = "url"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String HTML_URL = "html_url"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String MILESTONE_TYPE_ID = "milestone_type_id"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String VALUES = "values"; //$NON-NLS-1$

	/**
	 * The key used for the name of the project.
	 */
	private static final String SUBMILESTONES = "submilestones"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapBacklogItem deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		TuleapBacklogItem milestone = new TuleapBacklogItem();

		return milestone;
	}

}
