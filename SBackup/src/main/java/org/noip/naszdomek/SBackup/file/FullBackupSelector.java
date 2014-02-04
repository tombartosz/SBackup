package org.noip.naszdomek.SBackup.file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FullBackupSelector implements Selector {

	private final Logger LOGGER = Logger.getLogger(FullBackupSelector.class);

	@Autowired
	private Config config;

	@Override
	public Set<File> getFilesSelectedToBackup() {

		return selectFiles(config.getDirectoryToBackup());

	}

	private Set<File> selectFiles(File directory) {
		Set<File> selectedFiles = new HashSet<>();
		LOGGER.debug("Entering directory: " + directory.getAbsolutePath());
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				selectedFiles.addAll(selectFiles(f));
			} else {
				LOGGER.debug("Selecting file: " + f.getAbsolutePath());
				selectedFiles.add(f);
			}
		}
		return selectedFiles;
	}

}
