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
import org.tuleap.mylyn.task.ui.tests.internal.wizards.query.QueryFieldVisitorTest;
import org.tuleap.mylyn.task.ui.tests.internal.wizards.query.TuleapDateQueryElementTest;
import org.tuleap.mylyn.task.ui.tests.internal.wizards.query.TuleapDoubleQueryElementTest;
import org.tuleap.mylyn.task.ui.tests.internal.wizards.query.TuleapIntegerQueryElementTest;
import org.tuleap.mylyn.task.ui.tests.internal.wizards.query.TuleapSelectBoxQueryElementTest;

/**
 * Test suite for unit tests that don't need an actual Tuleap server to run.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@RunWith(Suite.class)
@SuiteClasses({
	// wizards.query
	QueryFieldVisitorTest.class, TuleapDateQueryElementTest.class, TuleapDoubleQueryElementTest.class,
	TuleapIntegerQueryElementTest.class, TuleapSelectBoxQueryElementTest.class })
public final class AllTuleapUiUnitTests {

	/**
	 * The constructor.
	 */
	private AllTuleapUiUnitTests() {
		// prevent instantiation
	}
}
