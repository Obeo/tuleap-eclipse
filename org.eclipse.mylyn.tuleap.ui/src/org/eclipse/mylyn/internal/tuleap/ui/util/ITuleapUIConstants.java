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
package org.eclipse.mylyn.internal.tuleap.ui.util;

/**
 * Utility interface containing constants for the user interface of the Tuleap connector.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapUIConstants {
	/**
	 * The path of all the availables icons.
	 * 
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 * @since 1.0
	 */
	public interface Icons {
		/**
		 * The icon of the wizard.
		 */
		String WIZARD_TULEAP_LOGO_48 = "/icons/wizards/tuleap-logo_48x48.png"; //$NON-NLS-1$

		/**
		 * The icon of the connector.
		 */
		String VIEW_TULEAP_LOGO_16 = "/icons/tools/tuleap-logo_16x16.png"; //$NON-NLS-1$
	}
}
