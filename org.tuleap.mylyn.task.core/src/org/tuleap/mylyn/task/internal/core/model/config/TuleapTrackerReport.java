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
package org.tuleap.mylyn.task.internal.core.model.config;

/**
 * This utility class represents a report available for a tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTrackerReport {

	/**
	 * The id of the report.
	 */
	private int id;

	/**
	 * The uri of the report.
	 */
	private String uri;

	/**
	 * The name of the report.
	 */
	private String label;

	/**
	 * The url of the report.
	 */
	private String reportUrl;

	/**
	 * The constructor.
	 * 
	 * @param reportId
	 *            The id of the report.
	 * @param reportLabel
	 *            The label of the report.
	 */
	public TuleapTrackerReport(int reportId, String reportLabel) {
		super();
		this.id = reportId;
		this.label = reportLabel;
	}

	/**
	 * Returns the id of the report.
	 * 
	 * @return The id of the report.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the name of the report.
	 * 
	 * @return The name of the report.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Returns the url of the report.
	 * 
	 * @return The url of the report.
	 */
	public String getReportUrl() {
		return this.reportUrl;
	}

	/**
	 * Returns the uri of the report.
	 * 
	 * @return The uri of the report.
	 */
	public String getReportUri() {
		return this.uri;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.label + " [" + this.id + "]"; //$NON-NLS-1$//$NON-NLS-2$
	}

}
