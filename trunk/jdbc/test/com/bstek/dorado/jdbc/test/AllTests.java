package com.bstek.dorado.jdbc.test;

import junit.framework.Test;
import junit.framework.TestCase;

import com.bstek.dorado.jdbc.feature.FeatureTestSuit;

public class AllTests extends TestCase {

	public static Test suite() {
		FeatureTestSuit suite = new FeatureTestSuit();
		
		return suite;
	}

}
