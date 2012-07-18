package com.bstek.dorado.web.loader;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 资源包的配置信息。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class Package {
	private String name;
	private String[] fileNames;
	private String pattern;
	private String contentType;
	private String charset;
	private Set<String> depends = new LinkedHashSet<String>();
	private Set<String> dependedBy = new LinkedHashSet<String>();
	private String baseUri;
	private boolean mergeRequests = true;

	/**
	 * @param name
	 *            资源包的名称。
	 */
	public Package(String name) {
		this.name = name;
	}

	/**
	 * 返回资源包的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回引用的pattern的名称。如果不指定此参数则将引用默认的pattern。
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * 设置返回引用的pattern的名称。
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 返回ContentType。
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 设置ContentType，取值范围包括：
	 * <ul>
	 * <li>text/javascrip</li>
	 * <li>text/css</li>
	 * </ul>
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 返回字符集。
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * 设置字符集。
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 返回此资源包依赖的其他资源包的集合。集合中的项为依赖的资源包的名称。
	 */
	public Set<String> getDepends() {
		return depends;
	}

	public Set<String> getDependedBy() {
		return dependedBy;
	}

	/**
	 * 返回对应的文件名。
	 */
	public String[] getFileNames() {
		return fileNames;
	}

	/**
	 * 设置对应的文件名。此处可以指定多个文件，但这些文件必须是同一种ContentType的。
	 */
	public void setFileNames(String[] fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			String fileName = fileNames[i];
			if (fileName != null) {
				fileNames[i] = StringUtils.trim(fileName);
			}
		}
		this.fileNames = fileNames;
	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public boolean isMergeRequests() {
		return mergeRequests;
	}

	public void setMergeRequests(boolean mergeRequests) {
		this.mergeRequests = mergeRequests;
	}

}
