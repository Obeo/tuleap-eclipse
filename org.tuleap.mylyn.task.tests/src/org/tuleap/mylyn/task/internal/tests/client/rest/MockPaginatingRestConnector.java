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
package org.tuleap.mylyn.task.internal.tests.client.rest;

import com.google.common.collect.Maps;

import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

/**
 * Mock object to test pagination mechanism.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MockPaginatingRestConnector extends MockRestConnector {

	/**
	 * The responses this object will return. The key correspond to the offset query parameter received.
	 */
	private Map<String, ServerResponse> responses = Maps.newHashMap();

	@Override
	public ServerResponse sendRequest(HttpMethod method) {
		String queryString = method.getQueryString();
		String[] strings = queryString.split("&"); //$NON-NLS-1$
		for (String s : strings) {
			if (s.startsWith("offset=")) { //$NON-NLS-1$
				String offset = s.substring("offset=".length()).trim(); //$NON-NLS-1$
				invocationsCount++;
				return responses.get(offset);
			}
		}
		return super.sendRequest(method);
	}

	/**
	 * Sets the response to return for a given offset received in query parameter.
	 * 
	 * @param offset
	 * @param response
	 */
	public void putResponse(int offset, ServerResponse response) {
		responses.put(Integer.toString(offset), response);
	}
}
