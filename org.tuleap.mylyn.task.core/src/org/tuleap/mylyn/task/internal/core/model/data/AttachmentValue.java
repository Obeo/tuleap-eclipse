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
package org.tuleap.mylyn.task.internal.core.model.data;

import org.tuleap.mylyn.task.internal.core.model.config.TuleapUser;

/**
 * This class is used to represent an attachment in a {@link AttachmentFieldValue}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class AttachmentValue {
	/**
	 * The identifier of the attachment.
	 */
	private String attachmentId;

	/**
	 * The name.
	 */
	private String filename;

	/**
	 * The author.
	 */
	private TuleapUser person;

	/**
	 * The size.
	 */
	private int size;

	/**
	 * The description.
	 */
	private String description;

	/**
	 * The content type.
	 */
	private String contentType;

	/**
	 * The constructor.
	 * 
	 * @param attachmentId
	 *            The identifier of the file
	 * @param name
	 *            The name
	 * @param uploadedBy
	 *            The identifier of the author
	 * @param filesize
	 *            The size
	 * @param desc
	 *            The description
	 * @param type
	 *            The content type
	 */
	public AttachmentValue(String attachmentId, String name, TuleapUser uploadedBy, int filesize,
			String desc, String type) {
		this.attachmentId = attachmentId;
		this.filename = name;
		this.person = uploadedBy;
		this.size = filesize;
		this.description = desc;
		this.contentType = type;
	}

	/**
	 * Returns the identifier of the file.
	 * 
	 * @return The identifier of the file
	 */
	public String getAttachmentId() {
		return attachmentId;
	}

	/**
	 * Returns the identifier of the author.
	 * 
	 * @return The identifier of the author
	 */
	public TuleapUser getPerson() {
		return person;
	}

	/**
	 * Returns the filename.
	 * 
	 * @return The filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Returns the size.
	 * 
	 * @return The size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the description.
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * returns the content type.
	 * 
	 * @return The content type
	 */
	public String getContentType() {
		return contentType;
	}
}
