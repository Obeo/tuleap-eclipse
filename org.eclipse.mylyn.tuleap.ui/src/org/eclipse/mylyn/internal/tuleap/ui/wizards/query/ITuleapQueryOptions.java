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
package org.eclipse.mylyn.internal.tuleap.ui.wizards.query;

import org.eclipse.mylyn.internal.tuleap.ui.util.TuleapMylynTasksUIMessages;

/**
 * This interface contains the list of options available for each type of widget.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapQueryOptions {
	/**
	 * The options for string fields.
	 * 
	 * @noextend This class is not intended to be subclassed by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 * @since 1.0
	 */
	public interface StringOptions {
		/**
		 * Contains.
		 */
		String STRING_OPTIONS_CONTAINS = TuleapMylynTasksUIMessages
				.getString("ITuleapQueryOptions.StringsOptions.Contains"); //$NON-NLS-1$

		/**
		 * Starts with.
		 */
		String STRING_OPTIONS_STARTS_WITH = TuleapMylynTasksUIMessages
				.getString("ITuleapQueryOptions.StringsOptions.StartsWith"); //$NON-NLS-1$

		/**
		 * Ends with.
		 */
		String STRING_OPTIONS_ENDS_WITH = TuleapMylynTasksUIMessages
				.getString("ITuleapQueryOptions.StringsOptions.EndsWith"); //$NON-NLS-1$

		/**
		 * Equals.
		 */
		String STRING_OPTIONS_EQUALS = TuleapMylynTasksUIMessages
				.getString("ITuleapQueryOptions.StringsOptions.Equals"); //$NON-NLS-1$
	}
}
