package com.bstek.dorado.jdbc.ide.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.KeyGeneratorManager;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;

/**
 * {@link KeyGenerator}列表
 * 
 * @author mark.li@bstek.com
 *
 */
public class ListKeyGeneratorResolver extends AbstractResolver {
	private KeyGeneratorManager keyGeneratorManager;
	
	public KeyGeneratorManager getKeyGeneratorManager() {
		return keyGeneratorManager;
	}

	public void setKeyGeneratorManager(KeyGeneratorManager keyGeneratorManager) {
		this.keyGeneratorManager = keyGeneratorManager;
	}

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		return toContent();
	}

	public String toContent() {
		KeyGenerator<Object>[] keyGenerators = keyGeneratorManager.list();
		List<String> names = new ArrayList<String>(keyGenerators.length);
		for (KeyGenerator<Object> kg: keyGenerators) {
			names.add(kg.getName());
		}
		
		return StringUtils.join(names, ',');
	}
}
