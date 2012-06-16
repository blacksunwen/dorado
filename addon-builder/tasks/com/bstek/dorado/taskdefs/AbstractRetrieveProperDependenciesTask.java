package com.bstek.dorado.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public abstract class AbstractRetrieveProperDependenciesTask extends Task {
	public static final String[] CONFS = new String[] { "compile", "runtime" };

	public static class ArtifactDescriptor {
		private String groupId;
		private String artifactId;
		private String revision;

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
	};

	private Ivy ivy;

	private String dependenciesDir;
	private String workDir;
	private String ivySettingsFile;
	private String ivyTemplateFile;

	public String getDependenciesDir() {
		return dependenciesDir;
	}

	public void setDependenciesDir(String dependenciesDir) {
		this.dependenciesDir = dependenciesDir;
	}

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public String getIvySettingsFile() {
		return ivySettingsFile;
	}

	public void setIvySettingsFile(String ivySettingsFile) {
		this.ivySettingsFile = ivySettingsFile;
	}

	public String getIvyTemplateFile() {
		return ivyTemplateFile;
	}

	public void setIvyTemplateFile(String ivyTemplateFile) {
		this.ivyTemplateFile = ivyTemplateFile;
	}

	protected Ivy getIvy() throws IOException, ParseException {
		if (ivy == null) {
			ivy = Ivy.newInstance();

			IvySettings settings = ivy.getSettings();
			settings.addAllVariables(System.getProperties());
			settings.setVariable("ivy.default.configuration.m2compatible",
					"true");

			File settingsFile = new File(ivySettingsFile);
			if (!settingsFile.exists()) {
				throw new IOException("ivy configuration file not found: "
						+ ivySettingsFile);
			} else if (settingsFile.isDirectory()) {
				throw new IOException("ivy configuration file is not a file: "
						+ ivySettingsFile);
			}
			ivy.configure(settingsFile);
		}
		return ivy;
	}

	protected void resolve(ArtifactDescriptor artifactDescriptor,
			String[] confs, File targetDir) throws IOException, ParseException {
		String groupId = artifactDescriptor.getGroupId();
		String artifactId = artifactDescriptor.getArtifactId();
		String revision = artifactDescriptor.getRevision();

		Ivy ivy = getIvy();
		IvySettings settings = ivy.getSettings();
		settings.setVariable("groupId", groupId);
		settings.setVariable("artifactId", artifactId);
		settings.setVariable("revision", revision);

		ResolveOptions resolveOptions = new ResolveOptions().setConfs(confs)
				.setValidate(true);

		File ivyfile = new File(ivyTemplateFile);
		ResolveReport report = ivy.resolve(ivyfile.toURI().toURL(),
				resolveOptions);
		if (report.hasError()) {
			throw new IllegalStateException("Resolve \"" + groupId + ':'
					+ artifactId + ':' + revision + "\" failed.");
		}

		retrieve(report, confs, targetDir);
	}

	protected void retrieve(ResolveReport report, String[] confs, File targetDir)
			throws IOException {
		ModuleDescriptor md = report.getModuleDescriptor();
		String retrievePattern = targetDir.getCanonicalPath()
				+ "/[artifact]-[revision](-[classifier]).[ext]";
		ivy.retrieve(md.getModuleRevisionId(), retrievePattern,
				new RetrieveOptions().setConfs(confs));
	}

	protected ArtifactDescriptor getArtifactDescriptor(File file)
			throws IOException {
		ZipFile zipFile = new ZipFile(file);
		try {
			ZipEntry zipEntry = zipFile
					.getEntry("META-INF/dorado-package.properties");
			if (zipEntry != null) {
				InputStream in = zipFile.getInputStream(zipEntry);
				try {
					Properties properties = new Properties();
					properties.load(in);

					String groupId = properties.getProperty("groupId");
					String artifactId = properties.getProperty("artifactId");
					String revision = properties.getProperty("revision");

					if (StringUtils.isNotEmpty(groupId)
							&& StringUtils.isNotEmpty(artifactId)
							&& StringUtils.isNotEmpty(revision)) {
						if (groupId.equals("com.bstek.dorado")
								&& revision.endsWith("-RELEASE")) {
							revision = StringUtils.substringBeforeLast(
									revision, "-RELEASE");
						}

						ArtifactDescriptor descriptor = new ArtifactDescriptor();
						descriptor.setGroupId(groupId);
						descriptor.setArtifactId(artifactId);
						descriptor.setRevision(revision);
						return descriptor;
					}
				} finally {
					in.close();
				}
			}
		} finally {
			zipFile.close();
		}
		return null;
	}

	protected void processDoradoAddon(ArtifactDescriptor artifactDescriptor,
			File jarFile, File workDirFile) throws Exception {
		log("Dorado Addon Found \"" + jarFile.getName() + "\".");
		log("Retrieve 2nd dependencies to \"" + workDirFile.getCanonicalPath()
				+ "\"...");
		log("ArtifactDescriptor: " + artifactDescriptor.getGroupId() + ", "
				+ artifactDescriptor.getArtifactId() + ", "
				+ artifactDescriptor.getRevision());
		resolve(artifactDescriptor, CONFS, workDirFile);
	}

	@Override
	public void execute() throws BuildException {
		try {
			if (StringUtils.isEmpty(dependenciesDir)) {
				throw new IllegalArgumentException(
						"\"dependenciesDir\" undefined.");
			}
			if (StringUtils.isEmpty(workDir)) {
				throw new IllegalArgumentException("\"workDir\" undefined.");
			}
			if (StringUtils.isEmpty(ivySettingsFile)) {
				throw new IllegalArgumentException(
						"\"ivySettingsFile\" undefined.");
			}
			if (StringUtils.isEmpty(ivyTemplateFile)) {
				throw new IllegalArgumentException(
						"\"ivyTemplateFile\" undefined.");
			}

			File dependenciesDirFile = new File(dependenciesDir);
			if (!dependenciesDirFile.exists()) {
				if (!dependenciesDirFile.mkdirs()) {
					throw new IOException("Make dir \""
							+ dependenciesDirFile.getCanonicalPath()
							+ "\" failed.");
				}
			}
			File workDirFile = new File(workDir);
			if (!workDirFile.exists()) {
				if (!workDirFile.mkdirs()) {
					throw new IOException("Make dir \""
							+ workDirFile.getCanonicalPath() + "\" failed.");
				}
			}

			doExecute();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	protected void doExecute() throws Exception {
		File dependenciesDirFile = new File(dependenciesDir);
		File workDirFile = new File(workDir);

		for (File jarFile : dependenciesDirFile.listFiles()) {
			if (jarFile.isFile()) {
				String fileName = jarFile.getName();
				if (fileName.startsWith("servlet-api-")
						|| fileName.startsWith("jsp-api-")) {
					jarFile.delete();
					continue;
				}

				log("Go through \"" + fileName + "\"...");
				ArtifactDescriptor artifactDescriptor = getArtifactDescriptor(jarFile);
				if (artifactDescriptor != null) {
					processDoradoAddon(artifactDescriptor, jarFile, workDirFile);
				}
			}
		}

		for (File jarFile : workDirFile.listFiles()) {
			File dependenciesJarFile = new File(dependenciesDirFile,
					jarFile.getName());
			if (dependenciesJarFile.exists()) {
				log("Delete \"" + dependenciesJarFile.getName() + "\"...");
				if (!dependenciesJarFile.delete()) {
					throw new IOException("Delete \""
							+ dependenciesJarFile.getCanonicalPath()
							+ "\" failed.");
				}
			}
			jarFile.delete();
		}
		workDirFile.delete();

		StringBuffer properDependencies = new StringBuffer();
		for (File jarFile : dependenciesDirFile.listFiles()) {
			if (jarFile.isFile()) {
				if (properDependencies.length() > 0) {
					properDependencies.append(',');
				}
				properDependencies.append(jarFile.getName());
			}
		}
		getProject().setUserProperty("properDependencies",
				properDependencies.toString());
	}
}
