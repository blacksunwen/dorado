package com.bstek.dorado.vidorsupport.internal;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

class Utils {
	
	static Collection<String> filter(Collection<String> sources, Map<String, Object> parameter) {
		if (parameter == null) {
			return new LinkedHashSet<String>(sources);
		} else {
			FilterParameter filterParameter = new FilterParameter(parameter);
			Filter filter = new Filter(sources, filterParameter);
			Collection<String> result = filter.run();
			
			return result;
		}
	}
	
	static class Filter {
		private Collection<String> sources;
		private FilterParameter parameter;
		
		Filter(Collection<String> sources, FilterParameter parameter) {
			this.sources = sources;
			this.parameter = parameter;
		}
		
		Collection<String> run() {
			if (parameter.hasFilterValue()) {
				LinkedHashSet<String> target = new LinkedHashSet<String>(sources.size());
				for (String source: sources) {
					if (parameter.accept(source)) {
						target.add(source);
						if (target.size() >= parameter.getMaxSize()) {
							return target;
						}
					}
				}
				
				return target;
			} else {
				return sources;
			}
		}
	}
	
	static class FilterParameter {
		static final String PARAMETER_FILTER_VALUE = "filterValue";
		static final String PARAMETER_IGNORE_CASE = "ignoreCase";
		static final String PARAMETER_MAX_SIZE = "maxSize";
		
		private String filterValue;
		private boolean ignoreCase = true;
		private int maxSize = Integer.MAX_VALUE;
		
		FilterParameter(Map<String, Object> parameter) {
			Object filterValueObject = parameter.get(PARAMETER_FILTER_VALUE);
			if (filterValueObject != null) {
				if (filterValueObject instanceof String) {
					this.filterValue = (String)filterValueObject;
				} else {
					this.filterValue = filterValueObject.toString();
				}
			}
			
			Object ignoreCaseObject = parameter.get(PARAMETER_IGNORE_CASE);
			if (ignoreCaseObject != null) {
				if (ignoreCaseObject instanceof Boolean) {
					this.ignoreCase = (Boolean)ignoreCaseObject;
				} else {
					this.ignoreCase = Boolean.parseBoolean(ignoreCaseObject.toString());
				}
			}
			
			Object maxSizeObject = parameter.get(PARAMETER_MAX_SIZE);
			if (maxSizeObject != null) {
				if (maxSizeObject instanceof Number) {
					maxSize = ((Number)maxSizeObject).intValue();
				} else {
					String str = maxSizeObject.toString();
					maxSize = Integer.valueOf(str, 10);
				}
			}
		}
		
		public boolean accept(String source) {
			if(this.hasFilterValue()) {
				String filterValue = this.getFilterValue();
				boolean ignoreCase = this.isIgnoreCase();
				
				if (ignoreCase) {
					filterValue = filterValue.toLowerCase();
					source = source.toLowerCase();
				}
				return source.contains(filterValue);
			} else {
				return true;
			}
		}
		
		public String getFilterValue() {
			return filterValue;
		}
		public boolean hasFilterValue() {
			return filterValue != null && filterValue.length() > 0;
		}
		
		public boolean isIgnoreCase() {
			return ignoreCase;
		}
		
		public int getMaxSize() {
			return maxSize;
		}
	}
}
