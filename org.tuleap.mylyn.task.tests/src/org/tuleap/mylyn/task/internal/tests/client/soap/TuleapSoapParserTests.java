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
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.soap.CommentedArtifact;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapParser;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
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
		TuleapTrackerConfiguration firstCrackerConfiguration = new TuleapTrackerConfiguration(123, null,
				null, null, null, System.currentTimeMillis());
		tuleapProjectConfiguration.addTracker(firstCrackerConfiguration);

		this.tuleapServerConfiguration.addProject(tuleapProjectConfiguration);
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
	 * </ul>
	 * This unit test will use the configuration of the tracker with the identifier 0.
	 */
	@Test
	public void testParseArtifactWithoutFields() {
		int artifactId = 456;
		int trackerId = 123;
		int userId = 789;
		String htmlUrl = this.repositoryUrl + ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR + artifactId;

		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.tuleapServerConfiguration
				.getTrackerConfiguration(trackerId);

		Date creationDate = new Date();
		Date lastUpdateDate = new Date();

		Artifact artifact = new Artifact();
		artifact.setArtifact_id(artifactId);
		artifact.setTracker_id(trackerId);
		artifact.setSubmitted_by(userId);
		artifact.setSubmitted_on(Long.valueOf(creationDate.getTime() / 1000).intValue());
		artifact.setLast_update_date(Long.valueOf(lastUpdateDate.getTime() / 1000).intValue());
		artifact.setValue(new ArtifactFieldValue[0]);

		List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

		CommentedArtifact commentedArtifact = new CommentedArtifact(artifact, comments);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTrackerConfiguration,
				commentedArtifact);

		assertThat(tuleapArtifact, is(notNullValue()));
		assertThat(tuleapArtifact.getId(), is(artifactId));
		assertThat(tuleapArtifact.getConfigurationId(), is(trackerId));
		assertThat(tuleapArtifact.getCreationDate().toString(), is(creationDate.toString()));
		assertThat(tuleapArtifact.getLastModificationDate().toString(), is(lastUpdateDate.toString()));
		assertThat(tuleapArtifact.getHtmlUrl(), is(htmlUrl));
		assertThat(tuleapArtifact.getUrl(), is(nullValue()));
		assertThat(tuleapArtifact.getNewComment(), is(nullValue()));
		assertThat(tuleapArtifact.getComments(), empty());
		assertThat(tuleapArtifact.getFieldValues(), empty());
		assertThat(tuleapArtifact.getLabel(), is(nullValue()));
	}
}
