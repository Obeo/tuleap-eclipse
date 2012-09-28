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
package org.eclipse.mylyn.tuleap.tests.core;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;

/**
 * Test class for {@link TuleapUtil}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapUtilTests extends TestCase {
	/**
	 * Test the download of the configuration.
	 */
	public void testDownloadConfiguration() {
		File file;
		try {
			file = File.createTempFile("repositoryConfiguration", "xml"); //$NON-NLS-1$ //$NON-NLS-2$
			TuleapUtil.download("https://demo.tuleap.net/plugins/tracker/?tracker=409&func=admin-export", //$NON-NLS-1$
					file);
			System.out.println(file.getAbsolutePath());
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
