package com.bstek.dorado.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import net.sf.json.util.JSONBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-10-17
 */
public class Directory2JsonTask extends Task {
	private String root;
	private String outputDir;

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	@Override
	public void execute() throws BuildException {
		if (StringUtils.isEmpty(root)) {
			throw new BuildException("[root] undefined.");
		}
		if (StringUtils.isEmpty(outputDir)) {
			throw new BuildException("[outputDir] undefined.");
		}

		try {
			goThroughDir(new File(getProject().getBaseDir(), root), "root", new Context());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	private void goThroughDir(File dir, String outputfileName, Context context)
			throws Exception {
		System.out.println("Processing: " + dir.getAbsolutePath());
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) return;

		File outDir = new File(getProject().getBaseDir(), outputDir);
		File outputFile = new File(outDir, outputfileName + ".js");
		outputFile.createNewFile();
		Writer writer = new FileWriter(outputFile);
		try {
			JSONBuilder jsonBuilder = new JSONBuilder(writer);
			jsonBuilder.array();
			for (File file : dir.listFiles()) {
				jsonBuilder.object();
				String id = context.newFileId(), name = file.getName(), ext = "";
				int i = name.lastIndexOf('.');
				if (i > 0) {
					ext = name.substring(i + 1);
				}
				jsonBuilder.key("id").value(id);
				jsonBuilder.key("name").value(name);
				jsonBuilder.key("type").value(ext);
				jsonBuilder.key("isDirectory").value(file.isDirectory());
				jsonBuilder.key("lastModified").value(file.lastModified());
				jsonBuilder.key("length").value(file.length());
				jsonBuilder.key("canWrite").value(file.canWrite());
				jsonBuilder.key("canRead").value(file.canRead());
				jsonBuilder.key("canExecute").value(file.canExecute());
				jsonBuilder.key("isHidden").value(file.isHidden());
				jsonBuilder.endObject();

				if (file.isDirectory()) {
					goThroughDir(file, id, context);
				}
			}
			jsonBuilder.endArray();
		}
		finally {
			writer.close();
		}
	}
}

class Context {
	private int counter;

	public String newFileId() {
		return (counter++) + "";
	}
}
