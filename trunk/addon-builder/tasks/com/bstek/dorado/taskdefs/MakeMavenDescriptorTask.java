package com.bstek.dorado.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MakeMavenDescriptorTask extends Task {
	private String pom;
	private String dist;
	private String doradoPackageFile;

	public String getPom() {
		return pom;
	}

	public void setPom(String pom) {
		this.pom = pom;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getDoradoPackageFile() {
		return doradoPackageFile;
	}

	public void setDoradoPackageFile(String doradoPackageFile) {
		this.doradoPackageFile = doradoPackageFile;
	}

	@Override
	public void execute() throws BuildException {
		try {
			if (StringUtils.isEmpty(pom)) {
				throw new IllegalArgumentException("\"pom\" undefined.");
			}
			if (StringUtils.isEmpty(dist)) {
				throw new IllegalArgumentException("\"dist\" undefined.");
			}
			if (StringUtils.isEmpty(doradoPackageFile)) {
				throw new IllegalArgumentException(
						"\"doradoPackageFile\" undefined.");
			}

			File pomFile = new File(pom);

			log("Parsing  POM: " + pomFile.getCanonicalPath());
			if (pomFile.exists()) {
				SAXReader reader = new SAXReader();
				Document document = reader.read(pomFile);
				Element projectElement = document.getRootElement();

				String groupId = projectElement.elementText("groupId");
				if (StringUtils.isEmpty(groupId)) {
					throw new IOException("Can not read \"groupId\" from \""
							+ pomFile.getCanonicalPath() + "\".");
				}

				String artifactId = projectElement.elementText("artifactId");
				if (StringUtils.isEmpty(artifactId)) {
					throw new IOException("Can not read \"artifactId\" from \""
							+ pomFile.getCanonicalPath() + "\".");
				}

				String version = projectElement.elementText("version");
				if (StringUtils.isEmpty(version)) {
					throw new IOException("Can not read \"version\" from \""
							+ pomFile.getCanonicalPath() + "\".");
				}

				File distDir = new File(dist, "META-INF/maven");
				if (distDir.exists()) {
					if (!distDir.mkdirs()) {
						throw new IOException("Mkdir \""
								+ distDir.getCanonicalPath() + "\" failed.");
					}
				}

				File pomDir = new File(distDir, groupId + '/' + artifactId);
				if (pomDir.exists()) {
					if (!pomDir.mkdirs()) {
						throw new IOException("Mkdir \""
								+ pomDir.getCanonicalPath() + "\" failed.");
					}
				}

				File targetPomFile = new File(pomDir, "pom.xml");
				log("Copy POM \"" + pomFile.getCanonicalPath() + "\" --> \""
						+ targetPomFile.getCanonicalPath() + "\".");
				FileUtils.getFileUtils().copyFile(pomFile, targetPomFile);

				Properties properties = new Properties();
				properties.setProperty("groupId", groupId);
				properties.setProperty("artifactId", artifactId);
				properties.setProperty("version", version);

				File pomPropertiesFile = new File(pomDir, "pom.properties");
				log("Generate POM \"" + pomPropertiesFile.getCanonicalPath()
						+ "\".");
				FileOutputStream out = new FileOutputStream(pomPropertiesFile);
				try {
					properties.store(out, null);
				} finally {
					out.close();
				}

				File packageFile = new File(doradoPackageFile);
				log("Write Package File \"" + packageFile.getCanonicalPath()
						+ "\".");
				if (!packageFile.exists()) {
					throw new IOException("\"" + packageFile.getCanonicalPath()
							+ "\" not exists.");
				}

				FileInputStream in = new FileInputStream(packageFile);
				try {
					properties.load(in);
				} finally {
					in.close();
				}
				properties.setProperty("groupId", groupId);
				properties.setProperty("artifactId", artifactId);
				out = new FileOutputStream(packageFile);
				try {
					properties.store(out, null);
				} finally {
					out.close();
				}
			} else {
				log("Unable to parse pom file \"" + pomFile.getCanonicalPath()
						+ "\".");
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
