package com.bstek.dorado.taskdefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.mozilla.javascript.Context;

public class ReplaceZipResourcesTask extends Task {
	private String zipFilePath;
	private String zipFileNamePattern;
	private boolean merge;
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

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
	}

	@Override
	public void execute() throws BuildException {
		try {
			Context ctx = Context.enter();
			try {
				ctx.initStandardObjects();

				for (FileSet fs : fileSets) {
					DirectoryScanner ds = fs.getDirectoryScanner(getProject());
					File dir = ds.getBasedir();
					String[] srcs = ds.getIncludedFiles();
					for (String src : srcs) {
						File file = new File(dir, src);
						if (file.exists()) {

						}
					}
				}
			} finally {
				Context.exit();
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

}
