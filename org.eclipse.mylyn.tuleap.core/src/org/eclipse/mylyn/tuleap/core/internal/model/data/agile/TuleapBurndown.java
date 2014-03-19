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
package org.eclipse.mylyn.tuleap.core.internal.model.data.agile;

import java.io.Serializable;

import org.eclipse.core.runtime.Assert;

/**
 * A card in a cardwall.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBurndown implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 4612256708623941590L;

	/**
	 * The burndown duration.
	 */
	private int duration;

	/**
	 * The burndown capacity.
	 */
	private double capacity;

	/**
	 * The burndown points.
	 */
	private double[] points;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapBurndown() {
		// Default constructor for deserialization.
	}

	/**
	 * The constructor used to initialize attributes.
	 * 
	 * @param duration
	 *            the duration
	 * @param capacity
	 *            the capacity
	 * @param points
	 *            the points
	 */
	public TuleapBurndown(int duration, double capacity, double[] points) {
		this.duration = duration;
		this.capacity = capacity;
		Assert.isNotNull(points);
		this.points = points;
	}

	/**
	 * The duration getter.
	 * 
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * The duration setter.
	 * 
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * The capacity getter.
	 * 
	 * @return the capacity
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * The capacity setter.
	 * 
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	/**
	 * The points getter.
	 * 
	 * @return the points
	 */
	public double[] getPoints() {
		if (points != null) {
			return points.clone();
		}
		return new double[0];
	}

	/**
	 * The points setter.
	 * 
	 * @param points
	 *            the points to set
	 */
	public void setPoints(double[] points) {
		this.points = points;
	}

}
