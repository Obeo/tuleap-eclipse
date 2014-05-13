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
package org.tuleap.mylyn.task.core.internal.parser;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactLinkFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifactWithAttachment;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapElementComment;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.core.internal.serializer.ArtifactLinkFieldValueAdapter;
import org.tuleap.mylyn.task.core.internal.serializer.BoundFieldValueSerializer;
import org.tuleap.mylyn.task.core.internal.serializer.LiteralFieldValueSerializer;
import org.tuleap.mylyn.task.core.internal.serializer.TuleapArtifactSerializer;
import org.tuleap.mylyn.task.core.internal.serializer.TuleapArtifactWithAttachmentSerializer;
import org.tuleap.mylyn.task.core.internal.serializer.TuleapArtifactWithCommentSerializer;
import org.tuleap.mylyn.task.core.internal.serializer.TuleapCardSerializer;
import org.tuleap.mylyn.task.core.internal.serializer.TuleapMilestoneSerializer;

/**
 * Utility class to configure the Gson instance(s) used by the connector for Tuleap.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
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
		gsonBuilder.registerTypeAdapter(TuleapCardwall.class, new TuleapCardwallDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapCard.class, new TuleapCardDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapCard.class, new TuleapCardSerializer());
		gsonBuilder.registerTypeAdapter(TuleapMilestone.class, new TuleapMilestoneSerializer());
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
