package org.noip.naszdomek.SBackup.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("incrementalBackupSelector")
public class IncrementalBackupSelector implements Selector {

	private final Logger LOGGER = Logger.getLogger(IncrementalBackupSelector.class);

	@Autowired
	private Config config;
	
	@Autowired
	private ChecksumComputator checksumComputator;
	
	private Set<String> deletedFiles = new HashSet<>();

	@Override
	public Set<File> getFilesSelectedToBackup() {
		
		Set<File> allFiles = selectFiles(config.getDirectoryToBackup());
		
		Map<String, String> prevFilesSums = getPrevSums();
		
		Map<String, String> currFilesSums = checksumComputator.checksum(allFiles);
		
		// Get deleted files
		deletedFiles.clear();
		for (String path : prevFilesSums.keySet()) {
			if (!currFilesSums.containsKey(path)) {
				LOGGER.debug("Deleted file: " + path);
				deletedFiles.add(path);
			}
		}
		
		Set<File> selectedFiles = new HashSet<>();
		for (String path : currFilesSums.keySet()) {
			String prevFileSum = prevFilesSums.get(path);
			String currFileSum = currFilesSums.get(path);
			if (prevFileSum == null || !prevFileSum.equals(currFileSum) ) {
				// Select file
				File selectedFile = new File(config.getDirectoryToBackup().getAbsolutePath()+path);
				LOGGER.debug("Selected file after checksum: " + selectedFile.getAbsolutePath());
				selectedFiles.add(selectedFile);
			}
		}
		
		return selectedFiles;
		
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
		

	private Map<String, String> getPrevSums() {
		Map<String, String> ret = new HashMap<>();
		List<File> checksumFiles = getChecksumFiles();
		List<File> infoFiles = getInfoFiles();
		
		
		try {
			/* Add data */
			for (File file : checksumFiles) {
				List<String> lines = Files.readAllLines(file.toPath(),
						Charset.forName("UTF-8"));
				for (String line : lines) {
					String path, sum;
					int lastSpace = line.lastIndexOf(" ");
					path = line.substring(0, lastSpace).trim();
					sum = line.substring(lastSpace).trim();
					ret.put(path, sum);
				}
			}
			
			/* Remove keys for deleted files */
			for (File file : infoFiles) {
				List<String> lines = Files.readAllLines(file.toPath(),
						Charset.forName("UTF-8"));
				for (String line : lines) {
					String path = line.trim();
					ret.remove(path);
				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return ret;
	}

	private List<File> getInfoFiles() {
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.contains("_info.txt");
			}
		};
		
		List<File> files = getFilteredFiles(filter);
		return files;
	}

	private List<File> getChecksumFiles() {
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.contains("_checksums.txt");
			}
		};
		
		List<File> files = getFilteredFiles(filter);
		return files;
	}

	private List<File> getFilteredFiles(FilenameFilter filter) {
		File dir = config.getBackupDirectory();
		File [] listed = dir.listFiles(filter);
		List<File> files = Arrays.asList(listed);
		Collections.sort(files);
		return files;
	}

	@Override
	public Set<String> getRemovedFiles() {
		return this.deletedFiles;
	}

}
