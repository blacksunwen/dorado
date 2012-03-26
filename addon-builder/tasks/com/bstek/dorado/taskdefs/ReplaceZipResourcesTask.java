package com.bstek.dorado.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.mozilla.javascript.Context;

import com.bstek.dorado.ant.utils.ZipUtils;

public class ReplaceZipResourcesTask extends Task {
	private String zipFilePath;
	private String zipFileNamePattern;
	private String clean;
	private List<FileSet> fileSets = new ArrayList<FileSet>();

	public String getZipFilePath() {
		return zipFilePath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public String getZipFileNamePattern() {
		return zipFileNamePattern;
	}

	public void setZipFileNamePattern(String zipFileNamePattern) {
		this.zipFileNamePattern = zipFileNamePattern;
	}

	public String getClean() {
		return clean;
	}

	public void setClean(String clean) {
		this.clean = clean;
	}

	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
	}

	protected File findZipFile() throws IOException {
		File zipFileDir = new File(zipFilePath);
		if (!zipFileDir.exists()) {
			throw new IllegalArgumentException("\""
					+ zipFileDir.getCanonicalPath() + "\" does not exist.");
		}
		if (!zipFileDir.isDirectory()) {
			throw new IllegalArgumentException("\""
					+ zipFileDir.getCanonicalPath() + "\" is not a directory.");
		}

		File zipFile = null;
		Pattern pattern = Pattern.compile(zipFileNamePattern);
		for (File file : zipFileDir.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}

			Matcher matcher = pattern.matcher(file.getName());
			if (matcher.matches()) {
				zipFile = file;
				break;
			}
		}

		return zipFile;
	}

	@Override
	public void execute() throws BuildException {
		try {
			if (StringUtils.isEmpty(zipFilePath)) {
				throw new IllegalArgumentException("\"zipFilePath\" undefined.");
			}
			if (StringUtils.isEmpty(zipFileNamePattern)) {
				throw new IllegalArgumentException(
						"\"zipFileNamePattern\" undefined.");
			}

			File zipFile = findZipFile();
			if (zipFile == null) {
				throw new IllegalArgumentException("Zip File matches \""
						+ zipFilePath + '/' + zipFileNamePattern
						+ "\" not found.");
			}

			File workDir = File.createTempFile("replace", ".zip");
			workDir.delete();
			workDir.mkdirs();

			InputStream in = new FileInputStream(zipFile);
			try {
				ZipInputStream zis = new ZipInputStream(in);
				ZipUtils.unzip(zis, workDir);
				zis.close();
			} finally {
				in.close();
			}

			if (StringUtils.isNotEmpty(clean)) {
				for (String cleanFile : StringUtils.split(clean, ';')) {
					File file = new File(workDir, cleanFile);
					if (file.exists()) {
						if (file.isDirectory()) {
							com.bstek.dorado.ant.utils.FileUtils
									.removeDirectory(file);
						} else {
							file.delete();
						}
					}
				}
			}

			Context ctx = Context.enter();
			try {
				ctx.initStandardObjects();

				for (FileSet fs : fileSets) {
					DirectoryScanner ds = fs.getDirectoryScanner(getProject());
					File dir = ds.getBasedir();
					for (String src : ds.getIncludedDirectories()) {
						File targetFile = new File(workDir, src);
						if (!targetFile.exists()) {
							targetFile.mkdirs();
						}
					}

					for (String src : ds.getIncludedFiles()) {
						File targetFile = new File(workDir, src);
						if (!targetFile.getParentFile().exists()) {
							targetFile.getParentFile().mkdirs();
						}
						File file = new File(dir, src);
						FileUtils.getFileUtils().copyFile(file, targetFile);
					}
				}
			} finally {
				Context.exit();
			}

			zipFile.delete();

			OutputStream out = new FileOutputStream(zipFile);
			try {
				ZipOutputStream zos = new ZipOutputStream(out);
				ZipUtils.zip(workDir, zos);
				zos.close();
			} finally {
				out.close();
			}

			fileSets.clear();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
