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
package org.tuleap.mylyn.task.internal.core.repository;

import org.tuleap.mylyn.task.agile.core.AbstractAgileRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * Tuleap Agile repository connector, which is exposed as an OSGI Service. This connector is in charge of
 * providing the relevant wizards and actions specific to the agile planner for Tuleap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapAgileRepositoryConnector extends AbstractAgileRepositoryConnector {

	/**
	 * {@inheritDoc}
	 * <p>
	 * The supported connector kind returned is the constant {@code ITuleapConstants.CONNECTOR_KIND}.
	 * </p>
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.AbstractAgileRepositoryConnector#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return ITuleapConstants.CONNECTOR_KIND;
	}

}
