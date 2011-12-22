package com.bstek.dorado.config.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.TextSection;
import com.bstek.dorado.core.bean.BeanFactoryUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-25
 */
public class TextParserHelper {

	private static class TextSectionInfo {
		private List<Class<?>> sourceTypes;
		private String parser;

		public String getParser() {
			return parser;
		}

		public void setParser(String parser) {
			this.parser = parser;
		}

		public void addSourceType(Class<?> sourceType) {
			if (sourceTypes == null) {
				sourceTypes = new ArrayList<Class<?>>();
			}
			sourceTypes.add(sourceType);
		}

		public List<Class<?>> getSourceTypes() {
			return sourceTypes;
		}
	}

	private Map<Class<?>, TextSectionInfo> textSectionInfoCache = new HashMap<Class<?>, TextSectionInfo>();

	public TextParser getTextParser(Class<?> beanType) throws Exception {
		TextParser textParser = null;
		TextSectionInfo textSectionInfo = getTextSectionInfo(beanType);
		if (textSectionInfo != null) {
			if (StringUtils.isNotEmpty(textSectionInfo.getParser())) {
				textParser = (TextParser) BeanFactoryUtils
						.getBean(textSectionInfo.getParser());
			}
		}
		return textParser;
	}

	protected TextSectionInfo getTextSectionInfo(Class<?> type) {
		if (textSectionInfoCache.containsKey(type)) {
			return textSectionInfoCache.get(type);
		}

		TextSectionInfo textNodeInfo = new TextSectionInfo();
		doGetTextSectionInfo(textNodeInfo, type);
		if (textNodeInfo.getSourceTypes() == null) {
			textNodeInfo = null;
		}

		textSectionInfoCache.put(type, textNodeInfo);
		return textNodeInfo;
	}

	private void doGetTextSectionInfo(TextSectionInfo textSectionInfo,
			Class<?> type) {
		for (Class<?> i : type.getInterfaces()) {
			doGetTextSectionInfo(textSectionInfo, i);
		}

		Class<?> superclass = type.getSuperclass();
		if (superclass != null && !superclass.equals(Object.class)) {
			doGetTextSectionInfo(textSectionInfo, superclass);
		}

		collectXmlNodeInfo(textSectionInfo, type);
	}

	private void collectXmlNodeInfo(TextSectionInfo textSectionInfo,
			Class<?> type) {
		TextSection textSection = type.getAnnotation(TextSection.class);
		if (textSection == null) {
			return;
		}
		textSectionInfo.addSourceType(type);

		if (StringUtils.isNotEmpty(textSection.parser())) {
			textSectionInfo.setParser(textSection.parser());
		}
	}
}
