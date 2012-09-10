package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

@Widget(name = "Shell", category = "Advance", dependsPackage = "desktop")
@ClientObject(prototype = "dorado.widget.desktop.Shell",
		shortTypeName = "desktop.Shell")
public class Shell extends Control {
	private InnerElementReference<AbstractDesktop> desktopRef = new InnerElementReference<AbstractDesktop>(
			this);
	private InnerElementReference<Taskbar> taskbarRef = new InnerElementReference<Taskbar>(
			this);

	private List<App> apps;
	private String wallpaper;

	/**
	 * @return the desktopRef
	 */
	@XmlSubNode(implTypes = "com.bstek.dorado.desktop.*")
	@ClientProperty
	public AbstractDesktop getDesktop() {
		return desktopRef.get();
	}

	/**
	 * @param desktopRef
	 *            the desktopRef to set
	 */
	public void setDesktop(AbstractDesktop desktop) {
		this.desktopRef.set(desktop);
	}

	/**
	 * @return the taskbarRef
	 */
	@XmlSubNode
	@ClientProperty
	public Taskbar getTaskbar() {
		return taskbarRef.get();
	}

	/**
	 * @param taskbar
	 *            the taskbarRef to set
	 */
	public void setTaskbar(Taskbar taskbar) {
		this.taskbarRef.set(taskbar);
	}

	/**
	 * @return the apps
	 */
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Apps"))
	@ClientProperty
	public List<App> getApps() {
		return apps;
	}

	/**
	 * @param apps
	 *            the apps to set
	 */
	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	/**
	 * @return the wallpaper
	 */
	public String getWallpaper() {
		return wallpaper;
	}

	/**
	 * @param wallpaper
	 *            the wallpaper to set
	 */
	public void setWallpaper(String wallpaper) {
		this.wallpaper = wallpaper;
	}

}
