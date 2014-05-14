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

import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifactWithAttachment;

/**
 * This class is used to serialize the JSON representation of a {@link TuleapArtifactWithAttachment}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapArtifactWithAttachmentSerializer extends TuleapArtifactWithCommentSerializer<TuleapArtifactWithAttachment> {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.serializer.AbstractTuleapSerializer#mustSerialize(org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField,
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
