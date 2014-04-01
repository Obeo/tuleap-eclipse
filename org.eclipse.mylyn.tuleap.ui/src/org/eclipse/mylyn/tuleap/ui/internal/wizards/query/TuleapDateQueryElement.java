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
package org.eclipse.mylyn.tuleap.ui.internal.wizards.query;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.workbench.forms.DatePicker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapQueryCriterion;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUiMessagesKeys;
import org.eclipse.swt.widgets.Combo;

/**
 * The elements used to store the query.
 *
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapDateQueryElement extends AbstractTuleapCustomQueryElement<TuleapDate> {

	/**
	 * Graphical query form element operation.
	 */
	protected final Combo operator;

	/**
	 * The widget for minimum or default date.
	 */
	protected DatePicker datePickerMin;

	/**
	 * The widget for maximum date if <code>operator == "between"</code>.
	 */
	protected DatePicker datePickerMax;

	/**
	 * The constructor.
	 *
	 * @param field
	 *            The field, must not be null.
	 * @param operator
	 *            The operation, must not be null.
	 * @param from
	 *            The graphical element for minimum date, must not be null.
	 * @param to
	 *            The graphical element for maximum date, must not be null.
	 */
	public TuleapDateQueryElement(TuleapDate field, Combo operator, DatePicker from, DatePicker to) {
		super(field);
		Assert.isNotNull(operator);
		Assert.isNotNull(from);
		Assert.isNotNull(to);
		this.operator = operator;
		this.datePickerMin = from;
		this.datePickerMax = to;
		createDecorator(from.getParent(), ""); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#getCriterion()
	 */
	@Override
	public IQueryCriterion<Date[]> getCriterion() throws CoreException {
		TuleapQueryCriterion<Date[]> result = null;
		Calendar min = datePickerMin.getDate();
		if (min != null) {
			result = new TuleapQueryCriterion<Date[]>();
			if (TuleapQueryCriterion.OP_BETWEEN.equals(getOperator())) {
				Calendar max = datePickerMax.getDate();
				if (max != null) {
					result.setOperator(IQueryCriterion.OP_BETWEEN);
					result.setValue(new Date[] {min.getTime(), max.getTime() });
				} else {
					throw new CoreException(new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID,
							TuleapUIMessages
							.getString(TuleapUiMessagesKeys.tuleapQueryDatesMandatoryForBetween)));
				}
			} else {
				result.setOperator(getOperator());
				result.setValue(new Date[] {min.getTime() });
			}
		}
		return result;
	}

	/**
	 * Get the query operation for the current field.
	 *
	 * @return operation
	 */
	@Override
	public String getOperator() {
		return operator.getText();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#validate()
	 */
	@Override
	public boolean validate() {
		decorator.hide();
		decorator.setDescriptionText(""); //$NON-NLS-1$
		if (IQueryCriterion.OP_BETWEEN.equals(getOperator())
				&& datePickerMin.getDate() == null != (datePickerMax.getDate() == null)) {
			decorator.setDescriptionText(TuleapUIMessages
					.getString(TuleapUiMessagesKeys.tuleapQueryDatesMandatoryForBetween));
			decorator.show();
			return false;
		}
		return true;
	}
}
