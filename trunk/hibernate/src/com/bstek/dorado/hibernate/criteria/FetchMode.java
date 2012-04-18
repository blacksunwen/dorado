package com.bstek.dorado.hibernate.criteria;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;

@XmlNode
public class FetchMode {

	private boolean available = true;
	private String associationPath;
	private Mode mode = Mode.DEFAULT;

	public static enum Mode {
		DEFAULT, JOIN, SELECT
	}

	@ClientProperty(escapeValue="true")
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
}
