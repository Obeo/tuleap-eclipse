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
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.soap.CommentedArtifact;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapParser;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;

import static org.junit.Assert.fail;

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
	@BeforeClass
	public void setUp() {
		this.tuleapServerConfiguration = new TuleapServerConfiguration(this.repositoryUrl);

		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(projectName,
				projectId);

		TuleapTrackerConfiguration firstCrackerConfiguration = null;
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
		int trackerId = 0;
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.tuleapServerConfiguration
				.getTrackerConfiguration(trackerId);

		Artifact artifact = new Artifact();
		List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

		CommentedArtifact commentedArtifact = new CommentedArtifact(artifact, comments);

		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapArtifact tuleapArtifact = tuleapSoapParser.parseArtifact(tuleapTrackerConfiguration,
				commentedArtifact);

		fail("the unit test fails");
	}
}
