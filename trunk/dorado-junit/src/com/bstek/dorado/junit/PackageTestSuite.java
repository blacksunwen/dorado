package com.bstek.dorado.junit;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * 一起运行某个package下的所有TestCase，例如： <pre>
 * <code>
 * String[] packages = new String[]{"com.bstek.dorado.jdbc.feature"};
 * PackageTestSuite suite = new PackageTestSuite(packages);
 * </code>
 * </pre>
 */
public class PackageTestSuite extends TestSuite {
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	
	private String[] basePackages;
	
	public PackageTestSuite(String... basePackages) {
		this.basePackages = basePackages;
		
		List<Class<? extends TestCase>> clazzList = scan();
		for (Class<? extends TestCase> clazz: clazzList) {
			this.addTestSuite(clazz);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<Class<? extends TestCase>> scan(){
		List<Class<? extends TestCase>> clazzList = new ArrayList<Class<? extends TestCase>>();
		
		for (String basePackage : basePackages) {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			
			try {
				Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
					if (TestCase.class.isAssignableFrom(clazz)) {
						clazzList.add(clazz);
						System.out.println(" + [" + clazz.getName() + "]");
					} else {
						System.out.println(" - [" + clazz.getName() + "]");
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return clazzList; 
	}
	
	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}
}
