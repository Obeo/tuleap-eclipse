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
package org.eclipse.mylyn.tuleap.core.tests.internal.client.soap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.mylyn.tuleap.core.internal.client.soap.CommentedArtifact;
import org.eclipse.mylyn.tuleap.core.internal.client.soap.TuleapSoapParser;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapArtifactLink;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapOpenList;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapText;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.Artifact;
import org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.ArtifactFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.FieldValue;
import org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.FieldValueFileInfo;
import org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerFieldBindValue;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * This class is used for the unit tests of the Tuleap SOAP parser class. The goal is to ensure that the
 * {@link TuleapArtifact} created by the {@link TuleapSoapParser} contains all the necessary information.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapSoapParserTests {
	/**
	 * The URL of the repository.
	 */
	private String repositoryUrl = "https://tuleap.net"; //$NON-NLS-1$

	/**
	 * The name of the project.
	 */
	private String projectName = "Project"; //$NON-NLS-1$

	/**
	 * The identifier of the project.
	 */
	private int projectId = 1;

	/**
	 * The configuration of the server.
	 */
	private TuleapServer tuleapServer;

	/**
	 * Prepare the configuration of the server.
	 */
	@Before
	public void setUp() {
		this.tuleapServer = new TuleapServer(this.repositoryUrl);

		TuleapProject tuleapProject = new TuleapProject(projectName, projectId);

		// The first tracker does not have a single field.
		TuleapTracker firstTrackerConfiguration = new TuleapTracker(0, null, null, null, null, System
				.currentTimeMillis());

		// The second tracker has one string field
		TuleapTracker secondTrackerConfiguration = new TuleapTracker(1, null, null, null, null, System
				.currentTimeMillis());
		TuleapString tuleapString = new TuleapString(12);
		tuleapString.setName("string field name"); //$NON-NLS-1$
		secondTrackerConfiguration.addField(tuleapString);

		// The third tracker has one text field
		TuleapTracker thirdTrackerConfiguration = new TuleapTracker(2, null, null, null, null, System
				.currentTimeMillis());
		TuleapText tuleapText = new TuleapText(12);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		thirdTrackerConfiguration.addField(tuleapText);

		// The fourth tracker has one integer field
		TuleapTracker fourthTrackerConfiguration = new TuleapTracker(3, null, null, null, null, System
				.currentTimeMillis());
		TuleapInteger tuleapInteger = new TuleapInteger(12);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		fourthTrackerConfiguration.addField(tuleapInteger);

		// The fifth tracker has one float field
		TuleapTracker fifthTrackerConfiguration = new TuleapTracker(4, null, null, null, null, System
				.currentTimeMillis());
		TuleapFloat tuleapFloat = new TuleapFloat(12);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		fifthTrackerConfiguration.addField(tuleapFloat);

		// The sixth tracker has one date field
		TuleapTracker sixthTrackerConfiguration = new TuleapTracker(5, null, null, null, null, System
				.currentTimeMillis());
		TuleapDate tuleapDate = new TuleapDate(12);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		sixthTrackerConfiguration.addField(tuleapDate);

		// The seventh tracker has one open list field
		TuleapTracker seventhTrackerConfiguration = new TuleapTracker(6, null, null, null, null, System
				.currentTimeMillis());
		TuleapOpenList tuleapOpenList = new TuleapOpenList(12);
		tuleapOpenList.setName("open list field name"); //$NON-NLS-1$
		seventhTrackerConfiguration.addField(tuleapOpenList);

		// The eighth tracker has one open list field
		TuleapTracker eighthTrackerConfiguration = new TuleapTracker(7, null, null, null, null, System
				.currentTimeMillis());
		TuleapArtifactLink tuleapArtifactLink = new TuleapArtifactLink(12);
		tuleapArtifactLink.setName("artifact links field name"); //$NON-NLS-1$
		eighthTrackerConfiguration.addField(tuleapArtifactLink);

		// The ninth tracker has one select box
		TuleapTracker ninthTrackerConfiguration = new TuleapTracker(8, null, null, null, null, System
				.currentTimeMillis());
		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(12);
		tuleapSelectBox.setName("tuleap select box field name"); //$NON-NLS-1$
		ninthTrackerConfiguration.addField(tuleapSelectBox);

		// The tenth tracker has one multi-select box
		TuleapTracker tenthTrackerConfiguration = new TuleapTracker(9, null, null, null, null, System
				.currentTimeMillis());
		TuleapMultiSelectBox tuleapMultiSelectBox = new TuleapMultiSelectBox(12);
		tuleapMultiSelectBox.setName("tuleap multi-select box field name"); //$NON-NLS-1$
		tenthTrackerConfiguration.addField(tuleapMultiSelectBox);

		// The eleventh tracker has one file attachment
		TuleapTracker eleventhTrackerConfiguration = new TuleapTracker(10, null, null, null, null, System
				.currentTimeMillis());
		TuleapFileUpload tuleapFileUpload = new TuleapFileUpload(12);
		tuleapFileUpload.setName("tuleap file upload field name"); //$NON-NLS-1$
		eleventhTrackerConfiguration.addField(tuleapFileUpload);

		tuleapProject.addTracker(firstTrackerConfiguration);
		tuleapProject.addTracker(secondTrackerConfiguration);
		tuleapProject.addTracker(thirdTrackerConfiguration);
		tuleapProject.addTracker(fourthTrackerConfiguration);
		tuleapProject.addTracker(fifthTrackerConfiguration);
		tuleapProject.addTracker(sixthTrackerConfiguration);
		tuleapProject.addTracker(seventhTrackerConfiguration);
		tuleapProject.addTracker(eighthTrackerConfiguration);
		tuleapProject.addTracker(ninthTrackerConfiguration);
		tuleapProject.addTracker(tenthTrackerConfiguration);
		tuleapProject.addTracker(eleventhTrackerConfiguration);

		this.tuleapServer.addProject(tuleapProject);
	}

	/**
	 * Utility method used to create a commented artifact with the given properties.
	 * 
	 * @param artifactId
	 *            The artifact id
	 * @param trackerId
	 *            The tracker id
	 * @param userId
	 *            The user id
	 * @param creationDate
	 *            The creation date
	 * @param lastUpdateDate
	 *            The last update date
	 * @param comments
	 *            The comments
	 * @return The commented artifact
	 */
	private CommentedArtifact createCommentedArtifact(int artifactId, int trackerId, int userId,
			Date creationDate, Date lastUpdateDate, List<TuleapElementComment> comments) {
		Artifact artifact = new Artifact();
		artifact.setArtifact_id(artifactId);
		artifact.setTracker_id(trackerId);
		artifact.setSubmitted_by(userId);
		artifact.setSubmitted_on(Long.valueOf(creationDate.getTime() / 1000).intValue());
		artifact.setLast_update_date(Long.valueOf(lastUpdateDate.getTime() / 1000).intValue());
		artifact.setValue(new ArtifactFieldValue[0]);

		return new CommentedArtifact(artifact, comments);
	}

	/**
	 * This test will try to parse an artifact without any field. The goal is to check that the retrieval of
	 * the following properties:
	 * <ul>
	 * <li>artifact id</li>
	 * <li>tracker id</li>
	 * <li>label</li>
	 * <li>url</li>
	 * <li>html url</li>
	 * <li>creation date</li>
	 * <li>last modification date</li>
	 * <li>comments</li>
	 * </ul>
	 * This unit test will use the configuration of the tracker with the identifier 0.
	 */
	@Test
	public void testParseArtifactWithoutFields() {
		int artifactId = 456;
		int trackerId = 0;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

		// The first comment
		String firstCommentBody = "This is the first comment"; //$NON-NLS-1$
		TuleapUser firstCommentSubmitter = new TuleapUser("first-username", "first-realname", 17, //$NON-NLS-1$ //$NON-NLS-2$
				"first-email", null); //$NON-NLS-1$
		Date firstCommentSubmitDate = new Date();

		TuleapElementComment firstTuleapElementComment = new TuleapElementComment(firstCommentBody,
				firstCommentSubmitter, firstCommentSubmitDate);
		comments.add(firstTuleapElementComment);

		// The second comment
		String secondCommentBody = "This is the second comment"; //$NON-NLS-1$
		TuleapUser secondCommentSubmitter = new TuleapUser("second-username", "second-realname", 18, //$NON-NLS-1$ //$NON-NLS-2$
				"second-email", null); //$NON-NLS-1$
		Date secondCommentSubmitDate = new Date();

		TuleapElementComment secondTuleapElementComment = new TuleapElementComment(secondCommentBody,
				secondCommentSubmitter, secondCommentSubmitDate);
		comments.add(secondTuleapElementComment);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, comments);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getTracker().getId(), is(trackerId));
		assertThat(tuleapArtifact.getSubmittedOn().toString(), is(creationDate.toString()));
		assertThat(tuleapArtifact.getLastModifiedDate().toString(), is(lastUpdateDate.toString()));

		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectId, trackerId, artifactId);
		String htmlUrl = taskId.getTaskUrl(repositoryUrl);
		assertThat(tuleapArtifact.getHtmlUrl(), is(htmlUrl));

		assertThat(tuleapArtifact.getUri(), is(nullValue()));
		assertThat(tuleapArtifact.getFieldValues().size(), is(0));
		assertThat(tuleapArtifact.getLabel(), is(nullValue()));

		assertThat(tuleapArtifact.getComments().size(), is(comments.size()));
		assertThat(tuleapArtifact.getComments().get(0).getBody(), is(firstCommentBody));
		assertThat(tuleapArtifact.getComments().get(0).getSubmittedOn(), is(firstCommentSubmitDate));
		assertThat(tuleapArtifact.getComments().get(0).getSubmitter().getId(), is(firstCommentSubmitter
				.getId()));
		assertThat(tuleapArtifact.getComments().get(0).getSubmitter().getEmail(), is(firstCommentSubmitter
				.getEmail()));
		assertThat(tuleapArtifact.getComments().get(0).getSubmitter().getRealName(), is(firstCommentSubmitter
				.getRealName()));
		assertThat(tuleapArtifact.getComments().get(0).getSubmitter().getUserName(), is(firstCommentSubmitter
				.getUserName()));

		assertThat(tuleapArtifact.getComments().get(1).getBody(), is(secondCommentBody));
		assertThat(tuleapArtifact.getComments().get(1).getSubmittedOn(), is(secondCommentSubmitDate));
		assertThat(tuleapArtifact.getComments().get(1).getSubmitter().getId(), is(secondCommentSubmitter
				.getId()));
		assertThat(tuleapArtifact.getComments().get(1).getSubmitter().getEmail(), is(secondCommentSubmitter
				.getEmail()));
		assertThat(tuleapArtifact.getComments().get(1).getSubmitter().getRealName(),
				is(secondCommentSubmitter.getRealName()));
		assertThat(tuleapArtifact.getComments().get(1).getSubmitter().getUserName(),
				is(secondCommentSubmitter.getUserName()));
	}

	/**
	 * Creates a literal field value for the artifact.
	 * 
	 * @param field
	 *            The field from the configuration that we will "instanciate"
	 * @param fieldValue
	 *            The value that we will store in the artifact
	 * @return The literal field value created
	 */
	private ArtifactFieldValue createLiteralFieldValue(AbstractTuleapField field, String fieldValue) {
		ArtifactFieldValue artifactFieldValue = new ArtifactFieldValue();
		String fieldName = field.getName();
		FieldValue fValue = new FieldValue();
		fValue.setValue(fieldValue);

		artifactFieldValue.setField_name(fieldName);
		artifactFieldValue.setField_value(fValue);
		return artifactFieldValue;
	}

	/**
	 * Test a literal field value.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 * @param field
	 *            The field from the configuration
	 * @param fieldValue
	 *            The value expected
	 * @param tuleapArtifact
	 *            The tuleap artifact containing the field value
	 */
	private void testLiteralFieldValue(int artifactId, AbstractTuleapField field, String fieldValue,
			TuleapArtifact tuleapArtifact) {
		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getFieldValues().size(), is(1));

		Collection<AbstractFieldValue> fieldValues = tuleapArtifact.getFieldValues();
		AbstractFieldValue abstractFieldValue = fieldValues.iterator().next();

		assertThat(abstractFieldValue, is(notNullValue()));
		assertThat(abstractFieldValue.getFieldId(), is(field.getIdentifier()));
		assertThat(abstractFieldValue, is(instanceOf(LiteralFieldValue.class)));

		LiteralFieldValue literalFieldValue = (LiteralFieldValue)abstractFieldValue;
		assertThat(literalFieldValue.getFieldValue(), is(fieldValue));
	}

	/**
	 * Test a literal field value.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 * @param field
	 *            The field from the configuration
	 * @param fieldValue
	 *            The value expected
	 * @param tuleapArtifact
	 *            The tuleap artifact containing the field value
	 */
	private void checkDateFieldValue(int artifactId, AbstractTuleapField field, String fieldValue,
			TuleapArtifact tuleapArtifact) {
		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getFieldValues().size(), is(1));

		Collection<AbstractFieldValue> fieldValues = tuleapArtifact.getFieldValues();
		AbstractFieldValue abstractFieldValue = fieldValues.iterator().next();

		assertThat(abstractFieldValue, is(notNullValue()));
		assertThat(abstractFieldValue.getFieldId(), is(field.getIdentifier()));
		assertThat(abstractFieldValue, is(instanceOf(LiteralFieldValue.class)));

		LiteralFieldValue literalFieldValue = (LiteralFieldValue)abstractFieldValue;
		assertThat(literalFieldValue.getFieldValue(), is(fieldValue + "000"));
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a string field. The goal is to ensure
	 * that the field is properly created in the TuleapArtifact. This test will use the tracker with the
	 * identifier 1.
	 */
	@Test
	public void testParseArtifactWithString() {
		int artifactId = 456;
		int trackerId = 1;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = "string field value"; //$NON-NLS-1$

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testLiteralFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a text field. The goal is to ensure
	 * that the field is properly created in the Tuleap artifact. this test will use the tracker with the
	 * identifier 2.
	 */
	@Test
	public void testParseArtifactWithText() {
		int artifactId = 456;
		int trackerId = 2;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = "text field value"; //$NON-NLS-1$

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testLiteralFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only an integer field. The goal is to
	 * ensure that the field is properly created in the Tuleap artifact. this test will use the tracker with
	 * the identifier 3.
	 */
	@Test
	public void testParseArtifactWithInteger() {
		int artifactId = 456;
		int trackerId = 3;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = "12"; //$NON-NLS-1$

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testLiteralFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a float field. The goal is to ensure
	 * that the field is properly created in the Tuleap artifact. this test will use the tracker with the
	 * identifier 4.
	 */
	@Test
	public void testParseArtifactWithFloat() {
		int artifactId = 456;
		int trackerId = 4;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = "12.5"; //$NON-NLS-1$

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testLiteralFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a date field. The goal is to ensure
	 * that the field is properly created in the Tuleap artifact. this test will use the tracker with the
	 * identifier 5.
	 */
	@Test
	public void testParseArtifactWithDate() {
		int artifactId = 456;
		int trackerId = 5;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = Long.toString(new Date().getTime() / 1000);

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.checkDateFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only an artifact links field. The goal is
	 * to ensure that the field is properly created in the Tuleap artifact. this test will use the tracker
	 * with the identifier 6.
	 */
	@Test
	public void testParseArtifactWithArtifactLinks() {
		int artifactId = 456;
		int trackerId = 6;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = "141, 218, 21, 8197"; //$NON-NLS-1$

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testLiteralFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only an open list field. The goal is to
	 * ensure that the field is properly created in the Tuleap artifact. this test will use the tracker with
	 * the identifier 7.
	 */
	@Test
	public void testParseArtifactWithOpenList() {
		int artifactId = 456;
		int trackerId = 7;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		String fieldValue = "this, is, a, list, of, tags"; //$NON-NLS-1$

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createLiteralFieldValue(field, fieldValue), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testLiteralFieldValue(artifactId, field, fieldValue, tuleapArtifact);
	}

	/**
	 * Creates the artifact field value with the given bind value ids.
	 * 
	 * @param field
	 *            The field to use
	 * @param bindValueIds
	 *            The bind value ids
	 * @return The artifact field value
	 */
	private ArtifactFieldValue createBoundFieldValue(AbstractTuleapField field, int[] bindValueIds) {
		ArtifactFieldValue artifactFieldValue = new ArtifactFieldValue();
		String fieldName = field.getName();
		FieldValue fValue = new FieldValue();

		TrackerFieldBindValue[] bindValues = new TrackerFieldBindValue[bindValueIds.length];

		for (int i = 0; i < bindValueIds.length; i++) {
			bindValues[i] = new TrackerFieldBindValue(bindValueIds[i], null);
		}

		fValue.setBind_value(bindValues);

		artifactFieldValue.setField_name(fieldName);
		artifactFieldValue.setField_value(fValue);
		return artifactFieldValue;
	}

	/**
	 * Test the content of the field value in the Tuleap artifact for the given field.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact expected
	 * @param field
	 *            The field tested
	 * @param bindValueIds
	 *            The values expected
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 */
	private void testBoundFieldValues(int artifactId, AbstractTuleapField field, int[] bindValueIds,
			TuleapArtifact tuleapArtifact) {
		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getFieldValues().size(), is(1));

		Collection<AbstractFieldValue> fieldValues = tuleapArtifact.getFieldValues();
		AbstractFieldValue abstractFieldValue = fieldValues.iterator().next();

		assertThat(abstractFieldValue, is(notNullValue()));
		assertThat(abstractFieldValue.getFieldId(), is(field.getIdentifier()));
		assertThat(abstractFieldValue, is(instanceOf(BoundFieldValue.class)));

		List<Integer> valueIds = new ArrayList<Integer>();
		for (int bindValueId : bindValueIds) {
			valueIds.add(Integer.valueOf(bindValueId));
		}
		BoundFieldValue boundFieldValue = (BoundFieldValue)abstractFieldValue;
		assertThat(boundFieldValue.getValueIds(), is(valueIds));
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a select box field. The goal is to
	 * ensure that the field is properly created in the Tuleap artifact. this test will use the tracker with
	 * the identifier 8.
	 */
	@Test
	public void testParseArtifactWithSelectBox() {
		int artifactId = 456;
		int trackerId = 8;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		int[] bindValueIds = new int[] {1, };

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createBoundFieldValue(field, bindValueIds), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testBoundFieldValues(artifactId, field, bindValueIds, tuleapArtifact);
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a multi-select box field. The goal is
	 * to ensure that the field is properly created in the Tuleap artifact. this test will use the tracker
	 * with the identifier 9.
	 */
	@Test
	public void testParseArtifactWithMultiSelectBox() {
		int artifactId = 456;
		int trackerId = 9;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		int[] bindValueIds = new int[] {1, 2, 3, 4, };

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this
				.createBoundFieldValue(field, bindValueIds), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testBoundFieldValues(artifactId, field, bindValueIds, tuleapArtifact);
	}

	/**
	 * Creates the artifact field value with the given file descriptions.
	 * 
	 * @param field
	 *            The field to use
	 * @param fileDescriptions
	 *            The file descriptions
	 * @return The artifact field value
	 */
	private ArtifactFieldValue createFileDescriptionValue(AbstractTuleapField field,
			AttachmentFieldValue fileDescriptions) {
		ArtifactFieldValue artifactFieldValue = new ArtifactFieldValue();
		String fieldName = field.getName();
		FieldValue fValue = new FieldValue();

		FieldValueFileInfo[] fileInfo = new FieldValueFileInfo[fileDescriptions.getAttachments().size()];
		for (int i = 0; i < fileDescriptions.getAttachments().size(); i++) {
			AttachmentValue attachmentValue = fileDescriptions.getAttachments().get(i);
			fileInfo[i] = new FieldValueFileInfo(attachmentValue.getAttachmentId(), attachmentValue
					.getPerson().getId(), attachmentValue.getDescription(), attachmentValue.getFilename(),
					attachmentValue.getSize(), attachmentValue.getContentType(), null);
		}

		fValue.setFile_info(fileInfo);

		artifactFieldValue.setField_name(fieldName);
		artifactFieldValue.setField_value(fValue);
		return artifactFieldValue;
	}

	/**
	 * Test the content of the field value in the Tuleap artifact for the given field.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact expected
	 * @param field
	 *            The field tested
	 * @param fileDescriptions
	 *            The values expected
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 */
	private void testFileDescriptionValues(int artifactId, AbstractTuleapField field,
			AttachmentFieldValue fileDescriptions, TuleapArtifact tuleapArtifact) {
		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getFieldValues().size(), is(1));

		Collection<AbstractFieldValue> fieldValues = tuleapArtifact.getFieldValues();
		AbstractFieldValue abstractFieldValue = fieldValues.iterator().next();

		assertThat(abstractFieldValue, is(notNullValue()));
		assertThat(abstractFieldValue.getFieldId(), is(field.getIdentifier()));
		assertThat(abstractFieldValue, is(instanceOf(AttachmentFieldValue.class)));

		AttachmentFieldValue attachmentFieldValue = (AttachmentFieldValue)abstractFieldValue;
		assertThat(attachmentFieldValue.getAttachments().size(), is(fileDescriptions.getAttachments().size()));

		for (int i = 0; i < attachmentFieldValue.getAttachments().size(); i++) {
			assertThat(attachmentFieldValue.getAttachments().get(i).getAttachmentId(), is(fileDescriptions
					.getAttachments().get(i).getAttachmentId()));
			assertThat(attachmentFieldValue.getAttachments().get(i).getContentType(), is(fileDescriptions
					.getAttachments().get(i).getContentType()));
			assertThat(attachmentFieldValue.getAttachments().get(i).getDescription(), is(fileDescriptions
					.getAttachments().get(i).getDescription()));
			assertThat(attachmentFieldValue.getAttachments().get(i).getFilename(), is(fileDescriptions
					.getAttachments().get(i).getFilename()));
			assertThat(attachmentFieldValue.getAttachments().get(i).getSize(), is(fileDescriptions
					.getAttachments().get(i).getSize()));
			assertThat(attachmentFieldValue.getAttachments().get(i).getPerson(), nullValue());
		}
	}

	/**
	 * This test will try to parse an artifact from a tracker with only a file attachments field. The goal is
	 * to ensure that the field is properly created in the Tuleap artifact. this test will use the tracker
	 * with the identifier 10.
	 */
	@Test
	public void testParseArtifactWithFileAttachments() {
		int artifactId = 456;
		int trackerId = 10;
		int userId = 789;
		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		TuleapTracker tuleapTracker = this.tuleapServer.getTracker(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		AbstractTuleapField field = fields.iterator().next();

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapUser firstUploadedBy = new TuleapUser("first username", "first realname", 1, "first email",
				null);
		attachments.add(new AttachmentValue("first id", "first name", firstUploadedBy, 123456,
				"first description", "first type"));

		TuleapUser secondUploadedBy = new TuleapUser("second username", "second realname", 2, "second email",
				null);
		attachments.add(new AttachmentValue("second id", "second name", secondUploadedBy, 789456,
				"second description", "second type"));

		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(field.getIdentifier(), attachments);

		ArtifactFieldValue[] value = new ArtifactFieldValue[] {this.createFileDescriptionValue(field,
				fileDescriptions), };

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTracker, commentedArtifact);

		this.testFileDescriptionValues(artifactId, field, fileDescriptions, tuleapArtifact);
	}
}