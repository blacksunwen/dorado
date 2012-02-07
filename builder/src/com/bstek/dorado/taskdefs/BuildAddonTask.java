package com.bstek.dorado.taskdefs;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.Path;

public class BuildAddonTask extends Ant {
	private static final String ADDON_BUILDER_XML = "addon-builder.xml";

	private Path compileClasspath;

	private String name;
	private String archieveName;
	private String vendor;
	private String version;
	private String qualifier = "RELEASE";
	private String homeDir;
	private String baseDir;
	private String workDir;
	private String targetDir;
	private boolean buildJar = true;
	private boolean buildSourceJar = false;
	private boolean buildJsDoc = false;
	private boolean buildJavaDoc = false;
	private String skins = "default";
	private String pomFile;
	private String ivyFile;
	private String srcCharset = "utf-8";
	private String clientSrc = "client";
	private String javaSrc = "src";
	private String javaCompatSource = "1.5";
	private String javaCompatTarget = "1.5";

	public BuildAddonTask() {
		super();
		initialize();
	}

	public BuildAddonTask(Task owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		setInheritRefs(true);
		setUseNativeBasedir(true);
	}

	@Override
	public void execute() throws BuildException {
		log("Dorado AddonBuilder [" + Constants.FULL_VERSION + "]");

		try {
			if (StringUtils.isEmpty(name)) {
				throw new IllegalArgumentException(
						"'name' undefined. etc: 'myaddon'");
			}
			if (StringUtils.isEmpty(archieveName)) {
				archieveName = "dorado-" + name;
			}
			if (StringUtils.isEmpty(vendor)) {
				throw new IllegalArgumentException(
						"'vendor' undefined. etc: 'www.BSTEK.com'");
			}
			if (StringUtils.isEmpty(version)) {
				throw new IllegalArgumentException(
						"'version' undefined. etc: '0.9.1'");
			}
			if (StringUtils.isEmpty(targetDir)) {
				throw new IllegalArgumentException(
						"'targetDir' undefined. etc: 'D:/dorado7/target'");
			}

			Preprocessor preprocessor = new Preprocessor();
			File homeDirFile = preprocessor.prepareHome(getProject(), name,
					homeDir);
			homeDir = homeDirFile.getCanonicalPath();
			File buildFile = new File(homeDirFile, ADDON_BUILDER_XML);

			if (StringUtils.isEmpty(workDir)) {
				workDir = homeDir + "/work";
			}
			if (StringUtils.isEmpty(javaSrc)) {
				javaSrc = baseDir + "/src";
			}
			if (StringUtils.isEmpty(clientSrc)) {
				clientSrc = baseDir + "/client";
			}
			if (StringUtils.isEmpty(ivyFile)) {
				ivyFile = baseDir + "/ivy.xml";
			}

			setAntfile(buildFile.getCanonicalPath());

			createProperty("addon.name", name);
			createProperty("addon.archieveName", archieveName);
			createProperty("addon.vendor", vendor);
			createProperty("addon.version", version);
			createProperty("addon.qualifier", qualifier);
			createProperty("homeDir", homeDir); // 注意此处没有addon前缀
			createProperty("workDir", workDir); // 注意此处没有addon前缀
			createProperty("addon.targetDir", targetDir);

			if (buildJar) {
				createProperty("addon.buildJar", buildJar);
			}
			if (buildSourceJar) {
				createProperty("addon.buildSourceJar", buildSourceJar);
			}
			if (buildJsDoc) {
				createProperty("addon.buildJsDoc", buildJsDoc);
			}
			if (buildJavaDoc) {
				createProperty("addon.buildJavaDoc", buildJavaDoc);
			}

			createProperty("addon.skins", skins);
			createProperty("addon.pomFile", pomFile);
			createProperty("addon.ivyFile", ivyFile);
			createProperty("addon.clientSrc", clientSrc);
			createProperty("addon.javaSrc", javaSrc);
			createProperty("addon.srcCharset", srcCharset);
			createProperty("addon.javaCompatSource", javaCompatSource);
			createProperty("addon.javaCompatTarget", javaCompatTarget);

			if (StringUtils.isNotEmpty(baseDir)) {
				createProperty("basedir", baseDir);
			}

			Project project = getProject();
			Path userClasspath = (Path) project.createDataType("path");
			project.addReference("userClasspath", userClasspath);
			if (compileClasspath != null) {
				userClasspath.append(compileClasspath);
			}

			super.execute();

			log("Dorado addon [" + name + "] build OK.");
		} catch (Exception e) {
			// try {
			// File file = new File("e:/temp/log.txt");
			// file.createNewFile();
			// FileOutputStream out = new FileOutputStream(file);
			// PrintWriter writer = new PrintWriter(out);
			// e.printStackTrace(writer);
			// writer.flush();
			// writer.close();
			// out.flush();
			// out.close();
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }

			log(e, Project.MSG_ERR);
			throw new BuildException(e.getMessage(), e);
		}
	}

	protected void createProperty(String name, Object value) {
		if (value != null) {
			Property Property = createProperty();
			Property.setName(name);
			Property.setValue(value);
		}
	}

	public void setClasspath(Path classpath) {
		if (compileClasspath == null)
			compileClasspath = classpath;
		else
			compileClasspath.append(classpath);
	}

	public Path getClasspath() {
		return compileClasspath;
	}

	private Path createClasspath() {
		if (compileClasspath == null)
			compileClasspath = new Path(getProject());
		return compileClasspath.createPath();
	}

	public void setClasspathRef(Reference r) {
		createClasspath().setRefid(r);
	}

	public Path getCompileClasspath() {
		return compileClasspath;
	}

	public void setCompileClasspath(Path compileClasspath) {
		this.compileClasspath = compileClasspath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("Set: " + name);
		this.name = name;
	}

	public String getArchieveName() {
		return archieveName;
	}

	public void setArchieveName(String archieveName) {
		this.archieveName = archieveName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public boolean isBuildJar() {
		return buildJar;
	}

	public void setBuildJar(boolean buildJar) {
		this.buildJar = buildJar;
	}

	public boolean isBuildSourceJar() {
		return buildSourceJar;
	}

	public void setBuildSourceJar(boolean buildSourceJar) {
		this.buildSourceJar = buildSourceJar;
	}

	public boolean isBuildJsDoc() {
		return buildJsDoc;
	}

	public void setBuildJsDoc(boolean buildJsDoc) {
		this.buildJsDoc = buildJsDoc;
	}

	public boolean isBuildJavaDoc() {
		return buildJavaDoc;
	}

	public void setBuildJavaDoc(boolean buildJavaDoc) {
		this.buildJavaDoc = buildJavaDoc;
	}

	public String getSkins() {
		return skins;
	}

	public void setSkins(String skins) {
		this.skins = skins;
	}

	public String getPomFile() {
		return pomFile;
	}

	public void setPomFile(String pomFile) {
		this.pomFile = pomFile;
	}

	public String getIvyFile() {
		return ivyFile;
	}

	public void setIvyFile(String ivyFile) {
		this.ivyFile = ivyFile;
	}

	public String getSrcCharset() {
		return srcCharset;
	}

	public void setSrcCharset(String srcCharset) {
		this.srcCharset = srcCharset;
	}

	public String getClientSrc() {
		return clientSrc;
	}

	public void setClientSrc(String clientSrc) {
		this.clientSrc = clientSrc;
	}

	public String getJavaSrc() {
		return javaSrc;
	}

	public void setJavaSrc(String javaSrc) {
		this.javaSrc = javaSrc;
	}

	public String getJavaCompatSource() {
		return javaCompatSource;
	}

	public void setJavaCompatSource(String javaCompatSource) {
		this.javaCompatSource = javaCompatSource;
	}

	public String getJavaCompatTarget() {
		return javaCompatTarget;
	}

	public void setJavaCompatTarget(String javaCompatTarget) {
		this.javaCompatTarget = javaCompatTarget;
	}

}
