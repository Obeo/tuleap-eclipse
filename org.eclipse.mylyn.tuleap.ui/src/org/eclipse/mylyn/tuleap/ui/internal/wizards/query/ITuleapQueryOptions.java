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
		String STRING_OPTIONS_CONTAINS = "contains"; //$NON-NLS-1$

		/**
		 * Starts with.
		 */
		String STRING_OPTIONS_STARTS_WITH = "starts with"; //$NON-NLS-1$

		/**
		 * Ends with.
		 */
		String STRING_OPTIONS_ENDS_WITH = "ends with"; //$NON-NLS-1$

		/**
		 * Equals.
		 */
		String STRING_OPTIONS_EQUALS = "equals"; //$NON-NLS-1$

		/**
		 * Date equals.
		 */
		String STRING_OPTIONS_DATE_EQUALS = "="; //$NON-NLS-1$

		/**
		 * Before.
		 */
		String STRING_OPTIONS_BEFORE = "<"; //$NON-NLS-1$

		/**
		 * After.
		 */
		String STRING_OPTIONS_AFTER = ">"; //$NON-NLS-1$

	}
}
