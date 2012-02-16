package com.bstek.dorado.core.pkgs;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ClassUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-22
 */
public final class PackageManager {
	private static final String PACKAGE_PROPERTIES_LOCATION = "META-INF/dorado-package.properties";
	private static final String UNKNOWN_VERSION = "<Unknown Version>";

	private static final Map<String, PackageInfo> packageInfosMap = new LinkedHashMap<String, PackageInfo>();
	private static boolean packageInfoBuilded = false;

	private PackageManager() {
	}

	private static void calculateDepends(PackageInfo packageInfo,
			Set<PackageInfo> calculatedPackages,
			Map<String, PackageInfo> packageMap) {
		Dependence[] dependences = packageInfo.getDepends();
		if (dependences == null || dependences.length == 0) {
			calculatedPackages.add(packageInfo);
			return;
		}

		for (Dependence dependence : dependences) {
			PackageInfo dependedPackageInfo = packageMap.get(dependence
					.getPackageName());
			if (dependedPackageInfo == null) {
				throw new IllegalArgumentException("Package  ["
						+ dependence.getPackageName()
						+ "] not found, Which is depended by ["
						+ packageInfo.getName() + "].");
			}

			/*
			 * boolean versionMatch = true; String minVersion =
			 * dependence.getMinVersion(); String maxVersion =
			 * dependence.getMaxVersion(); String dependedPackageVersion =
			 * dependedPackageInfo.getVersion(); if (minVersion != null) {
			 * versionMatch = (minVersion.compareTo(dependedPackageVersion) <=
			 * 0); } if (versionMatch && maxVersion != null) { versionMatch =
			 * (maxVersion.compareTo(dependedPackageVersion) >= 0); }
			 * 
			 * if (!versionMatch) { throw new IllegalArgumentException(
			 * "Depended version mismatch. Expect [" + ((minVersion != null) ?
			 * minVersion : "*") + " ~ " + ((maxVersion != null) ? maxVersion :
			 * "*") + "] but [" + dependedPackageVersion + "] found."); }
			 */

			calculateDepends(dependedPackageInfo, calculatedPackages,
					packageMap);
		}

		if (!calculatedPackages.contains(packageInfo)) {
			calculatedPackages.add(packageInfo);
		}
	}

	private static Dependence parseDependence(String text) {
		final String ANY_VERSION = "*";

		Dependence dependence = new Dependence();

		String packageName = "", version = "";
		boolean colonFound = false;
		char c;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (!colonFound) {
				if (c == ':') {
					colonFound = true;
				} else {
					packageName += c;
				}
			} else {
				version += c;
			}
		}

		if (StringUtils.isEmpty(packageName)) {
			throw new IllegalArgumentException(
					"Depended packageName undefined.");
		}
		dependence.setPackageName(packageName);

		if (version != null && !version.equals(ANY_VERSION)) {
			dependence.setVersion(version);
		}
		return dependence;
	}

	private static void buildPackageInfos() throws Exception {
		Map<String, PackageInfo> packageMap = new HashMap<String, PackageInfo>();

		Enumeration<URL> defaultContextFileResources = ClassUtils
				.getDefaultClassLoader().getResources(
						PACKAGE_PROPERTIES_LOCATION);
		while (defaultContextFileResources.hasMoreElements()) {
			URL url = defaultContextFileResources.nextElement();
			InputStream in = null;
			try {
				URLConnection con = url.openConnection();
				con.setUseCaches(false);
				in = con.getInputStream();
				Properties properties = new Properties();
				properties.load(in);

				String packageName = properties.getProperty("name");
				if (StringUtils.isEmpty(packageName)) {
					throw new IllegalArgumentException(
							"Package name undefined.");
				}

				PackageInfo packageInfo = new PackageInfo(packageName);

				packageInfo.setVersion(StringUtils.defaultIfEmpty(
						properties.getProperty("version"), UNKNOWN_VERSION));

				String dependsText = properties.getProperty("depends");
				if (StringUtils.isNotBlank(dependsText)) {
					List<Dependence> dependences = new ArrayList<Dependence>();
					for (String depends : StringUtils.split(dependsText, ",; ")) {
						if (StringUtils.isNotEmpty(depends)) {
							Dependence dependence = parseDependence(depends);
							dependences.add(dependence);
						}
					}
					if (!dependences.isEmpty()) {
						packageInfo.setDepends(dependences
								.toArray(new Dependence[0]));
					}
				}

				packageInfo.setContextLocations(properties
						.getProperty("contextConfigLocations"));
				packageInfo.setServletContextLocations(properties
						.getProperty("servletContextConfigLocations"));

				packageMap.put(packageName, packageInfo);
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Error occured during parsing [" + url.getPath() + "].",
						e);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}

		Set<PackageInfo> calculatedPackages = new LinkedHashSet<PackageInfo>();
		for (PackageInfo packageInfo : packageMap.values()) {
			calculateDepends(packageInfo, calculatedPackages, packageMap);
		}

		packageInfosMap.clear();
		for (PackageInfo packageInfo : calculatedPackages) {
			packageInfosMap.put(packageInfo.getName(), packageInfo);
		}
	}

	public static Map<String, PackageInfo> getPackageInfoMap() throws Exception {
		if (!packageInfoBuilded) {
			packageInfoBuilded = true;
			buildPackageInfos();
		}
		return packageInfosMap;
	}

}
