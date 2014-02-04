package org.noip.naszdomek.SBackup.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.noip.naszdomek.SBackup.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CopyReplicator implements Replicator {

	private final Logger LOGGER = Logger.getLogger(CopyReplicator.class);

	@Autowired
	private Config config;
	
	@Autowired
	private ChecksumComputator checksumComputator;
	
	@Autowired
	private Compressor compressor;

	@Override
	public void replicate(Set<File> files) throws IOException {
		String replace = config.getDirectoryToBackup().getAbsolutePath();
		String replacement = config.getBackupDirectory().getAbsolutePath();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
		String uniquePart = sdf.format(new Date());
		replacement = replacement + File.separator + uniquePart;
		for (File srcFile : files) {
			File destFile = new File(Utils.pathPartReplacer(srcFile, replace,
					replacement));

			LOGGER.debug("Copy: " + srcFile.getAbsolutePath() + " -> "
					+ destFile.getAbsolutePath());

			File parent = destFile.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new RuntimeException();
			}
			Files.copy(srcFile.toPath(), destFile.toPath());

		}

		File checksumFile = new File(config.getBackupDirectory(), uniquePart
				+ "_checksums.txt");
		
		checksumComputator.createChecksumFile(files, checksumFile);
		
		File compressedFile = new File(config.getBackupDirectory(), uniquePart
				+ ".zip");
		
		compressor.compress(new File(replacement), compressedFile);
		
		Utils.deleteDirectory(new File(replacement));

	}

}
