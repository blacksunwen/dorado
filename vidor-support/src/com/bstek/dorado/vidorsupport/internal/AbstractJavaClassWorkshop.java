package com.bstek.dorado.vidorsupport.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.vidorsupport.iapi.IJavaClassWorkshop;

public abstract class AbstractJavaClassWorkshop implements IJavaClassWorkshop {
	
	@SuppressWarnings("unchecked")
	private List<AbstractJavaClassProvider> providers = Collections.EMPTY_LIST;

	public List<AbstractJavaClassProvider> getProviders() {
		return providers;
	}
	public void setProviders(List<AbstractJavaClassProvider> providers) {
		this.providers = providers;
	}

	public List<String> names(Map<String, Object> parameter) throws Exception {
		LinkedHashSet<String> names = new LinkedHashSet<String>();
		for (AbstractJavaClassProvider provider: providers) {
			List<String> ns = provider.names();
			names.addAll(ns);
		}
		return new ArrayList<String>(Utils.filter(names, parameter));
	}
}
