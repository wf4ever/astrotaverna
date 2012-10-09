package org.purl.wf4ever.astrotaverna.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.taverna.t2.reference.ExternalReferenceBuilderSPI;
import net.sf.taverna.t2.reference.ReferenceContext;
import net.sf.taverna.t2.reference.impl.external.file.FileReference;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class FileReferenceBuilder implements
		ExternalReferenceBuilderSPI<FileReference> {

	private static Logger logger = Logger
			.getLogger(FileReferenceBuilder.class);

	@Override
	public FileReference createReference(InputStream byteStream,
			ReferenceContext context) {
		File tmpFile = null;
		FileOutputStream tmpStream = null;
		try {
			tmpFile = File.createTempFile("taverna", ".tmp");
			tmpStream = FileUtils.openOutputStream(tmpFile);
			try {
				IOUtils.copyLarge(byteStream, tmpStream);
			} finally {
				tmpStream.close();
			}
			logger.debug("Converted reference to file " + tmpFile + " ("
					+ tmpFile.length() + " bytes)");
			return new FileReference(tmpFile);
		} catch (IOException e) {
			String message = "Can't save data to " + tmpFile;
			logger.warn(message);
			throw new RuntimeException(message);
		} finally {
			try {				
				byteStream.close();
			} catch (IOException e) {
				logger.warn("Can't close rendering stream", e);
			}
		}
	}

	@Override
	public Class<FileReference> getReferenceType() {
		return FileReference.class;
	}

	@Override
	public boolean isEnabled(ReferenceContext context) {
		return true;
	}

	@Override
	public float getConstructionCost() {
		return 1.0f;
	}
}
