/**
 * 
 */
package com.bstek.dorado.taskdefs;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-10
 */
public class MakeAddonBuilderHomeTask extends Task {
	private String homeDir;

	@Override
	public void execute() throws BuildException {
		try {
			Preprocessor preprocessor = new Preprocessor();
			File homeDirFile = preprocessor.prepareHome(getProject(), homeDir);
			homeDir = homeDirFile.getCanonicalPath();
			getProject().setUserProperty("addonBuilder.homeDir", homeDir);
		} catch (Exception e) {
			log(e, Project.MSG_ERR);
			throw new BuildException(e.getMessage(), e);
		}
	}

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

}
