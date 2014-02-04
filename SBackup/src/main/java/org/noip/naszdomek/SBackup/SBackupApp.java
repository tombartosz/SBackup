package org.noip.naszdomek.SBackup;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.noip.naszdomek.SBackup.file.Replicator;
import org.noip.naszdomek.SBackup.file.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SBackupApp implements App {

	private static final Logger LOGGER = Logger.getLogger(SBackupApp.class);
	
	@Autowired
	private Config config;
	
	@Autowired
	private Selector selector;
	
	@Autowired
	private Replicator replicator;
	
	
	public void run() throws IOException {
		
		LOGGER.debug("Application is starting");
		
		if (!config.isConfigured()) {
			return;
		}
		
		Set<File> filesToBackup = selector.getFilesSelectedToBackup();
		
		replicator.replicate(filesToBackup);
		

	}

}
