/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.tests.internal.repository;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tasks.core.LocalTask;
import org.eclipse.mylyn.internal.tasks.core.TaskTask;
import org.eclipse.mylyn.internal.tasks.core.data.FileTaskAttachmentSource;
import org.eclipse.mylyn.internal.tasks.core.data.TextTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.client.TuleapClientManager;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestConnector;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapFile;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapTaskAttachmentHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The tests class for the Tuleap task attachment handler.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @since 0.7
 */
public class TuleapTaskAttachmentHandlerTests {

	private TaskRepository repository;

	private TaskAttribute attachmentAttribute;

	private TuleapRestClient client;

	private String repositoryUrl;

	private Gson gson;

	private RestResourceFactory restResourceFactory;

	private String dataToPost;

	private String dataToGet;

	private String dataToUpdate;

	private TuleapClientManager clientManager;

	private TuleapServer tuleapServer;

	private ITuleapRepositoryConnector repositoryConnector;

	private String artifactWithComment;

	private int fileIdentifier;

	private TuleapTracker trackerWithUploadField;

	private TuleapTracker trackerWithoutUploadField;

	private TuleapArtifact artifactBeforeUpdate;

	/**
	 * Prepare the configuration.
	 */
	@Before
	public void setUp() {
		fileIdentifier = -1;
		repositoryUrl = "test URL";
		this.repository = new TaskRepository("test", repositoryUrl);

		AbstractWebLocation abstractWebLocation = new TaskRepositoryLocationFactory()
				.createWebLocation(repository);
		gson = TuleapGsonProvider.defaultGson();
		ILog logger = Platform.getLog(Platform.getBundle(TuleapCoreActivator.PLUGIN_ID));
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(abstractWebLocation, logger);

		restResourceFactory = new RestResourceFactory(RestResourceFactory.BEST_VERSION, tuleapRestConnector,
				gson, logger);

		clientManager = new TuleapClientManager() {
			@Override
			public TuleapRestClient getRestClient(TaskRepository pTaskRepository) {
				return client;
			}
		};

		client = new TuleapRestClient(restResourceFactory, gson, repository) {
			@Override
			public TuleapArtifact getArtifact(int artifactId, TuleapServer server, IProgressMonitor monitor)
					throws CoreException {
				return artifactBeforeUpdate;
			}

			@Override
			public TuleapReference createArtifactFile(String data, String type, String name,
					String description, IProgressMonitor monitor) throws CoreException {
				dataToPost = data;
				return new TuleapReference(50, "artifact_files/50");
			}

			@Override
			public TuleapFile getArtifactFile(int fileId, int offset, int limit, IProgressMonitor monitor)
					throws CoreException {
				TuleapFile file = new TuleapFile();
				file.setData(dataToGet);
				return file;
			}

			@Override
			public void updateArtifactFile(int fileId, String data, int offset, IProgressMonitor monitor)
					throws CoreException {
				dataToUpdate = data;
				fileIdentifier = fileId;
			}

			@Override
			public void updateArtifact(TuleapArtifactWithComment artifact, IProgressMonitor monitor)
					throws CoreException {
				artifactWithComment = gson.toJson(artifact);
			}

		};

		this.tuleapServer = new TuleapServer(this.repositoryUrl);

		TuleapProject project = new TuleapProject("Project", 123);

		// The first tracker 101 has one attachment field, id = 666
		trackerWithUploadField = new TuleapTracker(101, null, "Tracker 101", null,
				"Tracker with attachment field", new Date());
		TuleapFileUpload field = new TuleapFileUpload(666);
		field.setPermissions(new String[] {"read", "update" });
		trackerWithUploadField.addField(field);

		// The second tracker 102 has one string field but no attachment field
		trackerWithoutUploadField = new TuleapTracker(102, null, "Tracker 102", null,
				"Tracker without attachment field", new Date());
		TuleapString tuleapString = new TuleapString(999);
		tuleapString.setName("string field name"); //$NON-NLS-1$
		trackerWithoutUploadField.addField(tuleapString);

		project.addTracker(trackerWithUploadField);
		project.addTracker(trackerWithoutUploadField);

		this.tuleapServer.addProject(project);

		repositoryConnector = new ITuleapRepositoryConnector() {
			public TuleapServer getServer(TaskRepository repo) {
				return TuleapTaskAttachmentHandlerTests.this.tuleapServer;
			}

			public TuleapClientManager getClientManager() {
				return clientManager;
			}

			public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker configuration,
					IProgressMonitor monitor) throws CoreException {
				return configuration;
			}
		};

		artifactBeforeUpdate = new TuleapArtifact(42, new TuleapReference(101, "/trackers/101"),
				new TuleapReference(123, "/projects/123"));
		artifactBeforeUpdate.addFieldValue(new AttachmentFieldValue(666, Collections
				.<AttachmentValue> emptyList()));
	}

	/**
	 * Test getting the file content with complete information.
	 *
	 * @throws CoreException
	 * @throws IOException
	 */
	@Test
	public void testGetContentWithCompleteInformation() throws CoreException, IOException {
		TaskData taskData = new TaskData(new TaskAttributeMapper(repository), "REST", repositoryUrl, "TaskId");
		attachmentAttribute = taskData.getRoot().createAttribute("attachment---0");
		attachmentAttribute.getMetaData().setType(TaskAttribute.TYPE_ATTACHMENT);
		// fileSize = 29;
		attachmentAttribute.createMappedAttribute(TaskAttribute.ATTACHMENT_SIZE).setValue("29");
		TaskAttachmentMapper attachmentMapper = TaskAttachmentMapper.createFrom(attachmentAttribute);
		attachmentMapper.setFileName("test-file.txt");
		attachmentMapper.setDescription("test file");
		attachmentMapper.applyTo(attachmentAttribute);

		// This is the Base64 representation of "This is the file data to test"
		// fileSize = 29
		dataToGet = "VGhpcyBpcyB0aGUgZmlsZSBkYXRhIHRvIHRlc3Q=";

		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(null, clientManager);
		InputStream is = null;
		try {
			is = attachmentHandler.getContent(repository, null, attachmentAttribute, null);
			assertNotNull(is);
			assertEquals("This is the file data to test", getStringFromInputStream(is));
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Test getting the file content with complete information.
	 *
	 * @throws CoreException
	 * @throws IOException
	 */
	@Test
	public void testGetContentWithBinaryAttachment() throws CoreException, IOException {
		TaskData taskData = new TaskData(new TaskAttributeMapper(repository), "REST", repositoryUrl, "TaskId");
		attachmentAttribute = taskData.getRoot().createAttribute("attachment---0");
		attachmentAttribute.getMetaData().setType(TaskAttribute.TYPE_ATTACHMENT);
		int fileSize = 15 * 1024;
		attachmentAttribute.createMappedAttribute(TaskAttribute.ATTACHMENT_SIZE).setValue(
				Integer.toString(fileSize));
		TaskAttachmentMapper attachmentMapper = TaskAttachmentMapper.createFrom(attachmentAttribute);
		attachmentMapper.setFileName("test-file.txt");
		attachmentMapper.setDescription("test file");
		attachmentMapper.applyTo(attachmentAttribute);

		byte[] bytes = new byte[fileSize];
		for (int i = 0; i < fileSize; i++) {
			bytes[i] = (byte)(i % 256);
		}
		dataToGet = Base64.encodeBase64String(bytes);

		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(null, clientManager);
		InputStream is = null;
		try {
			is = attachmentHandler.getContent(repository, null, attachmentAttribute, null);
			assertNotNull(is);
			byte[] readBytes = new byte[15 * 1024];
			int sizeRead = 0;
			int read = is.read(readBytes);
			while (read > 0) {
				sizeRead += read;
				read = is.read(readBytes, sizeRead, readBytes.length - sizeRead);
			}
			assertEquals(fileSize, sizeRead);
			for (int i = 0; i < 256; i++) {
				assertEquals(bytes[i], readBytes[i]);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Test posting the file content with non null attachment attribute..
	 *
	 * @throws CoreException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testPostFirstAttachment() throws CoreException, URISyntaxException, IOException {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		ITask task = new LocalTask("123:101#42", "label");

		TextTaskAttachmentSource source = new TextTaskAttachmentSource("Text to post");

		attachmentHandler.postContent(repository, task, source, "This is a comment"/* Ignored */,
				attachmentAttribute, null);

		assertEquals("{\"values\":[{\"field_id\":666,\"value\":[50]}]}", artifactWithComment);
		assertEquals(Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf8("Text to post")), dataToPost);
		// fileIdentifier not updated since update not called
		assertEquals(-1, fileIdentifier);
		// update must not have been called since chunk is small
		assertNull(dataToUpdate);
	}

	/**
	 * Test posting the file content with non null attachment attribute..
	 *
	 * @throws CoreException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testPostAttachmentWhenTheresAlreadyOne() throws CoreException, URISyntaxException,
			IOException {
		// Change the current state of artifact to update so thyat it already has one attachment
		artifactBeforeUpdate.addFieldValue(new AttachmentFieldValue(666, Arrays.asList(new AttachmentValue(
				"777", "att 777", 0, 1500, "desc", "image/png", "/artifact_files/777"))));

		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		ITask task = new LocalTask("123:101#42", "label");

		TextTaskAttachmentSource source = new TextTaskAttachmentSource("Text to post");

		attachmentHandler.postContent(repository, task, source, "This is a comment"/* Ignored */,
				attachmentAttribute, null);

		assertEquals("{\"values\":[{\"field_id\":666,\"value\":[777,50]}]}", artifactWithComment);
		assertEquals(Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf8("Text to post")), dataToPost);
		// fileIdentifier not updated since update not called
		assertEquals(-1, fileIdentifier);
		// update must not have been called since chunk is small
		assertNull(dataToUpdate);
	}

	/**
	 * Test posting the file content with non null attachment attribute..
	 *
	 * @throws CoreException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testPostContentUsingBinaryFile() throws CoreException, URISyntaxException, IOException {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		FileOutputStream fop = null;
		File file = null;
		byte[] bytes = null;
		try {
			file = File.createTempFile("temp", "png");
			fop = new FileOutputStream(file);

			// get the content in bytes
			bytes = new byte[256];
			for (int i = 0; i < 256; i++) {
				bytes[i] = (byte)i;
			}

			fop.write(bytes);
			fop.flush();
			fop.close();
		} finally {
			if (fop != null) {
				fop.close();
			}
		}

		FileTaskAttachmentSource source = new FileTaskAttachmentSource(file);
		source.setDescription("Comment for test");

		ITask task = new LocalTask("123:101#42", "label");

		attachmentHandler.postContent(repository, task, source, "This is a comment"/* Ignored */,
				attachmentAttribute, null);

		assertEquals(
				"{\"values\":[{\"field_id\":666,\"value\":[50]}],\"comment\":{\"body\":\"Comment for test\",\"format\":\"text\"}}",
				artifactWithComment);
		assertNull(dataToUpdate);
		assertEquals(-1, fileIdentifier);
		assertEquals(new String(Base64.encodeBase64(bytes)), dataToPost);
	}

	/**
	 * Test posting the file content with non null attachment attribute..
	 *
	 * @throws CoreException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testPostContentUsingBinaryFileWithoutComment() throws Exception {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		FileOutputStream fop = null;
		File file = null;
		byte[] bytes = null;
		try {
			file = File.createTempFile("temp", "png");
			fop = new FileOutputStream(file);

			// get the content in bytes
			bytes = new byte[256];
			for (int i = 0; i < 256; i++) {
				bytes[i] = (byte)i;
			}

			fop.write(bytes);
			fop.flush();
			fop.close();
		} finally {
			if (fop != null) {
				fop.close();
			}
		}

		FileTaskAttachmentSource source = new FileTaskAttachmentSource(file);

		ITask task = new LocalTask("123:101#42", "label");

		attachmentHandler.postContent(repository, task, source, "This is a comment"/* Ignored */,
				attachmentAttribute, null);

		assertEquals("{\"values\":[{\"field_id\":666,\"value\":[50]}]}", artifactWithComment);
		assertNull(dataToUpdate);
		assertEquals(-1, fileIdentifier);
		assertEquals(new String(Base64.encodeBase64(bytes)), dataToPost);
	}

	@Test
	public void testCanGetContent() {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		// tracker 101 has an updatable upload field
		assertTrue(attachmentHandler.canGetContent(repository, new TaskTask("test", repositoryUrl,
				"123:101#1000")));
		assertFalse(attachmentHandler.canGetContent(repository, new TaskTask("test", repositoryUrl,
				"123:102#1001")));
	}

	@Test
	public void testCanPostContent() {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		// tracker 101 has an updatable upload field
		assertTrue(attachmentHandler.canPostContent(repository, new TaskTask("test", repositoryUrl,
				"123:101#1000")));
		assertFalse(attachmentHandler.canPostContent(repository, new TaskTask("test", repositoryUrl,
				"123:102#1001")));
	}

	@Test
	public void testCanGetContentOnNonUpdatableUploadField() {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		trackerWithUploadField.getAttachmentField().setPermissions(new String[] {"read" });
		// tracker 101 has no more an updatable upload field
		assertTrue(attachmentHandler.canGetContent(repository, new TaskTask("test", repositoryUrl,
				"123:101#1000")));
	}

	@Test
	public void testCanGetContentOnNonReadableUploadField() {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		// doesn't make much sense to have an unreadable updatable field, but anyway...
		trackerWithUploadField.getAttachmentField().setPermissions(new String[] {"update" });
		// tracker 101 has no more a readable upload field
		assertFalse(attachmentHandler.canGetContent(repository, new TaskTask("test", repositoryUrl,
				"123:101#1000")));
	}

	@Test
	public void testCanPostContentOnNonUpdatableUploadField() {
		TuleapTaskAttachmentHandler attachmentHandler = new TuleapTaskAttachmentHandler(repositoryConnector,
				clientManager);
		trackerWithUploadField.getAttachmentField().setPermissions(new String[] {"read" });
		// tracker 101 has no more an updatable upload field
		assertFalse(attachmentHandler.canPostContent(repository, new TaskTask("test", repositoryUrl,
				"123:101#1000")));
	}

	/**
	 * Get a String from an InputStream.
	 *
	 * @param is
	 *            the inputstream
	 * @return The resulted String
	 */
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
