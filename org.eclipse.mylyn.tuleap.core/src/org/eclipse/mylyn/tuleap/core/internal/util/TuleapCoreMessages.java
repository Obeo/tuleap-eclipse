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
package org.eclipse.mylyn.tuleap.core.internal.util;

import org.eclipse.osgi.util.NLS;

/**
 * Utility class for internationalization.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public final class TuleapCoreMessages {

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private TuleapCoreMessages() {
		// prevents instantiation
	}

	/**
	 * Returns the specified {@link String} from the resource bundle.
	 * 
	 * @param key
	 *            Key of the String we seek.
	 * @return The String from the resource bundle associated with <code>key</code>.
	 *         <code>'!' + key + '!'</code> will be returned in case we didn't find it in the bundle.
	 */
	public static String getString(String key) {
		return NLS.bind(key, new Object[] {});
	}

	/**
	 * Returns a String from the resource bundle bound with the given arguments.
	 * 
	 * @param key
	 *            Key of the String we seek.
	 * @param arguments
	 *            Arguments for the String formatting.
	 * @return formatted {@link String}.
	 */
	public static String getString(String key, Object... arguments) {
		if (arguments == null) {
			return getString(key);
		}
		return NLS.bind(key, arguments);
	}
}
