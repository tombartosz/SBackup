package org.noip.naszdomek.SBackup;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SBackupApp implements App {

	private static final Logger LOGGER = Logger.getLogger(SBackupApp.class);
	
	@Autowired
	private Config config;
	
	
	public void run() {
		
		LOGGER.debug("Application is starting");
		

	}

}
