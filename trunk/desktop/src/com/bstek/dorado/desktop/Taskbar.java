package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;
import com.bstek.dorado.view.widget.InnerElementReference;
import com.bstek.dorado.view.widget.base.SimpleButton;

@ClientObject(prototype = "dorado.widget.desktop.Taskbar",
		shortTypeName = "desktop.Taskbar")
@ClientEvents({ @ClientEvent(name = "onTaskButtonContextMenu") })
public class Taskbar extends Control {
	private List<TaskButton> taskButtonsRef = new InnerElementList<TaskButton>(
			this);
	private List<QuickButton> quickButtonsRef = new InnerElementList<QuickButton>(
			this);
	private List<SimpleButton> trayButtonsRef = new InnerElementList<SimpleButton>(
			this);
	private InnerElementReference<SimpleButton> startButtonRef = new InnerElementReference<SimpleButton>(
			this);
	private boolean showTimeLabel = true;

	/**
	 * @return the taskButtons
	 */
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "TaskButtons"))
	@ClientProperty
	public List<TaskButton> getTaskButtons() {
		return taskButtonsRef;
	}

	/**
	 * @param taskButtons
	 *            the taskButtons to set
	 */
	public void setTaskButtons(List<TaskButton> taskButtons) {
		taskButtonsRef.clear();
		taskButtonsRef.addAll(taskButtons);
	}

	/**
	 * @return the quickButtons
	 */
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "QuickButtons"))
	@ClientProperty
	public List<QuickButton> getQuickButtons() {
		return quickButtonsRef;
	}

	/**
	 * @param quickButtons
	 *            the quickButtons to set
	 */
	public void setQuickButtons(List<QuickButton> quickButtons) {
		quickButtonsRef.clear();
		quickButtonsRef.addAll(quickButtons);
	}

	/**
	 * @return the trayButtons
	 */
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "TrayButtons"))
	@ClientProperty
	public List<SimpleButton> getTrayButtons() {
		return trayButtonsRef;
	}

	/**
	 * @param trayButtons
	 *            the trayButtons to set
	 */
	public void setTrayButtons(List<SimpleButton> trayButtons) {
		trayButtonsRef.clear();
		trayButtonsRef.addAll(trayButtons);
	}

	/**
	 * @return the showTimeLabel
	 */
	@ClientProperty(escapeValue = "true")
	public boolean isShowTimeLabel() {
		return showTimeLabel;
	}

	/**
	 * @param showTimeLabel
	 *            the showTimeLabel to set
	 */
	public void setShowTimeLabel(boolean showTimeLabel) {
		this.showTimeLabel = showTimeLabel;
	}

	/**
	 * @return the startButton
	 */
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "StartButton"))
	@ClientProperty
	public SimpleButton getStartButton() {
		return startButtonRef.get();
	}

	/**
	 * @param startButton
	 *            the startButton to set
	 */
	public void setStartButton(SimpleButton startButton) {
		startButtonRef.set(startButton);
	}

}
