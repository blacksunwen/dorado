package com.bstek.dorado.hibernate.hql;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.resolver.VelocityHelper;

public class HqlUtil {

	public static Hql build(String vclause, Object parameter) throws Exception {
		String clause = buildVelocityClause(vclause, parameter);
		return build(clause);
	}
	
	@SuppressWarnings("unchecked")
	private static String buildVelocityClause(String hql, Object parameter) throws Exception {
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
		if (hql.charAt(hql.length()-1) == '$') {
			hql += " ";
		}
		volocityEngine.evaluate(context, result, "", hql);
		return result.toString();
	}

	
	private static Hql build(String clause) throws Exception {
		if (StringUtils.isEmpty(clause)) {
			return null;
		} else {
			String translatedClause = "";
			List<String> paramExprs = new ArrayList<String>();

			boolean inQuot = false;
			boolean inParam = false;
			String expr = "";

			for (int i = 0; i < clause.length(); i++) {
				char c = clause.charAt(i);
				if (c == '\'') {
					inQuot = !inQuot;
				} else if (!inQuot && c == ':') {
					inParam = true;
					translatedClause += '?';
					continue;
				}

				if (inParam) {
					if (HqlVarExpr.acceptByExpr(c)) {
						expr += c;

						if ((i + 1) == clause.length()) {
							paramExprs.add(expr);
						}
						continue;
					} else {
						Assert.isTrue(expr.length() > 0, "Error when HQL parsing [" + clause + "]");

						paramExprs.add(expr);
						expr = "";
						inParam = false;
					}
				}
				translatedClause += c;
			}

			Hql hql = new Hql(translatedClause);
			if (paramExprs.size() > 0) {
				for (int i = 0; i < paramExprs.size(); i++) {
					String paramExpr = paramExprs.get(i);
					HqlVarExpr p = new HqlVarExpr(paramExpr, i);
					hql.addVarExpr(p);
				}
			}
			return hql;
		}
	}

}
