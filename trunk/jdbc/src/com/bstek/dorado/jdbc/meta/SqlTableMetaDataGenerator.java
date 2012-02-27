package com.bstek.dorado.jdbc.meta;

import java.util.Properties;

import org.w3c.dom.Document;

/**
 * 数据库SQL属性的输出
 * 
 * @author mark.li@bstek.com
 *
 */
public interface SqlTableMetaDataGenerator {
	
	/**
	 * 在已有的Document基础上输出特定SQL的Document对象
	 * @param properties
	 * @param oldDocument
	 * @return
	 */
	Document merge(Properties properties, Document oldDocument);
}
