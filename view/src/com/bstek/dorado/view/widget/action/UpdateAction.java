package com.bstek.dorado.view.widget.action;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.resolver.DataResolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since May 13, 2009
 */
@Widget(name = "UpdateAction", category = "Action", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.UpdateAction", shortTypeName = "UpdateAction")
@XmlNode(nodeName = "UpdateAction")
@ClientEvents({ @ClientEvent(name = "onGetUpdateData") })
public class UpdateAction extends Action {
	private boolean async = true;
	private DataResolver dataResolver;
	private List<UpdateItem> UpdateItems = new ArrayList<UpdateItem>();
	private boolean alwaysExecute;

	@Override
	@ViewAttribute(defaultValue = "true")
	public boolean isAsync() {
		return async;
	}

	@Override
	public void setAsync(boolean async) {
		this.async = async;
	}

	@XmlProperty(parser = "dorado.ignoreParser")
	@ViewAttribute(outputter = "dorado.dataResolverOutputter")
	public DataResolver getDataResolver() {
		return dataResolver;
	}

	public void setDataResolver(DataResolver dataResolver) {
		this.dataResolver = dataResolver;
	}

	@ViewAttribute
	@XmlSubNode(path = "#self")
	public List<UpdateItem> getUpdateItems() {
		return UpdateItems;
	}

	public boolean isAlwaysExecute() {
		return alwaysExecute;
	}

	public void setAlwaysExecute(boolean alwaysExecute) {
		this.alwaysExecute = alwaysExecute;
	}
}
