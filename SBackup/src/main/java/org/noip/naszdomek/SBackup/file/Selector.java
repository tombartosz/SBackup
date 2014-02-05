package org.noip.naszdomek.SBackup.file;

import java.io.File;
import java.util.Set;

public interface Selector {
	public Set<File> getFilesSelectedToBackup();
	
	public Set<String> getRemovedFiles();
}
