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
package org.tuleap.mylyn.task.internal.core.data;

import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifactComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapAttachment;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapArtifactLink;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapText;
import org.tuleap.mylyn.task.internal.core.model.field.dynamic.TuleapComputedValue;

/**
 * The Tuleap Task Mapper will be used to manipulate the task data model from Mylyn with a higher level of
 * abstraction.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapper extends AbstractTaskMapper {

	/**
	 * The identifier of an invalid tracker.
	 */
	public static final int INVALID_TRACKER_ID = -1;

	/**
	 * The identifier of an invalid project.
	 */
	public static final int INVALID_PROJECT_ID = -1;

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 * @param createNonExistingAttributes
	 *            <code>true</code> to allow the creation of non existing attributes, <code>false</code>
	 *            otherwise.
	 */
	public TuleapTaskMapper(TaskData taskData, boolean createNonExistingAttributes) {
		super(taskData, createNonExistingAttributes);
	}

	/**
	 * Initialize an empty task data from the given Tuleap tracker configuration.
	 * 
	 * @param tuleapTrackerConfiguration
	 *            The configuration of the tuleap tracker.
	 */
	public void initializeEmptyTaskData(TuleapTrackerConfiguration tuleapTrackerConfiguration) {
		// call all the other private method (createXXXX)
		// keep an eye on the permissions -> read only in the metadata
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the creation date.
	 */
	private void createCreationDateTaskAttribute() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the last update date.
	 */
	private void createLastUpdateDateTaskAttribute() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the author of the task.
	 */
	private void createSubmittedByTaskAttribute() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the completion date.
	 */
	private void createCompletionDateTaskAttribute() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the new comment.
	 */
	private void createNewCommentTaskAttribute() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the task kind.
	 */
	private void createTaskKindTaskAttribute() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the summary.
	 */
	private void createSummaryTaskAttribute() {
		// String
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the status.
	 */
	private void createStatusTaskAttribute() {
		// SB and MSB + workflow
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		// status section etc + a mylyn operation task attribute to modify the real status field
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the person to which the task is assigned.
	 */
	private void createAssignedToTaskAttribute() {
		// SB and MSB + workflow
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		// people section etc
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the initial effort of the task.
	 */
	private void createInitialEffort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the cross references of the task.
	 */
	private void createCrossReferencesTaskAttribute() {
		// read only!
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the string field.
	 * 
	 * @param tuleapString
	 *            The string field
	 */
	private void createStringTaskAttribute(TuleapString tuleapString) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the text field.
	 * 
	 * @param tuleapText
	 *            The text field
	 */
	private void createTextTaskAttribute(TuleapText tuleapText) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the selectbox field.
	 * 
	 * @param tuleapSelectBox
	 *            The selectbox field
	 */
	private void createSelectBoxTaskAttribute(TuleapSelectBox tuleapSelectBox) {
		// workflow -> filter options
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the multi selectbox field.
	 * 
	 * @param tuleapMultiSelectBox
	 *            The multi selectbox field
	 */
	private void createMultiSelectBoxTaskAttribute(TuleapMultiSelectBox tuleapMultiSelectBox) {
		// workflow -> filter options
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the checkbox field.
	 * 
	 * @param tuleapMultiSelectBox
	 *            The checkbox field
	 */
	private void createCheckBoxTaskAttribute(TuleapMultiSelectBox tuleapMultiSelectBox) {
		// Call createMultiSelectBoxTaskAttribute
		// workflow -> filter options
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the date field.
	 * 
	 * @param tuleapDate
	 *            The date field
	 */
	private void createDateTaskAttribute(TuleapDate tuleapDate) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the file field.
	 * 
	 * @param tuleapFile
	 *            The file field
	 */
	private void createFileUploadTaskAttribute(TuleapFileUpload tuleapFile) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the integer field.
	 * 
	 * @param tuleapInteger
	 *            The integer field
	 */
	private void createIntTaskAttribute(TuleapInteger tuleapInteger) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the float field.
	 * 
	 * @param tuleapFloat
	 *            The float field
	 */
	private void createFloatTaskAttribute(TuleapFloat tuleapFloat) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the open list field.
	 * 
	 * @param tuleapOpenList
	 *            The open list field
	 */
	private void createOpenListTaskAttribute(TuleapOpenList tuleapOpenList) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the artifact link field.
	 * 
	 * @param tuleapArtifactLink
	 *            The artifact link field
	 */
	private void createArtifactLinkTaskAttribute(TuleapArtifactLink tuleapArtifactLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates the task attribute representing the computed value.
	 * 
	 * @param tuleapComputedValue
	 *            The computed value
	 */
	private void createComputedValueTaskAttribute(TuleapComputedValue tuleapComputedValue) {
		// computed value -> read only
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the identifier of the tracker.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 */
	public void setTrackerId(int trackerId) {
		// should not appear in the attribute part so no task attribute type!
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the identifier of the tracker or INVALID_TRACKER otherwise.
	 * 
	 * @return The identifier of the tracker or INVALID_TRACKER otherwise.
	 */
	public int getTrackerId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the identifier of the project.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 */
	public void setProjectId(int projectId) {
		// should not appear in the attribute part so no task attribute type!
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the identifier of the project.
	 * 
	 * @return The identifier of the project
	 */
	public int getProjectId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the identifier of the artifact.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 */
	public void setArtifactId(int artifactId) {
		// task attribute taskkey
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the identifier of the artifact.
	 * 
	 * @return The identifier of the artifact
	 */
	public int getArtifactId() {
		// task attribute taskkey
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the url of the task.
	 * 
	 * @param url
	 *            the url of the task
	 */
	public void setUrl(String url) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the summary of the task.
	 * 
	 * @param value
	 *            The value of the summary
	 * @param fieldId
	 *            The identifier of the field used for the summary
	 */
	public void setSummary(String value, int fieldId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the status of the task.
	 * 
	 * @param valuesId
	 *            The identifier of the field values selected
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setStatus(Set<Integer> valuesId, int fieldId) {
		// if the value id is among the values id of the closed status, set the completion date to the last
		// modification date and ensure that the attributes cannot be edited again, else remove any existing
		// completion date (the task is re-opened)
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the values representing who this task is assigned to.
	 * 
	 * @param valuesId
	 *            The identifier of the field values selected
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setAssignedTo(Set<Integer> valuesId, int fieldId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the initial effort of the task.
	 * 
	 * @param initialEffort
	 *            The initial effort
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setInitialEffort(int initialEffort, int fieldId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the value of the literal field with the given field identifier.
	 * 
	 * @param value
	 *            The value of the field
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setValue(String value, int fieldId) {
		// string, text(, int, float, computed field, open list, artifact links?) or individual method for
		// each setTextValue, setIntValue, setFloatValue, setComputedFieldValue, setOpenListValue, etc...
		// They will all have exactly the same behavior...
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the value of the date field with the given field identifier.
	 * 
	 * @param value
	 *            The timestamp representing the date
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setDateValue(int value, int fieldId) {
		// int <-> long
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the value of the select box field with the given field identifier.
	 * 
	 * @param valueId
	 *            The identifier of the selected value
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setSelectBoxValue(int valueId, int fieldId) {
		// ITuleapConstants -> 100 nothing selected
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the value of the multi select box field with the given field identifier.
	 * 
	 * @param valuesId
	 *            The identifier of the values of the select box selected
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setMultiSelectBoxValues(Set<Integer> valuesId, int fieldId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the value of the check box field with the given field identifier.
	 * 
	 * @param valuesId
	 *            The identifier of the values of the check box selected
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setCheckboxValues(Set<Integer> valuesId, int fieldId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Adds a comment to the task.
	 * 
	 * @param tuleapArtifactComment
	 *            The comment to add
	 */
	public void addComment(TuleapArtifactComment tuleapArtifactComment) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Adds an attachment to the task.
	 * 
	 * @param tuleapAttachment
	 *            The attachment to add
	 */
	public void addAttachement(TuleapAttachment tuleapAttachment) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the set of the fields value.
	 * 
	 * @return The set of the fields value
	 */
	public Set<AbstractTuleapFieldValue> getFieldsValue() {
		// returns all the tuleap field value in order to send them to the server
		// attachments are not uploaded with the same mechanism so no need to return them here
		// do not return the fields computed by tuleap or mylyn: creation date, completion date, id, etc
		throw new UnsupportedOperationException();
	}

}
