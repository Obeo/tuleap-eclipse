/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.core.internal.model.data;

/**
 * Abstract super-class of query criteria.
 *
 * @param <T>
 *            The type of the criterion.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapQueryCriterion<T> implements IQueryCriterion<T> {

	/**
	 * The operator.
	 */
	private String operator;

	/**
	 * The criterion value.
	 */
	private T value;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.model.data.IQueryCriterion#getOperator()
	 */
	@Override
	public String getOperator() {
		return operator;
	}

	/**
	 * Operator setter.
	 *
	 * @param operator
	 *            the operator to set.
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Value getter.
	 *
	 * @return the value
	 */
	@Override
	public T getValue() {
		return value;
	}

	/**
	 * Value setter.
	 *
	 * @param value
	 *            the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

}
