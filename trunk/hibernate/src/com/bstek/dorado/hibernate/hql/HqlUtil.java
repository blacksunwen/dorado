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
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.resolver.VelocityHelper;

public class HqlUtil {

	@SuppressWarnings("unchecked")
	public static String build(String hql, Object parameter) throws Exception {
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
		volocityEngine.evaluate(context, result, "", hql);
		return result.toString();
	}

	public static Hql build(String clause) throws Exception {
		if (StringUtils.isEmpty(clause)) {
			return null;
		} else {
			String translatedClause = "";
			List<String> paramExprs = new ArrayList<String>();

			boolean inQuot = false;
			boolean inParam = false;
			String param = "";

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
					if (Character.isJavaIdentifierPart(c) || (c == '.') || (c == '(') || (c == ')')) {
						param += c;

						if ((i + 1) == clause.length()) {
							paramExprs.add(param);
						}
						continue;
					} else {
						Assert.isTrue(param.length() > 0);

						paramExprs.add(param);
						param = "";
						inParam = false;
					}
				}
				translatedClause += c;
			}

			Hql hql = new Hql(translatedClause);
			if (paramExprs.size() > 0) {
				for (int i = 0; i < paramExprs.size(); i++) {
					String paramExpr = paramExprs.get(i);
					String paramName = paramExpr;
					DataType dataType = null;
					if (paramExpr.charAt(0) == '(') {
						int sprIdx = paramExpr.indexOf(')');
						if (sprIdx>1) {
							String dataTypeName = paramExpr.substring(1, sprIdx);
							dataType = DataUtils.getDataType(dataTypeName);
							paramName = paramExpr.substring(sprIdx + 1);
						}
					}
					Assert.notEmpty(paramName, "Error when HQL parsing [" + clause + "]");
					
					HqlParameter p = new HqlParameter();
					p.setExpr(paramName);
					p.setIndex(i);
					if (dataType != null) {
						p.setDataType(dataType);
					}

					hql.addParameter(p);
				}
			}
			return hql;
		}
	}

}
