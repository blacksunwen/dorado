package com.bstek.dorado.console.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.view.loader.Package;
import com.bstek.dorado.view.loader.PackagesConfig;
import com.bstek.dorado.view.loader.PackagesConfigManager;
import com.bstek.dorado.web.DoradoContext;

/**
 * Packages Config Service
 * 
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since 2012-12-27
 */

public class PackagesConfigService {
	@DataProvider
	public Collection<DoradoPackage> getPackageList() throws Exception {
		PackagesConfigManager manager = (PackagesConfigManager) DoradoContext
				.getAttachedWebApplicationContext().getBean(
						"dorado.packagesConfigManager");
		PackagesConfig config = manager.getPackagesConfig();
		Map<String, Package> map = config.getPackages();
		List<DoradoPackage> list = new ArrayList<DoradoPackage>();
		DoradoPackage doradoPackage;
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			doradoPackage = new DoradoPackage(map.get(name));
			list.add(doradoPackage);
		}

		return list;

	}
}
