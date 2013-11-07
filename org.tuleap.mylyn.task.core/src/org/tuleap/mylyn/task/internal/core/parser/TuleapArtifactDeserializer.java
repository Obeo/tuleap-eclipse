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
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;

/**
 * This class is used to deserialize the JSON representation of a cardwall card.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapArtifactDeserializer extends AbstractTuleapDeserializer<TuleapArtifact> {

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
	 * @see org.tuleap.mylyn.task.internal.core.parser.AbstractProjectElementDeserializer#getPrototype()
	 */
	@Override
	protected TuleapArtifact getPrototype() {
		return new TuleapArtifact();
	}

}
