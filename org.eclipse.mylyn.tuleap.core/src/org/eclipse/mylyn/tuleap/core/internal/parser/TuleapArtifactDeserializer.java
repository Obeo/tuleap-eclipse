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
package org.eclipse.mylyn.tuleap.core.internal.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;

/**
 * This class is used to deserialize the JSON representation of a tuleap artifact.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapArtifactDeserializer extends AbstractTuleapDeserializer<Integer, TuleapArtifact> {

	/**
	 * {@inheritDoc}.
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public TuleapArtifact deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return super.deserialize(element, type, context);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.parser.AbstractProjectElementDeserializer#getPrototype(String)
	 */
	@Override
	protected TuleapArtifact getPrototype(String jsonId) {
		TuleapArtifact result = new TuleapArtifact();
		result.setId(Integer.valueOf(jsonId));
		return result;
	}

}
