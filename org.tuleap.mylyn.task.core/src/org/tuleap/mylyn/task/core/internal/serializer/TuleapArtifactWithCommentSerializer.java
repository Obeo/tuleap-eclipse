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
package org.tuleap.mylyn.task.core.internal.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a {@link TuleapArtifactWithComment}.
 *
 * @param <U>
 *            The type of the object to serialize
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapArtifactWithCommentSerializer<U extends TuleapArtifactWithComment> extends AbstractTuleapSerializer<U> {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(U commentedArtifact, Type type, JsonSerializationContext context) {
		JsonObject elementObject = (JsonObject)super.serialize(commentedArtifact, type, context);
		elementObject.remove(ITuleapConstants.ID);
		String comment = commentedArtifact.getNewComment();
		if (comment != null && !comment.isEmpty()) {
			JsonObject commentObject = new JsonObject();
			elementObject.add(ITuleapConstants.COMMENT, commentObject);
			commentObject.add(ITuleapConstants.BODY, new JsonPrimitive(comment));
			commentObject.add(ITuleapConstants.FORMAT, new JsonPrimitive("text")); //$NON-NLS-1$
		}
		return elementObject;
	}
}
