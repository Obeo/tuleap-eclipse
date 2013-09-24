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
package org.tuleap.mylyn.task.internal.core.client.soap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.model.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.model.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.MultiSelectFieldValue;
import org.tuleap.mylyn.task.internal.core.model.SingleSelectFieldValue;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValueFileInfo;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerFieldBindValue;

/**
 * Utility class used to transform the the data structure used by the SOAP API in {@link TuleapArtifact}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapSoapParser {

	/**
	 * Parse the SOAP data and return a {@link TuleapArtifact} representing the artifact.
	 * 
	 * @param tuleapTrackerConfiguration
	 *            The configuration of the Tuleap tracker
	 * @param artifactToParse
	 *            The SOAP data to parse
	 * @return The TuleapArtifact representing the artifact
	 */
	public TuleapArtifact parseArtifact(TuleapTrackerConfiguration tuleapTrackerConfiguration,
			Artifact artifactToParse) {
		int artifactId = artifactToParse.getArtifact_id();
		int trackerId = artifactToParse.getTracker_id();
		String label = null;
		String url = null;
		String htmlUrl = null;

		int submittedOn = artifactToParse.getSubmitted_on();
		Date creationDate = this.getDateFromTimestamp(submittedOn);

		int lastUpdateDate = artifactToParse.getLast_update_date();
		Date lastModificationDate = this.getDateFromTimestamp(lastUpdateDate);

		TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, label, url, htmlUrl,
				creationDate, lastModificationDate);

		ArtifactFieldValue[] value = artifactToParse.getValue();
		for (ArtifactFieldValue artifactFieldValue : value) {
			Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (artifactFieldValue.getField_name().equals(abstractTuleapField.getName())) {
					AbstractFieldValue abstractFieldValue = null;
					if (abstractTuleapField instanceof TuleapSelectBox) {
						// Select box?
						int bindValueId = -1;

						TrackerFieldBindValue[] bindValue = artifactFieldValue.getField_value()
								.getBind_value();
						if (bindValue.length > 0) {
							bindValueId = bindValue[0].getBind_value_id();
						}

						abstractFieldValue = new SingleSelectFieldValue(abstractTuleapField.getIdentifier(),
								bindValueId);
					} else if (abstractTuleapField instanceof TuleapMultiSelectBox) {
						// Multi-select box?
						List<Integer> bindValueIds = new ArrayList<Integer>();

						TrackerFieldBindValue[] bindValue = artifactFieldValue.getField_value()
								.getBind_value();
						for (TrackerFieldBindValue trackerFieldBindValue : bindValue) {
							bindValueIds.add(Integer.valueOf(trackerFieldBindValue.getBind_value_id()));
						}

						abstractFieldValue = new MultiSelectFieldValue(abstractTuleapField.getIdentifier(),
								bindValueIds);
					} else if (abstractTuleapField instanceof TuleapFileUpload) {
						// File attachment?
						List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();

						FieldValueFileInfo[] fileInfo = artifactFieldValue.getField_value().getFile_info();
						for (FieldValueFileInfo fieldValueFileInfo : fileInfo) {
							attachments.add(new AttachmentValue(fieldValueFileInfo.getId(),
									fieldValueFileInfo.getFilename(), fieldValueFileInfo.getSubmitted_by(),
									fieldValueFileInfo.getFilesize(), fieldValueFileInfo.getDescription(),
									fieldValueFileInfo.getFiletype()));
						}

						abstractFieldValue = new AttachmentFieldValue(abstractTuleapField.getIdentifier(),
								attachments);
					} else {
						// Literal
						abstractFieldValue = new LiteralFieldValue(abstractTuleapField.getIdentifier(),
								artifactFieldValue.getField_value().getValue());
					}
					tuleapArtifact.addFieldValue(abstractFieldValue);
				}
			}
		}

		return tuleapArtifact;
	}

	/**
	 * Creates the date from a timestamp.
	 * 
	 * @param timestamp
	 *            The timestamp
	 * @return The date created from the timestamp
	 */
	private Date getDateFromTimestamp(int timestamp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(timestamp).longValue() * 1000);
			return calendar.getTime();
		} catch (NumberFormatException e) {
			// Empty date
		}
		return null;
	}
}
