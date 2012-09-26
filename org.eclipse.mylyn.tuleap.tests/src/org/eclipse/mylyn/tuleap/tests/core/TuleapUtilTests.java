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

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;

/**
 * Test utility methods.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 1.0
 */
public class TuleapUtilTests extends TestCase {

	/**
	 * Test {@link TuleapUtil#download(String, File)}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	public void testDownload() throws Exception {
		String url = "https://demo.tuleap.net/downloads/Codendi_CLI.zip"; //$NON-NLS-1$
		File downloadedFile = File.createTempFile("temp", ""); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapUtil.download(url, downloadedFile);
		File originFile = new File("./resource/Codendi_CLI.zip"); //$NON-NLS-1$

		assertEquals(TuleapUtil.getChecksum(originFile), TuleapUtil.getChecksum(downloadedFile));
		downloadedFile.delete();
	}
}
