package com.bstek.dorado.jdbc.sql;

import java.io.StringWriter;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.view.resolver.VelocityHelper;

/**
 * JDBC模块的工具类
 * @author mark.li@bstek.com
 *
 */
public abstract class SqlUtils {

	private SqlUtils(){}
	
	public static JdbcParameterSource createJdbcParameter(Object parameter) {
		return new JdbcParameterSource(parameter);
	}
	
	public static String leftSpace(String token) {
		return " " + token;
	}
	public static String rightSpace(String token) {
		return token + " ";
	}
	public static String bothSpace(String token) {
		return " " + token + " ";
	}
	public static String brackets(String token) {
		return '(' + token + ')';
	}
	
	@SuppressWarnings("unchecked")
	public static String build(String sql, Object parameter) throws Exception {
		VelocityContext context = new VelocityContext();

		Map<Object, Object> map = null;
		if (parameter != null) {
			if (parameter instanceof Map) {
				map = (Map<Object, Object>)parameter;
			} else {
				map = BeanMap.create(parameter);
			}
		}

		if (map == null) {
			context = new VelocityContext();
		} else {
			context = new VelocityContext(map);
		}
		
		StringWriter result = new StringWriter(50);
		
		Context doradoContext = Context.getCurrent();
		VelocityHelper VelocityHelper = (VelocityHelper) doradoContext.getServiceBean("velocityHelper");
		VelocityEngine volocityEngine = VelocityHelper.getVelocityEngine();
		volocityEngine.evaluate(context, result, "", sql);
		return result.toString();
	}
	
}
