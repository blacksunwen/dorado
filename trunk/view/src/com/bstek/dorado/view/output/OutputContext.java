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
import com.bstek.dorado.view.widget.layout.Layout;

/**
 * 输出器上下文。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 6, 2008
 */
public class OutputContext {
	private Writer writer;
	private Stack<JsonBuilder> jsonBuilders = new Stack<JsonBuilder>();
	private boolean usePrettyJson;
	private boolean shouldOutputDataTypes = true;
	private String outputtableDataTypeIdPrefix;
	private boolean shouldOutputEntityState;
	private boolean escapeable = false;
	private Set<String> loadedDataTypes;
	private Map<String, DataType> includeDataTypes;
	private Set<String> dependsPackages = new LinkedHashSet<String>();
	private Set<String> javaScriptFiles = new LinkedHashSet<String>();
	private Set<String> styleSheetFiles = new LinkedHashSet<String>();
	private Stack<Object> dataObjectStack = new Stack<Object>();
	private Stack<Layout> layoutStack = new Stack<Layout>();
	private Map<Object, String> calloutHtmlMap = new HashMap<Object, String>();
	private long calloutSN;

	public OutputContext(Writer writer) {
		this.writer = writer;
	}

	/**
	 * @return
	 */
	public Writer getWriter() {
		return writer;
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

	public Set<String> getJavaScriptFiles() {
		return javaScriptFiles;
	}

	public Set<String> getStyleSheetFiles() {
		return styleSheetFiles;
	}

	/**
	 * 用于放置对象递归引用导致输出过程死锁的堆栈。
	 */
	public Stack<Object> getDataObjectStack() {
		return dataObjectStack;
	}

	public Stack<Layout> getLayoutStack() {
		return layoutStack;
	}

	public Map<Object, String> getCalloutHtmlMap() {
		return calloutHtmlMap;
	}

	public String getCalloutId() {
		return String.valueOf(++calloutSN);
	}
}
