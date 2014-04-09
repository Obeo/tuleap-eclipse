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

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestConnector;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapFile;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapAttachmentInputStream;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests of {@link TuleapAttachmentInputStream}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapAttachmentInputStreamTest {

	private TaskRepository repository;

	private TuleapRestClient client;

	private Gson gson;

	private RestResourceFactory resourceFactory;

	@Before
	public void setUp() throws Exception {
		this.repository = new TaskRepository("test", "http://test/url");
		AbstractWebLocation abstractWebLocation = new TaskRepositoryLocationFactory()
		.createWebLocation(repository);
		gson = TuleapGsonProvider.defaultGson();
		ILog logger = new ILog() {
			public void removeLogListener(ILogListener listener) {
				//
			}

			public void log(IStatus status) {
				//
			}

			public Bundle getBundle() {
				return null;
			}

			public void addLogListener(ILogListener listener) {
				//
			}
		};
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(abstractWebLocation, logger);
		resourceFactory = new RestResourceFactory(RestResourceFactory.BEST_VERSION, tuleapRestConnector,
				gson, logger);
	}

	@Test
	public void testWithSmallFile() throws IOException {
		byte[] bytes = new byte[1024];
		for (int i = 0; i < 1024; i++) {
			bytes[i] = (byte)(i % 256);
		}
		final TuleapFile file = new TuleapFile(Base64.encodeBase64String(bytes));
		client = new TuleapRestClient(resourceFactory, gson, repository) {
			@Override
			public TuleapFile getArtifactFile(int fileId, int offset, int limit, IProgressMonitor monitor)
					throws CoreException {
				if (offset == 0 && limit == 1024) {
					return file;
				}
				fail(String.format("Invalid arguments: offset = %d, limit = %d", offset, limit));
				return null;
			}
		};
		TuleapAttachmentInputStream stream = null;
		try {
			stream = new TuleapAttachmentInputStream(1024, 123, 1000000, client, null);
			for (int i = 0; i < 1024; i++) {
				assertEquals(bytes[i] & 0xff, stream.read());
			}
			assertEquals(-1, stream.read()); // Check that stream is over
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	@Test
	public void testWithLargeFile() throws IOException {
		byte[] bytes = new byte[512];
		byte[] bytes2 = new byte[512];
		for (int i = 0; i < 512; i++) {
			bytes[i] = (byte)(i % 256);
		}
		for (int i = 0; i < 512; i++) {
			bytes2[i] = (byte)((512 - i) % 256);
		}
		final TuleapFile file = new TuleapFile(Base64.encodeBase64String(bytes));
		final TuleapFile file2 = new TuleapFile(Base64.encodeBase64String(bytes2));
		client = new TuleapRestClient(resourceFactory, gson, repository) {
			@Override
			public TuleapFile getArtifactFile(int fileId, int offset, int limit, IProgressMonitor monitor)
					throws CoreException {
				if (offset == 0 && limit == 512) {
					return file;
				} else if (offset == 512 && limit == 512) {
					return file2;
				}
				fail(String.format("Invalid arguments: offset = %d, limit = %d", offset, limit));
				return null;
			}
		};
		TuleapAttachmentInputStream stream = null;
		try {
			stream = new TuleapAttachmentInputStream(1024, 123, 512, client, null);
			for (int i = 0; i < 512; i++) {
				assertEquals(bytes[i] & 0xff, stream.read());
			}
			for (int i = 512; i < 1024; i++) {
				assertEquals(bytes2[i - 512] & 0xff, stream.read());
			}
			assertEquals(-1, stream.read()); // Check that stream is over
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
}
