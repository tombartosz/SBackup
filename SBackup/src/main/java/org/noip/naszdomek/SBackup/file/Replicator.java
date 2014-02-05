package org.noip.naszdomek.SBackup.file;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface Replicator {
	
	public void replicate(Set<File> files, Set<String> removedFiles) throws IOException;

}
