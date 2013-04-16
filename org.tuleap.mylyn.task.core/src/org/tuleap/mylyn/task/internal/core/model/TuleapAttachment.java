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
package org.tuleap.mylyn.task.internal.core.model;


/**
 * This class will represents an attachement.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapAttachment {

	/**
	 * The identifier.
	 */
	private String id;

	/**
	 * The name.
	 */
	private String filename;

	/**
	 * The author.
	 */
	private TuleapPerson person;

	/**
	 * The size.
	 */
	private Long size;

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
	 * @param identifier
	 *            The identifier
	 * @param name
	 *            The name
	 * @param uploadedBy
	 *            The author
	 * @param filesize
	 *            The size
	 * @param desc
	 *            The description
	 * @param type
	 *            The content type
	 */
	public TuleapAttachment(String identifier, String name, TuleapPerson uploadedBy, Long filesize,
			String desc, String type) {
		this.id = identifier;
		this.filename = name;
		this.person = uploadedBy;
		this.size = filesize;
		this.description = desc;
		this.contentType = type;
	}

	/**
	 * Returns the identifier.
	 * 
	 * @return The identifier
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the author.
	 * 
	 * @return The author
	 */
	public TuleapPerson getPerson() {
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
	public Long getSize() {
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
