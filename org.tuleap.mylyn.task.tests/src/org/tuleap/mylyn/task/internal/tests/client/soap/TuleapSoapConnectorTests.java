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

import java.net.MalformedURLException;
import java.rmi.RemoteException;

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
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactComments;
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
}
