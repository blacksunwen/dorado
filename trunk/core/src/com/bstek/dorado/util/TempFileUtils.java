/**
 * 
 */
package com.bstek.dorado.util;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-23
 */
public final class TempFileUtils {
	private static Map<String, FileHandler> fileHandleMap = new Hashtable<String, FileHandler>();
	private static File rootDir;
	private static File tempDir;

	private TempFileUtils() {
	}

	private static File getRootDir() throws IOException {
		if (rootDir == null) {
			rootDir = new File(System.getProperty("java.io.tmpdir")
					+ File.separator + ".dorado");
			if (!rootDir.exists()) {
				if (!rootDir.mkdirs()) {
					throw new IOException("Make directory \""
							+ rootDir.getAbsolutePath() + "\" failed.");
				}
			} else if (!rootDir.isDirectory()) {
				throw new IOException("\"" + rootDir.getAbsolutePath()
						+ "\" is not a directory.");
			}
		}
		return rootDir;
	}

	private static File getTempDir() throws IOException {
		if (tempDir == null) {
			File rootDir = getRootDir();

			try {
				FileUtils.clearDirectory(rootDir);
			} catch (Exception e) {
				// do nothing
			}

			int seed = 1;
			do {
				tempDir = new File(rootDir, "instance-" + (seed++));
			} while (tempDir.exists());

			if (!tempDir.mkdirs()) {
				throw new IOException("Make directory \""
						+ tempDir.getAbsolutePath() + "\" failed.");
			}
			tempDir.deleteOnExit();
		}
		return tempDir;
	}

	public static FileHandler createTempFile(String id, String fileNamePrefix,
			String fileNamesuffix) throws IOException {
		Assert.notEmpty(id);

		deleteTempFile(id);

		File file = File.createTempFile(fileNamePrefix, fileNamesuffix,
				getTempDir());
		file.deleteOnExit();

		FileHandler handler = new FileHandler(file);
		fileHandleMap.put(id, handler);
		return handler;
	}

	public static void deleteTempFile(String id) throws IOException {
		Assert.notEmpty(id);

		FileHandler handler = fileHandleMap.remove(id);
		if (handler != null) {
			handler.delete();
		}
	}
}
