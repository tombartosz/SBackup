package org.noip.naszdomek.SBackup.config.impl;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.stereotype.Component;

@Component
public class DefaultConfig implements Config{
	
	private final static Logger LOGGER = Logger.getLogger(DefaultConfig.class .getName()); 

	public void setCmdParams(String [] args) {
		LOGGER.debug("Setting commandline parameters");
		// TODO Auto-generated method stub
		
	}

}
