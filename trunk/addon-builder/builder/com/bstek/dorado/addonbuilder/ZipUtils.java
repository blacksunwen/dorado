package com.bstek.dorado.addonbuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtils {
	private ZipUtils() {
	}

	public static void unzip(ZipInputStream zipInputStream, File targetDir)
			throws IOException {
		ZipEntry entry;
		while ((entry = zipInputStream.getNextEntry()) != null) {
			File file = new File(targetDir, entry.getName());

			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				FileOutputStream out = new FileOutputStream(file);
				try {
					byte[] buff = new byte[8192];
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
