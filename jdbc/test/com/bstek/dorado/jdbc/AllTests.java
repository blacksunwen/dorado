package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.test.PackageTestSuite;

import junit.framework.Test;
import junit.framework.TestCase;


public class AllTests extends TestCase {

	public static Test suite() {
		String[] packages = new String[]{"com.bstek.dorado.jdbc.feature"};
		PackageTestSuite suite = new PackageTestSuite(packages);
		
		return suite;
	}

}
