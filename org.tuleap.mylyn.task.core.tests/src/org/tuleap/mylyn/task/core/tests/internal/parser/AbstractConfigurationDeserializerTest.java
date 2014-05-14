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

import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;

/**
 * Super class of tests that deserialize project configuration parts.
 * 
 * @param <T>
 *            The type of deserialized object.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractConfigurationDeserializerTest<T> extends AbstractDeserializerTest<T> {

	/**
	 * The project configuration provider.
	 */
	private StubProjectProvider provider = new StubProjectProvider();

	/**
	 * Provides the project Configuration, after creating it if necessary. Not thread-safe.
	 * 
	 * @return The project configuration to use for tests.
	 */
	protected TuleapProject getProjectConfiguration() {
		return provider.getProjectConfiguration();
	}

}
