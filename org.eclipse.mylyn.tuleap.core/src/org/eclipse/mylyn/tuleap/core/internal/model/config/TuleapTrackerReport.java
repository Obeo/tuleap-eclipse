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
package org.eclipse.mylyn.tuleap.core.internal.model.config;

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
	 * References to the tracker report resources.
	 */
	private TuleapResource[] resources;

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

	/**
	 * Tracker report resources getter.
	 * 
	 * @return the reportResources, a list that is never <code>null</code> but possibly empty.
	 */
	public TuleapResource[] getReportResources() {
		return resources;
	}

	/**
	 * Tracker report resources setter.
	 * 
	 * @param reportResources
	 *            the reportResources to set
	 */
	public void setReportResources(TuleapResource[] reportResources) {
		this.resources = reportResources;
	}

	/**
	 * Indicates whether the given resource exists on this Tracker report.
	 * 
	 * @param key
	 *            The resource type being looked for
	 * @return {@code true} if and only if the given service is present in the list of services of this
	 *         Tracker report.
	 */
	public boolean hasResource(String key) {
		if (resources != null) {
			for (TuleapResource resource : resources) {
				if (resource.getType().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}

}
