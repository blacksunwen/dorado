package com.bstek.dorado.view.widget.base.accordion;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "Accordion", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Accordion", shortTypeName = "Accordion")
@XmlNode(nodeName = "Accordion")
@ClientEvents({ @ClientEvent(name = "beforeCurrentSectionChange"),
		@ClientEvent(name = "onCurrentSectionChange") })
public class Accordion extends Control {
	private List<Section> sections = new InnerElementList<Section>(this);

	public void addSection(Section section) {
		sections.add(section);
	}

	@ViewAttribute
	@XmlSubNode(path = "#self")
	public List<Section> getSections() {
		return sections;
	}
}
