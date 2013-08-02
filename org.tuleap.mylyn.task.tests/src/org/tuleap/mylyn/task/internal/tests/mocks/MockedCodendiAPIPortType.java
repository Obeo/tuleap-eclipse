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
package org.tuleap.mylyn.task.internal.tests.mocks;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Artifact;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactCC;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactCanned;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactDependency;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldNameValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFile;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFollowup;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFromReportResult;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactHistory;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactQueryResult;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactReport;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Criteria;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Docman_Item;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.FRSFile;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.FRSPackage;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.FRSRelease;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Group;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ItemInfo;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Metadata;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Session;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.SortCriteria;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.TrackerDesc;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Ugroup;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo;

/**
 * A mock of the common SOAP API.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedCodendiAPIPortType implements CodendiAPIPortType {
	/**
	 * Mocks of users.
	 */
	private Map<String, MockedUserInfo> usersMap = new HashMap<String, MockedUserInfo>();

	// CHECKSTYLE:OFF
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#login(java.lang.String,
	 *      java.lang.String)
	 */
	public Session login(String loginname, String passwd) throws RemoteException {
		return new Session();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#loginAs(java.lang.String,
	 *      java.lang.String)
	 */
	public String loginAs(String admin_session_hash, String loginname) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#retrieveSession(java.lang.String)
	 */
	public Session retrieveSession(String session_hash) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getAPIVersion()
	 */
	public String getAPIVersion() throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#logout(java.lang.String)
	 */
	public void logout(String sessionKey) throws RemoteException {
		// Do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getMyProjects(java.lang.String)
	 */
	public Group[] getMyProjects(String sessionKey) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getGroupByName(java.lang.String,
	 *      java.lang.String)
	 */
	public Group getGroupByName(String sessionKey, String unix_group_name) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getGroupById(java.lang.String,
	 *      int)
	 */
	public Group getGroupById(String sessionKey, int group_id) throws RemoteException {
		return new Group();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getGroupUgroups(java.lang.String,
	 *      int)
	 */
	public Ugroup[] getGroupUgroups(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getProjectGroupsAndUsers(java.lang.String,
	 *      int)
	 */
	public Ugroup[] getProjectGroupsAndUsers(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#checkUsersExistence(java.lang.String,
	 *      java.lang.String[])
	 */
	public UserInfo[] checkUsersExistence(String sessionKey, String[] users) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getUserInfo(java.lang.String,
	 *      int)
	 */
	public UserInfo getUserInfo(String sessionKey, int user_id) throws RemoteException {
		return usersMap.get(String.valueOf(user_id));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getTrackerList(java.lang.String,
	 *      int)
	 */
	public TrackerDesc[] getTrackerList(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactType(java.lang.String,
	 *      int, int)
	 */
	public ArtifactType getArtifactType(String sessionKey, int group_id, int group_artifact_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactTypes(java.lang.String,
	 *      int)
	 */
	public ArtifactType[] getArtifactTypes(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifacts(java.lang.String,
	 *      int, int, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Criteria[], int, int)
	 */
	public ArtifactQueryResult getArtifacts(String sessionKey, int group_id, int group_artifact_id,
			Criteria[] criteria, int offset, int max_rows) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactsFromReport(java.lang.String,
	 *      int, int, int, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Criteria[], int, int,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.SortCriteria[])
	 */
	public ArtifactFromReportResult getArtifactsFromReport(String sessionKey, int group_id,
			int group_artifact_id, int report_id, Criteria[] criteria, int offset, int max_rows,
			SortCriteria[] sort_criteria) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addArtifact(java.lang.String,
	 *      int, int, int, int, java.lang.String, java.lang.String, int,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValue[])
	 */
	public int addArtifact(String sessionKey, int group_id, int group_artifact_id, int status_id,
			int close_date, String summary, String details, int severity, ArtifactFieldValue[] extra_fields)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addArtifactWithFieldNames(java.lang.String,
	 *      int, int, int, int, java.lang.String, java.lang.String, int,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldNameValue[])
	 */
	public int addArtifactWithFieldNames(String sessionKey, int group_id, int group_artifact_id,
			int status_id, int close_date, String summary, String details, int severity,
			ArtifactFieldNameValue[] extra_fields) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateArtifact(java.lang.String,
	 *      int, int, int, int, int, java.lang.String, java.lang.String, int,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValue[])
	 */
	public int updateArtifact(String sessionKey, int group_id, int group_artifact_id, int artifact_id,
			int status_id, int close_date, String summary, String details, int severity,
			ArtifactFieldValue[] extra_fields) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateArtifactWithFieldNames(java.lang.String,
	 *      int, int, int, int, int, java.lang.String, java.lang.String, int,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldNameValue[])
	 */
	public int updateArtifactWithFieldNames(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, int status_id, int close_date, String summary, String details, int severity,
			ArtifactFieldNameValue[] extra_fields) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactFollowups(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactFollowup[] getArtifactFollowups(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactCannedResponses(java.lang.String,
	 *      int, int)
	 */
	public ArtifactCanned[] getArtifactCannedResponses(String sessionKey, int group_id, int group_artifact_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactReports(java.lang.String,
	 *      int, int)
	 */
	public ArtifactReport[] getArtifactReports(String sessionKey, int group_id, int group_artifact_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactAttachedFiles(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactFile[] getArtifactAttachedFiles(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactAttachedFile(java.lang.String,
	 *      int, int, int, int)
	 */
	public ArtifactFile getArtifactAttachedFile(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, int file_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactById(java.lang.String,
	 *      int, int, int)
	 */
	public Artifact getArtifactById(String sessionKey, int group_id, int group_artifact_id, int artifact_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactDependencies(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactDependency[] getArtifactDependencies(String sessionKey, int group_id,
			int group_artifact_id, int artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactInverseDependencies(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactDependency[] getArtifactInverseDependencies(String sessionKey, int group_id,
			int group_artifact_id, int artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addArtifactAttachedFile(java.lang.String,
	 *      int, int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int addArtifactAttachedFile(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, String encoded_data, String description, String filename, String filetype)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteArtifactAttachedFile(java.lang.String,
	 *      int, int, int, int)
	 */
	public int deleteArtifactAttachedFile(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, int file_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addArtifactDependencies(java.lang.String,
	 *      int, int, int, java.lang.String)
	 */
	public boolean addArtifactDependencies(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, String is_dependent_on_artifact_ids) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteArtifactDependency(java.lang.String,
	 *      int, int, int, int)
	 */
	public int deleteArtifactDependency(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, int dependent_on_artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addArtifactFollowup(java.lang.String,
	 *      int, int, int, java.lang.String, int, int)
	 */
	public boolean addArtifactFollowup(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, String body, int comment_type_id, int format) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateArtifactFollowUp(java.lang.String,
	 *      int, int, int, int, java.lang.String)
	 */
	public boolean updateArtifactFollowUp(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, int artifact_history_id, String comment) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteArtifactFollowUp(java.lang.String,
	 *      int, int, int, int)
	 */
	public boolean deleteArtifactFollowUp(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id, int artifact_history_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#existArtifactSummary(java.lang.String,
	 *      int, java.lang.String)
	 */
	public int existArtifactSummary(String sessionKey, int group_artifact_id, String summary)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactCCList(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactCC[] getArtifactCCList(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addArtifactCC(java.lang.String,
	 *      int, int, int, java.lang.String, java.lang.String)
	 */
	public boolean addArtifactCC(String sessionKey, int group_id, int group_artifact_id, int artifact_id,
			String cc_list, String cc_comment) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteArtifactCC(java.lang.String,
	 *      int, int, int, int)
	 */
	public boolean deleteArtifactCC(String sessionKey, int group_id, int group_artifact_id, int artifact_id,
			int artifact_cc_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getArtifactHistory(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactHistory[] getArtifactHistory(String sessionKey, int group_id, int group_artifact_id,
			int artifact_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getPackages(java.lang.String,
	 *      int)
	 */
	public FRSPackage[] getPackages(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addPackage(java.lang.String,
	 *      int, java.lang.String, int, int, boolean)
	 */
	public int addPackage(String sessionKey, int group_id, String package_name, int status_id, int rank,
			boolean approve_license) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getReleases(java.lang.String,
	 *      int, int)
	 */
	public FRSRelease[] getReleases(String sessionKey, int group_id, int package_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addRelease(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public int addRelease(String sessionKey, int group_id, int package_id, String name, String notes,
			String changes, int status_id, int release_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getFiles(java.lang.String,
	 *      int, int, int)
	 */
	public FRSFile[] getFiles(String sessionKey, int group_id, int package_id, int release_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getFileInfo(java.lang.String,
	 *      int, int, int, int)
	 */
	public FRSFile getFileInfo(String sessionKey, int group_id, int package_id, int release_id, int file_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getFile(java.lang.String,
	 *      int, int, int, int)
	 */
	public String getFile(String sessionKey, int group_id, int package_id, int release_id, int file_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getFileChunk(java.lang.String,
	 *      int, int, int, int, int, int)
	 */
	public String getFileChunk(String sessionKey, int group_id, int package_id, int release_id, int file_id,
			int offset, int size) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addFile(java.lang.String,
	 *      int, int, int, java.lang.String, java.lang.String, int, int, java.lang.String)
	 */
	public int addFile(String sessionKey, int group_id, int package_id, int release_id, String filename,
			String base64_contents, int type_id, int processor_id, String reference_md5)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addFileChunk(java.lang.String,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	public BigInteger addFileChunk(String sessionKey, String filename, String contents, boolean first_chunk)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#addUploadedFile(java.lang.String,
	 *      int, int, int, java.lang.String, int, int, java.lang.String)
	 */
	public int addUploadedFile(String sessionKey, int group_id, int package_id, int release_id,
			String filename, int type_id, int processor_id, String reference_md5) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getUploadedFiles(java.lang.String,
	 *      int)
	 */
	public String[] getUploadedFiles(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteFile(java.lang.String,
	 *      int, int, int, int)
	 */
	public boolean deleteFile(String sessionKey, int group_id, int package_id, int release_id, int file_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteEmptyPackage(java.lang.String,
	 *      int, int, boolean)
	 */
	public FRSPackage[] deleteEmptyPackage(String sessionKey, int group_id, int package_id,
			boolean cleanup_all) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteEmptyRelease(java.lang.String,
	 *      int, int, int, boolean)
	 */
	public FRSRelease[] deleteEmptyRelease(String sessionKey, int group_id, int package_id, int release_id,
			boolean cleanup_all) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getRootFolder(java.lang.String,
	 *      int)
	 */
	public int getRootFolder(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#listFolder(java.lang.String,
	 *      int, int)
	 */
	public Docman_Item[] listFolder(String sessionKey, int group_id, int item_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#searchDocmanItem(java.lang.String,
	 *      int, int, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Criteria[])
	 */
	public Docman_Item[] searchDocmanItem(String sessionKey, int group_id, int item_id, Criteria[] criterias)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getDocmanFileContents(java.lang.String,
	 *      int, int, int)
	 */
	public String getDocmanFileContents(String sessionKey, int group_id, int item_id, int version_number)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getDocmanFileMD5Sum(java.lang.String,
	 *      int, int, int)
	 */
	public String getDocmanFileMD5Sum(String sessionKey, int group_id, int item_id, int version_number)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getDocmanFileAllVersionsMD5Sum(java.lang.String,
	 *      int, int)
	 */
	public String[] getDocmanFileAllVersionsMD5Sum(String sessionKey, int group_id, int item_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getDocmanProjectMetadata(java.lang.String,
	 *      int)
	 */
	public Metadata[] getDocmanProjectMetadata(String sessionKey, int group_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getDocmanTreeInfo(java.lang.String,
	 *      int, int)
	 */
	public ItemInfo[] getDocmanTreeInfo(String sessionKey, int group_id, int parent_id)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanFolder(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int createDocmanFolder(String sessionKey, int group_id, int parent_id, String title,
			String description, String ordering, String status, Permission[] permissions,
			MetadataValue[] metadata, String owner, String create_date, String update_date)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanFile(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], int, java.lang.String,
	 *      java.lang.String, java.lang.String, int, int, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public int createDocmanFile(String sessionKey, int group_id, int parent_id, String title,
			String description, String ordering, String status, String obsolescence_date,
			Permission[] permissions, MetadataValue[] metadata, int file_size, String file_name,
			String mime_type, String content, int chunk_offset, int chunk_size, String author, String date,
			String owner, String create_date, String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanEmbeddedFile(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int createDocmanEmbeddedFile(String sessionKey, int group_id, int parent_id, String title,
			String description, String ordering, String status, String obsolescence_date, String content,
			Permission[] permissions, MetadataValue[] metadata, String author, String date, String owner,
			String create_date, String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanWikiPage(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int createDocmanWikiPage(String sessionKey, int group_id, int parent_id, String title,
			String description, String ordering, String status, String obsolescence_date, String content,
			Permission[] permissions, MetadataValue[] metadata, String owner, String create_date,
			String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanLink(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int createDocmanLink(String sessionKey, int group_id, int parent_id, String title,
			String description, String ordering, String status, String obsolescence_date, String content,
			Permission[] permissions, MetadataValue[] metadata, String owner, String create_date,
			String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanEmptyDocument(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int createDocmanEmptyDocument(String sessionKey, int group_id, int parent_id, String title,
			String description, String ordering, String status, String obsolescence_date,
			Permission[] permissions, MetadataValue[] metadata, String owner, String create_date,
			String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanFileVersion(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String,
	 *      java.lang.String, int, int, java.lang.String, java.lang.String)
	 */
	public int createDocmanFileVersion(String sessionKey, int group_id, int item_id, String label,
			String changelog, int file_size, String file_name, String mime_type, String content,
			int chunk_offset, int chunk_size, String author, String date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#createDocmanEmbeddedFileVersion(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public int createDocmanEmbeddedFileVersion(String sessionKey, int group_id, int item_id, String label,
			String changelog, String content, String author, String date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#appendDocmanFileChunk(java.lang.String,
	 *      int, int, java.lang.String, int, int)
	 */
	public int appendDocmanFileChunk(String sessionKey, int group_id, int item_id, String content,
			int chunk_offset, int chunk_size) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#moveDocmanItem(java.lang.String,
	 *      int, int, int)
	 */
	public boolean moveDocmanItem(String sessionKey, int group_id, int item_id, int new_parent)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#getDocmanFileChunk(java.lang.String,
	 *      int, int, int, int, int)
	 */
	public String getDocmanFileChunk(String sessionKey, int group_id, int item_id, int version_number,
			int chunk_offset, int chunk_size) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#deleteDocmanItem(java.lang.String,
	 *      int, int)
	 */
	public int deleteDocmanItem(String sessionKey, int group_id, int item_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#monitorDocmanItem(java.lang.String,
	 *      int, int)
	 */
	public boolean monitorDocmanItem(String sessionKey, int group_id, int item_id) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateDocmanFolder(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int updateDocmanFolder(String sessionKey, int group_id, int item_id, String title,
			String description, String status, Permission[] permissions, MetadataValue[] metadata,
			String owner, String create_date, String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateDocmanFile(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int updateDocmanFile(String sessionKey, int group_id, int item_id, String title,
			String description, String status, String obsolescence_date, Permission[] permissions,
			MetadataValue[] metadata, String owner, String create_date, String update_date)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateDocmanEmbeddedFile(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int updateDocmanEmbeddedFile(String sessionKey, int group_id, int item_id, String title,
			String description, String status, String obsolescence_date, Permission[] permissions,
			MetadataValue[] metadata, String owner, String create_date, String update_date)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateDocmanWikiPage(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int updateDocmanWikiPage(String sessionKey, int group_id, int item_id, String title,
			String description, String status, String obsolescence_date, String content,
			Permission[] permissions, MetadataValue[] metadata, String owner, String create_date,
			String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateDocmanLink(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int updateDocmanLink(String sessionKey, int group_id, int item_id, String title,
			String description, String status, String obsolescence_date, String content,
			Permission[] permissions, MetadataValue[] metadata, String owner, String create_date,
			String update_date) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType#updateDocmanEmptyDocument(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.Permission[],
	 *      org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.MetadataValue[], java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int updateDocmanEmptyDocument(String sessionKey, int group_id, int item_id, String title,
			String description, String status, String obsolescence_date, Permission[] permissions,
			MetadataValue[] metadata, String owner, String create_date, String update_date)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	// CHECKSTYLE:ON

	/**
	 * Set user.
	 * 
	 * @param user
	 *            User
	 */
	public void setUser(MockedUserInfo user) {
		usersMap.put(user.getId(), user);
	}
}
