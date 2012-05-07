package com.bstek.dorado.jdbc;

import java.io.IOException;
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

public class PackageTestSuite extends TestSuite {

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	
	private String basePackage;
	
	public PackageTestSuite(String basePackage) {
		this.basePackage = basePackage;
		
		List<Class<? extends TestCase>> clazzList = scan();
		for (Class<? extends TestCase> clazz: clazzList) {
			this.addTestSuite(clazz);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<Class<? extends TestCase>> scan(){
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
				resolveBasePackage(basePackage) + "/" + this.resourcePattern;
		List<Class<? extends TestCase>> clazzList = new ArrayList<Class<? extends TestCase>>();
		try {
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
				Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
				if (TestCase.class.isAssignableFrom(clazz)) {
					clazzList.add(clazz);
					System.out.println("add [" + clazz.getName() + "]");
				} else {
					System.out.println("skip [" + clazz.getName() + "]");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return clazzList; 
	}
	
	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}
}
