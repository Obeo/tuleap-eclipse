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
package org.eclipse.mylyn.internal.tuleap.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will hold the configuration of a Tuleap instance.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapInstanceConfiguration implements Serializable {

	/**
	 * The generated serialization ID.
	 */
	private static final long serialVersionUID = 2416708654638468973L;

	/**
	 * This map contains the ID of the tracker of the Tuleap instance and their matching configuration.
	 */
	private Map<String, TuleapTrackerConfiguration> trackerId2trackerConfiguration = new HashMap<String, TuleapTrackerConfiguration>();

}
