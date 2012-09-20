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
package org.eclipse.mylyn.internal.tuleap.ui.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tasks.core.IRepositoryConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.ui.util.TuleapMylynTasksUIMessages;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;

/**
 * The wizard page displaying the settings of the Tuleap repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
@SuppressWarnings("restriction")
public class TuleapRepositorySettingsPage extends AbstractRepositorySettingsPage {

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            the Mylyn task repository.
	 */
	public TuleapRepositorySettingsPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapRepositorySettingsPage.Name"), //$NON-NLS-1$
				TuleapMylynTasksUIMessages.getString("TuleapRepositorySettingsPage.Description"), //$NON-NLS-1$
				taskRepository);
		// TODO Check with Enalean for the options of the Tuleap tracker
		this.setNeedsAnonymousLogin(true);
		this.setNeedsEncoding(true);
		this.setNeedsValidateOnFinish(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return ITuleapConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#createAdditionalControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createAdditionalControls(Composite parent) {
		// nothing yet (consider supporting multiple versions of Tuleap later)
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#isValidUrl(java.lang.String)
	 */
	@Override
	protected boolean isValidUrl(String url) {
		// The Tuleap tracker that the repository will respect the following pattern
		// "https://<domainName>/plugins/tracker/?group_id=<trackerId>"
		boolean isValid = super.isValidUrl(url);
		return isValid && url.matches("https://.*/plugins/tracker/\\?group_id=[0-9]*"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#applyTo(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public void applyTo(TaskRepository taskRepository) {
		super.applyTo(taskRepository);
		taskRepository
				.setProperty(IRepositoryConstants.PROPERTY_CATEGORY, IRepositoryConstants.CATEGORY_BUGS);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#getValidator(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	protected Validator getValidator(TaskRepository taskRepository) {
		return new TuleapRepositoryValidator(taskRepository);
	}

	/**
	 * The validator used to check the configuration of the repository.
	 * 
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 * @since 1.0
	 */
	private class TuleapRepositoryValidator extends Validator {

		/**
		 * The validation engine.
		 */
		private TuleapValidator tuleapValidator;

		/**
		 * The constructor.
		 * 
		 * @param taskRepository
		 *            The Mylyn task repository
		 */
		public TuleapRepositoryValidator(TaskRepository taskRepository) {
			this.tuleapValidator = new TuleapValidator(taskRepository);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage.Validator#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor) throws CoreException {
			this.tuleapValidator.validate(monitor);
		}

	}
}
