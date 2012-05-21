/**
 * 
 */
package com.bstek.dorado.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-23
 */
public final class TempFileUtils {
	private static final String TEMP_DIR_PREFIX = "instance-";
	private static final String LOCK_FILE = "lock";

	private static File rootDir;
	private static File tempDir;

	private TempFileUtils() {
	}

	private static File getRootDir() throws IOException {
		if (rootDir == null) {
			rootDir = new File(System.getProperty("java.io.tmpdir")
					+ File.separator + ".dorado.tmp");
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

	private static void clearRootDir(File rootDir) {
		for (File file : rootDir.listFiles()) {
			try {
				if (file.isFile()) {
					file.delete();
				} else if (file.getName().startsWith(TEMP_DIR_PREFIX)) {
					File lockFile = new File(file, LOCK_FILE);
					if (lockFile.exists()) {
						if (!lockFile.delete()) {
							continue;
						}
					}
					FileUtils.clearDirectory(rootDir);
				} else {
					FileUtils.clearDirectory(rootDir);
				}
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	private static File getTempDir() throws IOException {
		if (tempDir == null) {
			File rootDir = getRootDir();

			try {
				clearRootDir(rootDir);
			} catch (Exception e) {
				// do nothing
			}

			int seed = 1;
			do {
				tempDir = new File(rootDir, TEMP_DIR_PREFIX + (seed++));
			} while (tempDir.exists());

			if (!tempDir.mkdirs()) {
				throw new IOException("Make directory \""
						+ tempDir.getAbsolutePath() + "\" failed.");
			} else {
				File lockFile = new File(tempDir, LOCK_FILE);
				FileChannel channel = new RandomAccessFile(lockFile, "rw")
						.getChannel();
				FileLock lock = null;
				try {
					lock = channel.tryLock();
					if (lock == null) {
						throw new IllegalStateException("Lock file \""
								+ tempDir + "\" failed.");
					}
				} catch (OverlappingFileLockException e) {
					throw new IllegalStateException("\"" + tempDir
							+ "\" is already locked.", e);
				}
			}
		}
		return tempDir;
	}

	public static FileHandler createTempFile(String fileNamePrefix,
			String fileNamesuffix) throws IOException {
		File file = File.createTempFile(fileNamePrefix, fileNamesuffix,
				getTempDir());
		file.deleteOnExit();
		return new FileHandler(file);
	}

}
