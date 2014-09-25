package com.bstek.dorado.vidorsupport.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;


public class PackageJavaClassProvider extends AbstractJavaClassProvider {

	protected static final String CLASS_POSTFIX = ".class";
	
	private String javaPackage = null;
	private boolean containsPackages = true;
	
	@Override
	public List<String> names() throws Exception {
		ResourcePatternResolver resolver = this.getResourcePatternResolver();
		String resExpr = this.buildResourcesExpression();
		Resource[] resources = resolver.getResources(resExpr);
		List<String> names = new ArrayList<String>(resources.length);
		for (Resource resource: resources) {
			String name = this.convertToName(resource);
			names.add(name);
		}
		return names;
	}
	
	protected String buildResourcesExpression() {
		if (javaPackage == null || javaPackage.length() == 0) {
			throw new RuntimeException("javaPackage must not be empty.");
		}
		
		String expr = javaPackage.replace('.', '/');
		if (isContainsPackages()) {
			expr += "/**/*" + CLASS_POSTFIX;
		} else {
			expr += "/*" + CLASS_POSTFIX;
		}
		
		return expr;
	}
	
	public String getJavaPackage() {
		return javaPackage;
	}
	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}

	public boolean isContainsPackages() {
		return containsPackages;
	}
	public void setContainsPackages(boolean containsPackages) {
		this.containsPackages = containsPackages;
	}

	protected ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}
	
	protected ResourcePatternResolver getResourcePatternResolver() {
		ClassLoader classLoader = getClassLoader();
		ClassPathResourcePatternResolver resolver = new ClassPathResourcePatternResolver(classLoader);
		return resolver;
	}
	
	protected String convertToName(Resource resource) {
		String path = ((ClassPathResource)resource).getPath();
		String name = path.substring(0, path.length() - CLASS_POSTFIX.length());
		name = name.replace('/', '.').replace('\\', '.');
		return name;
	}
	
	protected class ClassPathResourcePatternResolver extends PathMatchingResourcePatternResolver {
		private final Log logger = LogFactory.getLog(this.getClass());
		
		public ClassPathResourcePatternResolver(ClassLoader classLoader) {
			super(classLoader);
		}

		@Override
		public Resource getResource(String location) {
			Resource r = super.getResource(location);
			List<String> rootDirs = this.getClassPathRoots();
			ClassPathResource cResource = this.toClassPathResource(r, rootDirs);
			return cResource;
		}

		@Override
		public Resource[] getResources(String locationPattern) throws IOException {
			Resource[] superResult =  super.getResources(locationPattern);
			if (CLASSPATH_ALL_URL_PREFIX.equals(locationPattern)) {
				return superResult;
			} else {
				List<Resource> result = new ArrayList<Resource>(superResult.length);
				List<String> rootDirs = this.getClassPathRoots();
				for (Resource r: superResult) {
					ClassPathResource cResource = this.toClassPathResource(r, rootDirs);
					if (cResource != null) {
						result.add(cResource);
					}
				}
				return result.toArray(new Resource[result.size()]);
			}
		}
		
		private ClassPathResource toClassPathResource(Resource r, List<String> rootDirs) {
			if (r instanceof ClassPathResource) {
				return (ClassPathResource)r;
			} else {
				try {
					URL jarUrl = r.getURL();
					String urlFile = jarUrl.getFile();
					int separatorIndex = urlFile.indexOf("!/");
					ClassLoader classLoader = this.getClassLoader();
					if (separatorIndex != -1) {//jar resource
						String classPath = urlFile.substring(separatorIndex+2);
						ClassPathResource classPathResource = new ClassPathResource(classPath, classLoader);
						return classPathResource;
					} 
					if (ResourceUtils.isFileURL(jarUrl)) {
						String filePath = r.getFile().getCanonicalPath();
						for (String rootDirPath: rootDirs) {
							if (filePath.startsWith(rootDirPath)) {
								String classPath = filePath.substring(rootDirPath.length());
								ClassPathResource classPathResource = new ClassPathResource(classPath, classLoader);
								return classPathResource;
							}
						}
					}
				} catch (Exception e) {
					logger.error("error when convert to ClassPathResource (" + r.getDescription() + ")", e);
				}
			}
			
			return null;
		}

		private List<String> getClassPathRoots() {
			Resource[] rootResources = new Resource[0];
			try {
				rootResources = this.findAllClassPathResources("");
			} catch (IOException e) {
				logger.error("error when look class path root dir.", e);
			}
			List<String> rootDirs = new ArrayList<String>(rootResources.length);
			for (Resource r: rootResources) {
				try {
					File rDir = r.getFile();
					if (rDir.exists() && rDir.isDirectory()) {
						String path = rDir.getCanonicalPath();
						rootDirs.add(path);
					}
				} catch (Exception e) {
					logger.error("error when convert to ClassPathResource (" + r.getDescription() + ")", e);
				}
			}
			return rootDirs;
		}
		
	}

}
