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
package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap;

import java.util.Hashtable;

import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;

import org.apache.axis.transport.http.HTTPConstants;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.WebUtil;
import org.eclipse.mylyn.internal.provisional.commons.soap.SoapHttpSender;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APILocator;

/**
 * This subclass of the TuleapTrackerV5APILocatorImpl is used to configure the Mylyn SOAP API.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
@SuppressWarnings("all")
public class TuleapTrackerV5APILocatorImpl extends TuleapTrackerV5APILocator {

	/**
	 * The generated serialization Id.
	 */
	private static final long serialVersionUID = 2993577501717328621L;

	private final AbstractWebLocation location;

	public TuleapTrackerV5APILocatorImpl(org.apache.axis.EngineConfiguration config,
			AbstractWebLocation webLocation) {
		super(config);
		this.location = webLocation;
	}

	@Override
	public Call createCall() throws ServiceException {
		Call call = super.createCall();

		call.setProperty(SoapHttpSender.LOCATION, this.location);

		Hashtable<String, String> headers = new Hashtable<String, String>();
		headers.put(HTTPConstants.HEADER_USER_AGENT, WebUtil.getUserAgent("TuleapConnector Axis/1.4")); //$NON-NLS-1$
		// some servers break with a 411 Length Required when chunked encoding is used
		headers.put(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED, Boolean.FALSE.toString());
		call.setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		return call;
	}
}
