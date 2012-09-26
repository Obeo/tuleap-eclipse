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
package org.eclipse.mylyn.tuleap.tests.ui;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.mylyn.internal.tuleap.ui.repository.TuleapRepositorySettingsPage;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * Test Tuleap repository settings page.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 */
public class TuleapRepositorySettingsPageTest extends TestCase {
	/**
	 * Repository settings test page.
	 */
	private MyTuleapRepositorySettingsPage page;

	/**
	 * Wizard.
	 */
	private WizardDialog dialog;

	/**
	 * Tuleap repository settings page test.
	 */
	public TuleapRepositorySettingsPageTest() {
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		page = new MyTuleapRepositorySettingsPage(null);

		// stub wizard and dialog
		Wizard wizard = new Wizard() {
			@Override
			public boolean performFinish() {
				return true;
			}
		};
		wizard.addPage(page);
		dialog = new WizardDialog(null, wizard);
		dialog.create();
	}

	@Override
	protected void tearDown() throws Exception {
		if (dialog != null) {
			dialog.close();
		}
	}

	/**
	 * Created to get access to isValidUrl to test it.
	 * 
	 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
	 */
	private static class MyTuleapRepositorySettingsPage extends TuleapRepositorySettingsPage {
		/**
		 * Constructor.
		 * 
		 * @param taskRepository
		 *            Task repository
		 */
		public MyTuleapRepositorySettingsPage(TaskRepository taskRepository) {
			super(taskRepository);
		}

		@Override
		protected void applyValidatorResult(Validator validator) {
			// see AbstractRespositorySettingsPage.validate()
			if (validator.getStatus() == null) {
				validator.setStatus(Status.OK_STATUS);
			}
			super.applyValidatorResult(validator);
		}

		@Override
		protected boolean isValidUrl(String name) {
			return super.isValidUrl(name);
		}

	}

	/**
	 * Test {@link TuleapRepositorySettingsPage.isValidUrl}.
	 */
	public void testValidUrl() {
		assertFalse(page.isValidUrl("")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http:/google.com")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http:/google.com/")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http://google.com/")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http://google.com/foo /space")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http://google.com")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("https://google.com")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http://mylyn.org/trac30")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("http://www.mylyn.org/trac30")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("https://plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=aa")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("https://my.demo.domain /plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertFalse(page.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=42x")); //$NON-NLS-1$
		assertTrue(page.isValidUrl("https://my.demo.domain/subdomain/plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertTrue(page.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=42")); //$NON-NLS-1$
	}

}
