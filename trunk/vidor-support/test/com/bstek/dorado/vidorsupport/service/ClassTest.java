package com.bstek.dorado.vidorsupport.service;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;

import com.bstek.dorado.vidorsupport.impl.JavaClassWorkshop;
import com.bstek.dorado.vidorsupport.impl.PackageJavaClassProvider;

public class ClassTest {

	@Test
	public void test1() throws Exception {
		Enumeration<URL> resources = this.getClass().getClassLoader().getResources("");
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			System.out.println(url);
		}
	}
	
	@Test
	public void test2() throws Exception {
		JavaClassWorkshop w = new JavaClassWorkshop();
		List<String> names = w.names(null);
		for (String name: names) {
			System.out.println(name);
		}
	}
	
	@Test
	public void test3() throws Exception {
		PackageJavaClassProvider w = new PackageJavaClassProvider();
		w.setJavaPackage("com.bstek.dorado.core");
		w.setContainsPackages(false);
		
		System.out.println("-");
		List<String> names = w.names();
		for (String name: names) {
			System.out.println(name);
		}
	}
}
