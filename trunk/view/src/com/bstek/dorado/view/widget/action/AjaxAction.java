package com.bstek.dorado.view.widget.action;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "AjaxAction", category = "Action",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.AjaxAction",
		shortTypeName = "AjaxAction")
public class AjaxAction extends Action {
	private boolean async = true;
	private long timeout;
	private boolean modal = true;
	private boolean batchable = true;
	private String service;
	private boolean supportsEntity = true;

	@Override
	@ClientProperty(escapeValue = "true")
	public boolean isAsync() {
		return async;
	}

	@Override
	public void setAsync(boolean async) {
		this.async = async;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isBatchable() {
		return batchable;
	}

	public void setBatchable(boolean batchable) {
		this.batchable = batchable;
	}

	@IdeProperty(highlight = 1)
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isSupportsEntity() {
		return supportsEntity;
	}

	public void setSupportsEntity(boolean supportsEntity) {
		this.supportsEntity = supportsEntity;
	}
}
