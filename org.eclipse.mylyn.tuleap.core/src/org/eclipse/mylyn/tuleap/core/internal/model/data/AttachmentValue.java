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
package org.eclipse.mylyn.tuleap.core.internal.model.data;

/**
 * This class is used to represent an attachment in a {@link AttachmentFieldValue}.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AttachmentValue {
	/**
	 * The identifier of the attachment.
	 */
	private String id;

	/**
	 * The file name.
	 */
	private String name;

	/**
	 * Id of the submitter.
	 */
	private int submittedBy;

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
	private String type;

	/**
	 * The identifier of the attachment.
	 */
	private String uri;

	/**
	 * Default constructor.
	 */
	public AttachmentValue() {
		// Default constructor for JSON deserialization.
	}

	/**
	 * The constructor.
	 *
	 * @param attachmentId
	 *            The identifier of the file
	 * @param name
	 *            The name
	 * @param submittedBy
	 *            The identifier of the submitter
	 * @param filesize
	 *            The size
	 * @param desc
	 *            The description
	 * @param type
	 *            The content type
	 * @param uri
	 *            The attachment uri
	 */
	public AttachmentValue(String attachmentId, String name, int submittedBy, int filesize, String desc,
			String type, String uri) {
		this.id = attachmentId;
		this.name = name;
		this.submittedBy = submittedBy;
		this.size = filesize;
		this.description = desc;
		this.type = type;
		this.uri = uri;
	}

	/**
	 * Returns the identifier of the file.
	 *
	 * @return The identifier of the file
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the identifier of the author.
	 *
	 * @return The identifier of the author
	 */
	public int getSubmittedBy() {
		return submittedBy;
	}

	/**
	 * Returns the filename.
	 *
	 * @return The filename
	 */
	public String getName() {
		return name;
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
	public String getType() {
		return type;
	}

	/**
	 * returns the attachment Uri.
	 *
	 * @return The uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Attachment ID setter.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Name setter.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Submitter ID setter.
	 *
	 * @param submittedBy
	 *            the submittedBy to set
	 */
	public void setSubmittedBy(int submittedBy) {
		this.submittedBy = submittedBy;
	}

	/**
	 * size setter.
	 *
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Description setter.
	 *
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Content type setter.
	 *
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * URI setter.
	 *
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
