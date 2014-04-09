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
package org.eclipse.mylyn.tuleap.core.internal.model.config.field;

import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;

/**
 * Abstract super-class for {@link ITuleapFieldVisitor} implementations. Provides relevant default
 * implementation for {@link AbstractTuleapField} and {@link AbstractTuleapSelectBox}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTuleapFieldVisitor implements ITuleapFieldVisitor {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.field.ITuleapFieldVisitor#visit(org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField)
	 */
	public void visit(AbstractTuleapField field) {
		field.accept(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.field.ITuleapFieldVisitor#visit(org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox)
	 */
	public void visit(AbstractTuleapSelectBox field) {
		field.accept(this);
	}

}
