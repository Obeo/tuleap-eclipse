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

import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;

/**
 * Utility class used to transform the the data structure used by the SOAP API in {@link TuleapArtifact}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapSoapParser {
	/**
	 * Parse the SOAP data and return a {@link TuleapProjectConfiguration} representing the project.
	 * 
	 * @return The {@link TuleapProjectConfiguration} representing the project
	 */
	public TuleapProjectConfiguration getProjectConfiguration() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parse the SOAP data and return a {@link TuleapArtifact} representing the artifact.
	 * 
	 * @return The TuleapArtifact representing the artifact
	 */
	public TuleapArtifact parseArtifact() {
		throw new UnsupportedOperationException();
	}
}
