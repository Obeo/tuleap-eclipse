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

import java.io.InputStream;

/**
 * The descriptor of the attachment to upload.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapAttachmentDescriptor {
	/**
	 * The name of the field.
	 */
	private String fieldName;

	/**
	 * The label of the field.
	 */
	private String fieldLabel;

	/**
	 * The name of the file.
	 */
	private String fileName;

	/**
	 * The type of the file.
	 */
	private String fileType;

	/**
	 * The description.
	 */
	private String description;

	/**
	 * The length.
	 */
	private Long length;

	/**
	 * The input stream.
	 */
	private InputStream inputStream;

	/**
	 * The constructor.
	 * 
	 * @param fieldname
	 *            The name of the field
	 * @param fieldlabel
	 *            The label of the field
	 * @param filename
	 *            The name of the file
	 * @param filetype
	 *            The type of the file
	 * @param desc
	 *            The description
	 * @param size
	 *            The length
	 * @param is
	 *            The input stream
	 */
	public TuleapAttachmentDescriptor(String fieldname, String fieldlabel, String filename, String filetype,
			String desc, Long size, InputStream is) {
		this.fieldName = fieldname;
		this.fieldLabel = fieldlabel;
		this.fileName = filename;
		this.fileType = filetype;
		this.description = desc;
		this.length = size;
		this.inputStream = is;
	}

	/**
	 * Returns the name of the file.
	 * 
	 * @return The name of the file.
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * Returns the name of the field.
	 * 
	 * @return The name of the field
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * Returns the label of the field.
	 * 
	 * @return The label of the field
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}

	/**
	 * Returns the type of the file.
	 * 
	 * @return The type of the file
	 */
	public String getFileType() {
		return this.fileType;
	}

	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the input stream.
	 * 
	 * @return the input stream
	 */
	public InputStream getInputStream() {
		return this.inputStream;
	}

	/**
	 * Returns the length.
	 * 
	 * @return the length
	 */
	public Long getLength() {
		return this.length;
	}
}
