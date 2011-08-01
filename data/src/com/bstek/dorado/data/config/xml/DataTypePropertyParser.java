package com.bstek.dorado.data.config.xml;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-22
 */
public class DataTypePropertyParser extends PropertyParser {
	protected DataObjectParseHelper dataObjectParseHelper;

	public void setDataObjectParseHelper(
			DataObjectParseHelper dataObjectParseHelper) {
		this.dataObjectParseHelper = dataObjectParseHelper;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Object value = super.doParse(node, context);
		if (value instanceof String) {
			String dataTypeName = (String) value;
			if (StringUtils.isNotEmpty(dataTypeName)) {
				value = dataObjectParseHelper.getDataTypeByName(dataTypeName,
						(DataParseContext) context, true);
			}
		}
		return value;
	}

}
