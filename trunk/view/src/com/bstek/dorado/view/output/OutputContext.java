/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.output;

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.Control;

/**
 * 输出器上下文。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 6, 2008
 */
public class OutputContext {
	private Writer writer;
	private View currentView;
	private Stack<JsonBuilder> jsonBuilders = new Stack<JsonBuilder>();
	private boolean usePrettyJson;
	private boolean shouldOutputDataTypes = true;
	private String outputtableDataTypeIdPrefix;
	private boolean shouldOutputEntityState;
	private boolean escapeable;
	private Set<String> loadedDataTypes;
	private Map<String, DataType> includeDataTypes;
	private Set<String> dependsPackages;
	private Set<Object> javaScriptContents;
	private Set<Object> styleSheetContents;
	private Stack<Object> dataObjectStack;
	private Map<Control, String> calloutHtmlMap;
	private int calloutSN;

	public OutputContext(Writer writer) {
		this.writer = writer;
	}

	/**
	 * @return
	 */
	public Writer getWriter() {
		return writer;
	}

	public View getCurrentView() {
		return currentView;
	}

	public void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

	/**
	 * @return
	 */
	public JsonBuilder getJsonBuilder() {
		if (jsonBuilders.isEmpty()) {
			return createJsonBuilder();
		} else {
			return jsonBuilders.peek();
		}
	}

	public JsonBuilder createJsonBuilder() {
		JsonBuilder jsonBuilder = new JsonBuilder(getWriter(), true);
		if (usePrettyJson) {
			jsonBuilder.setPrettyFormat(true);
			if (!jsonBuilders.isEmpty()) {
				JsonBuilder parentJsonBuilder = jsonBuilders.peek();
				jsonBuilder.setLeadingTab(parentJsonBuilder.getLeadingTab());
			}
		}
		jsonBuilders.push(jsonBuilder);
		return jsonBuilder;
	}

	public void restoreJsonBuilder() {
		if (!jsonBuilders.isEmpty()) {
			jsonBuilders.pop();
		}
	}

	public boolean isUsePrettyJson() {
		return usePrettyJson;
	}

	public void setUsePrettyJson(boolean usePrettyJson) {
		this.usePrettyJson = usePrettyJson;
	}

	/**
	 * 是否需要向客户端输出DataType的信息。
	 */
	public boolean isShouldOutputDataTypes() {
		return shouldOutputDataTypes;
	}

	/**
	 * 设置是否需要向客户端输出DataType的信息。默认值为true。
	 */
	public void setShouldOutputDataTypes(boolean shouldOutputDataTypes) {
		this.shouldOutputDataTypes = shouldOutputDataTypes;
	}

	public String getOutputtableDataTypeIdPrefix() {
		return outputtableDataTypeIdPrefix;
	}

	public void setOutputtableDataTypeIdPrefix(
			String outputtableDataTypeIdPrefix) {
		this.outputtableDataTypeIdPrefix = outputtableDataTypeIdPrefix;
	}

	/**
	 * @return the shouldOutputEntityState
	 */
	public boolean isShouldOutputEntityState() {
		return shouldOutputEntityState;
	}

	/**
	 * @param shouldOutputEntityState
	 *            the shouldOutputEntityState to set
	 */
	public void setShouldOutputEntityState(boolean shouldOutputEntityState) {
		this.shouldOutputEntityState = shouldOutputEntityState;
	}

	public boolean isEscapeable() {
		return escapeable;
	}

	public void setEscapeable(boolean escapeable) {
		this.escapeable = escapeable;
	}

	public void setLoadedDataTypes(Collection<String> loadedDataTypes) {
		if (loadedDataTypes != null && !(loadedDataTypes instanceof Set<?>)) {
			Collection<String> collection = loadedDataTypes;
			loadedDataTypes = new HashSet<String>();
			for (String dataTypeName : collection) {
				loadedDataTypes.add(dataTypeName);
			}
		}
		this.loadedDataTypes = (Set<String>) loadedDataTypes;
	}

	public boolean isDataTypeLoaded(String dataTypeName) {
		return (loadedDataTypes != null) ? loadedDataTypes
				.contains(dataTypeName) : false;
	}

	/**
	 * 返回客户端需要的DataType的集合。
	 */
	public Map<String, DataType> getIncludeDataTypes(boolean allowCreate) {
		if (includeDataTypes == null && allowCreate) {
			includeDataTypes = new LinkedHashMap<String, DataType>();
		}
		return includeDataTypes;
	}

	/**
	 * 返回客户端依赖的资源包的集合。
	 */
	public Set<String> getDependsPackages() {
		return dependsPackages;
	}

	public void addDependsPackage(String packageName) {
		if (dependsPackages == null) {
			dependsPackages = new LinkedHashSet<String>();
		}
		dependsPackages.add(packageName);
	}

	public Set<Object> getJavaScriptContents() {
		return javaScriptContents;
	}

	public void addJavaScriptContent(Object content) {
		Assert.notNull(content);
		if (javaScriptContents == null) {
			javaScriptContents = new LinkedHashSet<Object>();
		}
		javaScriptContents.add(content);
	}

	public Set<Object> getStyleSheetContents() {
		return styleSheetContents;
	}

	public void addStyleSheetContent(Object content) {
		Assert.notNull(content);
		if (styleSheetContents == null) {
			styleSheetContents = new LinkedHashSet<Object>();
		}
		styleSheetContents.add(content);
	}

	/**
	 * 用于放置对象递归引用导致输出过程死锁的堆栈。
	 */
	public Stack<Object> getDataObjectStack() {
		if (dataObjectStack == null) {
			dataObjectStack = new Stack<Object>();
		}
		return dataObjectStack;
	}

	public Map<Control, String> getCalloutHtmlMap() {
		return calloutHtmlMap;
	}

	public void addCalloutHtml(Control control, String htmlId) {
		if (calloutHtmlMap == null) {
			calloutHtmlMap = new HashMap<Control, String>();
		}
		calloutHtmlMap.put(control, htmlId);
	}

	public String getCalloutId() {
		return String.valueOf(++calloutSN);
	}
}
