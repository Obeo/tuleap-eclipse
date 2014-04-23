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
package org.eclipse.mylyn.tuleap.core.internal.model.config.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.ArtifactLinkFieldValue;

/**
 * The Tuleap artifact link field.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifactLink extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -8648649700121180143L;

	/**
	 * The constructor.
	 *
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapArtifactLink(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Creates and returns a new TaskAttribute using this field. This method is meant to be overridden in the
	 * subclasses to provide the relevant behavior depending on the type of tuleap field.
	 *
	 * @param parent
	 *            The parent task attribute in which the new task attribute must be created.
	 * @return The created task attribute.
	 */
	@Override
	public TaskAttribute createTaskAttribute(TaskAttribute parent) {
		int id = getIdentifier();
		TaskAttribute attribute = this.getWriteableAttribute(parent, String.valueOf(id), getMetadataType());
		if (attribute != null) {
			initializeMetaData(attribute.getMetaData());
		}

		return attribute;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_TASK_DEPENDENCY;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#createFieldValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      int)
	 */
	@Override
	public AbstractFieldValue createFieldValue(TaskAttribute attribute, int fieldId) {
		// For compatibility, we continue to assume that artifact links can either
		// have one value which is a list of comma-separated IDs,
		// or a list of values.
		// Because older versions of the connector created a list of values for such fields,
		// even though the mylyn editor sets one value with a comma-separated list...
		// Request-6750
		List<String> taskAttValues = attribute.getValues();
		if (taskAttValues.size() == 1 && taskAttValues.get(0).contains(",")) { //$NON-NLS-1$
			taskAttValues = Arrays.asList(taskAttValues.get(0).split(",")); //$NON-NLS-1$
		}
		int[] values = new int[taskAttValues.size()];
		int i = 0;
		for (String attValue : taskAttValues) {
			int v = extractArtifactIdFromTaskLinkId(attValue.trim());
			if (v != -1) {
				values[i] = v;
			}
			i++;
		}
		return new ArtifactLinkFieldValue(fieldId, values);
	}

	/**
	 * Extracts the integer id from the string representation of this ID, which can be complex in certain
	 * cases.
	 *
	 * @param attValue
	 *            One of the String values contained by the "artifact link" field.
	 * @return The integer value to use, or -1 if the value cannot be extracted.
	 */
	private int extractArtifactIdFromTaskLinkId(String attValue) {
		int v = -1;
		try {
			v = Integer.parseInt(attValue);
		} catch (NumberFormatException e) {
			// FIXME SBE Support full task key in the task dependency fields!!!!!!!
			// /!\HACKISH/!\ We may have, as the id of the task, an identifier (ie: 917)
			// or a complex identifier (ie: MyRepository:MyProject[116] #917 - My Task
			// Name). We will try to parse the value as an integer, if it fails, then we
			// know that we have a complex identifier, in that case, we will parse the
			// identifier from this complex identifier and use it.
			Pattern pattern = Pattern.compile("#(\\d+)"); //$NON-NLS-1$
			Matcher matcher = pattern.matcher(attValue);
			if (matcher.find()) {
				v = Integer.parseInt(matcher.group(1));
			}
		}
		return v;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof ArtifactLinkFieldValue);
		ArtifactLinkFieldValue alValue = (ArtifactLinkFieldValue)value;
		List<String> values = new ArrayList<String>();
		for (int val : alValue.getLinks()) {
			values.add(Integer.toString(val));
		}
		attribute.setValues(values);
	}

	@Override
	public void accept(ITuleapFieldVisitor visitor) {
		visitor.visit(this);
	}
}
