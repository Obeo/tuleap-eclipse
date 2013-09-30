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
package org.tuleap.mylyn.task.internal.tests.client.soap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.soap.CommentedArtifact;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapParser;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
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
	private TuleapServerConfiguration tuleapServerConfiguration;

	/**
	 * Prepare the configuration of the server.
	 */
	@Before
	public void setUp() {
		this.tuleapServerConfiguration = new TuleapServerConfiguration(this.repositoryUrl);

		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(projectName,
				projectId);

		// The first tracker does not have a single field.
		TuleapTrackerConfiguration firstTrackerConfiguration = new TuleapTrackerConfiguration(0, null, null,
				null, null, System.currentTimeMillis());

		// The second tracker has one string field
		TuleapTrackerConfiguration secondTrackerConfiguration = new TuleapTrackerConfiguration(1, null, null,
				null, null, System.currentTimeMillis());
		TuleapString tuleapString = new TuleapString(12);
		tuleapString.setName("string field name"); //$NON-NLS-1$
		secondTrackerConfiguration.addField(tuleapString);

		tuleapProjectConfiguration.addTracker(firstTrackerConfiguration);
		tuleapProjectConfiguration.addTracker(secondTrackerConfiguration);

		this.tuleapServerConfiguration.addProject(tuleapProjectConfiguration);
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

		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.tuleapServerConfiguration
				.getTrackerConfiguration(trackerId);

		List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

		// The first comment
		String firstCommentBody = "This is the first comment"; //$NON-NLS-1$
		TuleapPerson firstCommentSubmitter = new TuleapPerson("first-username", "first-realname", 17, //$NON-NLS-1$ //$NON-NLS-2$
				"first-email"); //$NON-NLS-1$
		int firstCommentSubmitDate = Long.valueOf(new Date().getTime() / 1000).intValue();

		TuleapElementComment firstTuleapElementComment = new TuleapElementComment(firstCommentBody,
				firstCommentSubmitter, firstCommentSubmitDate);
		comments.add(firstTuleapElementComment);

		// The second comment
		String secondCommentBody = "This is the second comment"; //$NON-NLS-1$
		TuleapPerson secondCommentSubmitter = new TuleapPerson("second-username", "second-realname", 18, //$NON-NLS-1$ //$NON-NLS-2$
				"second-email"); //$NON-NLS-1$
		int secondCommentSubmitDate = Long.valueOf(new Date().getTime() / 1000).intValue();

		TuleapElementComment secondTuleapElementComment = new TuleapElementComment(secondCommentBody,
				secondCommentSubmitter, secondCommentSubmitDate);
		comments.add(secondTuleapElementComment);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, comments);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTrackerConfiguration,
				commentedArtifact);

		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getConfigurationId(), is(trackerId));
		assertThat(tuleapArtifact.getCreationDate().toString(), is(creationDate.toString()));
		assertThat(tuleapArtifact.getLastModificationDate().toString(), is(lastUpdateDate.toString()));

		String htmlUrl = this.repositoryUrl + ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR + artifactId;
		assertThat(tuleapArtifact.getHtmlUrl(), is(htmlUrl));

		assertThat(tuleapArtifact.getUrl(), is(nullValue()));
		assertThat(tuleapArtifact.getNewComment(), is(nullValue()));
		assertThat(tuleapArtifact.getFieldValues(), empty());
		assertThat(tuleapArtifact.getLabel(), is(nullValue()));

		assertThat(tuleapArtifact.getComments(), hasSize(comments.size()));
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

		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.tuleapServerConfiguration
				.getTrackerConfiguration(trackerId);

		CommentedArtifact commentedArtifact = this.createCommentedArtifact(artifactId, trackerId, userId,
				creationDate, lastUpdateDate, new ArrayList<TuleapElementComment>());

		// Add a string
		ArtifactFieldValue[] value = new ArtifactFieldValue[1];

		ArtifactFieldValue artifactFieldValue = new ArtifactFieldValue();
		String fieldLabel = "string field label"; //$NON-NLS-1$
		String fieldName = "string field name"; //$NON-NLS-1$
		String fieldValue = "string field value"; //$NON-NLS-1$
		FieldValue fValue = new FieldValue();
		fValue.setValue(fieldValue);

		artifactFieldValue.setField_label(fieldLabel);
		artifactFieldValue.setField_name(fieldName);
		artifactFieldValue.setField_value(fValue);

		value[0] = artifactFieldValue;

		commentedArtifact.getArtifact().setValue(value);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTrackerConfiguration,
				commentedArtifact);

		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getFieldValues(), hasSize(1));

		Collection<AbstractFieldValue> fieldValues = tuleapArtifact.getFieldValues();
		AbstractFieldValue abstractFieldValue = fieldValues.iterator().next();

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		AbstractTuleapField field = fields.iterator().next();

		assertThat(abstractFieldValue, is(notNullValue()));
		assertThat(abstractFieldValue.getFieldId(), is(field.getIdentifier()));
		assertThat(abstractFieldValue, is(instanceOf(LiteralFieldValue.class)));

		LiteralFieldValue literalFieldValue = (LiteralFieldValue)abstractFieldValue;
		assertThat(literalFieldValue.getFieldValue(), is(fieldValue));
	}
}
