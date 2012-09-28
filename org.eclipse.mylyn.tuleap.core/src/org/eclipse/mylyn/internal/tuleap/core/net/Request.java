/*******************************************************************************
 * Copyright (c) 2006, 2012 Steffen Pingel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Steffen Pingel - initial API and implementation
 *     Stephane Begaudeau - Heavy refactoring
 *******************************************************************************/
package org.eclipse.mylyn.internal.tuleap.core.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;

/**
 * This class manipulate Http requests. <br />
 * <br />
 * This class is heavily inspired by the EPL licensed class TracWebClient.Request from the Eclipse Mylyn Tasks
 * project created by Steffen Pingel.
 * 
 * @author <a href="mailto:steffen.pingel@tasktop.com">Steffen Pingel</a>
 * @since 1.0
 */
public class Request {

	/**
	 * Artificial status code to indicate that SSL cert authentication failed.
	 */
	public static final int SC_CERT_AUTH_FAILED = 499;

	/**
	 * The login url.
	 */
	private static final String LOGIN_URL = "/account/login.php"; //$NON-NLS-1$

	/**
	 * The key of the login.
	 */
	private static final String FORM_LOGIN = "form_loginname"; //$NON-NLS-1$

	/**
	 * The key of the password.
	 */
	private static final String FORM_PASSWORD = "form_pw"; //$NON-NLS-1$

	/**
	 * The url of the request.
	 */
	private final String url;

	/**
	 * The location of the tracker.
	 */
	private AbstractWebLocation location;

	/**
	 * The user name.
	 */
	private String name;

	/**
	 * The user password.
	 */
	private String password;

	/**
	 * The constructor.
	 * 
	 * @param userLogin
	 *            The login
	 * @param userPassword
	 *            The password
	 * @param trackerLocation
	 *            The location of the tracker
	 * @param requestUrl
	 *            The url of the http request.
	 */
	public Request(String userLogin, String userPassword, AbstractWebLocation trackerLocation, String requestUrl) {
		this.name = userLogin;
		this.password = userPassword;
		this.location = trackerLocation;
		this.url = requestUrl;
	}

	/**
	 * Execute the request.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return The http response
	 */
	public HttpResponse execute(IProgressMonitor monitor) {
		HttpResponse response = null;
		for (int attempt = 0; attempt < 2; attempt++) {
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient();

				HttpGet httpget = new HttpGet(this.location.getUrl());

				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();

				System.out.println("Login form get: " + response.getStatusLine());
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				System.out.println("Initial set of cookies:");
				List<Cookie> cookies = httpclient.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}

				String domainUrl = this.location.getUrl();
				if (domainUrl.contains(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE)) {
					domainUrl = domainUrl.substring(0, domainUrl
							.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE));
				}
				HttpPost httpost = new HttpPost(domainUrl + LOGIN_URL);

				List<NameValuePair> nvps = new ArrayList<NameValuePair>();

				nvps.add(new BasicNameValuePair(FORM_LOGIN, this.name));
				nvps.add(new BasicNameValuePair(FORM_PASSWORD, this.password));

				httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

				response = httpclient.execute(httpost);
				entity = response.getEntity();

				System.out.println("Login form get: " + response.getStatusLine());
				if (entity != null) {
					EntityUtils.consume(entity);
				}

				System.out.println("Post logon cookies:");
				cookies = httpclient.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}

				httpget = new HttpGet(this.url);

				response = httpclient.execute(httpget);
				entity = response.getEntity();

				System.out.println("Webpage get: " + response.getStatusLine());
				final int ok = 200;
				if (entity != null && ok == response.getStatusLine().getStatusCode()) {
					return response;
				}

				// When HttpClient instance is no longer needed,
				// shut down the connection manager to ensure
				// immediate deallocation of all system resources
				httpclient.getConnectionManager().shutdown();
			} catch (IOException e) {
				TuleapCoreActivator.log(e, true);
			}
		}
		return response;
	}
}
