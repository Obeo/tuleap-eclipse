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
package org.tuleap.mylyn.task.internal.core.parser;

import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;

/**
 * Utility class to hide all the logic of the JSON serialization.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapJsonSerializer {
	/**
	 * Serialize in JSON the fields of the Tuleap artifact.
	 * 
	 * @param tuleapTaskMapper
	 *            The Tuleap task mapper
	 * @return A JSON representation of the fields of the Tuleap artifact
	 */
	public String serializeArtifactFields(TuleapTaskMapper tuleapTaskMapper) {
		return null;
	}
}
