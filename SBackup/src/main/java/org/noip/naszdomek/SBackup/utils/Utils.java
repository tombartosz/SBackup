package org.noip.naszdomek.SBackup.utils;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Utils {

	private Utils() {

	}

	public static String pathPartReplacer(File f, String replace,
			String replacement) {
		String path = f.getAbsolutePath();
		int replaceStart = path.indexOf(replace);

		if (replaceStart == -1) {
			throw new RuntimeException();
		}

		path = path.substring(0, replaceStart) + replacement
				+ path.substring(replaceStart + replace.length());
		return path;

	}

	public static void deleteDirectory(File dirFile) {
		Path dir = dirFile.toPath();
		try {
			Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {
					if (exc == null) {
						Files.delete(dir);
						return CONTINUE;
					} else {
						throw exc;
					}
				}

			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
