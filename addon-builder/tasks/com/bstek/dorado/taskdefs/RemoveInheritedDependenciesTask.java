package com.bstek.dorado.taskdefs;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Ant.Reference;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

public class RemoveInheritedDependenciesTask extends
		AbstractRetrieveProperDependenciesTask {
	private Path compileClasspath;

	public Path getClasspath() {
		return compileClasspath;
	}

	public void setClasspath(Path classpath) {
		compileClasspath = classpath;
	}

	public Path createClasspath() {
		if (compileClasspath == null) {
			compileClasspath = new Path(getProject());
		}
		return compileClasspath.createPath();
	}

	public void setClasspathRef(Reference r) {
		createClasspath().setRefid(r);
	}

	@Override
	public void doExecute() throws Exception {
		String dependenciesDir = getDependenciesDir();
		if (StringUtils.isEmpty(dependenciesDir)) {
			throw new IllegalArgumentException("\"dependenciesDir\" undefined.");
		}
		File dependenciesDirFile = new File(dependenciesDir);
		if (!dependenciesDirFile.exists()) {
			if (!dependenciesDirFile.mkdirs()) {
				throw new IOException("Make dir \""
						+ dependenciesDirFile.getCanonicalPath() + "\" failed.");
			}
		}

		if (compileClasspath != null) {
			FileUtils fileUtils = FileUtils.getFileUtils();
			for (String path : compileClasspath.list()) {
				File jarFile = new File(path);
				if (jarFile.exists()) {
					fileUtils.copyFile(jarFile, new File(dependenciesDir,
							jarFile.getName()));
				} else if (jarFile.isDirectory()) {
					continue;
				} else {
					log("Library File \"" + path + "\" not exists.",
							Project.MSG_WARN);
				}
			}
		}

		super.doExecute();
	}
}
