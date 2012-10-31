/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.widget.base.accordion;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.ResourceInjection;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "Accordion", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.Accordion",
		shortTypeName = "Accordion")
@ClientEvents({ @ClientEvent(name = "beforeCurrentSectionChange"),
		@ClientEvent(name = "onCurrentSectionChange") })
@ResourceInjection(subObjectMethod = "getSection")
public class Accordion extends Control {
	private List<Section> sections = new InnerElementList<Section>(this);
	private boolean animate;

	public void addSection(Section section) {
		sections.add(section);
	}

	public Section getSection(String name) {
		for (Section section : sections) {
			if (name.equals(section.getName())) {
				return section;
			}
		}
		return null;
	}

	@XmlSubNode
	@ClientProperty
	public List<Section> getSections() {
		return sections;
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}
}
