package com.bstek.dorado.console.addon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.core.pkgs.PackageConfigurer;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageListener;
import com.bstek.dorado.core.pkgs.PackageManager;

/**
 * Dorado Addon Service
 * 
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since 2012-12-07
 */
public class AddonService {

	@DataProvider
	public Collection<Addon> getAddonList() throws Exception {
		List<Addon> list = new ArrayList<Addon>();
		Iterator<PackageInfo> iterator = PackageManager.getPackageInfoMap()
				.values().iterator();
		Addon addon;
		PackageInfo packageInfo;
		while (iterator.hasNext()) {
			packageInfo = iterator.next();
			addon = new Addon();
			addon.setContextLocations(packageInfo.getContextLocations());
			com.bstek.dorado.core.pkgs.Dependence[] depends = packageInfo
					.getDepends();
			Dependence depend;
			List<Dependence> dependList = new ArrayList<Dependence>();
			if (depends != null && depends.length > 0) {
				for (com.bstek.dorado.core.pkgs.Dependence dependence : depends) {
					depend = new Dependence();
					depend.setPackageName(dependence.getPackageName());
					depend.setVersion(dependence.getVersion());
					dependList.add(depend);
				}
			}
			addon.setDepends(dependList);
			addon.setHomePage(packageInfo.getHomePage());
			addon.setLicense(packageInfo.getLicense());
			addon.setLoadUnlicensed(packageInfo.isLoadUnlicensed());
			addon.setDescription(packageInfo.getDescription());
			addon.setName(packageInfo.getName());
			addon.setVersion(packageInfo.getVersion());
			addon.setServletContextLocations(packageInfo
					.getServletContextLocations());
			addon.setPropertiesLocations(packageInfo.getPropertiesLocations());
			addon.setClassifier(packageInfo.getClassifier());
			PackageConfigurer configurer = packageInfo.getConfigurer();
			PackageListener listener = packageInfo.getListener();
			if (configurer != null)
				addon.setConfigurerClassName(configurer.getClass().getName());
			if (listener != null)
				addon.setListenerClassName(listener.getClass().getName());
			list.add(addon);
		}
		return list;
	}
}
