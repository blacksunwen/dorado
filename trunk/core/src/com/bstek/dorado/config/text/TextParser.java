package com.bstek.dorado.config.text;

import com.bstek.dorado.config.Parser;

/**
 * 字符串解析器的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2008
 * @see com.bstek.dorado.config.ParseContext
 */
public interface TextParser extends Parser {
	/**
	 * 解析给定的字符串，并返回解析结果。
	 * @param charArray 要解析的字符串
	 * @param context 解析的上下文对象
	 * @return 解析结果
	 * @throws Exception
	 */
	Object parse(char[] charArray, TextParseContext context) throws Exception;
}
