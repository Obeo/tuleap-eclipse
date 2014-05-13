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
 * QueryCriterion interface. A map of such criteria indexed by the associated field ID will be sent to the
 * Tuleap server to perform queries.
 *
 * @param <T>
 *            The type of the criterion value.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IQueryCriterion<T> {

	/**
	 * The constant for the operator "between".
	 */
	String OP_BETWEEN = "between"; //$NON-NLS-1$

	/**
	 * The constant for the operator ">" (greater than).
	 */
	String OP_GT = ">"; //$NON-NLS-1$

/**
	 * The constant for the operator "<" (less than).
	 */
	String OP_LT = "<"; //$NON-NLS-1$

	/**
	 * The constant for the operator "=" (equal).
	 */
	String OP_EQ = "="; //$NON-NLS-1$

	/**
	 * The constant for the operator "contains".
	 */
	String OP_CONTAINS = "contains"; //$NON-NLS-1$

	/**
	 * Operator getter.
	 *
	 * @return the operator
	 */
	String getOperator();

	/**
	 * Value getter.
	 *
	 * @return the value
	 */
	T getValue();

}
