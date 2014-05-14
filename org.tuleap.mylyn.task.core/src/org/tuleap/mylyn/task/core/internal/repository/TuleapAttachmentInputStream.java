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
package org.tuleap.mylyn.task.core.internal.repository;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.tuleap.mylyn.task.core.internal.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreKeys;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreMessages;

/**
 * An input stream that iterates over file chunks to manage large files without consuming too much memory.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapAttachmentInputStream extends InputStream {
	/**
	 * The size of the file to download.
	 */
	private final int size;

	/**
	 * Chunk size, by default 1 MiO.
	 */
	private final int chunkSize;

	/**
	 * The progress monitor.
	 */
	private final IProgressMonitor monitor;

	/**
	 * The client to use to retrieve data.
	 */
	private final TuleapRestClient client;

	/**
	 * The attachment ID.
	 */
	private final int attachmentId;

	/**
	 * The bytes received.
	 */
	private byte[] bytes;

	/**
	 * The current index.
	 */
	private int index;

	/**
	 * Constructor.
	 *
	 * @param size
	 *            the size of the file to download
	 * @param attachmentId
	 *            The id of the attachment to download
	 * @param chunkSize
	 *            The chunk size to use
	 * @param client
	 *            The client to use
	 * @param monitor
	 *            The progress monitor, can be null
	 */
	public TuleapAttachmentInputStream(int size, int attachmentId, int chunkSize, TuleapRestClient client,
			IProgressMonitor monitor) {
		this.size = size;
		this.monitor = monitor;
		this.client = client;
		this.attachmentId = attachmentId;
		index = 0;
		this.chunkSize = chunkSize;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		if (index < size) {
			int arrIndex = index % chunkSize;
			if (arrIndex == 0) {
				readNextChunk(index);
			}
			index++;
			// CHECKSTYLE:OFF Useless constant 0xff
			return bytes[arrIndex] & 0xff;
			// CHECKSTYLE:ON
		}
		return -1; // Finished
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.InputStream#available()
	 */
	@Override
	public int available() throws IOException {
		if (bytes != null) {
			int arrIndex = index % chunkSize;
			if (arrIndex > 0) {
				return bytes.length - arrIndex;
			}
		}
		return 0;
	}

	/**
	 * Reads the next chunk for the file to download.
	 *
	 * @param offset
	 *            the offset of the chunk to load
	 * @return the supposed size of data you'll get after decoding the base64-encoded downloaded data.
	 * @throws IOException
	 *             If an error occurs while downloading the file chunks.
	 */
	private int readNextChunk(int offset) throws IOException {
		int sizeToDownload = chunkSize;
		if (offset + sizeToDownload > size) {
			sizeToDownload = size - offset;
		}
		try {
			if (monitor != null) {
				monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.downloadingAttachment, Integer
						.toString(offset), Integer.toString(size)));
			}
			String chunkBase64 = client.getArtifactFile(attachmentId, offset, sizeToDownload, monitor)
					.getData();
			// Each chunk must be decoded one after the other, you cannot concatenate the strings and
			// decode after because concatenated base64 strings don't result in a valid base64 string
			bytes = Base64.decodeBase64(chunkBase64);
		} catch (CoreException e) {
			throw new IOException(e);
		}
		return sizeToDownload;
	}
}
