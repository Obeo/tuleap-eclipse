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
package org.eclipse.mylyn.tuleap.core.tests.internal.client.rest;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResource;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.ServerResponse;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.model.TuleapToken;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapFormElement;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.core.tests.internal.TestLogger;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.MockRestConnector.ServerRequest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.ParserUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests of {@link TuleapRestClient}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapRestClientTest {

	public static final String[] READ_CREATE = {AbstractTuleapFormElement.PERMISSION_READ,
		AbstractTuleapFormElement.PERMISSION_SUBMIT, };

	public static final String[] READ_UPDATE = {AbstractTuleapFormElement.PERMISSION_READ,
		AbstractTuleapFormElement.PERMISSION_UPDATE, };

	public static final String[] READ_ONLY = {AbstractTuleapFormElement.PERMISSION_READ };

	public static final String[] READ_CREATE_UPDATE = {AbstractTuleapFormElement.PERMISSION_READ,
		AbstractTuleapFormElement.PERMISSION_SUBMIT, AbstractTuleapFormElement.PERMISSION_UPDATE, };

	public static final String[] CREATE_UPDATE = {AbstractTuleapFormElement.PERMISSION_SUBMIT,
		AbstractTuleapFormElement.PERMISSION_UPDATE, };

	public static final String[] CREATE_ONLY = {AbstractTuleapFormElement.PERMISSION_SUBMIT, };

	public static final String[] UPDATE_ONLY = {AbstractTuleapFormElement.PERMISSION_UPDATE, };

	private final String serverUrl = "https://test/url"; //$NON-NLS-1$

	private String apiVersion = "v12.3"; //$NON-NLS-1$

	private MockRestConnector connector;

	private Gson gson;

	private TaskRepository repository;

	private RestResourceFactory restResourceFactory;

	private TuleapRestClient client;

	@Test
	public void testRetrieveUserGroups() throws CoreException, ParseException {
		String jsonGroups = ParserUtil.loadFile("/groups/groups.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonGroups, respHeaders);
		connector.setResponse(response);
		client.getProjectUserGroups(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/projects/200/user_groups", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveUsers() throws CoreException, ParseException {
		String jsonAdmins = ParserUtil.loadFile("/users/project_admins.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonAdmins, respHeaders);
		connector.setResponse(response);
		client.getUserGroupUsers("200", null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/user_groups/200/users", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveTrackerReports() throws CoreException, ParseException {
		String jsonTrackerReports = ParserUtil.loadFile("/tracker_reports/tracker_reports.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTrackerReports,
				respHeaders);
		connector.setResponse(response);
		client.getTrackerReports(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/trackers/200/tracker_reports", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveProjects() throws CoreException, ParseException {
		String jsonProjects = ParserUtil.loadFile("/projects/projects.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonProjects, respHeaders);
		connector.setResponse(response);
		client.getProjects(null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/projects", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveProjectTrackers() throws CoreException, ParseException {
		String jsonTrackers = ParserUtil.loadFile("/trackers/trackers_part_1.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTrackers, respHeaders);
		connector.setResponse(response);
		client.getProjectTrackers(101, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/projects/101/trackers", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveTrackerReportArtifacts() throws CoreException, ParseException {
		String artifacts = ParserUtil.loadFile("/artifacts/artifacts.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, artifacts, respHeaders);
		connector.setResponse(response);
		client.getTrackerReportArtifacts(10, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/tracker_reports/10/artifacts", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveArtifact() throws CoreException, ParseException {
		String artifact = ParserUtil.loadFile("/artifacts/artifact-0.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, artifact, respHeaders);
		connector.setResponse(response);
		client.getArtifact(10, new TuleapServer(repository.getUrl()), null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifacts/10", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveArtifactFile() throws CoreException, ParseException {
		String artifact = ParserUtil.loadFile("/artifact_files/file-0.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, artifact, respHeaders);
		connector.setResponse(response);
		client.getArtifactFile(10, 0, 1024 * 1024, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifact_files/10", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testCreateArtifactFile() throws CoreException, ParseException {
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"{\"id\": 50, \"uri\": \"artifact_files/50\" }", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.createArtifactFile("The file data", "application/zip", "fileName", "file description", null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifact_temporary_files", request.url); //$NON-NLS-1$
		assertEquals("POST", request.method); //$NON-NLS-1$
		assertEquals(
				"{\"name\":\"fileName\",\"description\":\"file description\",\"mimetype\":\"application/zip\",\"content\":\"The file data\"}", //$NON-NLS-1$
				request.body);
	}

	@Test
	public void testUpdateArtifactFile() throws CoreException, ParseException {
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateArtifactFile(10, "The file content", 64, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifact_temporary_files/10", request.url); //$NON-NLS-1$
		assertEquals("PUT", request.method); //$NON-NLS-1$
		assertEquals("{\"content\":\"The file content\",\"offset\":64}", //$NON-NLS-1$
				request.body);
	}

	@Test
	public void testDeleteArtifactFile() throws CoreException, ParseException {
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,DELETE"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,DELETE"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.deleteArtifactFile(10, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifact_temporary_files/10", request.url); //$NON-NLS-1$
		assertEquals("DELETE", request.method); //$NON-NLS-1$
	}

	@Test
	public void testCreateArtifact() throws CoreException, ParseException {
		TuleapReference trackerRef = new TuleapReference(100, "t/100");
		TuleapReference projectRef = new TuleapReference(50, "p/50");
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"{\"id\": 1,\"uri\": \"artifacts/1\",\"tracker\": {\"id\": 15, \"uri\": \"trackers/15\"}}",
				respHeaders);
		connector.setResponse(response);

		client.createArtifact(artifact, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifacts", request.url); //$NON-NLS-1$
		assertEquals("POST", request.method); //$NON-NLS-1$
	}

	@Test
	public void testUpdateArtifact() throws CoreException {
		TuleapReference trackerRef = new TuleapReference(100, "t/100");
		TuleapReference projectRef = new TuleapReference(50, "p/50");
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(10, trackerRef, projectRef);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateArtifact(artifact, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifacts/10", request.url); //$NON-NLS-1$
		assertEquals("PUT", request.method); //$NON-NLS-1$
		assertEquals("{\"values\":[]}", //$NON-NLS-1$
				request.body);
	}

	@Test
	public void testUpdateArtifactWithSerializableAttachmentField() throws CoreException {
		TuleapReference trackerRef = new TuleapReference(100, "t/100");
		TuleapReference projectRef = new TuleapReference(50, "p/50");
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(10, trackerRef, projectRef);

		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapFileUpload field = new TuleapFileUpload(222);

		field.setPermissions(new String[] {"update" });
		artifact.addField(field);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		// TuleapUser firstUploadedBy = new TuleapUser(
		//				"first username", "first realname", 1, "first email", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		//		TuleapUser secondUploadedBy = new TuleapUser("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
		//				"second email", null); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(222, attachments);

		artifact.addFieldValue(fileDescription);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateArtifact(artifact, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifacts/10", request.url); //$NON-NLS-1$
		assertEquals("PUT", request.method); //$NON-NLS-1$
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"value\":[100000,100001]}],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}", //$NON-NLS-1$
				request.body);
	}

	@Test
	public void testRetrieveArtifactComments() throws CoreException, ParseException {
		String jsonTrackers = ParserUtil.loadFile("/changesets/changesets.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTrackers, respHeaders);
		connector.setResponse(response);
		client.getArtifactComments(10, new TuleapServer(repository.getUrl()), null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifacts/10/changesets", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testTracker() throws CoreException, ParseException {
		String jsonTracker = ParserUtil.loadFile("/trackers/tracker-5.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTracker, respHeaders);
		connector.setResponse(response);
		client.getTracker(102, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/trackers/102", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	/**
	 * Test that the token creation on the server is well done.
	 *
	 * @throws CoreException
	 */
	@Test
	public void testlogin() throws CoreException {

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$

		String token = ParserUtil.loadFile("/tokens/token-0.json");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, token, respHeaders);

		connector.setResponse(response);
		client.login();

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/tokens", request.url); //$NON-NLS-1$
		assertEquals("POST", request.method); //$NON-NLS-1$
		assertEquals("{\"username\":\"admin\",\"password\":\"password\"}", //$NON-NLS-1$
				request.body);

		TuleapToken token2 = client.getToken();
		assertEquals("Zza25pV0V5eXFhJDJ5JDE1JHpSbU1nMldoRUo1b21QNFQ4eEhDUC4xM0FybW9Ud3FwdmRUejA3R1B2WF",
				token2.getToken());
		assertEquals("539", token2.getUserId());
	}

	/**
	 * Test that checks that a TuleapRestClient that has no token (yet) will automatically login and retrieve
	 * a token, then re-run a request.
	 *
	 * @throws CoreException
	 *             if something goes wrong.
	 */
	@Test
	public void testAutomaticLoginWhenNoTokenIsKnown() throws CoreException {
		MockListRestConnector listConnector = new MockListRestConnector();
		this.restResourceFactory = new RestResourceFactory(apiVersion, listConnector, gson, new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		listConnector.setResourceFactory(restResourceFactory);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET,PUT,POST"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,PUT,POST"); //$NON-NLS-1$

		ServerResponse response401Unauthorized = new ServerResponse(ServerResponse.STATUS_UNAUTHORIZED, "",
				respHeaders);

		String tokenJson = ParserUtil.loadFile("/tokens/token-0.json");
		ServerResponse tokenResponse = new ServerResponse(ServerResponse.STATUS_OK, tokenJson, respHeaders);

		String trackerJson = ParserUtil.loadFile("/trackers/tracker-0.json");
		ServerResponse trackersResponse = new ServerResponse(ServerResponse.STATUS_OK, trackerJson,
				respHeaders);

		listConnector.addServerResponse(response401Unauthorized);
		listConnector.addServerResponse(tokenResponse);
		listConnector.addServerResponse(trackersResponse);
		// Need to create a new client to use the specific connector.
		client = new TuleapRestClient(restResourceFactory, gson, repository);

		TuleapTracker tracker = client.getTracker(200, null);
		assertNotNull(tracker);
		List<ServerRequest> requestsSent = listConnector.requestsSent;
		assertEquals(3, requestsSent.size());

		ServerRequest req = requestsSent.get(0);
		assertEquals("GET", req.method);
		assertEquals("/api/v12.3/trackers/200", req.url);

		req = requestsSent.get(1);
		assertEquals("POST", req.method);
		assertEquals("/api/v12.3/tokens", req.url);

		req = requestsSent.get(2);
		assertEquals("GET", req.method);
		assertEquals("/api/v12.3/trackers/200", req.url);
	}

	@Test
	public void testValidateConnection() throws CoreException {
		TuleapToken token = new TuleapToken();
		token.setUserId("123");
		token.setToken("toktoktok");
		token.setUri("/some/uri");
		String json = gson.toJson(token);
		ServerResponse response = new ServerResponse(200, json, Collections.<String, String> emptyMap());
		connector.setResponse(response);
		IStatus status = client.validateConnection(null);
		assertEquals(Status.OK_STATUS, status);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/tokens", request.url);
		assertEquals("POST", request.method);
		assertEquals("{\"username\":\"admin\",\"password\":\"password\"}", request.body);
		assertEquals("123", client.getToken().getUserId());
		assertEquals("toktoktok", client.getToken().getToken());
		assertEquals("/some/uri", client.getToken().getUri());
	}

	@Before
	public void setUp() {
		connector = new MockRestConnector();

		gson = TuleapGsonProvider.defaultGson();
		this.restResourceFactory = new RestResourceFactory(apiVersion, connector, gson, new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		connector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, gson, repository);
	}

}
