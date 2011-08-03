package com.bstek.dorado.hibernate.hql;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.bstek.dorado.hibernate.LazyProxyMap;
import com.bstek.dorado.util.Assert;

public class HqlUtil {

	public static String build(String hql, Object parameter) {
		AutoFilterVar filter = new AutoFilterVar();
		return build(hql, parameter, filter);
	}

	public static String build(String hql, Object parameter,
			AutoFilterVar filter) {
		VelocityContext context = new VelocityContext();
		context.put("filter", filter);

		if (parameter != null) {
			LazyProxyMap pmap = LazyProxyMap.create(parameter);
			context = new VelocityContext(pmap, context);
		}

		StringWriter result = new StringWriter(50);
		try {
			Velocity.evaluate(context, result, "", hql);
		} catch (ParseErrorException e) {
			throw new RuntimeException(e);
		} catch (MethodInvocationException e) {
			throw new RuntimeException(e);
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException(e);
		}

		return result.toString();
	}

	public static Hql build(String clause) {

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
					if (Character.isJavaIdentifierPart(c) || (c == '.')) {
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
					HqlParameter p = new HqlParameter();
					p.setExpr(paramExpr);
					p.setIndex(i);

					hql.addParameter(p);
				}
			}
			return hql;
		}
	}

}
