package com.bstek.dorado.jdbc.sql;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.util.Assert;
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
	
	public static VarSql build(String vClause, Object parameter) throws Exception {
		String clause = buildVelocityClause(vClause, parameter);
		JdbcParameterSource parameterSource = createJdbcParameter(parameter);
		
		return build(clause, parameterSource);
	}
	
	private static VarSql build(String clause, JdbcParameterSource parameterSource) throws Exception {
		String translatedClause = "";
		List<VarExpr> paramExprs = new ArrayList<VarExpr>();

		boolean inQuot = false;
		boolean inParam = false;
		String expr = "";

		for (int i = 0; i < clause.length(); i++) {
			char c = clause.charAt(i);
			if (c == '\'') {
				inQuot = !inQuot;
			} else if (!inQuot && c == ':') {
				inParam = true;
				continue;
			}
			
			if (inParam) {
				if (VarExpr.acceptByExpr(c)) {
					expr += c;

					if ((i + 1) == clause.length()) {
						VarExpr varExpr = VarExpr.create(expr, parameterSource);
						paramExprs.add(varExpr);
						translatedClause += ":" + varExpr.getVarName();
					}
					continue;
				} else {
					Assert.isTrue(expr.length() > 0, "Error when HQL parsing [" + clause + "]");
					VarExpr varExpr = VarExpr.create(expr, parameterSource);
					paramExprs.add(varExpr);
					translatedClause += ":" + varExpr.getVarName();
					expr = "";
					inParam = false;
				}
			} 

			if (inQuot) {
				translatedClause += c;
			} else if (c!='\n' && c != '\r') {
				translatedClause += c;
			}
		} 
		
		VarSql sql = new VarSql(translatedClause, paramExprs, parameterSource);
		return sql;
	}
	
	@SuppressWarnings("unchecked")
	private static String buildVelocityClause(String sql, Object parameter) throws Exception {
		VelocityContext context = null;

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
		
		VelocityEngine volocityEngine = getVelocityEngine();
		if (sql.charAt(sql.length()-1) == '$') {
			sql += " ";
		}
		volocityEngine.evaluate(context, result, "", sql);
		return result.toString();
	}
	
	private static VelocityHelper velocityHelper = null;
	private static VelocityEngine getVelocityEngine() throws Exception {
		if (velocityHelper == null) {
			Context doradoContext = Context.getCurrent();
			velocityHelper = (VelocityHelper) doradoContext.getServiceBean("velocityHelper");
		}
		
		return velocityHelper.getVelocityEngine();
	}
}
