package com.bstek.dorado.view.widget;

/**
 * 容器事件的描述对象
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 3, 2008
 */
public class ContainerEvent {
	private Container container;
	private Component component;

	/**
	 * @param container 触发事件的容器对象
	 * @param component 事件相关的子控件对象
	 */
	public ContainerEvent(Container container, Component component) {
		this.container = container;
		this.component = component;
	}

	/**
	 * 返回触发事件的容器对象
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * 返回事件相关的子控件对象
	 */
	public Component getComponent() {
		return component;
	}
}
