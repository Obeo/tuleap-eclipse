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
package org.eclipse.mylyn.tuleap.ui.internal.util;

/**
 * Utility interface containing constants for the user interface of the Tuleap connector.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public interface ITuleapUIConstants {
	/**
	 * The path of all the availables icons.
	 * 
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 * @since 0.7
	 */
	public interface Icons {
		/**
		 * The icon of the settings page of the repository.
		 */
		String BANNER_REPOSITORY_SETTINGS_75X66 = "/icons/wizards/banner-repository-settings_75x66.png"; //$NON-NLS-1$

		/**
		 * The Tuleap logo in 16x16.
		 */
		String TULEAP_LOGO_16X16 = "/icons/tools/tuleap-logo_16x16.png"; //$NON-NLS-1$

		/**
		 * The Tuleap logo in 48x48.
		 */
		String TULEAP_LOGO_48X48 = "/icons/wizards/tuleap-logo_48x48.png"; //$NON-NLS-1$

		/**
		 * The Tuleap logo for wizards.
		 */
		String TULEAP_LOGO_WIZARD_75X66 = "/icons/wizards/tuleap-logo_wizard_75x66.png"; //$NON-NLS-1$

		/**
		 * The Tuleap locked project icon.
		 */
		String TULEAP_PROJECT_LOCK_16X16 = "/icons/wizards/tuleap-project-lock_16x16.png"; //$NON-NLS-1$

		/**
		 * The Tuleap unlocked project icon.
		 */
		String TULEAP_PROJECT_UNLOCK_16X16 = "/icons/wizards/tuleap-project-unlock_16x16.png"; //$NON-NLS-1$

		/**
		 * The Tuleap tracker icon.
		 */
		String TULEAP_TRACKER_16X16 = "/icons/wizards/tuleap-tracker_16x16.png"; //$NON-NLS-1$

		/**
		 * The Tuleap planning icon.
		 */
		String TULEAP_PLANNING_16X16 = "/icons/wizards/tuleap-planning_16x16.png"; //$NON-NLS-1$

		/**
		 * The Tuleap cardwall icon.
		 */
		String TULEAP_CARDWALL_16X16 = "/icons/wizards/tuleap-cardwall_16x16.png"; //$NON-NLS-1$
	}
}
