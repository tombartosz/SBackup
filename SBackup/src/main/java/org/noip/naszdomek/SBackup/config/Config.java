package org.noip.naszdomek.SBackup.config;

import java.io.File;

public interface Config {
	
	public void setCmdParams(String[] args);
	
	public File getDirectoryToBackup();
	
	public File getBackupDirectory();
	
	public Boolean isConfigured();

}
