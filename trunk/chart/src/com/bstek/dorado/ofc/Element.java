package com.bstek.dorado.ofc;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.event.ClientEvent;
import com.bstek.dorado.common.event.ClientEventHolder;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.widget.datacontrol.DataControl;

@XmlNode(implTypes = "com.bstek.dorado.ofc.element.*")
@ClientEvents({ @com.bstek.dorado.annotation.ClientEvent(name = "onClick") })
public abstract class Element implements ClientEventSupported, DataControl {
	private String[] colors;
	private String values;
	private int fontSize;
	private String text;
	private String toolTip;
	private Double alpha;
	private String dataSet;
	private String dataPath;
	private ClientEventHolder clientEventHolder = new ClientEventHolder(this);

	@ComponentReference("DataSet")
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	@XmlProperty
	@ClientProperty
	public String[] getColors() {
		return colors;
	}

	public void setColors(String[] colors) {
		this.colors = colors;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public Double getAlpha() {
		return alpha;
	}

	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	public void addClientEventListener(String eventName,
			ClientEvent eventListener) {
		clientEventHolder.addClientEventListener(eventName, eventListener);
	}

	public List<ClientEvent> getClientEventListeners(String eventName) {
		return clientEventHolder.getClientEventListeners(eventName);
	}

	public void clearClientEventListeners(String eventName) {
		clientEventHolder.clearClientEventListeners(eventName);
	}

	public Map<String, List<ClientEvent>> getAllClientEventListeners() {
		return clientEventHolder.getAllClientEventListeners();
	}
}
