/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.internal.model.data;

import java.util.List;

/**
 * This class will represents an attachment.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class AttachmentFieldValue extends AbstractFieldValue {

	/**
	 * The description of the file attached.
	 */
	private List<AttachmentValue> attachments;

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 * @param attachments
	 *            The attachments
	 */
	public AttachmentFieldValue(int fieldId, List<AttachmentValue> attachments) {
		super(fieldId);
		this.attachments = attachments;
	}

	/**
	 * Returns the description of the file attached.
	 * 
	 * @return The description of the file attached
	 */
	public List<AttachmentValue> getAttachments() {
		return this.attachments;
	}
}
