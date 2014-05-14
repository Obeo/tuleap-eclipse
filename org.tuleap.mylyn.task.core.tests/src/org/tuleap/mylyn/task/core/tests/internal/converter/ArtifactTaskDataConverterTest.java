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
package org.tuleap.mylyn.task.core.tests.internal.converter;

import java.util.Arrays;
import java.util.Date;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.core.internal.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapString;
import org.tuleap.mylyn.task.core.internal.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.repository.TuleapAttributeMapper;
import org.tuleap.mylyn.task.core.internal.repository.TuleapRepositoryConnector;

import static org.junit.Assert.assertEquals;

/**
 * Tests of {@link ArtifactTaskDataConverter}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ArtifactTaskDataConverterTest {

	private ArtifactTaskDataConverter converter;

	private TuleapTracker tracker;

	private TaskRepository repository;

	private TuleapRepositoryConnector connector;

	@Before
	public void setUp() throws Exception {
		TuleapProject project = new TuleapProject("Test prj", 101);

		tracker = new TuleapTracker(123, "t/123", "Bugs", "bug", "Tracker for bugs", new Date());
		project.addTracker(tracker);

		TuleapString summaryField = new TuleapString(2000);
		summaryField.setLabel("Title");
		summaryField.setName("title");
		summaryField.setSemanticTitle(true);
		tracker.addField(summaryField);

		TuleapSelectBox sbField = new TuleapSelectBox(2001);
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(100);
		item.setLabel("To Do");
		sbField.addItem(item);
		sbField.getOpenStatus().add(item); // To Do is open
		item = new TuleapSelectBoxItem(101);
		item.setLabel("Current");
		sbField.addItem(item);
		sbField.getOpenStatus().add(item); // Current is open
		item = new TuleapSelectBoxItem(102);
		item.setLabel("Done");
		sbField.addItem(item); // But Done is closed
		tracker.addField(sbField);

		TuleapSelectBox contribField = new TuleapSelectBox(2002);
		item = new TuleapSelectBoxItem(200);
		item.setLabel("FitzChivalry");
		contribField.addItem(item);
		item = new TuleapSelectBoxItem(201);
		item.setLabel("Fool");
		contribField.addItem(item);
		item = new TuleapSelectBoxItem(202);
		item.setLabel("Burrich");
		contribField.addItem(item);
		contribField.setSemanticContributor(true);
		tracker.addField(contribField);

		repository = new TaskRepository("test", "https://test.url");
		connector = new TuleapRepositoryConnector();
		converter = new ArtifactTaskDataConverter(tracker, repository, connector);
	}

	@Test
	public void testSummaryField() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.addField(tracker.getTitleField());
		artifact.addFieldValue(new LiteralFieldValue(2000, "This is the title"));
		converter.populateTaskData(taskData, artifact, null);

		assertEquals("This is the title", taskData.getRoot().getAttribute(TaskAttribute.SUMMARY).getValue());
	}

	@Test
	public void testContributorField() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.addField(tracker.getContributorField());
		artifact.addFieldValue(new BoundFieldValue(2002, Arrays.asList(201)));
		converter.populateTaskData(taskData, artifact, null);

		assertEquals("201", taskData.getRoot().getAttribute(TaskAttribute.USER_ASSIGNED).getValue());
	}

	@Test
	public void testStatusField() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.addField(tracker.getStatusField());
		artifact.addFieldValue(new BoundFieldValue(2001, Arrays.asList(101)));
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals(101, mapper.getStatusAsInt());
		assertEquals("Current", mapper.getStatus());
	}

	@Test
	public void testHtmlUrl() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.setHtmlUrl("/some/url/?aid=1001");
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals("https://test.url/some/url/?aid=1001", mapper.getTaskUrl());
	}

	@Test
	public void testHtmlUrlNull() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals("", mapper.getTaskUrl());
	}

	@Test
	public void testHtmlUrlEmpty() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.setHtmlUrl("");
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals("", mapper.getTaskUrl());
	}

	@Test
	public void testHtmlUrlWithoutSlash() {
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.setHtmlUrl("some/url/?aid=1001");
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals("https://test.url/some/url/?aid=1001", mapper.getTaskUrl());
	}

	@Test
	public void testHtmlUrlWithSlashAtTheEndOfRepoUrl() {
		repository.setRepositoryUrl("https://test.url/"); // slash at the end

		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url/", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.setHtmlUrl("some/url/?aid=1001");
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals("https://test.url/some/url/?aid=1001", mapper.getTaskUrl());
	}

	@Test
	public void testHtmlUrlWith2Slashes() {
		repository.setRepositoryUrl("https://test.url/"); // slash at the end

		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, connector), "test",
				"https://test.url/", "101:123#1001");
		TuleapArtifact artifact = new TuleapArtifact(1001, new TuleapReference(123, "t/123"),
				new TuleapReference(101, "p/101"));
		artifact.setHtmlUrl("/some/url/?aid=1001"); // salsh at the beginning
		converter.populateTaskData(taskData, artifact, null);

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		assertEquals("https://test.url/some/url/?aid=1001", mapper.getTaskUrl());
	}
}
