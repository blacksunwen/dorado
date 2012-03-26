package com.bstek.dorado.taskdefs;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class ParseVersionTask extends Task {
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private String version;

	@Override
	public void execute() throws BuildException {
		try {
			if (StringUtils.isEmpty(version)) {
				throw new IllegalArgumentException("\"version\" undefined.");
			}

			String majorVersion = "0";
			String minorVersion = "0";
			String incrementalVersion = "0";

			String[] sections = StringUtils.split(version, '.');
			majorVersion = sections[0];
			if (sections.length > 1) {
				minorVersion = sections[1];
				if (sections.length > 2) {
					incrementalVersion = sections[2];
				}
			}

			Project project = getProject();
			project.setUserProperty("majorVersion", majorVersion);
			project.setUserProperty("minorVersion", minorVersion);
			project.setUserProperty("incrementalVersion", incrementalVersion);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
