package com.bstek.dorado.view.widget.tree;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-30
 */
@XmlNode
public class Node implements NodeHolder {
	private List<Node> nodes;

	private String label;
	private String icon;
	private String iconClass;
	private String expandedIcon;
	private String expandedIconClass;
	private boolean checkable;
	private boolean checked;
	private boolean autoCheckChildren = true;
	private String tip;
	private Object data;
	private boolean hasChild;
	private boolean expanded;
	private Object tags;

	@XmlSubNode
	@ClientProperty
	public List<Node> getNodes() {
		if (nodes == null)
			nodes = new ArrayList<Node>();
		return nodes;
	}

	public void addNode(Node node) {
		getNodes().add(node);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getExpandedIcon() {
		return expandedIcon;
	}

	public void setExpandedIcon(String expandedIcon) {
		this.expandedIcon = expandedIcon;
	}

	public String getExpandedIconClass() {
		return expandedIconClass;
	}

	public void setExpandedIconClass(String expandedIconClass) {
		this.expandedIconClass = expandedIconClass;
	}

	public boolean isCheckable() {
		return checkable;
	}

	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isAutoCheckChildren() {
		return autoCheckChildren;
	}

	public void setAutoCheckChildren(boolean autoCheckChildren) {
		this.autoCheckChildren = autoCheckChildren;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	@XmlSubNode
	@ClientProperty
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public Object getTags() {
		return tags;
	}

	public void setTags(Object tags) {
		this.tags = tags;
	}
}
