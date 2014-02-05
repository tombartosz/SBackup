package org.noip.naszdomek.SBackup.file;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface ChecksumComputator {
	
	public void createChecksumFile(Set<File> files, File dest);
	public Map<String, String> checksum(Set<File> files);

}
