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
package org.eclipse.mylyn.tuleap.core.internal.model.data.agile;

/**
 * A Tuleap element status. Currently, the only statuses that exist are "Open" and "Closed".
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public enum TuleapStatus {
	/**
	 * Means that the element is closed.
	 */
	Closed,
	/**
	 * Means that the element is open.
	 */
	Open;
}
