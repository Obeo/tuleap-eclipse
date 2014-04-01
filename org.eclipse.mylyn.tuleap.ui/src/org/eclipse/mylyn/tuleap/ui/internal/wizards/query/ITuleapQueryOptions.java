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
package org.eclipse.mylyn.tuleap.ui.internal.wizards.query;

/**
 * This interface contains the list of options available for each type of widget.
 *
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public interface ITuleapQueryOptions {
	/**
	 * The options for string fields.
	 *
	 * @noextend This class is not intended to be subclassed by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 * @since 0.7
	 */
	public interface StringOptions {
		/**
		 * Contains.
		 */
		String OPTION_CONTAINS = "contains"; //$NON-NLS-1$

		/**
		 * Date equals.
		 */
		String OPTION_EQUALS = "="; //$NON-NLS-1$

		/**
		 * Before.
		 */
		String OPTION_BEFORE = "<"; //$NON-NLS-1$

		/**
		 * After.
		 */
		String OPTION_AFTER = ">"; //$NON-NLS-1$

		/**
		 * Between (for dates).
		 */
		String OPTION_BETWEEN = "between"; //$NON-NLS-1$

	}
}
