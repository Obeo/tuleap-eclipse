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
package org.tuleap.mylyn.task.internal.core.client.rest;

import com.google.common.collect.Multimap;

import java.util.Map;

/**
 * Interface representing a RESTful operation, i.e. the association of a URL and an HTTP method to perform on
 * this URL.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IRestOperation {

	/**
	 * Provides the URL of this REST operation.
	 * 
	 * @return The URL on which the operation will be performed.
	 */
	String getUrl();

	/**
	 * Provides the name of the HTTP method that will be called when performing the operation.
	 * 
	 * @return The name of the HTTP method that will be performed. Probably one of
	 *         <ul>
	 *         <li>GET</li>
	 *         <li>OPTIONS</li>
	 *         <li>PUT</li>
	 *         <li>POST</li>
	 *         </ul>
	 */
	String getMethodName();

	/**
	 * Execute this operation.
	 * 
	 * @return The {@link ServerResponse} returned by the server when invoking the method on the URL.
	 */
	ServerResponse run();

	/**
	 * Sets the body to send in the request.
	 * 
	 * @param someBody
	 *            The body to send.
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withBody(String someBody);

	/**
	 * Adds a header property to send in the request.
	 * 
	 * @param key
	 *            The key of the header property to send.
	 * @param value
	 *            The value of the header property. If there is already one entry for this key, it is
	 *            replaced.
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withHeader(String key, String value);

	/**
	 * Adds properties to the header to send in the request.
	 * 
	 * @param someHeaders
	 *            The headers to add to the resource. Existing entries are replaced if needed but never
	 *            removed. The given map's entries are added to the existing map of headers.
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withHeaders(Map<String, String> someHeaders);

	/**
	 * Adds one query parameter.
	 * 
	 * @param key
	 *            The key of the parameter.
	 * @param value
	 *            The value of the parameter. If there is already one or more entries for this key, a new
	 *            entry is added with this key, existing entries are not modified.
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withQueryParameter(String key, String value);

	/**
	 * Adds query parameters to this REST resource.
	 * 
	 * @param queryParameters
	 *            The query parameters to add to the resource.
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withQueryParameters(Multimap<String, String> queryParameters);

	/**
	 * Clears all query parameters for this resource.
	 * 
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withoutQueryParameter();

	/**
	 * Clears the query parameters with the given key.
	 * 
	 * @param key
	 *            Key to remove from the query parameters, all entries will be removed for this key.
	 * @return An instance of {@link IRestOperation} for a fluent API.
	 */
	IRestOperation withoutQueryParameters(String key);
}
