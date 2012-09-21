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
package org.eclipse.mylyn.internal.tuleap.core.util;

/**
 * This interface is a container of constants used accros the Mylyn tasks Tuleap connector.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapConstants {
	/**
	 * The kind of Mylyn tasks connector.
	 */
	String CONNECTOR_KIND = "tuleap"; //$NON-NLS-1$

	/**
	 * The descriptor file.
	 */
	String TULEAP_DESCRIPTOR_FILE = "tuleap.desciptorFile"; //$NON-NLS-1$

	/**
	 * The repository url separator.
	 */
	String REPOSITORY_URL_SEPARATOR = "#"; //$NON-NLS-1$

	/**
	 * The ID representing the UTC timezone.
	 */
	String TIMEZONE_UTC = "UTC"; //$NON-NLS-1$

	/**
	 * The ID of the default static binding.
	 */
	String TULEAP_STATIC_BINDING_ID = "static"; //$NON-NLS-1$

	/**
	 * The ID of the default dynamic binding.
	 */
	String TULEAP_DYNAMIC_BINDING_USERS = "users"; //$NON-NLS-1$

	/**
	 * Part of the Tuleap repository URL. The URL format is :
	 * "https://<domainName>/plugins/tracker/?group_id=<trackerId>"
	 */
	String TULEAP_REPOSITORY_URL_STRUCTURE = "/plugins/tracker/?group_id="; //$NON-NLS-1$
}
