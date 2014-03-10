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
package org.eclipse.mylyn.tuleap.core.tests.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.osgi.framework.Bundle;

/**
 * A logger used to test the message logged.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TestLogger implements ILog {

	/**
	 * The collection of all the status logged.
	 */
	private List<IStatus> status = new ArrayList<IStatus>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ILog#addLogListener(org.eclipse.core.runtime.ILogListener)
	 */
	public void addLogListener(ILogListener listener) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ILog#getBundle()
	 */
	public Bundle getBundle() {
		return Platform.getBundle(TuleapCoreActivator.PLUGIN_ID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ILog#log(org.eclipse.core.runtime.IStatus)
	 */
	public void log(IStatus aStatus) {
		this.status.add(aStatus);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.ILog#removeLogListener(org.eclipse.core.runtime.ILogListener)
	 */
	public void removeLogListener(ILogListener listener) {
		// do nothing
	}

	/**
	 * Return all the status logged.
	 * 
	 * @return The status logged
	 */
	public List<IStatus> getStatus() {
		return status;
	}

}
