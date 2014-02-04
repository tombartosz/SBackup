package org.noip.naszdomek.SBackup.file;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ZipCompressor implements Compressor {

	private static final Logger LOGGER = Logger.getLogger(ZipCompressor.class);

	@Override
	public void compress(File source, File destination) {
		try {
			LOGGER.debug("Creating zip file: " + destination.getAbsolutePath());
			
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(destination);

			// Initiate Zip Parameters which define various properties such
			// as compression method, etc.
			ZipParameters parameters = new ZipParameters();

			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			// Set the compression level
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			// Add folder to the zip file
			zipFile.addFolder(source, parameters);
			
			LOGGER.debug("Zip file created");

		} catch (ZipException e) {
			throw new RuntimeException(e);
		}

	}

}
