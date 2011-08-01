package com.bstek.dorado.config.text;

import java.util.Map;

/**
 * 可在Spring配置文件中方便的进行配置的字符串分派解析器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2008
 */
public class ConfigurableDispatchableTextParser extends DispatchableTextParser {
	private boolean hasHeader;

	@Override
	public boolean supportsHeader() {
		return hasHeader;
	}

	/**
	 * 设置要解析的字符串是否包含一个头信息。
	 */
	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	/**
	 * 设置所有的子属性解析器。
	 * @param attributeParsers 子属性解析器的映射集合。其中Map的键值为约束条件，值为子解析器的实例。
	 */
	public void setAttributeParsers(Map<String, TextParser> attributeParsers) {
		for (Map.Entry<String, TextParser> entry : attributeParsers.entrySet()) {
			registerAttributeParser(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 设置所有的子解析器。
	 * @param subParsers 子解析器的映射集合。其中Map的键值为约束条件，值为子解析器的实例。
	 */
	public void setSubParsers(Map<String, TextParser> subParsers) {
		for (Map.Entry<String, TextParser> entry : subParsers.entrySet()) {
			registerSubParser(entry.getKey(), entry.getValue());
		}
	}
}
