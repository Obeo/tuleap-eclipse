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
package org.eclipse.mylyn.tuleap.core.internal.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapArtifactLink;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapOpenList;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractTuleapConfigurableElement;
import org.eclipse.mylyn.tuleap.core.internal.model.data.ArtifactLinkFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a AbstractTuleapConfigurableElement.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @param <T>
 */
public abstract class AbstractTuleapSerializer<T extends AbstractTuleapConfigurableElement<?>> extends AbstractProjectElementSerializer<T> {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(T element, Type type, JsonSerializationContext context) {
		JsonObject json = super.serialize(element, type, context).getAsJsonObject();
		JsonArray values = new JsonArray();
		json.add(ITuleapConstants.VALUES, values);
		for (AbstractTuleapField field : element.getFields()) {
			if (mustSerialize(field, element.isNew())) {
				AbstractFieldValue fieldValue = element.getFieldValue(field.getIdentifier());
				if (fieldValue == null) {
					if (field instanceof TuleapFileUpload) {
						fieldValue = new AttachmentFieldValue(field.getIdentifier(), null);
					} else if (field instanceof TuleapArtifactLink) {
						fieldValue = new ArtifactLinkFieldValue(field.getIdentifier(), null);
					} else if (field instanceof TuleapOpenList || field instanceof TuleapSelectBox
							|| field instanceof TuleapMultiSelectBox) {
						fieldValue = new BoundFieldValue(field.getIdentifier(), null);
					} else {
						fieldValue = new LiteralFieldValue(field.getIdentifier(), null);
					}
				}
				values.add(context.serialize(fieldValue));
			}
		}
		return json;
	}

	/**
	 * Override this method in subclasses to configure which field are serialized. By default, all fields with
	 * authorization matching the operation (creation or update) are serialized.
	 *
	 * @param field
	 *            A field present in the object to serialize.
	 * @param isNew
	 *            Flag indicating whether serialization is for creation or update
	 * @return <code>true</code> if and only if the field must be included in the serialization.
	 */
	protected boolean mustSerialize(AbstractTuleapField field, boolean isNew) {
		if (field == null) {
			return false;
		}
		boolean result = false;
		// By default, never send file upload field info, to prevent losing attachments
		if (!(field instanceof TuleapFileUpload)) {
			if (isNew) {
				result = field.isSubmitable();
			} else {
				result = field.isUpdatable();
			}
		}
		return result;
	}
}
