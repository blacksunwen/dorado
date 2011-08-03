package com.bstek.dorado.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class BuildI18NResourceTask extends Task {
	private String inputDir;
	private String outputDir;

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	@Override
	public void execute() throws BuildException {
		try {
			assert StringUtils.isNotBlank(inputDir);
			if (StringUtils.isBlank(outputDir))
				outputDir = inputDir;

			File fileInputDir = new File(inputDir);
			File[] listFiles = fileInputDir.listFiles(new FilenameFilter() {
				public boolean accept(File paramFile, String name) {
					return name.endsWith(".properties");
				}
			});
			for (File propertiesFile : listFiles) {
				Properties properties = new Properties();
				InputStream in = new FileInputStream(propertiesFile);
				try {
					properties.load(in);
				} finally {
					in.close();
				}

				File outputFile = new File(outputDir
						+ '/'
						+ propertiesFile.getName()
								.replace(".properties", ".js"));
				System.out.println("Building " + outputFile.getAbsolutePath()
						+ "...");

				OutputStream out = new FileOutputStream(outputFile);
				Writer writer = new PrintWriter(out);
				try {
					String namespace = properties.getProperty("namespace");
					properties.remove("namespace");
					writer.append("dorado.util.Resource.append(");
					if (StringUtils.isEmpty(namespace)) {
						writer.append("\n");
					} else {
						writer.append("\"")
								.append(StringEscapeUtils
										.escapeJavaScript(namespace))
								.append("\",");
					}

					int i = 0;
					writer.append("{\n");
					for (Map.Entry<?, ?> entry : properties.entrySet()) {
						String value = (String) entry.getValue();
						if (value == null)
							continue;
						if (i > 0)
							writer.append(",\n");
						writer.append("\"")
								.append((String) entry.getKey())
								.append("\":")
								.append('\"')
								.append(StringEscapeUtils
										.escapeJavaScript(value)).append('\"');
						i++;
					}
					writer.append("});");
					writer.flush();
				} finally {
					writer.close();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}
}
