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
package org.tuleap.mylyn.task.internal.core.client.soap;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;

/**
 * Wrapper of an Artifact with its comments.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CommentedArtifact {

	/**
	 * The wrapped Artifact, directly received by SOAP.
	 */
	private final Artifact artifact;

	/**
	 * The related comments, already converted into Tuleap POJOs.
	 */
	private final List<TuleapElementComment> comments;

	/**
	 * Constructor.
	 * 
	 * @param artifact
	 *            artifact
	 * @param comments
	 *            list of comments
	 */
	public CommentedArtifact(final Artifact artifact, final List<TuleapElementComment> comments) {
		Assert.isNotNull(artifact);
		Assert.isNotNull(comments);
		this.artifact = artifact;
		this.comments = Collections.unmodifiableList(comments);
	}

	/**
	 * The artifact.
	 * 
	 * @return The artifact.
	 */
	public Artifact getArtifact() {
		return artifact;
	}

	/**
	 * Retrieve the artifact's comments.
	 * 
	 * @return Un unmodifiable view of the artifact's comments.
	 */
	public List<TuleapElementComment> getComments() {
		return comments;
	}
}
