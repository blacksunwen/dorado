package com.bstek.dorado.idesupport;

import java.util.List;

import junit.framework.TestCase;

import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.idesupport.model.RuleSet;

public class StandaloneRuleSetExporterTest extends TestCase {

	/**
	 * @param args
	 * @throws Exception
	 */
	public void test() throws Exception {
		RuleSet ruleSet = StandaloneRuleSetExporter.getRuleSet(System
				.getenv("DORADO7_WORKSPACE") + "/sample-center/web/WEB-INF/dorado-home");
		List<PackageInfo> packageInfos = ruleSet.getPackageInfos();

		assertNotNull(packageInfos);
		assertTrue(packageInfos.size() > 0);
	}

}
