package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;

@ClientObject(prototype = "dorado.widget.desktop.IFrameApp",
		shortTypeName = "IFrame")
public class IFrameApp extends DialogApp {
	private String path;
	private int width;
	private int height;
	private int left;
	private int top;
	private boolean center = true;
	private boolean maximizeable = true;
	private boolean minimizeable = true;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the center
	 */
	@ClientProperty(escapeValue = "true")
	public boolean isCenter() {
		return center;
	}

	/**
	 * @param center
	 *            the center to set
	 */
	public void setCenter(boolean center) {
		this.center = center;
	}

	/**
	 * @return the maximizeable
	 */
	@ClientProperty(escapeValue = "true")
	public boolean isMaximizeable() {
		return maximizeable;
	}

	/**
	 * @param maximizeable
	 *            the maximizeable to set
	 */
	public void setMaximizeable(boolean maximizeable) {
		this.maximizeable = maximizeable;
	}

	/**
	 * @return the minimizeable
	 */
	@ClientProperty(escapeValue = "true")
	public boolean isMinimizeable() {
		return minimizeable;
	}

	/**
	 * @param minimizeable
	 *            the minimizeable to set
	 */
	public void setMinimizeable(boolean minimizeable) {
		this.minimizeable = minimizeable;
	}

	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * @return the top
	 */
	public int getTop() {
		return top;
	}

	/**
	 * @param top
	 *            the top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}

}
