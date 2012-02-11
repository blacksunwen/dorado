package com.bstek.dorado.addonbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipInputStream;

import org.apache.tools.ant.Project;

public class Preprocessor {
	private static final String DEFAULT_TARGET_DIR = "dorado-builder";
	private static final String SYSTEM_DATA_FILE = "system.data";
	private static final String RESOURCES_FILE = "/resources/home.zip";

	private boolean validateHomeDir(Project project, File homeDirFile)
			throws Exception {
		boolean isValid = false;
		File dataFile = new File(homeDirFile, SYSTEM_DATA_FILE);
		if (dataFile.exists()) {
			Properties data = new Properties();
			InputStream in = new FileInputStream(dataFile);
			try {
				data.load(in);
			} finally {
				in.close();
			}

			String foundVersion = data.getProperty("fullVersion");
			project.log("Home dir found: " + foundVersion);

			if (Constants.FULL_VERSION.equals(foundVersion)) {
				isValid = true;
			}
		}
		return isValid;
	}

	public void extractFiles(Project project, File homeDirFile)
			throws Exception {
		project.log("Extracting home dir...");

		InputStream in = this.getClass().getResourceAsStream(RESOURCES_FILE);
		if (in == null) {
			throw new IOException("Resource \"" + RESOURCES_FILE
					+ "\" not exists");
		}

		try {
			ZipInputStream zis = new ZipInputStream(in);
			ZipUtils.unzip(zis, homeDirFile);
			zis.close();
		} finally {
			in.close();
		}

		File dataFile = new File(homeDirFile, SYSTEM_DATA_FILE);
		dataFile.createNewFile();

		Properties data = new Properties();
		data.setProperty("fullVersion", Constants.FULL_VERSION);
		FileOutputStream out = new FileOutputStream(dataFile);
		try {
			data.store(out, null);
		} finally {
			out.close();
		}
	}

	public File prepareHome(Project project, String homeDir) throws Exception {
		if (StringUtils.isEmpty(homeDir)) {
			homeDir = System.getProperty("user.home") + File.separator
					+ DEFAULT_TARGET_DIR;
		}
		project.log("Home dir: " + homeDir);

		boolean shouldExtractFiles = true;

		File homeDirFile = new File(homeDir);
		if (homeDirFile.exists()) {
			if (!homeDirFile.isDirectory()) {
				throw new IOException("\"" + homeDirFile.getCanonicalPath()
						+ "\" is not valid directory.");
			} else {
				shouldExtractFiles = !validateHomeDir(project, homeDirFile);
				if (shouldExtractFiles) {
					FileUtils.clearDirectory(homeDirFile);
				}
			}
		} else if (!homeDirFile.mkdirs()) {
			throw new IOException("Can not make dir \""
					+ homeDirFile.getCanonicalPath() + "\".");
		}

		if (shouldExtractFiles) {
			extractFiles(project, homeDirFile);
		}

		project.log("Home dir created.");
		return homeDirFile;
	}
}
