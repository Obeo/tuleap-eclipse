package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.WebLocation;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.CodendiAPIPortType;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Session;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemantic;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflow;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APILocator;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;

/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class Test {
	private static final String CONFIG_FILE = "org/eclipse/mylyn/internal/tuleap/core/wsdl/soap/client-config.wsdd"; //$NON-NLS-1$

	public static void main(String[] args) {
		Test test = new Test();
		test.doStuff();
	}

	private void doStuff() {
		String domain = "https://demo.tuleap.net/projects/obeo";
		AbstractWebLocation location = new WebLocation(domain);

		EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
				CONFIG_FILE));
		TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, location);
		try {
			String loginname = "";
			String passwd = "";

			URL url = new URL("https://demo.tuleap.net/soap/");
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(loginname, passwd);
			String session_hash = session.getSession_hash();
			int user_id = session.getUser_id();

			System.out.println("Session hash: " + session_hash);
			System.out.println("User Id: " + user_id);

			int groupId = 141;

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config, location);
			url = new URL("https://demo.tuleap.net/plugins/tracker/soap/");
			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);
			Tracker[] trackers = tuleapTrackerV5APIPort.getTrackerList(session_hash, groupId);
			System.out.println("Trackers length: " + trackers.length);

			for (Tracker tracker : trackers) {
				String name = tracker.getName();
				String item_name = tracker.getItem_name();
				String description = tracker.getDescription();
				int tracker_id = tracker.getTracker_id();
				System.out.println("Tracker " + name + " (" + item_name + ") [" + tracker_id + "] "
						+ description);

				TrackerField[] trackerFields = tuleapTrackerV5APIPort.getTrackerFields(session_hash, groupId,
						tracker_id);
				System.out.println("Tracker Fields Length: " + trackerFields.length);

				TrackerStructure trackerStructure = tuleapTrackerV5APIPort.getTrackerStructure(session_hash,
						groupId, tracker_id);
				TrackerSemantic semantic = trackerStructure.getSemantic();
				TrackerWorkflow workflow = trackerStructure.getWorkflow();
				System.out.println("Semantic : " + semantic.getContributor().getField_name() + " "
						+ semantic.getTitle().getField_name() + " " + semantic.getStatus().getField_name());
				System.out.println("Semantic Title: " + semantic.getTitle().getField_name());
				System.out.println("Semantic Status: " + semantic.getStatus().getField_name());
				System.out.println("Semantic Status Values Length: "
						+ semantic.getStatus().getValues().length);
				System.out.println("Semantic Contributor: " + semantic.getContributor().getField_name());
				System.out.println("Workflow Id: " + workflow.getField_id());
				System.out.println("Workflow Transitions Length: " + workflow.getTransitions().length);

				Criteria[] criteria = new Criteria[] {};
				ArtifactQueryResult artifacts = tuleapTrackerV5APIPort.getArtifacts(session_hash, groupId,
						tracker_id, criteria, 0, 10);
				int total_artifacts_number = artifacts.getTotal_artifacts_number();
				System.out.println("Number of artifacts: " + total_artifacts_number);

				Artifact[] downloadedArtifacts = artifacts.getArtifacts();
				for (Artifact artifact : downloadedArtifacts) {
					int artifact_id = artifact.getArtifact_id();
					System.out.println("Artifact Id: " + artifact_id);

					ArtifactFieldValue[] artifactFieldValues = artifact.getValue();
					for (ArtifactFieldValue artifactFieldValue : artifactFieldValues) {
						String field_name = artifactFieldValue.getField_name();
						String field_value = artifactFieldValue.getField_value();
						System.out.println("Name: " + field_name + " Value: " + field_value);
					}
				}

				if (tracker_id == 484) {
					ArtifactFieldValue[] value = new ArtifactFieldValue[] {new ArtifactFieldValue("String",
							"string", "New Artifact " + System.currentTimeMillis()) };
					int artifact_id = tuleapTrackerV5APIPort.addArtifact(session_hash, groupId, tracker_id,
							value);
					System.out.println("Id of the newly created artifact: " + artifact_id);

					int updatedArtifactId = tuleapTrackerV5APIPort.updateArtifact(session_hash, groupId,
							tracker_id, artifact_id, value, "Updated without changes", "UTF-8");
					System.out.println("Updated artifact id: " + updatedArtifactId);
				}
			}

			codendiAPIPort.logout(session_hash);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
