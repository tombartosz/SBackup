package org.noip.naszdomek.SBackup;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.noip.naszdomek.SBackup.file.Replicator;
import org.noip.naszdomek.SBackup.file.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SBackupApp implements App {

	private static final Logger LOGGER = Logger.getLogger(SBackupApp.class);

	@Autowired
	private Config config;

	@Autowired
	@Qualifier("fullBackupSelector")
	private Selector fullBackupSelector;

	@Autowired
	@Qualifier("incrementalBackupSelector")
	private Selector incrementalBackupSelector;

	@Autowired
	private Replicator fullReplicator;

	public void run() throws IOException {

		LOGGER.debug("Application is starting");

		if (!config.isConfigured()) {
			return;
		}

		Selector selector = null;
		switch (config.getBackupType()) {
		case FULL:
			selector = fullBackupSelector;
			break;

		case INCREMENTAL:
			selector = incrementalBackupSelector;
			break;

		default:
			throw new RuntimeException();
		}

		Set<File> filesToBackup = selector.getFilesSelectedToBackup();
		Set<String> removedFiles = selector.getRemovedFiles();

		fullReplicator.replicate(filesToBackup, removedFiles);

	}

}
