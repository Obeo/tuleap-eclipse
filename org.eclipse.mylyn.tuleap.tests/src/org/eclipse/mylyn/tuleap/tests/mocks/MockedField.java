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
package org.eclipse.mylyn.tuleap.tests.mocks;

/**
 * Mock of field.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedField {
	/**
	 * Field ID.
	 */
	private int id;

	/**
	 * Field name.
	 */
	private String name;

	/**
	 * Field type.
	 */
	private String type;

	/**
	 * Field values.
	 */
	private String[] values;

	/**
	 * Constructor.
	 * 
	 * @param fieldId
	 *            Field ID
	 * @param fieldName
	 *            Field name
	 * @param fieldType
	 *            Field type
	 * @param fieldValues
	 *            Field values
	 */
	public MockedField(int fieldId, String fieldName, String fieldType, String... fieldValues) {
		this.id = fieldId;
		this.name = fieldName;
		this.type = fieldType;
		this.values = fieldValues;
	}

	/**
	 * Get the identifier.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public String[] getValues() {
		return values;
	}

}
