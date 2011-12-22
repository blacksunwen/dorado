package com.bstek.dorado.view.registry;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.widget.Component;

/**
 * 用于配置在Spring文件中的视图组件的注册器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 28, 2008
 */
public class DefaultComponentTypeRegister extends ComponentTypeRegister {
	private String dependsPackage;
	private Outputter outputter;

	/**
	 * 设置该组件依赖的JavaScript Package。<br>
	 * 如果要在此处定义多个Package，应使用","分割各个Package的名称。
	 */
	public void setDependsPackage(String dependsPackage) {
		this.dependsPackage = dependsPackage;
	}

	public String getDependsPackage() {
		return dependsPackage;
	}

	/**
	 * 设置该种组件使用的输出器。
	 */
	public void setOutputter(Outputter outputter) {
		this.outputter = outputter;
	}

	public Outputter getOutputter() {
		return outputter;
	}

	@Override
	protected ComponentTypeRegisterInfo createRegisterInfo(String name) {
		return new ComponentTypeRegisterInfo(name);
	}

	@Override
	protected ComponentTypeRegisterInfo getRegisterInfo() throws Exception {
		String classType = getClassType();
		if (StringUtils.isEmpty(classType)) {
			setClassType(getBeanName());
		}

		ComponentTypeRegisterInfo registerInfo = super.getRegisterInfo();

		Class<? extends Component> cl = registerInfo.getClassType();
		Widget widget = null;
		if (cl != null) {
			widget = cl.getAnnotation(Widget.class);
			if (widget != null) {
				dependsPackage = widget.dependsPackage();
			}
		}

		registerInfo.setDependsPackage(dependsPackage);
		return registerInfo;
	}
}
