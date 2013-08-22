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
package org.tuleap.mylyn.task.internal.tests.server;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.server.ServerResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the ServerResponse class.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ServerResponseTest {

	/**
	 * Test about standard ServerResponse manipulation.
	 */
	@Test
	public void testServerResponseWithEmptyMap() {
		int status = 123;
		String body = "{\n  \"hello\": \"world\""; //$NON-NLS-1$
		Map<String, String> headers = new HashMap<String, String>();
		ServerResponse response = new ServerResponse(status, body, headers);
		assertEquals(status, response.getStatus());
		assertEquals(body, response.getBody());
		assertTrue(response.getHeaders().isEmpty());

		String key = "Allow"; //$NON-NLS-1$
		String value = "OPTIONS, GET, PUT, POST"; //$NON-NLS-1$
		headers.put(key, value);
		response = new ServerResponse(status, body, headers);
		assertEquals(status, response.getStatus());
		assertEquals(body, response.getBody());
		assertEquals(1, response.getHeaders().size());
		assertEquals(value, response.getHeaders().get(key));
	}

}
