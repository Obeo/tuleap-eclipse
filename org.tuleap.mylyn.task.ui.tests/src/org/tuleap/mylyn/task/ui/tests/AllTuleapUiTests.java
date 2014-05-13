/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.ui.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tuleap.mylyn.task.ui.tests.internal.artifacts.TuleapRepositoryConnectorTests;

/**
 * Utility class used to launch all the unit tests of the bundle.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@RunWith(Suite.class)
@SuiteClasses({TuleapRepositoryConnectorTests.class })
public final class AllTuleapUiTests {

	/**
	 * The constructor.
	 */
	private AllTuleapUiTests() {
		// prevent instantiation
	}
}
