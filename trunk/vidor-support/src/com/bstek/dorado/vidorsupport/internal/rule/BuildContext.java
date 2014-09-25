/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.vidorsupport.internal.rule;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class BuildContext {
	protected Map<String, AbstractEditorMeta> editorMetas = new LinkedHashMap<String, AbstractEditorMeta>();
	
	static BuildContext create() {
		BuildContext context =  new BuildContext();
		
		//基本editor
		context.editorMetas.put(Boolean.class.getName(), new BooleanEditorMeta());
		for (TraditionEditorMeta editorMeta: TraditionEditorMeta.ALL) {
			String name = editorMeta.getName().toLowerCase();
			context.editorMetas.put(name, editorMeta);
		}
		return context;
	}
	
	public void addEditorMeta(AbstractEditorMeta meta) {
		String name = meta.getName();
		if (!editorMetas.containsKey(name)) {
			editorMetas.put(name, meta);
		}
	}
	
	public EnumEditorMeta addEnumEditorMeta(String[] enumValues) {
		for (AbstractEditorMeta meta: editorMetas.values()) {
			if (meta.getClass().equals(EnumEditorMeta.class)) {
				EnumEditorMeta alreadyMeta = (EnumEditorMeta)meta;
				if (Arrays.hashCode(enumValues) == Arrays.hashCode(alreadyMeta.getEnumValues())) {
					return alreadyMeta;
				}
			}
		}
		
		String name = "_editor_" + editorMetas.size();
		EnumEditorMeta meta = new EnumEditorMeta(name, enumValues);
		editorMetas.put(name, meta);
		return meta;
	}
	
	public boolean hasEditorMeta(String name) {
		return editorMetas.containsKey(name);
	}
}
