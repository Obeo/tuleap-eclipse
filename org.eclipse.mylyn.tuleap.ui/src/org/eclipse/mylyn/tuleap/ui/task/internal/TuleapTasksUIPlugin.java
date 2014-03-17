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
package org.eclipse.mylyn.tuleap.ui.task.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tuleap.core.internal.client.TuleapClientManager;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.ui.task.internal.util.TuleapUIMessages;
import org.eclipse.mylyn.tuleap.ui.task.internal.util.TuleapUiMessagesKeys;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator in charge of managing the lifecycle of the bundle.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTasksUIPlugin extends AbstractUIPlugin {
	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.eclipse.mylyn.tuleap.ui"; //$NON-NLS-1$

	/**
	 * The sole instance of the activator.
	 */
	private static TuleapTasksUIPlugin instance;

	/**
	 * The images.
	 */
	private Map<String, Image> imageMap = new HashMap<String, Image>();

	/**
	 * Returns the sole instance of the activator.
	 * 
	 * @return The sole instancea of the activator
	 */
	public static TuleapTasksUIPlugin getDefault() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;

		AbstractRepositoryConnector repositoryConnector = TasksUi
				.getRepositoryManager().getRepositoryConnector(
						ITuleapConstants.CONNECTOR_KIND);
		if (repositoryConnector instanceof TuleapRepositoryConnector) {
			TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector) repositoryConnector;
			TuleapClientManager clientManager = tuleapRepositoryConnector
					.getClientManager();
			TasksUi.getRepositoryManager().addListener(clientManager);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		AbstractRepositoryConnector repositoryConnector = TasksUi
				.getRepositoryManager().getRepositoryConnector(
						ITuleapConstants.CONNECTOR_KIND);
		if (repositoryConnector instanceof TuleapRepositoryConnector) {
			TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector) repositoryConnector;
			TuleapClientManager clientManager = tuleapRepositoryConnector
					.getClientManager();
			TasksUi.getRepositoryManager().removeListener(clientManager);
		}

		Iterator<Image> imageIterator = imageMap.values().iterator();
		while (imageIterator.hasNext()) {
			Image image = imageIterator.next();
			image.dispose();
		}
		imageMap.clear();
		super.stop(context);
	}

	/**
	 * Returns an image at the given plug-in relative path.
	 * 
	 * @param path
	 *            is a plug-in relative path
	 * @return the image
	 */
	public Image getImage(String path) {
		Image result = imageMap.get(path);
		if (result == null) {
			ImageDescriptor descriptor = getImageDescriptor(path);
			if (descriptor != null) {
				result = descriptor.createImage();
				imageMap.put(path, result);
			}
		}
		return result;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Trace an Exception in the error log.
	 * 
	 * @param e
	 *            Exception to log.
	 * @param blocker
	 *            <code>True</code> if the exception must be logged as error,
	 *            <code>False</code> to log it as a warning.
	 */
	public static void log(Exception e, boolean blocker) {
		if (e == null) {
			throw new NullPointerException(
					TuleapUIMessages
							.getString(TuleapUiMessagesKeys.logNullException));
		}

		if (getDefault() == null) {
			// We are out of eclipse. Prints the stack trace on standard error.
			// CHECKSTYLE:OFF
			e.printStackTrace();
			// CHECKSTYLE:ON
		} else if (e instanceof CoreException) {
			log(((CoreException) e).getStatus());
		} else if (e instanceof NullPointerException) {
			int severity = IStatus.WARNING;
			if (blocker) {
				severity = IStatus.ERROR;
			}
			log(new Status(severity, PLUGIN_ID, severity,
					TuleapUIMessages
							.getString(TuleapUiMessagesKeys.elementNotFound), e));
		} else {
			int severity = IStatus.WARNING;
			if (blocker) {
				severity = IStatus.ERROR;
			}
			log(new Status(severity, PLUGIN_ID, severity, e.getMessage(), e));
		}
	}

	/**
	 * Puts the given status in the error log view.
	 * 
	 * @param status
	 *            Error Status.
	 */
	public static void log(IStatus status) {
		// Eclipse platform displays NullPointer on standard error instead of
		// throwing it.
		// We'll handle this by throwing it ourselves.
		if (status == null) {
			throw new NullPointerException(
					TuleapUIMessages
							.getString(TuleapUiMessagesKeys.logNullStatus));
		}

		if (getDefault() != null) {
			getDefault().getLog().log(status);
		} else {
			// We are out of eclipse. Prints the message on standard error.
			// CHECKSTYLE:OFF
			System.err.println(status.getMessage());
			status.getException().printStackTrace();
			// CHECKSTYLE:ON
		}
	}

	/**
	 * Puts the given message in the error log view, as error or warning.
	 * 
	 * @param message
	 *            The message to put in the error log view.
	 * @param blocker
	 *            <code>True</code> if the message must be logged as error,
	 *            <code>False</code> to log it as a warning.
	 */
	public static void log(String message, boolean blocker) {
		if (getDefault() == null) {
			// We are out of eclipse. Prints the message on standard error.
			// CHECKSTYLE:OFF
			System.err.println(message);
			// CHECKSTYLE:ON
		} else {
			int severity = IStatus.WARNING;
			if (blocker) {
				severity = IStatus.ERROR;
			}
			String errorMessage = message;
			if (errorMessage == null || "".equals(errorMessage)) { //$NON-NLS-1$
				errorMessage = TuleapUIMessages
						.getString(TuleapUiMessagesKeys.unexpectedException);
			}
			log(new Status(severity, PLUGIN_ID, errorMessage));
		}
	}
}
