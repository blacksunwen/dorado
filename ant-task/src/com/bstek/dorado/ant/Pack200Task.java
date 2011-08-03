/**
 * 
 */
package com.bstek.dorado.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.jar.JarFile;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-3-24
 */
public class Pack200Task extends Task {
	private static final String JAR_EXT = ".jar";
	private static final String DEFAULT_TARGET_EXT = ".pack.gz";

	private String targetFileExt = DEFAULT_TARGET_EXT;
	private boolean removeSourceJar = true;
	private List<FileSet> fileSets = new ArrayList<FileSet>();

	public String getTargetFileExt() {
		return targetFileExt;
	}

	public void setTargetFileExt(String targetFileExt) {
		this.targetFileExt = targetFileExt;
	}

	public boolean isRemoveSourceJar() {
		return removeSourceJar;
	}

	public void setRemoveSourceJar(boolean removeSourceJar) {
		this.removeSourceJar = removeSourceJar;
	}

	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
	}

	@Override
	public void execute() throws BuildException {
		Packer packer = Pack200.newPacker();
		SortedMap<String, String> properties = packer.properties();
		properties.put(Packer.EFFORT, "9");
		properties.put(Packer.SEGMENT_LIMIT, "-1");

		try {
			for (FileSet fs : fileSets) {
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				File dir = ds.getBasedir();
				String[] srcs = ds.getIncludedFiles();
				for (String src : srcs) {
					File file = new File(dir, src);
					if (file.exists() && file.getName().endsWith(JAR_EXT)) {
						packJarFile(packer, file);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	public void packJarFile(Packer packer, File file) throws Exception {
		System.out.println("Pack200: " + file.getAbsolutePath() + "...");

		String path = file.getAbsolutePath();
		path = path.substring(0, path.length() - JAR_EXT.length())
				+ targetFileExt;

		JarFile jarFile = new JarFile(file);
		FileOutputStream fos = new FileOutputStream(path);
		try {
			packer.pack(jarFile, fos);
		} finally {
			jarFile.close();
			fos.close();
		}

		if (removeSourceJar) {
			file.delete();
		}
	}
}
