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
package org.tuleap.mylyn.task.core.internal.repository;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

/**
 * The Tuleap task attribute mapper. This class is in charge of computing and returning the property of each
 * attribute of the tasks.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapAttributeMapper extends TaskAttributeMapper {
	/**
	 * The constructor.
	 *
	 * @param taskRepository
	 *            The Mylyn task repository.
	 * @param repositoryConnector
	 *            The Tuleap repository connector.
	 */
	public TuleapAttributeMapper(TaskRepository taskRepository, ITuleapRepositoryConnector repositoryConnector) {
		super(taskRepository);
	}

	/**
	 * Overridden to manage TASK LINK attributes, in order to have only a list of ints separated by spaces in
	 * the TaskAttribute values, evenf if Rich Text Editor completion has added task keys (like
	 * "release #123"). {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      java.lang.String)
	 */
	@Override
	public void setValue(TaskAttribute attribute, String value) {
		if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(attribute.getMetaData().getType())) {
			setTaskDependencyValue(attribute, value);
		} else {
			super.setValue(attribute, value);
		}
	}

	/**
	 * Sets the value of a task attribute if it's a task dependency.
	 *
	 * @param attribute
	 *            The attribute
	 * @param value
	 *            The value to set
	 */
	private void setTaskDependencyValue(TaskAttribute attribute, String value) {
		String[] strValues = value.split(","); //$NON-NLS-1$
		List<String> valuesToSet = new ArrayList<String>();
		for (String str : strValues) {
			String val = str.trim();
			if (!val.isEmpty()) {
				// The following code also works for serialized TuleapTaskIds
				Pattern patt = Pattern.compile("#(\\d+)"); //$NON-NLS-1$
				Matcher matcher = patt.matcher(val);
				if (matcher.find()) {
					valuesToSet.add(matcher.group(1));
				} else {
					patt = Pattern.compile("\\d+"); //$NON-NLS-1$
					matcher = patt.matcher(val);
					if (matcher.matches()) {
						valuesToSet.add(val);
					}
				}
			}
		}
		attribute.setValue(Joiner.on(",").join(valuesToSet));
	}
}
