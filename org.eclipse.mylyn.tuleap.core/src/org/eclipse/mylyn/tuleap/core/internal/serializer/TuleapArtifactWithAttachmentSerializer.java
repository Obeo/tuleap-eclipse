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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithAttachment;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a {@link TuleapArtifactWithAttachment}.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactWithAttachmentSerializer extends TuleapArtifactWithCommentSerializer<TuleapArtifactWithAttachment> {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapArtifactWithAttachment tuleapArtifact, Type type,
			JsonSerializationContext context) {
		JsonObject elementObject = (JsonObject)super.serialize(tuleapArtifact, type, context);
		elementObject.remove(ITuleapConstants.ID);
		if (tuleapArtifact.getTracker() != null) {
			JsonObject trackerObject = new JsonObject();
			elementObject.add(ITuleapConstants.TRACKER, trackerObject);
			trackerObject.add(ITuleapConstants.ID, new JsonPrimitive(Integer.valueOf(tuleapArtifact
					.getTracker().getId())));
		}
		return elementObject;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.serializer.AbstractTuleapSerializer#mustSerialize(org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField,
	 *      boolean)
	 */
	@Override
	protected boolean mustSerialize(AbstractTuleapField field, boolean isNew) {
		if (field instanceof TuleapFileUpload) {
			// For attachments, the artifact is never new
			// We accept to submit if the field is either updatable or creatable (semantic unclear)
			return field.isSubmitable() || field.isUpdatable();
		}
		return super.mustSerialize(field, isNew);
	}
}
