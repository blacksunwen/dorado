package com.bstek.dorado.sample;

import com.bstek.dorado.core.io.LocationTransformer;
import com.bstek.dorado.core.io.ResourceUtils;

public class SourceLocationTransformer implements LocationTransformer {
	private String sourceRoot;

	public void setSourceRoot(String sourceRoot) {
		this.sourceRoot = sourceRoot;
	}

	public String transform(String protocal, String location) {
		return ResourceUtils.concatPath(sourceRoot,
				location.substring(protocal.length()));
	}

}
