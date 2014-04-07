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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapWorkflowTransition;
import org.eclipse.mylyn.tuleap.core.internal.model.data.ArtifactLinkFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithAttachment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.serializer.ArtifactLinkFieldValueAdapter;
import org.eclipse.mylyn.tuleap.core.internal.serializer.BoundFieldValueSerializer;
import org.eclipse.mylyn.tuleap.core.internal.serializer.LiteralFieldValueSerializer;
import org.eclipse.mylyn.tuleap.core.internal.serializer.TuleapArtifactSerializer;
import org.eclipse.mylyn.tuleap.core.internal.serializer.TuleapArtifactWithAttachmentSerializer;
import org.eclipse.mylyn.tuleap.core.internal.serializer.TuleapArtifactWithCommentSerializer;

/**
 * Utility class to configure the Gson instance(s) used by the connector for Tuleap.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class TuleapGsonProvider {

	/**
	 * Private constructor, to prevent instantiation.
	 */
	private TuleapGsonProvider() {
		// Prevent instantiation of utility class
	}

	/**
	 * Provides the default Gson properly configured to parse JSON structures from Tuleap and serialize them
	 * too.
	 *
	 * @return A properly configured new instance of Gson.
	 */
	public static Gson defaultGson() {
		return defaultBuilder().disableHtmlEscaping().create();
	}

	/**
	 * Provides a Gson that is like the default but does perform html escaping.
	 *
	 * @return A properly configured Gson instance that performs HTML escaping.
	 */
	public static Gson htmlEscapingGson() {
		return defaultBuilder().create();
	}

	/**
	 * Provides the default {@link GsonBuilder}.
	 *
	 * @return A new instance of GsonBuilder, properly configured for Tuleap.
	 */
	public static GsonBuilder defaultBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapTracker.class, new TuleapTrackerDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapElementComment.class, new TuleapChangesetDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapWorkflowTransition.class,
				new TuleapWorkflowTransitionDeserializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateIso8601Adapter());
		gsonBuilder.registerTypeAdapter(TuleapArtifact.class, new TuleapArtifactDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapArtifact.class, new TuleapArtifactSerializer());
		gsonBuilder.registerTypeAdapter(TuleapArtifactWithComment.class,
				new TuleapArtifactWithCommentSerializer<TuleapArtifactWithComment>());
		gsonBuilder.registerTypeAdapter(TuleapArtifactWithAttachment.class,
				new TuleapArtifactWithAttachmentSerializer());
		gsonBuilder.registerTypeAdapter(ArtifactLinkFieldValue.class, new ArtifactLinkFieldValueAdapter());
		gsonBuilder.registerTypeAdapter(LiteralFieldValue.class, new LiteralFieldValueSerializer());
		gsonBuilder.registerTypeAdapter(AttachmentFieldValue.class, new AttachmentFieldValueAdapter());
		gsonBuilder.registerTypeAdapter(BoundFieldValue.class, new BoundFieldValueSerializer());
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		gsonBuilder.serializeNulls();
		return gsonBuilder;
	}
}
