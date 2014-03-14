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
package org.eclispe.mylyn.tuleap.core.tests.internal.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;

import static org.junit.Assert.assertEquals;

/**
 * Abstract super-class of deserializer tests.
 * 
 * @param <T>
 *            The type of element to deserialize.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractDeserializerTest<T> {

	/**
	 * Parse the content of the file and return the matching configuration.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @param clazz
	 *            The class of the expected object
	 * @return The Tuleap BacklogItem Type matching the content of the file
	 */
	protected T parse(String fileContent, Class<T> clazz) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(clazz, getDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		T tuleapBacklogItemType = gson.fromJson(jsonObject, clazz);

		return tuleapBacklogItemType;
	}

	/**
	 * Provides the deserializer to use.
	 * 
	 * @return Teh deserializer to use.
	 */
	protected abstract JsonDeserializer<T> getDeserializer();

	/**
	 * Utility method to test field's basic information (id, name, label and permissions).
	 * 
	 * @param field
	 *            the field
	 * @param fieldName
	 *            the field name
	 * @param fieldLabel
	 *            the field label
	 * @param fieldId
	 *            the field id
	 * @param read
	 *            the read permission
	 * @param create
	 *            the create permission
	 * @param update
	 *            the update permission
	 */
	protected void checkBasicField(AbstractTuleapField field, String fieldName, String fieldLabel,
			int fieldId, boolean read, boolean create, boolean update) {
		assertEquals(fieldId, field.getIdentifier());
		assertEquals(fieldName, field.getName());
		assertEquals(fieldLabel, field.getLabel());
		assertEquals(field.isReadable(), read);
		assertEquals(field.isSubmitable(), create);
		assertEquals(field.isUpdatable(), update);

	}

}
