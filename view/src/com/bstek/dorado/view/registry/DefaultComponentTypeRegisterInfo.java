package com.bstek.dorado.view.registry;

import com.bstek.dorado.view.output.Outputter;

/**
 * 视图组件类型注册信息。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 28, 2008
 */
public class DefaultComponentTypeRegisterInfo extends ComponentTypeRegisterInfo {
	private String dependsPackage;

	private Outputter outputter;

	/**
	 * @param type 组件的类型名。
	 */
	public DefaultComponentTypeRegisterInfo(String name) {
		super(name);
	}

	/**
	 * 返回该组件依赖的JavaScript Package。
	 * @see com.bstek.dorado.view.registry.DefaultComponentTypeRegister#setDependsPackage(String)
	 */
	public String getDependsPackage() {
		return dependsPackage;
	}

	/**
	 * 设置该组件依赖的JavaScript Package。
	 * @see com.bstek.dorado.view.registry.DefaultComponentTypeRegister#setDependsPackage(String)
	 */
	public void setDependsPackage(String dependsPackage) {
		this.dependsPackage = dependsPackage;
	}

	/**
	 * 返回该种组件使用的输出器。
	 */
	public Outputter getOutputter() {
		return outputter;
	}

	/**
	 * 设置该种组件使用的输出器。
	 */
	public void setOutputter(Outputter outputter) {
		this.outputter = outputter;
	}
}
