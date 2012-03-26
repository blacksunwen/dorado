package com.bstek.dorado.ant.utils;

import java.io.File;
import java.io.IOException;

public final class FileUtils {
	private FileUtils() {
	}

	public static void clearDirectory(File dir) throws IOException {
		if (!dir.isDirectory()) {
			return;
		}

		for (File subFile : dir.listFiles()) {
			boolean deleted = false;

			if (subFile.isFile()) {
				deleted = subFile.delete();
			} else if (subFile.isDirectory()) {
				deleted = removeDirectory(subFile);
			}

			if (!deleted) {
				throw new IOException("Can not delete \""
						+ subFile.getAbsolutePath() + "\".");
			}
		}
	}

	public static boolean removeDirectory(File dir) throws IOException {
		if (!dir.isDirectory()) {
			return false;
		}
		clearDirectory(dir);
		return dir.delete();
	}
}
