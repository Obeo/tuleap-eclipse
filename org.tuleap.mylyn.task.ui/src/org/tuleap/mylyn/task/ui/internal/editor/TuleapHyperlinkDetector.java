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
package org.tuleap.mylyn.task.ui.internal.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TaskHyperlink;

/**
 * The Tuleap hyperlink detector.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapHyperlinkDetector extends AbstractHyperlinkDetector {

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region,
			boolean canShowMultipleHyperlinks) {
		String text = textViewer.getDocument().get();
		List<IHyperlink> hyperlinks = new ArrayList<IHyperlink>();
		Matcher matcher = Pattern.compile("([^\\s]+ #\\d+)").matcher(text); //$NON-NLS-1$
		int offset = 0;
		while (matcher.find(offset)) {
			int start = matcher.start();
			int length = matcher.end() - start;
			offset = matcher.end();

			Region hyperlinkRegion = new Region(start, length);
			hyperlinks.add(new TaskHyperlink(hyperlinkRegion,
					(TaskRepository)getAdapter(TaskRepository.class), matcher.group(1)));
			if (!canShowMultipleHyperlinks) {
				break;
			}
		}
		if (hyperlinks.isEmpty()) {
			return null;
		}
		return hyperlinks.toArray(new IHyperlink[hyperlinks.size()]);
	}

}
