package org.noip.naszdomek.SBackup.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.noip.naszdomek.SBackup.config.Config;
import org.noip.naszdomek.SBackup.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MD5ChecksumComputator implements ChecksumComputator {
	
	@Autowired
	private Config config;

	private static final Logger LOGGER = Logger
			.getLogger(MD5ChecksumComputator.class);

	@Override
	public void createChecksumFile(Set<File> files, File dest) {
		Map<String, String> sums = checksum(files);

		try {

			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dest), "UTF8"));

			for (String path : sums.keySet()) {

				String entry = path + " " + sums.get(path);
				LOGGER.debug("Checksum: " + entry);
				out.append(entry).append("\r\n");
			}

			out.flush();
			out.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Map<String, String> checksum(Set<File> files) {
		Map<String, String> ret = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		for (File file : files) {
			String relativePath = Utils.pathPartReplacer(file, config
					.getDirectoryToBackup().getAbsolutePath(), "");

			try {
				FileInputStream fis = new FileInputStream(file);
				String md5 = org.apache.commons.codec.digest.DigestUtils
						.md5Hex(fis);
				ret.put(relativePath, md5);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return ret;
	}

}
