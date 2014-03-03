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

import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a {@link TuleapArtifact}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactSerializer extends AbstractTuleapSerializer<TuleapArtifact> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapArtifact tuleapArtifact, Type type, JsonSerializationContext context) {
		JsonObject elementObject = (JsonObject)super.serialize(tuleapArtifact, TuleapArtifact.class, context);

		elementObject.remove(ITuleapConstants.ID);
		if (tuleapArtifact.getTracker() != null) {
			JsonObject trackerObject = new JsonObject();
			elementObject.add(ITuleapConstants.TRACKER, trackerObject);
			trackerObject.add(ITuleapConstants.ID, new JsonPrimitive(Integer.valueOf(tuleapArtifact
					.getTracker().getId())));
			trackerObject.add(ITuleapConstants.URI, new JsonPrimitive(tuleapArtifact.getTracker().getUri()));

		}
		return elementObject;
	}
}
