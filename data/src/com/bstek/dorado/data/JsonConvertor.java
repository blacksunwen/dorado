package com.bstek.dorado.data;

import org.codehaus.jackson.JsonNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-9-19
 */
public interface JsonConvertor {

	Object fromJSON(JsonNode jsonNode, JsonConvertContext jsonConvertContext)
			throws Exception;
}
