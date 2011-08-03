/**
 * 
 */
package com.bstek.dorado.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Unpacker;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-3-24
 */
public class Unpack200Task extends Task {
	private static final String JAR_EXT = ".jar";
	private static final String DEFAULT_SOURCE_EXT = ".pack.gz";

	private String sourceFileExt = DEFAULT_SOURCE_EXT;
	private boolean removeSourceFile = true;
	private List<FileSet> fileSets = new ArrayList<FileSet>();

	public String getSourceFileExt() {
		return sourceFileExt;
	}

	public void setSourceFileExt(String sourceFileExt) {
		this.sourceFileExt = sourceFileExt;
	}

	public boolean isRemovePackedFile() {
		return removeSourceFile;
	}

	public void setRemovePackedFile(boolean removeSourceFile) {
		this.removeSourceFile = removeSourceFile;
	}

	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
	}

	@Override
	public void execute() throws BuildException {
		Unpacker unpacker = Pack200.newUnpacker();

		try {
			for (FileSet fs : fileSets) {
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				File dir = ds.getBasedir();
				String[] srcs = ds.getIncludedFiles();
				for (String src : srcs) {
					File file = new File(dir, src);
					if (file.exists() && file.getName().endsWith(sourceFileExt)) {
						unpackJarFile(unpacker, file);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	public void unpackJarFile(Unpacker unpacker, File file) throws Exception {
		System.out.println("Unpack200: " + file.getAbsolutePath() + "...");

		String path = file.getAbsolutePath();
		path = path.substring(0, path.length() - sourceFileExt.length())
				+ JAR_EXT;

		FileOutputStream fos = new FileOutputStream(path);
		JarOutputStream jos = new JarOutputStream(fos);
		try {
			unpacker.unpack(file, jos);
		} finally {
			jos.close();
		}

		if (removeSourceFile) {
			file.delete();
		}
	}
}
