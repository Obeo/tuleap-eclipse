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

import java.io.Serializable;

/**
 * The canned response.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapCannedResponse implements Serializable {
	/**
	 * The generated serial id.
	 */
	private static final long serialVersionUID = -1216066669186379870L;

	/**
	 * The title of the response.
	 */
	private String title;

	/**
	 * The content of the response.
	 */
	private String content;

	/**
	 * The constructor.
	 * 
	 * @param responseTitle
	 *            The title of the response.
	 * @param responseContent
	 *            The content of the response.
	 */
	public TuleapCannedResponse(String responseTitle, String responseContent) {
		this.title = responseTitle;
		this.content = responseContent;
	}

	/**
	 * Returns the content of the response.
	 * 
	 * @return The content of the response.
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Returns the title of the response.
	 * 
	 * @return The title of the response.
	 */
	public String getTitle() {
		return this.title;
	}
}
