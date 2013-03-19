package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.WebLocation;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.CodendiAPIPortType;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Group;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Session;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.FieldValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.FieldValueFileInfo;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport;
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
		test.doStuff(args[0], args[1]);
		// test.doDownloadUploadStuff(args[0], args[1]);
	}

	private void doDownloadUploadStuff(String login, String password) {
		String domain = "https://demo.tuleap.net/projects/obeo";
		AbstractWebLocation location = new WebLocation(domain);

		EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
				CONFIG_FILE));
		TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, location);
		try {
			URL url = new URL("https://demo.tuleap.net/soap/");
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(login, password);
			String session_hash = session.getSession_hash();
			int user_id = session.getUser_id();

			System.out.println("Session hash: " + session_hash);
			System.out.println("User Id: " + user_id);

			int groupId = 141;

			Group group = codendiAPIPort.getGroupById(session_hash, groupId);
			String group_name = group.getGroup_name();
			System.out.println(group_name);

			config = new FileProvider(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
			TuleapTrackerV5APILocator tuleapLocator = new TuleapTrackerV5APILocatorImpl(config, location);
			url = new URL("https://demo.tuleap.net/plugins/tracker/soap/");
			TuleapTrackerV5APIPortType tuleapTrackerV5APIPort = tuleapLocator.getTuleapTrackerV5APIPort(url);
			Tracker[] trackers = tuleapTrackerV5APIPort.getTrackerList(session_hash, groupId);
			System.out.println("Trackers length: " + trackers.length);

			Artifact artifact = tuleapTrackerV5APIPort.getArtifact(session_hash, 0, 0, 739);
			ArtifactFieldValue[] artifactValue = artifact.getValue();
			for (ArtifactFieldValue artifactFieldValue : artifactValue) {
				if ("Attachments".equals(artifactFieldValue.getField_label())) {
					File file = null;
					int length2 = -1;
					int submitted_by = -1;
					String filetype = "";

					// download
					FieldValue field_value = artifactFieldValue.getField_value();
					String value = field_value.getValue();
					FieldValueFileInfo[] file_info = field_value.getFile_info();
					for (FieldValueFileInfo fieldValueFileInfo : file_info) {
						String action = fieldValueFileInfo.getAction();
						String description = fieldValueFileInfo.getDescription();
						int filesize = fieldValueFileInfo.getFilesize();
						filetype = fieldValueFileInfo.getFiletype();
						String id = fieldValueFileInfo.getId();
						String filename = fieldValueFileInfo.getFilename();
						submitted_by = fieldValueFileInfo.getSubmitted_by();

						String fileContent = "";

						int offset = 0;
						while (offset < filesize) {
							String artifactAttachmentChunk = tuleapTrackerV5APIPort
									.getArtifactAttachmentChunk(session_hash, 739, Integer.valueOf(id)
											.intValue(), offset, filesize);
							offset = offset + artifactAttachmentChunk.length();
							fileContent = fileContent + artifactAttachmentChunk;
						}

						byte[] decodeBase64 = Base64.decodeBase64(fileContent.getBytes());
						String content = new String(decodeBase64);
						int length = content.length();
						length2 = decodeBase64.length;
						file = new File("/" + filename);
						String absolutePath = file.getAbsolutePath();
						try {
							if (file.exists()) {
								file.delete();
							}
							file.createNewFile();
							FileOutputStream fileOutputStream = new FileOutputStream(file);
							fileOutputStream.write(decodeBase64);
							fileOutputStream.flush();
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					// Upload
					if (file != null) {
						try {
							tuleapTrackerV5APIPort.purgeAllTemporaryAttachments(session_hash);

							FileInputStream fileInputStream = new FileInputStream(file);
							byte[] bytes = new byte[length2];
							fileInputStream.read(bytes);
							fileInputStream.close();

							int length = bytes.length;
							byte[] encodeBase64 = Base64.encodeBase64(bytes);
							String attachmentName = tuleapTrackerV5APIPort
									.createTemporaryAttachment(session_hash);
							tuleapTrackerV5APIPort.appendTemporaryAttachmentChunk(session_hash,
									attachmentName, new String(encodeBase64));

							FieldValueFileInfo fi = new FieldValueFileInfo(attachmentName, submitted_by,
									"Copy of the previous attachement again", file.getName(), length2,
									filetype, "");
							FieldValueFileInfo[] f_i = new FieldValueFileInfo[] {fi };
							TrackerFieldBindValue[] trackerFieldBindValue = new TrackerFieldBindValue[] {};
							FieldValue f_v = new FieldValue(null, f_i, trackerFieldBindValue);
							ArtifactFieldValue fv = new ArtifactFieldValue(
									artifactFieldValue.getField_name(), artifactFieldValue.getField_label(),
									f_v);
							ArtifactFieldValue[] fieldValue = new ArtifactFieldValue[] {fv };
							tuleapTrackerV5APIPort.updateArtifact(session_hash, 141, 484, 739, fieldValue,
									"New attachement", "UTF-8");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

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

	private void doStuff(String login, String password) {
		String domain = "https://demo.tuleap.net/projects/obeo";
		AbstractWebLocation location = new WebLocation(domain);

		EngineConfiguration config = new FileProvider(getClass().getClassLoader().getResourceAsStream(
				CONFIG_FILE));
		TuleapSoapServiceLocator locator = new TuleapSoapServiceLocator(config, location);
		try {
			URL url = new URL("https://demo.tuleap.net/soap/");
			CodendiAPIPortType codendiAPIPort = locator.getCodendiAPIPort(url);
			Session session = codendiAPIPort.login(login, password);
			String session_hash = session.getSession_hash();
			int user_id = session.getUser_id();

			System.out.println("Session hash: " + session_hash);
			System.out.println("User Id: " + user_id);

			int groupId = 141;

			Group group = codendiAPIPort.getGroupById(session_hash, groupId);
			String group_name = group.getGroup_name();
			System.out.println(group_name);

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
						FieldValue field_value = artifactFieldValue.getField_value();
						FieldValueFileInfo[] file_info = field_value.getFile_info();
						System.out.println("Name: " + field_name + " Value: " + field_value.getValue());
					}

					// Comments
					// try {
					// ArtifactComments[] artifactComments = tuleapTrackerV5APIPort.getArtifactComments(
					// session_hash, artifact_id);
					// for (ArtifactComments artifactComment : artifactComments) {
					// int artifactCommentSubmitted_by = artifactComment.getSubmitted_by();
					// int artifactCommentSubmitted_on = artifactComment.getSubmitted_on();
					// String email = artifactComment.getEmail();
					// String body = artifactComment.getBody();
					// System.out.println("Comment from '" + artifactCommentSubmitted_by
					// + "', submitted on '" + artifactCommentSubmitted_on + "'");
					// System.out.println("Email: " + email);
					// System.out.println(body);
					// System.out.println();
					// }
					// } catch (RemoteException e) {
					// System.out.println("Comments are not working");
					// e.printStackTrace();
					// }
				}

				// Reports
				TrackerReport[] trackerReports = tuleapTrackerV5APIPort.getTrackerReports(session_hash,
						groupId, tracker_id);
				for (TrackerReport trackerReport : trackerReports) {
					int trackerReportId = trackerReport.getId();
					int trackerReportUserId = trackerReport.getUser_id();
					String trackerReportName = trackerReport.getName();
					String trackerReportDescription = trackerReport.getDescription();
					System.out.println("Report '" + trackerReportName + "', " + trackerReportDescription);

					ArtifactQueryResult artifactsFromReport = tuleapTrackerV5APIPort.getArtifactsFromReport(
							session_hash, trackerReportId, 0, 100);
					Artifact[] artifactsDownloadedFromReport = artifactsFromReport.getArtifacts();

					for (Artifact artifact : artifactsDownloadedFromReport) {
						int artifact_id = artifact.getArtifact_id();
						System.out.println("Artifact Id: " + artifact_id);

						ArtifactFieldValue[] artifactFieldValues = artifact.getValue();
						for (ArtifactFieldValue artifactFieldValue : artifactFieldValues) {
							String field_name = artifactFieldValue.getField_name();
							FieldValue field_value = artifactFieldValue.getField_value();
							FieldValueFileInfo[] file_info = field_value.getFile_info();
							System.out.println("Name: " + field_name + " Value: " + field_value.getValue());
						}
					}
				}

				if (tracker_id == 524) {
					TrackerFieldBindValue trackerFieldBindValue = new TrackerFieldBindValue(100, "");
					FieldValue field_value = new FieldValue("100", new FieldValueFileInfo[] {},
							new TrackerFieldBindValue[] {trackerFieldBindValue });
					ArtifactFieldValue artifactFieldValue = new ArtifactFieldValue("components_affected",
							"Components Affected", field_value);
					ArtifactFieldValue[] value = new ArtifactFieldValue[] {artifactFieldValue };
					int updatedArtifactId = tuleapTrackerV5APIPort.updateArtifact(session_hash, groupId,
							tracker_id, 896, value, "Updated without changes", "UTF-8");
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
