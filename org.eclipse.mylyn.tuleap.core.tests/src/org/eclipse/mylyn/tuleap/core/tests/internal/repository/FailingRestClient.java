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
package org.eclipse.mylyn.tuleap.core.tests.internal.repository;

import com.google.gson.Gson;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.TuleapToken;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTrackerReport;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUserGroup;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithAttachment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapFile;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;

import static org.junit.Assert.fail;

/**
 * Stub implementation of a Rest client that fails on any method call.
 */
public class FailingRestClient extends TuleapRestClient {

	public FailingRestClient(RestResourceFactory resourceFactory, Gson gson, TaskRepository taskRepository) {
		super(resourceFactory, gson, taskRepository);
	}

	@Override
	public TuleapTaskId createArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public TuleapReference createArtifactFile(String content, String type, String name, String description,
			IProgressMonitor monitor) throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public void deleteArtifactFile(int fileId, IProgressMonitor monitor) throws CoreException {
		fail("Should not be called.");
	}

	@Override
	public TuleapArtifact getArtifact(int artifactId, TuleapServer server, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapElementComment> getArtifactComments(int artifactId, TuleapServer server,
			IProgressMonitor monitor) throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public TuleapFile getArtifactFile(int fileId, int offset, int limit, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapArtifact> getArtifactsFromQuery(IRepositoryQuery query, TuleapTracker tracker,
			IProgressMonitor monitor) throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapProject> getProjects(IProgressMonitor monitor) throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapTracker> getProjectTrackers(int projectId, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapUserGroup> getProjectUserGroups(int projectId, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public TuleapToken getToken() {
		fail("Should not be called.");
		return null;
	}

	@Override
	public TuleapTracker getTracker(int trackerId, IProgressMonitor monitor) throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapArtifact> getTrackerReportArtifacts(int trackerReportId, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapTrackerReport> getTrackerReports(int trackerId, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public List<TuleapUser> getUserGroupUsers(String userGroupId, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
		return null;
	}

	@Override
	public void login() throws CoreException {
		fail("Should not be called.");
	}

	@Override
	public void updateArtifact(TuleapArtifactWithComment artifact, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
	}

	@Override
	public void updateArtifactAttachments(TuleapArtifactWithAttachment artifact, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
	}

	@Override
	public void updateArtifactFile(int fileId, String data, int offset, IProgressMonitor monitor)
			throws CoreException {
		fail("Should not be called.");
	}

	@Override
	public IStatus validateConnection(IProgressMonitor monitor) {
		fail("Should not be called.");
		return null;
	}
}
