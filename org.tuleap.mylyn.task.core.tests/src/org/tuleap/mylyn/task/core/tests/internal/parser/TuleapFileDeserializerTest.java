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
package org.tuleap.mylyn.task.core.tests.internal.parser;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapFile;
import org.tuleap.mylyn.task.core.internal.parser.TuleapGsonProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests the JSON deserialization of {@link TuleapFile}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapFileDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	/**
	 * Test a complete file.
	 */
	@Test
	public void testDeserializeCompleteFile() {
		String json = ParserUtil.loadFile("/artifact_files/file-0.json");
		TuleapFile tuleapFile = gson.fromJson(json, TuleapFile.class);
		assertNotNull(tuleapFile);
		assertEquals("VGhpcyBpcyB0aGUgZmlsZSBkYXRhIHRvIHRlc3Q=", tuleapFile.getData());
	}

	/**
	 * Test a file with null data.
	 */
	@Test
	public void testDeserializeFileWithNullData() {
		String json = ParserUtil.loadFile("/artifact_files/file-1.json");
		TuleapFile tuleapFile = gson.fromJson(json, TuleapFile.class);
		assertNotNull(tuleapFile);
		assertNull(tuleapFile.getData());
	}
}
