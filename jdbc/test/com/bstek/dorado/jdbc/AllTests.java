package com.bstek.dorado.jdbc;

import junit.framework.Test;
import junit.framework.TestCase;

import com.bstek.dorado.junit.PackageTestSuite;


public class AllTests extends TestCase {

	public static Test suite() {
		String[] packages = new String[]{"com.bstek.dorado.jdbc.feature"};
		PackageTestSuite suite = new PackageTestSuite(packages);
		
		return suite;
	}

}
