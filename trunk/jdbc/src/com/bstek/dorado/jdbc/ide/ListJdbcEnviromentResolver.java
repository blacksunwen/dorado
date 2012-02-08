package com.bstek.dorado.jdbc.ide;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

public class ListJdbcEnviromentResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		return toContent();
	}

	public String toContent() {
		final JdbcEnviroment[] envs = JdbcUtils.getEnviromentManager().listAll();
		List<String> names = new ArrayList<String>(envs.length);
		for (JdbcEnviroment env: envs) {
			names.add(env.getName());
		}
		return StringUtils.join(names, ',');
	}
}
