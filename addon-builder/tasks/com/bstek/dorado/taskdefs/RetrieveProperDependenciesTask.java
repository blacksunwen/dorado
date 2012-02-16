package com.bstek.dorado.taskdefs;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class RetrieveProperDependenciesTask extends
		AbstractRetrieveProperDependenciesTask {
	private String groupId;
	private String artifactId;
	private String revision;
	private String LibDir;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getLibDir() {
		return LibDir;
	}

	public void setLibDir(String libDir) {
		LibDir = libDir;
	}

	@Override
	protected void processDoradoAddon(ArtifactDescriptor artifactDescriptor,
			File jarFile, File inheritedLibDir) throws Exception {
		if (artifactDescriptor.getArtifactId().equals(artifactId)
				&& artifactDescriptor.getGroupId().equals(groupId)) {
			FileUtils.getFileUtils().copyFile(jarFile,
					new File(LibDir, jarFile.getName()));
			jarFile.delete();
		} else {
			super.processDoradoAddon(artifactDescriptor, jarFile,
					inheritedLibDir);
		}
	}

	@Override
	public void execute() throws BuildException {
		try {
			if (StringUtils.isEmpty(LibDir)) {
				throw new IllegalArgumentException("\"LibDir\" undefined.");
			}

			File libDirFile = new File(LibDir);
			if (!libDirFile.exists()) {
				if (!libDirFile.mkdirs()) {
					throw new IOException("Make dir \""
							+ libDirFile.getCanonicalPath() + "\" failed.");
				}
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}

		super.execute();
	}
}
