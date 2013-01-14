/**
 * CodendiAPIPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

public interface CodendiAPIPortType extends java.rmi.Remote {

    /**
     * Login Tuleap Server with given login and password.
     *      Returns a soap fault if the login failed.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Session login(java.lang.String loginname, java.lang.String passwd) throws java.rmi.RemoteException;

    /**
     * Login Tuleap Server with given admin_session_name and login.
     * Returns a soap fault if the login failed.
     */
    public java.lang.String loginAs(java.lang.String admin_session_hash, java.lang.String loginname) throws java.rmi.RemoteException;

    /**
     * Retrieve a valid session with a given session_hash and version.
     * Returns a soap fault if the session is not valid.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Session retrieveSession(java.lang.String session_hash) throws java.rmi.RemoteException;

    /**
     * Returns the current version of this Web Service API.
     */
    public java.lang.String getAPIVersion() throws java.rmi.RemoteException;

    /**
     * Logout the session identified by the given sessionKey From
     * Codendi Server.
     *      Returns a soap fault if the sessionKey is not a valid session
     * key.
     */
    public void logout(java.lang.String sessionKey) throws java.rmi.RemoteException;

    /**
     * Returns the list of Groups that the current user belong to
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Group[] getMyProjects(java.lang.String sessionKey) throws java.rmi.RemoteException;

    /**
     * Returns a Group object matching with the given unix_group_name,
     * or a soap fault if the name does not match with a valid project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Group getGroupByName(java.lang.String sessionKey, java.lang.String unix_group_name) throws java.rmi.RemoteException;

    /**
     * Returns the Group object associated with the given ID, or a
     * soap fault if the ID does not match with a valid project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Group getGroupById(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns the Ugroups associated to the given project:
     *      <pre>
     *        [ 
     *          ["ugroup_id" => 120,
     *           "name"      => "my custom group",
     *           "members"   => [ ["user_id"   => 115,
     *                             "user_name" => "john_doe"],
     *                          ]
     *          ]
     *        ]
     *      </pre>
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Ugroup[] getGroupUgroups(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns all groups defined in project both dynamic and static
     * (aka user group).
     *      <pre>
     *       [
     *         ["ugroup_id" => 3,
     *          "name"      => "project_members",
     *          "members"   => [ ["user_id"   => 115,
     *                            "user_name" => "john_doe"],
     *                           ["user_id"   => 120,
     *                            "user_name" => "foo_bar"]
     *                         ]
     *         ],
     *         ["ugroup_id" => 120,
     *          "name"      => "my custom group",
     *          "members"   => [ ["user_id"   => 115,
     *                            "user_name" => "john_doe"],
     *                         ]
     *         ]
     *       ]
     *      </pre>
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Ugroup[] getProjectGroupsAndUsers(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns the users that exist with their user name
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.UserInfo[] checkUsersExistence(java.lang.String sessionKey, java.lang.String[] users) throws java.rmi.RemoteException;

    /**
     * Returns the user info matching the given id
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.UserInfo getUserInfo(java.lang.String sessionKey, int user_id) throws java.rmi.RemoteException;

    /**
     * Returns the array of TrackerDesc (light description of trackers)
     * that belongs to the group identified by group ID.
     *      Returns a soap fault if the group ID does not match with a valid
     * project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.TrackerDesc[] getTrackerList(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns the ArtifactType (tracker) with the ID group_artifact_id
     * that belongs to the group identified by group ID.
     *      Returns a soap fault if the group ID does not match with a valid
     * project, or if the group_artifact_id is invalid or is not a tracker
     * of the project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactType getArtifactType(java.lang.String sessionKey, int group_id, int group_artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the array of ArtifactType (trackers) that belongs to
     * the group identified by group ID.
     *      Returns a soap fault if the group ID does not match with a valid
     * project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactType[] getArtifactTypes(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns the ArtifactQueryResult of the tracker group_artifact_id
     * in the project group_id 
     *      that are matching the given criteria. If offset AND max_rows
     * are filled, it returns only 
     *      max_rows artifacts, skipping the first offset ones.
     *      It is not possible to sort artifact with this function (use getArtifactsFromReport
     * if you want to sort).
     *      Returns a soap fault if the group_id is not a valid one or if
     * the group_artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactQueryResult getArtifacts(java.lang.String sessionKey, int group_id, int group_artifact_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Criteria[] criteria, int offset, int max_rows) throws java.rmi.RemoteException;

    /**
     * Returns the ArtifactReportResult of the tracker group_artifact_id
     * in the project group_id 
     *      with the report report_id that are matching the given criteria.
     * 
     *      If offset AND max_rows are filled, it returns only max_rows artifacts,
     * skipping the first offset ones.
     *      The result will be sorted, as defined in the param sort_criteria.
     * Returns a soap fault if the group_id is not a valid one, if the group_artifact_id
     * is not a valid one or if the report_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFromReportResult getArtifactsFromReport(java.lang.String sessionKey, int group_id, int group_artifact_id, int report_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Criteria[] criteria, int offset, int max_rows, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.SortCriteria[] sort_criteria) throws java.rmi.RemoteException;

    /**
     * Add an Artifact in the tracker group_artifact_id of the project
     * group_id with the values given by 
     *      status_id, close_date, summary, details, severity and extra_fields
     * for the non-standard fields. 
     *      Returns the Id of the created artifact if the creation succeed.
     * Returns a soap fault if the group_id is not a valid one, if the group_artifact_id
     * is not a valid one, or if the add failed.
     */
    public int addArtifact(java.lang.String sessionKey, int group_id, int group_artifact_id, int status_id, int close_date, java.lang.String summary, java.lang.String details, int severity, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldValue[] extra_fields) throws java.rmi.RemoteException;

    /**
     * Add an Artifact in the tracker tracker_name of the project
     * group_id with the values given by 
     *      status_id, close_date, summary, details, severity and extra_fields
     * for the non-standard fields. 
     *      Returns the Id of the created artifact if the creation succeed.
     * Returns a soap fault if the group_id is not a valid one, if the tracker_name
     * is not a valid one, or if the add failed.
     */
    public int addArtifactWithFieldNames(java.lang.String sessionKey, int group_id, int group_artifact_id, int status_id, int close_date, java.lang.String summary, java.lang.String details, int severity, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldNameValue[] extra_fields) throws java.rmi.RemoteException;

    /**
     * Update the artifact $artifact_id of the tracker $group_artifact_id
     * in the project group_id with the values given by 
     *      status_id, close_date, summary, details, severity and extra_fields
     * for the non-standard fields.
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifart_id is not a valid one, or if the update failed.
     */
    public int updateArtifact(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int status_id, int close_date, java.lang.String summary, java.lang.String details, int severity, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldValue[] extra_fields) throws java.rmi.RemoteException;

    /**
     * Update the artifact $artifact_id of the tracker $tracker_name
     * in the project group_id with the values given by 
     *      status_id, close_date, summary, details, severity and extra_fields
     * for the non-standard fields.
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifart_id is not a valid one, or if the update failed.
     */
    public int updateArtifactWithFieldNames(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int status_id, int close_date, java.lang.String summary, java.lang.String details, int severity, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldNameValue[] extra_fields) throws java.rmi.RemoteException;

    /**
     * Returns the list of follow-ups (ArtifactFollowup) of the artifact
     * artifact_id of the tracker group_artifact_id in the project group_id.
     * Returns a soap fault if the group_id is not a valid one, if the group_artifact_id
     * is not a valid one, 
     *      or if the artifart_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFollowup[] getArtifactFollowups(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the list of canned responses (ArtifactCanned) for the
     * tracker group_artifact_id of the project group_id. 
     *      Returns a soap fault if the group_id is not a valid one or if
     * group_artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactCanned[] getArtifactCannedResponses(java.lang.String sessionKey, int group_id, int group_artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the list of reports (ArtifactReport) for the tracker
     * group_artifact_id of the project group_id of the current user. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactReport[] getArtifactReports(java.lang.String sessionKey, int group_id, int group_artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the array of attached files (ArtifactFile) attached
     * to the artifact artifact_id in the tracker group_artifact_id of the
     * project group_id. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      or if the artifact_id is not a valid one. NOTE : for performance
     * reasons, the result does not contain the content of the file. Please
     * use getArtifactAttachedFile to get the content of a single file
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFile[] getArtifactAttachedFiles(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the attached file (ArtifactFile) with the id file_id
     * attached to the artifact artifact_id in the tracker group_artifact_id
     * of the project group_id. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifact_id is not a valid one, or if the file_id doesnt
     * match with the given artifact_id.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFile getArtifactAttachedFile(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int file_id) throws java.rmi.RemoteException;

    /**
     * Returns the artifact (Artifact) identified by the id artifact_id
     * in the tracker group_artifact_id of the project group_id. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      or if the artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Artifact getArtifactById(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the list of the dependencies (ArtifactDependency) for
     * the artifact artifact_id of the tracker group_artifact_id of the project
     * group_id. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      or if the artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactDependency[] getArtifactDependencies(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the list of the dependencies (ArtifactDependency) that
     * other artifact can have with the artifact artifact_id of the tracker
     * group_artifact_id of the project group_id. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      or if the artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactDependency[] getArtifactInverseDependencies(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Add an attached file to the artifact artifact_id of the tracker
     * group_artifact_id of the project group_id. 
     *      The attached file is described by the raw encoded_data (encoded
     * in base64), the description of the file, 
     *      the name of the file and it type (the mimi-type -- plain/text,
     * image/jpeg, etc ...). 
     *      Returns the ID of the attached file if the attachment succeed.
     * Returns a soap fault if the group_id is not a valid one, if the group_artifact_id
     * is not a valid one, 
     *      or if the artifact_id is not a valid one, or if the attachment
     * failed.
     */
    public int addArtifactAttachedFile(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, java.lang.String encoded_data, java.lang.String description, java.lang.String filename, java.lang.String filetype) throws java.rmi.RemoteException;

    /**
     * Delete the attached file file_id from the artifact artifact_id
     * of the tracker group_artifact_id of the project group_id. 
     *      Returns the ID of the deleted file if the deletion succeed. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifact_id is not a valid one, if the file_id is not
     * a valid one or if the deletion failed.
     */
    public int deleteArtifactAttachedFile(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int file_id) throws java.rmi.RemoteException;

    /**
     * Add the list of dependencies is_dependent_on_artifact_id to
     * the list of dependencies of the artifact artifact_id 
     *      of the tracker group_artifact_id of the project group_id.
     *      Returns true if the add succeed. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifact_id is not a valid one, or if the add failed.
     */
    public boolean addArtifactDependencies(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, java.lang.String is_dependent_on_artifact_ids) throws java.rmi.RemoteException;

    /**
     * Delete the dependency between the artifact dependent_on_artifact_id
     * and the artifact artifact_id of the tracker group_artifact_id of the
     * project group_id.
     *      Returns the ID of the deleted dependency if the deletion succeed.
     * 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifact_id is not a valid one, if the dependent_on_artifact_id
     * is not a valid artifact id, or if the deletion failed.
     */
    public int deleteArtifactDependency(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int dependent_on_artifact_id) throws java.rmi.RemoteException;

    /**
     * Add a follow-up body to the artifact artifact_id of the tracker
     * group_artifact_id of the project group_id,
     *      with optionals comment type and canned response. If canned response
     * is set, it will replace the body.
     *      Returns nothing if the add succeed. 
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifact_id is not a valid one, or if the add failed.
     */
    public boolean addArtifactFollowup(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, java.lang.String body, int comment_type_id, int format) throws java.rmi.RemoteException;

    /**
     * Update the follow_up artifact_history_id of the tracker $group_artifact_id
     * in the project group_id for the artifact $artifact_id with the new
     * comment $comment.
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifart_id is not a valid one, if the artifact_history_id
     * is not a valid one, or if the update failed.
     */
    public boolean updateArtifactFollowUp(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int artifact_history_id, java.lang.String comment) throws java.rmi.RemoteException;

    /**
     * Delete the follow_up artifact_history_id of the tracker $group_artifact_id
     * in the project group_id for the artifact $artifact_id.
     *      Returns a soap fault if the group_id is not a valid one, if the
     * group_artifact_id is not a valid one, 
     *      if the artifart_id is not a valid one, if the artifact_history_id
     * is not a valid one, or if the deletion failed.
     */
    public boolean deleteArtifactFollowUp(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int artifact_history_id) throws java.rmi.RemoteException;

    /**
     * Check if there is an artifact in the tracker group_artifact_id
     * that already have the summary summary (the summary is unique inside
     * a given tracker).
     *      Returns the ID of the artifact containing the same summary in
     * the tracker, or -1 if the summary does not exist in this tracker.
     */
    public int existArtifactSummary(java.lang.String sessionKey, int group_artifact_id, java.lang.String summary) throws java.rmi.RemoteException;

    /**
     * Get the list of emails or logins in the CC list of a specific
     * artifact
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactCC[] getArtifactCCList(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Add a list of emails or logins in the CC list of a specific
     * artifact, with an optional comment
     */
    public boolean addArtifactCC(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, java.lang.String cc_list, java.lang.String cc_comment) throws java.rmi.RemoteException;

    /**
     * Delete a CC to the CC list of the artifact
     */
    public boolean deleteArtifactCC(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id, int artifact_cc_id) throws java.rmi.RemoteException;

    /**
     * Get the history of the artifact (the history of the fields
     * values)
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactHistory[] getArtifactHistory(java.lang.String sessionKey, int group_id, int group_artifact_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the array of FRSPackages that belongs to the group
     * identified by group ID.
     *      Returns a soap fault if the group ID does not match with a valid
     * project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.FRSPackage[] getPackages(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Add a Package to the File Release Manager of the project group_id
     * with the values given by 
     *      package_name, status_id, rank and approve_license. 
     *      Returns the ID of the created package if the creation succeed.
     * Returns a soap fault if the group_id is not a valid one, or if the
     * add failed.
     */
    public int addPackage(java.lang.String sessionKey, int group_id, java.lang.String package_name, int status_id, int rank, boolean approve_license) throws java.rmi.RemoteException;

    /**
     * Returns the array of FRSReleases that belongs to the group
     * identified by group ID and 
     *      to the package identified by package_id.
     *      Returns a soap fault if the group ID does not match with a valid
     * project, or if the package ID
     *      does not match with the right group ID.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.FRSRelease[] getReleases(java.lang.String sessionKey, int group_id, int package_id) throws java.rmi.RemoteException;

    /**
     * Add a Release to the File Release Manager of the project group_id
     * with the values given by 
     *      package_id, name, notes, changes, status_id and release_date.
     * 
     *      Returns the ID of the created release if the creation succeed.
     * Returns a soap fault if the group_id is not a valid one, 
     *      if the package does not match with the group ID, or if the add
     * failed.
     */
    public int addRelease(java.lang.String sessionKey, int group_id, int package_id, java.lang.String name, java.lang.String notes, java.lang.String changes, int status_id, int release_date) throws java.rmi.RemoteException;

    /**
     * Returns the array of FRSFiles that belongs to the group identified
     * by group ID, 
     *      to the package identified by package_id and to the release identfied
     * by release_id.
     *      Returns a soap fault if the group ID does not match with a valid
     * project, or if the package ID
     *      does not match with the right group ID, or if the release ID
     * does not match with the right package ID.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.FRSFile[] getFiles(java.lang.String sessionKey, int group_id, int package_id, int release_id) throws java.rmi.RemoteException;

    /**
     * Returns the metadata of the file contained in 
     *      the release release_id in the package package_id, in the project
     * group_id.
     *      Returns a soap fault if the group ID does not match with a valid
     * project, or if the package ID
     *      does not match with the right group ID, or if the release ID
     * does not match with the right package ID,
     *      or if the file ID does not match with the right release ID.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.FRSFile getFileInfo(java.lang.String sessionKey, int group_id, int package_id, int release_id, int file_id) throws java.rmi.RemoteException;

    /**
     * Returns the <strong>content</strong> (encoded in base64) of
     * the file contained in 
     *      the release release_id in the package package_id, in the project
     * group_id.
     *      Returns a soap fault if the group ID does not match with a valid
     * project, or if the package ID
     *      does not match with the right group ID, or if the release ID
     * does not match with the right package ID,
     *      or if the file ID does not match with the right release ID.
     */
    public java.lang.String getFile(java.lang.String sessionKey, int group_id, int package_id, int release_id, int file_id) throws java.rmi.RemoteException;

    /**
     * Returns a part (chunk) of the <strong>content</strong>, encoded
     * in base64, of the file contained in 
     *      the release release_id in the package package_id, in the project
     * group_id.
     *      You specify the offset where the download should start and the
     * size to transfer.
     *      Returns a soap fault if the group ID does not match with a valid
     * project, or if the package ID
     *      does not match with the right group ID, or if the release ID
     * does not match with the right package ID,
     *      or if the file ID does not match with the right release ID.
     */
    public java.lang.String getFileChunk(java.lang.String sessionKey, int group_id, int package_id, int release_id, int file_id, int offset, int size) throws java.rmi.RemoteException;

    /**
     * Add a File to the File Release Manager of the project group_id
     * with the values given by 
     *      package_id, release_id, filename, base64_contents, type_id, processor_id
     * and reference_md5. 
     *      The content of the file must be encoded in base64.
     *      Returns the ID of the created file if the creation succeed.
     *      Returns a soap fault if the group_id is not a valid one, 
     *      if the package does not match with the group ID, 
     *      if the release does not match with the package ID,
     *      or if the add failed.
     */
    public int addFile(java.lang.String sessionKey, int group_id, int package_id, int release_id, java.lang.String filename, java.lang.String base64_contents, int type_id, int processor_id, java.lang.String reference_md5) throws java.rmi.RemoteException;

    /**
     * Add a chunk to a file in the incoming directory to be released
     * later in FRS. 
     *      The content of the chunk must be encoded in base64.
     *      Returns the size of the written chunk if the chunk addition succeed.
     * Returns a soap fault if the session is not valid
     *      or if the addition failed.
     */
    public java.math.BigInteger addFileChunk(java.lang.String sessionKey, java.lang.String filename, java.lang.String contents, boolean first_chunk) throws java.rmi.RemoteException;

    /**
     * Add a File to the File Release Manager of the project group_id
     * with the values given by 
     *      package_id, release_id, filename, type_id, processor_id and reference_md5.
     * 
     *      The file must already be present in the incoming directory.
     *      Returns the ID of the created file if the creation succeed.
     *      Returns a soap fault if the group_id is not a valid one, 
     *      if the package does not match with the group ID, 
     *      if the release does not match with the package ID,
     *      or if the add failed.
     */
    public int addUploadedFile(java.lang.String sessionKey, int group_id, int package_id, int release_id, java.lang.String filename, int type_id, int processor_id, java.lang.String reference_md5) throws java.rmi.RemoteException;

    /**
     * Get the file names of the file present in the incoming directory
     * on the server.
     */
    public java.lang.String[] getUploadedFiles(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Delete the file file_id in release release_id in package package_id.
     * Returns true if succeed, or a soap fault if an error occured.
     */
    public boolean deleteFile(java.lang.String sessionKey, int group_id, int package_id, int release_id, int file_id) throws java.rmi.RemoteException;

    /**
     * Delete a package or all empty packages in project group_id.
     * Returns the list of deleted packages if succeed, or a soap fault if
     * an error occured.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.FRSPackage[] deleteEmptyPackage(java.lang.String sessionKey, int group_id, int package_id, boolean cleanup_all) throws java.rmi.RemoteException;

    /**
     * Delete a release or all empty releases in package package_id.
     * Returns the list of deleted releases if succeed, or a soap fault if
     * an error occured.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.FRSRelease[] deleteEmptyRelease(java.lang.String sessionKey, int group_id, int package_id, int release_id, boolean cleanup_all) throws java.rmi.RemoteException;

    /**
     * Returns the document object id that is at the top of the docman
     * given a group object <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/></pre>
     */
    public int getRootFolder(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * List folder contents <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>item_id             item ID<br/></pre>
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Docman_Item[] listFolder(java.lang.String sessionKey, int group_id, int item_id) throws java.rmi.RemoteException;

    /**
     * Returns all the items that match given criterias <pre>sessionKey
     * Session key<br/>group_id            Group ID<br/>item_id         
     * item ID<br/>criterias           Criteria<br/></pre>
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Docman_Item[] searchDocmanItem(java.lang.String sessionKey, int group_id, int item_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Criteria[] criterias) throws java.rmi.RemoteException;

    /**
     * Returns the content of a file (or embedded file) base64 encoded.
     * (version_number = -1 means last) <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>item_id             item
     * ID<br/>version_number      Version number<br/></pre>
     */
    public java.lang.String getDocmanFileContents(java.lang.String sessionKey, int group_id, int item_id, int version_number) throws java.rmi.RemoteException;

    /**
     * Returns the MD5 checksum of the file (last version) corresponding
     * to the provided item ID <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>item_id             item ID<br/>version_number      Version
     * number<br/></pre>
     */
    public java.lang.String getDocmanFileMD5Sum(java.lang.String sessionKey, int group_id, int item_id, int version_number) throws java.rmi.RemoteException;

    /**
     * Returns the MD5 checksum of the file (all versions) corresponding
     * to the provided item ID <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>item_id             item ID<br/></pre>
     */
    public java.lang.String[] getDocmanFileAllVersionsMD5Sum(java.lang.String sessionKey, int group_id, int item_id) throws java.rmi.RemoteException;

    /**
     * Returns the metadata of the given project <pre>sessionKey 
     * Session key<br/>group_id            Group ID<br/></pre>
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Metadata[] getDocmanProjectMetadata(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns the tree information of the given project <pre>sessionKey
     * Session key<br/>group_id            Group ID<br/>parent_id       
     * Parent ID<br/></pre>
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ItemInfo[] getDocmanTreeInfo(java.lang.String sessionKey, int group_id, int parent_id) throws java.rmi.RemoteException;

    /**
     * Create a folder <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>parent_id           Parent ID<br/>title             
     * Title<br/>description         Description<br/>ordering           
     * Ordering (begin, end)<br/>status              Status (none, draft,
     * approved, rejected)<br/>permissions         Permissions<br/>metadata
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int createDocmanFolder(java.lang.String sessionKey, int group_id, int parent_id, java.lang.String title, java.lang.String description, java.lang.String ordering, java.lang.String status, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Creates a docman file <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>parent_id           Parent ID<br/>title             
     * Title<br/>description         Description<br/>ordering           
     * Ordering (begin, end)<br/>status              Status (none, draft,
     * approved, rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>permissions
     * Permissions<br/>metadata            Metadata values<br/>file_size
     * File size<br/>file_name           File name<br/>mime_type        
     * Mime type<br/>content             Content<br/>chunk_offset       
     * Chunk offset<br/>chunk_size          Chunk size<br/>author       
     * Version author<br/>date                Version date (timestamp)<br/>owner
     * Owner of the item<br/>create_date         Item creation date (timestamp)<br/>update_date
     * Item update date (timestamp)<br/></pre>
     */
    public int createDocmanFile(java.lang.String sessionKey, int group_id, int parent_id, java.lang.String title, java.lang.String description, java.lang.String ordering, java.lang.String status, java.lang.String obsolescence_date, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, int file_size, java.lang.String file_name, java.lang.String mime_type, java.lang.String content, int chunk_offset, int chunk_size, java.lang.String author, java.lang.String date, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Creates a docman embedded file <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>parent_id           Parent
     * ID<br/>title               Title<br/>description         Description<br/>ordering
     * Ordering (begin, end)<br/>status              Status (none, draft,
     * approved, rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>content
     * Content<br/>permissions         Permissions<br/>metadata         
     * Metadata values<br/>author              Version author<br/>date  
     * Version date (timestamp)<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int createDocmanEmbeddedFile(java.lang.String sessionKey, int group_id, int parent_id, java.lang.String title, java.lang.String description, java.lang.String ordering, java.lang.String status, java.lang.String obsolescence_date, java.lang.String content, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String author, java.lang.String date, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Creates a docman wiki page <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>parent_id           Parent
     * ID<br/>title               Title<br/>description         Description<br/>ordering
     * Ordering (begin, end)<br/>status              Status (none, draft,
     * approved, rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>content
     * Content<br/>permissions         Permissions<br/>metadata         
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int createDocmanWikiPage(java.lang.String sessionKey, int group_id, int parent_id, java.lang.String title, java.lang.String description, java.lang.String ordering, java.lang.String status, java.lang.String obsolescence_date, java.lang.String content, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Creates a docman link <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>parent_id           Parent ID<br/>title             
     * Title<br/>description         Description<br/>ordering           
     * Ordering (begin, end)<br/>status              Status (none, draft,
     * approved, rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>content
     * Content<br/>permissions         Permissions<br/>metadata         
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int createDocmanLink(java.lang.String sessionKey, int group_id, int parent_id, java.lang.String title, java.lang.String description, java.lang.String ordering, java.lang.String status, java.lang.String obsolescence_date, java.lang.String content, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Creates a docman empty document <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>parent_id           Parent
     * ID<br/>title               Title<br/>description         Description<br/>ordering
     * Ordering (begin, end)<br/>status              Status (none, draft,
     * approved, rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>permissions
     * Permissions<br/>metadata            Metadata values<br/>owner    
     * Owner of the item<br/>create_date         Item creation date (timestamp)<br/>update_date
     * Item update date (timestamp)<br/></pre>
     */
    public int createDocmanEmptyDocument(java.lang.String sessionKey, int group_id, int parent_id, java.lang.String title, java.lang.String description, java.lang.String ordering, java.lang.String status, java.lang.String obsolescence_date, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Creates a docman file version <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>item_id             item
     * ID<br/>label               version label<br/>changelog           Version
     * changelog<br/>file_size           File size<br/>file_name        
     * File name<br/>mime_type           Mime type<br/>content          
     * Content<br/>chunk_offset        Chunk offset<br/>chunk_size      
     * Chunk size<br/>author              Version author<br/>date       
     * Version date (timestamp)<br/></pre>
     */
    public int createDocmanFileVersion(java.lang.String sessionKey, int group_id, int item_id, java.lang.String label, java.lang.String changelog, int file_size, java.lang.String file_name, java.lang.String mime_type, java.lang.String content, int chunk_offset, int chunk_size, java.lang.String author, java.lang.String date) throws java.rmi.RemoteException;

    /**
     * Creates a docman embedded file version <pre>sessionKey    
     * Session key<br/>group_id            Group ID<br/>item_id         
     * item ID<br/>label               version label<br/>changelog      
     * Version changelog<br/>content             Content<br/>author     
     * Version author<br/>date                Version date (timestamp)<br/></pre>
     */
    public int createDocmanEmbeddedFileVersion(java.lang.String sessionKey, int group_id, int item_id, java.lang.String label, java.lang.String changelog, java.lang.String content, java.lang.String author, java.lang.String date) throws java.rmi.RemoteException;

    /**
     * Appends a chunk of data to the last version of a file <pre>sessionKey
     * Session key<br/>group_id            Group ID<br/>item_id         
     * item ID<br/>content             Content<br/>chunk_offset        Chunk
     * offset<br/>chunk_size          Chunk size<br/></pre>
     */
    public int appendDocmanFileChunk(java.lang.String sessionKey, int group_id, int item_id, java.lang.String content, int chunk_offset, int chunk_size) throws java.rmi.RemoteException;

    /**
     * Moves an item in a new folder <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>item_id             item
     * ID<br/>new_parent          New parent ID<br/></pre>
     */
    public boolean moveDocmanItem(java.lang.String sessionKey, int group_id, int item_id, int new_parent) throws java.rmi.RemoteException;

    /**
     * Returns a part (chunk) of the content, encoded in base64, of
     * the file/embedded file which id item_id of a given version version_number,
     * if the version is not specified it will be the current one, in the
     * project group_id.Returns an error if the group ID does not match with
     * a valid project, or if the item ID does not match with the right group
     * ID, or if the version number does not match with the item ID. <pre>sessionKey
     * Session key<br/>group_id            Group ID<br/>item_id         
     * item ID<br/>version_number      Version number<br/>chunk_offset  
     * Chunk offset<br/>chunk_size          Chunk size<br/></pre>
     */
    public java.lang.String getDocmanFileChunk(java.lang.String sessionKey, int group_id, int item_id, int version_number, int chunk_offset, int chunk_size) throws java.rmi.RemoteException;

    /**
     * Delete an item (document or folder) <pre>sessionKey       
     * Session key<br/>group_id            Group ID<br/>item_id         
     * item ID<br/></pre>
     */
    public int deleteDocmanItem(java.lang.String sessionKey, int group_id, int item_id) throws java.rmi.RemoteException;

    /**
     * Enables the monitoring of an item by a user <pre>sessionKey
     * Session key<br/>group_id            Group ID<br/>item_id         
     * item ID<br/></pre>
     */
    public boolean monitorDocmanItem(java.lang.String sessionKey, int group_id, int item_id) throws java.rmi.RemoteException;

    /**
     * Updates a docman folder <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>item_id             item ID<br/>title               Title<br/>description
     * Description<br/>status              Status (none, draft, approved,
     * rejected)<br/>permissions         Permissions<br/>metadata       
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int updateDocmanFolder(java.lang.String sessionKey, int group_id, int item_id, java.lang.String title, java.lang.String description, java.lang.String status, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Updates a docman file <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>item_id             item ID<br/>title               Title<br/>description
     * Description<br/>status              Status (none, draft, approved,
     * rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>permissions
     * Permissions<br/>metadata            Metadata values<br/>owner    
     * Owner of the item<br/>create_date         Item creation date (timestamp)<br/>update_date
     * Item update date (timestamp)<br/></pre>
     */
    public int updateDocmanFile(java.lang.String sessionKey, int group_id, int item_id, java.lang.String title, java.lang.String description, java.lang.String status, java.lang.String obsolescence_date, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Updates a docman embedded file <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>item_id             item
     * ID<br/>title               Title<br/>description         Description<br/>status
     * Status (none, draft, approved, rejected)<br/>obsolescence_date   Obsolescence
     * date (timestamp)<br/>permissions         Permissions<br/>metadata
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int updateDocmanEmbeddedFile(java.lang.String sessionKey, int group_id, int item_id, java.lang.String title, java.lang.String description, java.lang.String status, java.lang.String obsolescence_date, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Updates a docman wiki page <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>item_id             item
     * ID<br/>title               Title<br/>description         Description<br/>status
     * Status (none, draft, approved, rejected)<br/>obsolescence_date   Obsolescence
     * date (timestamp)<br/>content             Content<br/>permissions 
     * Permissions<br/>metadata            Metadata values<br/>owner    
     * Owner of the item<br/>create_date         Item creation date (timestamp)<br/>update_date
     * Item update date (timestamp)<br/></pre>
     */
    public int updateDocmanWikiPage(java.lang.String sessionKey, int group_id, int item_id, java.lang.String title, java.lang.String description, java.lang.String status, java.lang.String obsolescence_date, java.lang.String content, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Updates a docman link <pre>sessionKey          Session key<br/>group_id
     * Group ID<br/>item_id             item ID<br/>title               Title<br/>description
     * Description<br/>status              Status (none, draft, approved,
     * rejected)<br/>obsolescence_date   Obsolescence date (timestamp)<br/>content
     * Content<br/>permissions         Permissions<br/>metadata         
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int updateDocmanLink(java.lang.String sessionKey, int group_id, int item_id, java.lang.String title, java.lang.String description, java.lang.String status, java.lang.String obsolescence_date, java.lang.String content, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;

    /**
     * Updates a docman empty document <pre>sessionKey          Session
     * key<br/>group_id            Group ID<br/>item_id             item
     * ID<br/>title               Title<br/>description         Description<br/>status
     * Status (none, draft, approved, rejected)<br/>obsolescence_date   Obsolescence
     * date (timestamp)<br/>permissions         Permissions<br/>metadata
     * Metadata values<br/>owner               Owner of the item<br/>create_date
     * Item creation date (timestamp)<br/>update_date         Item update
     * date (timestamp)<br/></pre>
     */
    public int updateDocmanEmptyDocument(java.lang.String sessionKey, int group_id, int item_id, java.lang.String title, java.lang.String description, java.lang.String status, java.lang.String obsolescence_date, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.Permission[] permissions, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataValue[] metadata, java.lang.String owner, java.lang.String create_date, java.lang.String update_date) throws java.rmi.RemoteException;
}
