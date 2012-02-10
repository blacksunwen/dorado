package com.bstek.dorado.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.Path;

public class BuildDoradoAddonTask extends Ant {
	private static final String ADDON_BUILDER_XML = "addon-builder.xml";
	private static final String OUTPUT_PROPERTIES_FILE = "output.properties";
	private static final String LOCK_FILE = "lock";

	private Path compileClasspath;
	private List<Property> properties = new ArrayList<Property>();
	private Set<String> propertyNames = new HashSet<String>();

	private String name;
	private String archieveName;
	private String vendor;
	private String version;
	private String qualifier = "RELEASE";
	private String homeDir;
	private String baseDir;
	private String workDir;
	private String targetDir;
	private boolean compileJava = true;
	private boolean buildJar = true;
	private boolean buildSourceJar = true;
	private boolean buildJsDoc = false;
	private boolean buildJavaDoc = false;
	private String skins = "default";
	private boolean makePom = true;
	private String ivyFile;
	private String srcCharset = "utf-8";
	private String clientSrc = "client";
	private String javaSrc = "src";
	private String javaCompatSource = "1.5";
	private String javaCompatTarget = "1.5";

	public BuildDoradoAddonTask() {
		initialize();
	}

	public BuildDoradoAddonTask(Task owner) {
		this();
	}

	private void initialize() {
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
				archieveName = name;
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
			File homeDirFile = preprocessor.prepareHome(getProject(), homeDir);
			homeDir = homeDirFile.getCanonicalPath();

			File lockFile = new File(homeDirFile, LOCK_FILE);
			FileChannel channel = new RandomAccessFile(lockFile, "rw")
					.getChannel();
			FileLock lock = null;
			try {
				lock = channel.tryLock();
				if (lock == null) {
					throw new IllegalStateException("Lock file \"" + homeDir
							+ "\" failed.");
				}
			} catch (OverlappingFileLockException e) {
				throw new IllegalStateException("\"" + homeDir
						+ "\" is already locked.", e);
			}

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

			for (Property property : properties) {
				if (StringUtils.isNotEmpty(property.getName())) {
					propertyNames.add(property.getName());
				}
			}

			createProperty("homeDir", homeDir);
			createProperty("workDir", workDir);

			createProperty("name", name);
			createProperty("archieveName", archieveName);
			createProperty("vendor", vendor);
			createProperty("version", version);
			createProperty("qualifier", qualifier);
			createProperty("targetDir", targetDir);

			createProperty("compileJava", compileJava);
			createProperty("buildJar", buildJar);
			createProperty("buildSourceJar", buildSourceJar);
			createProperty("buildJsDoc", buildJsDoc);
			createProperty("buildJavaDoc", buildJavaDoc);

			createProperty("skins", skins);
			createProperty("makePom", makePom);
			createProperty("ivyFile", ivyFile);
			createProperty("clientSrc", clientSrc);
			createProperty("javaSrc", javaSrc);
			createProperty("srcCharset", srcCharset);
			createProperty("javaCompatSource", javaCompatSource);
			createProperty("javaCompatTarget", javaCompatTarget);

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

			Properties outputProperties = new Properties();
			InputStream in = new FileInputStream(new File(workDir + "/temp",
					OUTPUT_PROPERTIES_FILE));
			try {
				outputProperties.load(in);
				for (Object key : outputProperties.keySet()) {
					String propertyName = (String) key;
					project.setUserProperty(propertyName,
							outputProperties.getProperty(propertyName));
				}
			} finally {
				in.close();
			}
			project.setUserProperty("addonBuilder.homeDir", homeDir);

			if (lock != null) {
				lock.release();
			}
			channel.close();
			lockFile.delete();

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

	public Property createProperty() {
		Property property = super.createProperty();
		properties.add(property);
		return property;
	}

	protected void createProperty(String name, Object value) {
		if (value != null && !propertyNames.contains(name)) {
			Property property = super.createProperty();
			property.setName(name);
			property.setValue(value);
			log("Property: " + name + "=" + value);
		}
	}

	public void setClasspath(Path classpath) {
		if (compileClasspath == null) {
			compileClasspath = classpath;
		} else {
			compileClasspath.append(classpath);
		}
	}

	public Path getClasspath() {
		return compileClasspath;
	}

	private Path createClasspath() {
		if (compileClasspath == null) {
			compileClasspath = new Path(getProject());
		}
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

	public boolean isCompileJava() {
		return compileJava;
	}

	public void setCompileJava(boolean compileJava) {
		this.compileJava = compileJava;
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

	public boolean isMakePom() {
		return makePom;
	}

	public void isMakePom(boolean makePom) {
		this.makePom = makePom;
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
