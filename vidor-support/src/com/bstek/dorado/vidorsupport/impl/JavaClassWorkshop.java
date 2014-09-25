package com.bstek.dorado.vidorsupport.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


public class JavaClassWorkshop extends AbstractJavaClassWorkshop {

	private boolean useDefault = true;
	
	@Override
	public List<String> names(Map<String, Object> parameter) throws Exception {
		LinkedHashSet<String> names = new LinkedHashSet<String>();
		List<String> superNames = super.names(parameter);
		names.addAll(superNames);
		if (useDefault) {
			Collection<String> defaults = this.defaults(parameter);
			names.addAll(defaults);
		} 
		return new ArrayList<String>(names);
	}

	public boolean isUseDefault() {
		return useDefault;
	}
	public void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}

	protected List<String> defaults(Map<String, Object> parameter) throws Exception {
		AbstractJavaClassProvider provider = new DefaultPackageJavaClassProvider();
		List<String> names = provider.names();
		return new ArrayList<String>(Utils.filter(names, parameter));
	}
	
	protected class DefaultPackageJavaClassProvider extends PackageJavaClassProvider{

		protected static final String DEFAULT_RESOURCES_PATTERN = "**/*" + PackageJavaClassProvider.CLASS_POSTFIX;
		
		@Override
		protected String buildResourcesExpression() {
			return DEFAULT_RESOURCES_PATTERN;
		}
		
	}
	
}
