package org.noip.naszdomek.SBackup.config.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.stereotype.Component;

@Component
public class DefaultConfig implements Config {

	private final static Logger LOGGER = Logger.getLogger(DefaultConfig.class
			.getName());

	private Boolean configured = false;

	private File directoryToBackup = null;

	private File backupDirectory = null;

	public void setCmdParams(String[] args) {
		LOGGER.debug("Setting commandline parameters");

		if (args != null && args.length > 1) {
			parseDirectoryToBackup(args);
			parseBackupDirectory(args);
		}

		this.configured = checkConfiguration();
	}

	private void parseBackupDirectory(String[] args) {
		String backupDestinationPath = args[1];
		LOGGER.info("Backup destination path is: " + backupDestinationPath);
		this.backupDirectory = new File(backupDestinationPath);
		
	}

	private void parseDirectoryToBackup(String[] args) {
		String backupSourcePath = args[0];
		LOGGER.info("Backup source path is: " + backupSourcePath);
		this.directoryToBackup = new File(backupSourcePath);

	}

	private Boolean checkConfiguration() {
		if (directoryToBackup == null) {
			LOGGER.debug("Directory to backup is null");
			return false;
		}
		if (!directoryToBackup.exists()) {
			LOGGER.warn("Directory to backup not exists");
			return false;
		}
		if (!directoryToBackup.isDirectory()) {
			LOGGER.warn("Backup source path is not a directory!");
			return false;
		}
		if (backupDirectory == null) {
			LOGGER.debug("Backup directory is null");
			return false;
		}
		if (!backupDirectory.exists()) {
			LOGGER.warn("Creating backup directory: " + backupDirectory.getAbsolutePath());
			if (!backupDirectory.mkdir()) {
				LOGGER.warn("Can not create backup directory!");
				return false;
			}
		}
		if (!backupDirectory.isDirectory()) {
			LOGGER.warn("Backup destination is not a directory!");
			return false;
		}
		LOGGER.debug("Config is OK");
		return true;
	}

	public File getDirectoryToBackup() {
		return directoryToBackup;
	}

	public Boolean isConfigured() {
		return this.configured;
	}

	public File getBackupDirectory() {
		return backupDirectory;
	}

}
