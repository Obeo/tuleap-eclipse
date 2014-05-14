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
package org.tuleap.mylyn.task.core.internal.model.config.field;

import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;

/**
 * Visitor of {@link org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface ITuleapFieldVisitor {

	/**
	 * Visit a field. Implementation should be:<br/>
	 * <code>
	 * public void visit(AbstractTuleapField field) {<br/>
	 * &nbsp;&nbsp;field.accept(this);<br/>
	 * }
	 * </code>
	 *
	 * @param field
	 *            The field to visit.
	 */
	void visit(AbstractTuleapField field);

	/**
	 * Visit a select box or multi-select box.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(AbstractTuleapSelectBox field);

	/**
	 * Visit an artifact link.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapArtifactLink field);

	/**
	 * Visit a computed value field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapComputedValue field);

	/**
	 * Visit a date field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapDate field);

	/**
	 * Visit a file upload field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapFileUpload field);

	/**
	 * Visit a float field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapFloat field);

	/**
	 * Visit a integer field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapInteger field);

	/**
	 * Visit a multi-select box.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapMultiSelectBox field);

	/**
	 * Visit an open list field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapOpenList field);

	/**
	 * Visit a select box field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapSelectBox field);

	/**
	 * Visit a string field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapString field);

	/**
	 * Visit a text field.
	 *
	 * @param field
	 *            Field to visit.
	 */
	void visit(TuleapText field);
}
