package com.bstek.dorado.jdbc.sql;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.proxy.UnmutableMap;

/**
 * JDBC模块的工具类
 * @author mark
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
	
	public static String token(Table table) {
		Assert.notNull(table, "Table must not be null.");
		
		String name = table.getTableName();
		String catalog = table.getCatalog();
		String schema = table.getSchema();
		if (StringUtils.isEmpty(catalog) && StringUtils.isEmpty(schema)) {
			return name;
		} else if(StringUtils.isNotEmpty(catalog)){
			return catalog + "." + name;
		} else if(StringUtils.isNotEmpty(schema)){
			return schema + "." + name;
		} else {
			return catalog + "." + schema + "." + name;
		}
	}
	
	public static String token(Table table, String alias) {
		String token = token(table);
		return token + " " + KeyWord.AS + " " + alias;
	}
	
	public static String token(FromTable fromTable) {
		Table table = fromTable.getTable();
		String alias = fromTable.getTableAlias();
		return token(table, alias);
	}
	
	public static String build(String sql, Object parameter) {
		if (StringUtils.isEmpty(sql)) {
			return "";
		}
		
		VelocityContext context = new VelocityContext();
		
		if (parameter != null) {
			LazyProxyMap pmap = LazyProxyMap.create(parameter);
			context = new VelocityContext(pmap, context);
		}

		StringWriter result = new StringWriter(50);
		try {
			Velocity.evaluate(context, result, "", sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 

		return result.toString();
	}
	
	static class LazyProxyMap extends UnmutableMap<String, Object> {

		private static final LazyProxyMap EMPTY_MAP = new LazyProxyMap(Collections.EMPTY_MAP);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public LazyProxyMap(Map target) {
			super(target);
		}

		@SuppressWarnings("rawtypes")
		public static LazyProxyMap create(Object bean) {
			if (bean == null) {
				return EMPTY_MAP;
			}

			if (bean instanceof Map) {
				return new LazyProxyMap((Map)bean);
			} else {
				BeanMap beanMap = BeanMap.create(bean);
				return new LazyProxyMap(beanMap);
			}
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Object get(Object key) {
			Object value = super.get(key);
			if (value != null) {
				Class clazz = value.getClass();
				if (!clazz.isPrimitive()) {
					if (!clazz.equals(String.class)) {
						LazyProxyMap valueMap = create(value);
						value = valueMap;
					}
				}
			}
			return value;
		}
	}
}
