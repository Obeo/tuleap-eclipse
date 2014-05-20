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
package org.eclipse.mylyn.internal.tuleap.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator in charge of managing the lifecycle of thie bundle.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
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

	public static TuleapTasksUIPlugin getDefault() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
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
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
