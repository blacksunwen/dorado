package com.bstek.dorado.jdbc.ide;

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
		String result = null;
		JdbcEnviroment[] envs = JdbcUtils.getEnviromentManager().listAll();
		if (envs.length > 0) {
			String[] names = new String[envs.length];
			for (int i=0; i<envs.length; i++) {
				names[i] = envs[i].getName();
			}
			
			result = StringUtils.join(names, ',');
		}
		
		return result;
	}
}
