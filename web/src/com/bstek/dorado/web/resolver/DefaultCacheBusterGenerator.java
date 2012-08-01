package com.bstek.dorado.web.resolver;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.util.StringAliasUtils;

public class DefaultCacheBusterGenerator implements CacheBusterGenerator {
	private static final String UNKNOWN_VERSION = "<Unknown Version>";
	private static String packageInfoMD5;

	private String getCacheBuster() throws Exception {
		if (packageInfoMD5 == null) {
			StringBuffer stringForMD5 = new StringBuffer();
			for (PackageInfo packageInfo : PackageManager.getPackageInfoMap()
					.values()) {
				String addonVersion = packageInfo.getVersion();
				if (UNKNOWN_VERSION.equals(addonVersion)) {
					addonVersion = String.valueOf(System.currentTimeMillis());
				}
				stringForMD5.append(packageInfo.getName()).append(addonVersion);
			}

			packageInfoMD5 = StringAliasUtils.getMD5(stringForMD5.toString()
					.getBytes());
		}
		return packageInfoMD5;
	}

	public String getCacheBuster(String param) throws Exception {
		return (StringUtils.isEmpty(param)) ? getCacheBuster()
				: (param + getCacheBuster());
	}

}
