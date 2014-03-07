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
package org.tuleap.mylyn.task.internal.tests.client.rest;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.commons.net.WebLocation;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.parser.TuleapGsonProvider;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.TuleapSoapServiceLocator;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.TuleapTrackerV5APILocatorImpl;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Session;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactQueryResult;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Criteria;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerField;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APILocator;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;

/**
 * Tests differences between SOAP et REST APIs.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapSOAPRestCompareTest {

	private static final String CONFIG_FILE = "org/tuleap/mylyn/task/internal/core/wsdl/soap/client-config.wsdd"; //$NON-NLS-1$

	private Gson gson;

	@Test
	public void testCompareTrackerArtifacts() throws RemoteException, ServiceException, CoreException {

		// We have to enter the right parameters
		String login = "";
		String password = "";
		int projectId = 101;
		int trackerId = 149;
		String repositoryUrl = "https://tuleap.net";

		// Access to the server using SOAP API
		String domain = "https://tuleap.net/projects/eclipse-agile-planner/";
		AbstractWebLocation location = new WebLocation(domain);

		EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
				CONFIG_FILE));
		TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, location);

		URL url = null;
		try {
			url = new URL("https://tuleap.net/soap/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
		Session session = codendiAPIPort.login(login, password);
		String sessionhash = session.getSession_hash();
		int userid = session.getUser_id();

		System.out.println("Session hash: " + sessionhash);
		System.out.println("User Id: " + userid);

		config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
		TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config, location);
		try {
			url = new URL("https://tuleap.net/plugins/tracker/soap/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);
		Criteria[] criteria = new Criteria[] {};

		ArtifactQueryResult artifacts = tuleapTrackerV5APIPort.getArtifacts(sessionhash, projectId,
				trackerId, criteria, 0, 10);
		Artifact[] downloadedArtifacts = artifacts.getArtifacts();

		// Access to the server using REST API
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, repositoryUrl);
		AuthenticationCredentials credentials = new AuthenticationCredentials(login, password);
		taskRepository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);
		AbstractWebLocation webLocation = new TaskRepositoryLocationFactory()
				.createWebLocation(taskRepository);
		ILog logger = TuleapCoreActivator.getDefault().getLog();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(webLocation, logger);
		//
		RestResourceFactory restResourceFactory = new RestResourceFactory(RestResourceFactory.BEST_VERSION,
				tuleapRestConnector, gson, logger);
		TuleapRestClient tuleapRestClient = new TuleapRestClient(restResourceFactory, gson, taskRepository);
		tuleapRestClient.login();

		for (Artifact artifact : downloadedArtifacts) {
			int artifactId = artifact.getArtifact_id();
			TuleapArtifact tuleapArtifact = tuleapRestClient.getArtifact(artifactId, new TuleapServer(
					repositoryUrl), null);

			System.out.println("--------------------");
			if (artifact.getLast_update_date() != tuleapArtifact.getLastModifiedDate().getTime() / 1000) {
				System.out.println("The field \"last modified date\" of the artifact " + artifactId
						+ " is not the same on REST and SOAP:");
				System.out.println("In the SOAP side it has the value: " + artifact.getLast_update_date());
				System.out.println("In the REST side it has the value: "
						+ tuleapArtifact.getLastModifiedDate().getTime());
			} else if (artifact.getSubmitted_by() != tuleapArtifact.getSubmittedBy()) {
				System.out.println("The field \"submitted by\" of the artifact " + artifactId
						+ " is not the same on REST and SOAP:");
				System.out.println("In the SOAP side it has the value: " + artifact.getSubmitted_by());
				System.out.println("In the REST side it has the value: " + tuleapArtifact.getSubmittedBy());
			} else if (artifact.getSubmitted_on() != tuleapArtifact.getSubmittedOn().getTime()) {
				System.out.println("The field \"submitted on\" of the artifact " + artifactId
						+ " is not the same on REST and SOAP:");
				System.out.println("In the SOAP side it has the value: " + artifact.getSubmitted_on());
				System.out.println("In the REST side it has the value: "
						+ tuleapArtifact.getSubmittedOn().getTime());
			} else if (artifact.getSubmitted_on() != tuleapArtifact.getSubmittedOn().getTime()) {
				System.out.println("The tracker of the artifact " + artifactId
						+ " is not the same on REST and SOAP:");
				System.out.println("In the SOAP side it has the value: " + artifact.getTracker_id());
				System.out.println("In the REST side it has the value: "
						+ tuleapArtifact.getTracker().getId());

			}

			System.out.println("The number of fields of artifact " + artifactId
					+ ", gotten from REST API is :" + tuleapArtifact.getFieldValues().size());

			System.out.println("The number of fields of artifact " + artifactId
					+ ", gotten from SOAP API is :" + artifact.getValue().length);

			TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(sessionhash, projectId,
					trackerId);
			Collection<AbstractFieldValue> fields = tuleapArtifact.getFieldValues();
			ArtifactFieldValue[] artifactFieldValues = artifact.getValue();
			List<TrackerField> communTrackerRest = this.getCommunFieldsBetweenTrackerAndRest(fields,
					trackerFields);
			List<TrackerField> communTrackerSoap = this.getCommunFieldsBetweenTrackerAndSOAP(
					artifactFieldValues, trackerFields);

			for (TrackerField trackerField : communTrackerRest) {
				System.out.println("The field " + trackerField.getField_id() + " (" + trackerField.getLabel()
						+ ")" + " of the artifact " + tuleapArtifact.getId() + " exist on REST side");
			}

			System.out.println("***");
			for (TrackerField trackerField : communTrackerSoap) {
				System.out.println("The field " + trackerField.getField_id() + " (" + trackerField.getLabel()
						+ ")" + " of the artifact " + tuleapArtifact.getId() + " exist on SOAP side");
			}
		}
	}

	private List<TrackerField> getCommunFieldsBetweenTrackerAndRest(Collection<AbstractFieldValue> fields,
			TrackerField[] trackerFields) {
		List<TrackerField> result = new ArrayList<TrackerField>();
		for (AbstractFieldValue abstractFieldValue : fields) {
			for (TrackerField trackerField : trackerFields) {
				if (trackerField.getField_id() == abstractFieldValue.getFieldId()) {
					result.add(trackerField);
				}
			}
		}
		return result;
	}

	private List<TrackerField> getCommunFieldsBetweenTrackerAndSOAP(ArtifactFieldValue[] soapfields,
			TrackerField[] trackerFields) {
		List<TrackerField> result = new ArrayList<TrackerField>();
		for (ArtifactFieldValue soapField : soapfields) {
			for (TrackerField trackerField : trackerFields) {
				if (soapField.getField_name().equals(trackerField.getShort_name())) {
					result.add(trackerField);
				}
			}
		}
		return result;
	}

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}
}
