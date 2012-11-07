/**
 * TuleapTrackerV5APIPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public interface TuleapTrackerV5APIPortType extends java.rmi.Remote {

    /**
     * Returns the array of Tracker that belongs to the group identified
     * by group ID.
     *      Returns a soap fault if the group ID does not match with a valid
     * project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[] getTrackerList(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException;

    /**
     * Returns the array of Trackerfields that are used in the tracker
     * tracker_id of the project group_id.
     *      Returns a soap fault if the tracker ID or the group ID does not
     * match with a valid project.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[] getTrackerFields(java.lang.String sessionKey, int group_id, int tracker_id) throws java.rmi.RemoteException;

    /**
     * Returns the ArtifactQueryResult of the tracker tracker_id in
     * the project group_id
     *      that are matching the given criteria. If offset AND max_rows
     * are filled, it returns only
     *      max_rows artifacts, skipping the first offset ones.
     *      It is not possible to sort artifact with this function (use getArtifactsFromReport
     * if you want to sort).
     *      Returns a soap fault if the group_id is not a valid one or if
     * the tracker_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult getArtifacts(java.lang.String sessionKey, int group_id, int tracker_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria[] criteria, int offset, int max_rows) throws java.rmi.RemoteException;

    /**
     * Add an Artifact in the tracker tracker_id of the project group_id
     * with the values given by
     *      value (an ArtifactFieldValue).
     *      Returns the Id of the created artifact if the creation succeed.
     * Returns a soap fault if the group_id is not a valid one, if the tracker_name
     * is not a valid one, or if the add failed.
     */
    public int addArtifact(java.lang.String sessionKey, int group_id, int tracker_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value) throws java.rmi.RemoteException;

    /**
     * Update the artifact $artifact_id of the tracker $tracker_id
     * in the project group_id with the values given by
     *      value. Add a follow-up comment $comment.
     *      Returns a soap fault if the group_id is not a valid one, if the
     * tracker_id is not a valid one,
     *      if the artifart_id is not a valid one, or if the update failed.
     */
    public int updateArtifact(java.lang.String sessionKey, int group_id, int tracker_id, int artifact_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value, java.lang.String comment, java.lang.String comment_format) throws java.rmi.RemoteException;

    /**
     * Returns the artifact (Artifact) identified by the id artifact_id
     * Returns a soap fault if the group_id is not a valid one, if the tracker_id
     * is not a valid one,
     *      or if the artifact_id is not a valid one.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact getArtifact(java.lang.String sessionKey, int group_id, int tracker_id, int artifact_id) throws java.rmi.RemoteException;

    /**
     * Returns the tracker structure.
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure getTrackerStructure(java.lang.String sessionKey, int group_id, int tracker_id) throws java.rmi.RemoteException;
}
