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
package org.eclipse.mylyn.tuleap.core.internal.client.rest;

/**
 * The potential services activated on a Tuleap project.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ITuleapProjectServices {
	/**
	 * Trackers.
	 */
	String TRACKERS = "trackers"; //$NON-NLS-1$

	/**
	 * Agile dashboard.
	 */
	String AGILE_DASHBOARD = "agile_dashboard"; //$NON-NLS-1$
}
