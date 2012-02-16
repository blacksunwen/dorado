package com.bstek.dorado.jdbc.ide.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;

public class ListKeyGeneratorResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		return toContent(envName);
	}

	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		List<KeyGenerator<Object>> keyGenerators = jdbcEnv.getDialect().getKeyGenerators();
		
		List<String> names = new ArrayList<String>(keyGenerators.size());
		for (KeyGenerator<Object> kg: keyGenerators) {
			names.add(kg.getName());
		}
		
		return StringUtils.join(names, ',');
	}
}
