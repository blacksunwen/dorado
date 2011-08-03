package com.bstek.dorado.hibernate.criteria;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlNode;

@XmlNode(nodeName="FetchMode")
public class FetchMode {

	private boolean available = true;
	private String associationPath;
	private Mode mode = Mode.DEFAULT;
	
	public static enum Mode {
		DEFAULT, JOIN, SELECT
	}
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public String getAssociationPath() {
		return associationPath;
	}
	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}
	
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public void setMode(String mode) {
		if (StringUtils.isNotEmpty(mode)) {
			Mode m = Mode.valueOf(mode);
			if (m != null) {
				this.mode = m;
			} else {
				throw new IllegalArgumentException("unknown mode '" + mode + "'.");
			}
		}
	}
}
