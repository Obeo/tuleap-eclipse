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
package org.tuleap.mylyn.task.internal.core.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a {@link TuleapArtifactWithComment}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactWithCommentSerializer extends AbstractTuleapSerializer<TuleapArtifactWithComment> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapArtifactWithComment commentedArtifact, Type type,
			JsonSerializationContext context) {
		JsonObject elementObject = (JsonObject)super.serialize(commentedArtifact, TuleapArtifactWithComment.class,
				context);

		elementObject.remove(ITuleapConstants.ID);
		if (commentedArtifact.getNewComment() != null) {
			JsonObject commentObject = new JsonObject();
			elementObject.add(ITuleapConstants.COMMENT, commentObject);
			commentObject.add(ITuleapConstants.BODY, new JsonPrimitive(commentedArtifact.getNewComment()));
			commentObject.add(ITuleapConstants.FORMAT, new JsonPrimitive("text")); //$NON-NLS-1$

		}
		return elementObject;
	}
}
