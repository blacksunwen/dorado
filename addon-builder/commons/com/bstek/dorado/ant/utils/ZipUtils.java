package com.bstek.dorado.ant.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {
	private static final int BUFFER_SIZE = 8192;

	private ZipUtils() {
	}

	private static void putZipEntry(ZipOutputStream zipOutputStream, File file,
			String subPath) throws IOException {
		String path;
		if (StringUtils.isEmpty(subPath)) {
			path = file.getName();
		} else {
			path = subPath + '/' + file.getName();
		}

		ZipEntry entry;
		if (file.isDirectory()) {
			entry = new ZipEntry(path + '/');
		} else {
			entry = new ZipEntry(path);
		}
		entry.setTime(file.lastModified());
		zipOutputStream.putNextEntry(entry);

		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				putZipEntry(zipOutputStream, subFile, path);
			}
		} else {
			byte[] buff = new byte[BUFFER_SIZE];

			FileInputStream fi = new FileInputStream(file);
			BufferedInputStream fileData = new BufferedInputStream(fi,
					BUFFER_SIZE);
			try {
				int count;
				while ((count = fileData.read(buff, 0, BUFFER_SIZE)) != -1) {
					zipOutputStream.write(buff, 0, count);
				}
			} finally {
				fileData.close();
			}
		}
	}

	public static void zip(File targetDir, ZipOutputStream zipOutputStream)
			throws IOException {
		for (File file : targetDir.listFiles()) {
			putZipEntry(zipOutputStream, file, null);
		}
		zipOutputStream.finish();
		zipOutputStream.flush();
	}

	public static void unzip(ZipInputStream zipInputStream, File targetDir)
			throws IOException {
		byte[] buff = new byte[BUFFER_SIZE];
		ZipEntry entry;
		while ((entry = zipInputStream.getNextEntry()) != null) {
			File file = new File(targetDir, entry.getName());

			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream out = new FileOutputStream(file);
				try {
					int n;
					while ((n = zipInputStream.read(buff)) != -1) {
						out.write(buff, 0, n);
					}
				} finally {
					out.close();
				}
			}
			file.setLastModified(entry.getTime());
		}
	}
}
