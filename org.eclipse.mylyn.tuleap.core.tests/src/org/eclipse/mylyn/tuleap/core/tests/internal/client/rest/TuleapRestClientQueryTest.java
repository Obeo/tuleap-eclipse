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

import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.ITuleapQueryConstants;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.ServerResponse;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapArtifactLink;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapText;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.tests.internal.TestLogger;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.MockRestConnector.ServerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.TuleapRestClientTest.READ_CREATE_UPDATE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests of
 * {@link TuleapRestClient#getArtifactsFromQuery(IRepositoryQuery, TuleapTracker, org.eclipse.core.runtime.IProgressMonitor)}
 * .
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@RunWith(Parameterized.class)
public class TuleapRestClientQueryTest {

	private final String serverUrl = "https://test/url"; //$NON-NLS-1$

	private String apiVersion = "v12.3"; //$NON-NLS-1$

	private MockRestConnector connector;

	private Gson gson;

	private TaskRepository repository;

	private RestResourceFactory restResourceFactory;

	private TuleapRestClient client;

	private TuleapTracker tracker;

	private TuleapProject project;

	private String queryJson;

	private String expectedJson;

	public TuleapRestClientQueryTest(String label, String queryJson, String expectedJson,
			AbstractTuleapField field) {
		connector = new MockRestConnector();
		gson = TuleapGsonProvider.defaultGson();
		this.restResourceFactory = new RestResourceFactory(apiVersion, connector, gson, new TestLogger());
		this.repository = new TaskRepository("test", serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		connector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, gson, repository);

		project = new TuleapProject("prj", 101);
		tracker = new TuleapTracker(111, "t/111", "Bugs", "bug", "Bug tracker", new Date());
		project.addTracker(tracker);
		this.queryJson = queryJson;
		this.expectedJson = expectedJson;
		tracker.addField(field);
	}

	@Test
	public void test() throws Exception {
		IRepositoryQuery query = new RepositoryQuery("test", "handle");
		query.setAttribute(ITuleapQueryConstants.QUERY_KIND, ITuleapQueryConstants.QUERY_KIND_CUSTOM);
		query.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA, queryJson);

		connector.setResponse(new ServerResponse(200, "", Collections.<String, String> emptyMap()));

		client.getArtifactsFromQuery(query, tracker, null);

		assertEquals(1, connector.getInvocationsCount());
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(1, requestsSent.size());

		ServerRequest req = requestsSent.get(0);

		assertEquals("/api/v12.3/trackers/111/artifacts", req.url);

		String qs = URLDecoder.decode(req.queryString, "utf-8");

		System.out.println(qs);
		assertTrue(qs.indexOf("values=all") >= 0);
		assertTrue(qs.indexOf("limit=50") >= 0);
		assertTrue(qs.indexOf("query=" + expectedJson) >= 0);
	}

	// CHECKSTYLE:OFF inner blocks are useful here and too many instructions doen't matter
	@Parameters(name = "{index}: {0}")
	public static List<Object[]> data() {
		TuleapTracker tracker = new TuleapTracker(123, "t/123", "Bugs", "bug", "Tracker for bugs", new Date());
		TuleapProject project = new TuleapProject("prj", 101);
		project.addTracker(tracker);
		List<Object[]> result = new ArrayList<Object[]>();

		{
			TuleapText field = new TuleapText(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"ghjk\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"ghjk\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapString field = new TuleapString(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"ghjk\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"ghjk\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapFloat field = new TuleapFloat(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"2.5\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"2.5\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapInteger field = new TuleapInteger(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"2\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"2\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapArtifactLink field = new TuleapArtifactLink(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"123\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"123\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapSelectBox field = new TuleapSelectBox(666);
			field.setName("testField");
			TuleapSelectBoxItem item = new TuleapSelectBoxItem(1000);
			item.setLabel("lbl1000");
			field.addItem(item);
			item = new TuleapSelectBoxItem(1001);
			item.setLabel("lbl1001");
			field.addItem(item);
			item = new TuleapSelectBoxItem(1002);
			item.setLabel("lbl1002");
			field.addItem(item);
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"lbl1000\",\"lbl1002\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[1000,1002]}}", field, };
			result.add(testCase);
		}

		{
			TuleapMultiSelectBox field = new TuleapMultiSelectBox(666);
			field.setName("testField");
			TuleapSelectBoxItem item = new TuleapSelectBoxItem(1000);
			item.setLabel("lbl1000");
			field.addItem(item);
			item = new TuleapSelectBoxItem(1001);
			item.setLabel("lbl1001");
			field.addItem(item);
			item = new TuleapSelectBoxItem(1002);
			item.setLabel("lbl1002");
			field.addItem(item);
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName(),
					"{\"testField\":{\"operator\":\"contains\",\"value\":[\"lbl1000\",\"lbl1002\"]}}",
					"{\"testField\":{\"operator\":\"contains\",\"value\":[1000,1002]}}", field, };
			result.add(testCase);
		}

		{
			TuleapDate field = new TuleapDate(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName() + " - EQUALS",
					"{\"testField\":{\"operator\":\"=\",\"value\":[\"2014-07-14\"]}}",
					"{\"testField\":{\"operator\":\"=\",\"value\":[\"2014-07-14\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapDate field = new TuleapDate(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName() + " - LESS THAN",
					"{\"testField\":{\"operator\":\"<\",\"value\":[\"2014-07-14\"]}}",
					"{\"testField\":{\"operator\":\"<\",\"value\":[\"2014-07-14\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapDate field = new TuleapDate(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName() + " - GREATER THAN",
					"{\"testField\":{\"operator\":\">\",\"value\":[\"2014-07-14\"]}}",
					"{\"testField\":{\"operator\":\">\",\"value\":[\"2014-07-14\"]}}", field, };
			result.add(testCase);
		}

		{
			TuleapDate field = new TuleapDate(666);
			field.setName("testField");
			field.setPermissions(READ_CREATE_UPDATE);
			Object[] testCase = new Object[] {field.getClass().getSimpleName() + " - BETWEEN",
					"{\"testField\":{\"operator\":\"between\",\"value\":[\"2014-07-14\",\"2015-01-03\"]}}",
					"{\"testField\":{\"operator\":\"between\",\"value\":[\"2014-07-14\",\"2015-01-03\"]}}",
					field, };
			result.add(testCase);
		}

		return result;
	}
	// CHECKSTYLE:ON

}
