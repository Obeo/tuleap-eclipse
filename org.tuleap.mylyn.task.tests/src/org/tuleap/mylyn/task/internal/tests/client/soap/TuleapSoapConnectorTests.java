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

import com.google.common.collect.Lists;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.soap.CommentedArtifact;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.config.ITuleapConfigurationConstants;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIBindingStub;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Group;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactComments;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Tracker;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerField;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APIBindingStub;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.fail;

/**
 * Utility class to tests the SOAP connector.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapSoapConnectorTests {

	/**
	 * Test the validation of the connection.
	 */
	@Test
	public void testValidateConnection() {
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");
		AuthenticationCredentials credentials = new AuthenticationCredentials("admin", "password");
		taskRepository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);

		AbstractWebLocation location = new TaskRepositoryLocationFactory().createWebLocation(taskRepository);

		try {
			TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(location) {
				@Override
				protected IStatus login(IProgressMonitor monitor) throws MalformedURLException,
						ServiceException, RemoteException {
					return Status.OK_STATUS;
				}

				@Override
				protected IStatus logout() {
					return Status.OK_STATUS;
				}
			};

			IStatus status = tuleapSoapConnector.validateConnection(new NullProgressMonitor());
			assertThat(status.getSeverity(), is(IStatus.OK));
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (RemoteException e) {
			fail(e.getMessage());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the retrieval of a Tuleap artifact.
	 */
	@Test
	public void testGetArtifact() {
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");
		AuthenticationCredentials credentials = new AuthenticationCredentials("admin", "password");
		taskRepository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);

		AbstractWebLocation location = new TaskRepositoryLocationFactory().createWebLocation(taskRepository);

		try {
			int elementId = 42;

			final Artifact artifact = new Artifact();
			artifact.setArtifact_id(elementId);

			final ArtifactComments[] artifactComments = new ArtifactComments[1];

			int submittedBy = 17;
			String email = "email@email.com";
			int submittedOn = 42;
			String body = "body";
			artifactComments[0] = new ArtifactComments(submittedBy, email, submittedOn, body);

			final TuleapTrackerV5APIPortType tuleapTrackerV5APIPortType = new TuleapTrackerV5APIBindingStub() {
				@Override
				public Artifact getArtifact(String sessionKey, int groupId, int trackerId, int artifactId)
						throws RemoteException {
					return artifact;
				}

				@Override
				public ArtifactComments[] getArtifactComments(String sessionKey, int artifactId)
						throws RemoteException {
					return artifactComments;
				}
			};

			TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(location) {
				@Override
				protected IStatus login(IProgressMonitor monitor) throws MalformedURLException,
						ServiceException, RemoteException {
					return Status.OK_STATUS;
				}

				@Override
				protected IStatus logout() {
					return Status.OK_STATUS;
				}

				@Override
				protected TuleapTrackerV5APIPortType getTuleapTrackerV5APIPortType() {
					return tuleapTrackerV5APIPortType;
				}
			};

			TuleapServerConfiguration serverConfiguration = new TuleapServerConfiguration(
					"https://tuleap.net");
			TuleapPerson person = new TuleapPerson(null, null, submittedBy, email);
			serverConfiguration.register(person);

			CommentedArtifact commentedArtifact = tuleapSoapConnector.getArtifact(elementId,
					serverConfiguration, new NullProgressMonitor());
			assertThat(commentedArtifact.getArtifact().getArtifact_id(), is(42));
			assertThat(commentedArtifact.getComments().size(), is(1));
			assertThat(commentedArtifact.getComments().get(0).getSubmittedOn(), is(submittedOn));
			assertThat(commentedArtifact.getComments().get(0).getBody(), is(body));
			assertThat(commentedArtifact.getComments().get(0).getSubmitter(), is(notNullValue()));
			assertThat(commentedArtifact.getComments().get(0).getSubmitter().getEmail(), is(email));
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (RemoteException e) {
			fail(e.getMessage());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the creation of an artifact.
	 */
	@Test
	public void testCreateArtifact() {
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");
		AuthenticationCredentials credentials = new AuthenticationCredentials("admin", "password");
		taskRepository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);

		AbstractWebLocation location = new TaskRepositoryLocationFactory().createWebLocation(taskRepository);

		try {
			final int projectId = 137;
			final TuleapReference project = new TuleapReference(projectId, "p/137");
			final int configurationId = 25;
			final TuleapReference trackerRef = new TuleapReference(25, "t/25");
			final int elementId = 42;

			final Artifact artifact = new Artifact();
			artifact.setArtifact_id(elementId);

			final ArtifactComments[] artifactComments = new ArtifactComments[1];

			int submittedBy = 17;
			String email = "email@email.com";
			int submittedOn = 42;
			String body = "body";
			artifactComments[0] = new ArtifactComments(submittedBy, email, submittedOn, body);

			final Tracker tracker = new Tracker();
			tracker.setTracker_id(configurationId);

			final TrackerField[] trackerFields = new TrackerField[2];

			int firstFieldId = 22;
			TrackerField trackerField = new TrackerField(configurationId, firstFieldId, null, null,
					ITuleapConfigurationConstants.ARTLINK, null, null, new String[] {"submit", "update" });
			trackerFields[0] = trackerField;

			int bindValueId1 = 4745;
			int bindValueId2 = 89432;
			int bindValueId3 = 7945;

			int secondFieldId = 4;
			trackerField = new TrackerField(configurationId, secondFieldId, null, null,
					ITuleapConfigurationConstants.CB, null, null, new String[] {"submit", "update" });
			trackerField.setValues(new TrackerFieldBindValue[] {
					new TrackerFieldBindValue(bindValueId1, null),
					new TrackerFieldBindValue(bindValueId2, null),
					new TrackerFieldBindValue(bindValueId3, null), });
			trackerFields[1] = trackerField;

			final String literalFieldValue = "785, 213, 456786, 876";
			final List<Integer> boundFieldIds = Lists.newArrayList(bindValueId1, bindValueId2, bindValueId3);

			final CodendiAPIPortType codendiAPIPortType = new CodendiAPIBindingStub() {

				@Override
				public Group[] getMyProjects(String sessionKey) throws RemoteException {
					return new Group[] {new Group(projectId, null, null, null) };
				}
			};

			final TuleapTrackerV5APIPortType tuleapTrackerV5APIPortType = new TuleapTrackerV5APIBindingStub() {
				@Override
				public Tracker[] getTrackerList(String sessionKey, int groupId) throws RemoteException {
					return new Tracker[] {tracker, };
				}

				@Override
				public TrackerField[] getTrackerFields(String sessionKey, int groupId, int trackerId)
						throws RemoteException {
					return trackerFields;
				}

				@Override
				public int addArtifact(String sessionKey, int groupId, int trackerId,
						ArtifactFieldValue[] value) throws RemoteException {
					assertThat(value.length, is(2));
					assertThat(value[0].getField_value().getValue(), is(literalFieldValue));
					assertThat(value[1].getField_value().getBind_value().length, is(boundFieldIds.size()));
					for (int i = 0; i < value[1].getField_value().getBind_value().length; i++) {
						boundFieldIds.contains(value[1].getField_value().getBind_value()[i]
								.getBind_value_id());
					}
					return elementId;
				}
			};

			TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(location) {
				@Override
				protected IStatus login(IProgressMonitor monitor) throws MalformedURLException,
						ServiceException, RemoteException {
					return Status.OK_STATUS;
				}

				@Override
				protected IStatus logout() {
					return Status.OK_STATUS;
				}

				@Override
				protected TuleapTrackerV5APIPortType getTuleapTrackerV5APIPortType() {
					return tuleapTrackerV5APIPortType;
				}

				@Override
				protected CodendiAPIPortType getCodendiAPIPortType() {
					return codendiAPIPortType;
				}
			};

			TuleapArtifact tuleapArtifact = new TuleapArtifact(trackerRef, project);
			AbstractFieldValue firstFieldValue = new LiteralFieldValue(firstFieldId, literalFieldValue);
			tuleapArtifact.addFieldValue(firstFieldValue);
			AbstractFieldValue secondFieldValue = new BoundFieldValue(secondFieldId, boundFieldIds);
			tuleapArtifact.addFieldValue(secondFieldValue);

			String artifactIdentifier = tuleapSoapConnector.createArtifact(tuleapArtifact,
					new NullProgressMonitor());
			assertThat(artifactIdentifier, is(TuleapTaskIdentityUtil.getTaskDataId(projectId,
					configurationId, elementId)));
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (RemoteException e) {
			fail(e.getMessage());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}
}
