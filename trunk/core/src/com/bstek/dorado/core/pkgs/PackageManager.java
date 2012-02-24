package com.bstek.dorado.core.pkgs;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
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
	private static final char HEX_DIGITALS[] = { // 用来将字节转换成 16 进制表示的字符
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static final Map<String, PackageInfo> packageInfosMap = new LinkedHashMap<String, PackageInfo>();
	private static String packageInfoMD5;
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

	private static void doBuildPackageInfos() throws Exception {
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
		StringBuffer stringForMD5 = new StringBuffer();
		for (PackageInfo packageInfo : calculatedPackages) {
			String addonVersion = packageInfo.getVersion();
			if (UNKNOWN_VERSION.equals(addonVersion)) {
				addonVersion = String.valueOf(System.currentTimeMillis());
			}
			stringForMD5.append(packageInfo.getName()).append(addonVersion);
			packageInfosMap.put(packageInfo.getName(), packageInfo);
		}

		packageInfoMD5 = getMD5(stringForMD5.toString().getBytes());
	}

	protected static void buildPackageInfos() throws Exception {
		if (!packageInfoBuilded) {
			packageInfoBuilded = true;
			doBuildPackageInfos();
		}
	}

	public static String getMD5(byte[] source) throws NoSuchAlgorithmException {
		java.security.MessageDigest md = java.security.MessageDigest
				.getInstance("MD5");
		md.update(source);
		byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，用字节表示就是 16 个字节
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16
										// 进制需要 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节转换成 16
										// 进制字符的转换
			byte byte0 = tmp[i]; // 取第 i 个字节
			str[k++] = HEX_DIGITALS[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换
			str[k++] = HEX_DIGITALS[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		return new String(str); // 换后的结果转换为字符串
	}

	public static Map<String, PackageInfo> getPackageInfoMap() throws Exception {
		buildPackageInfos();
		return packageInfosMap;
	}

	public static String getPackageInfoMD5() throws Exception {
		buildPackageInfos();
		return packageInfoMD5;
	}
}
