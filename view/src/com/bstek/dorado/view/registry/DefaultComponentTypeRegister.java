package com.bstek.dorado.view.registry;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.Control;

/**
 * 用于配置在Spring文件中的视图组件的注册器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 28, 2008
 */
public class DefaultComponentTypeRegister extends ComponentTypeRegister {
	private static final String COMPONENT_OUTPUTTER = "dorado.componentOutputter";
	private static final String CONTROL_OUTPUTTER = "dorado.controlOutputter";
	private static final String CONTAINER_OUTPUTTER = "dorado.containerOutputter";
	private static final String VIEW_OUTPUTTER = "dorado.viewOutputter";

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
		return new DefaultComponentTypeRegisterInfo(name);
	}

	@Override
	protected ComponentTypeRegisterInfo getRegisterInfo() throws Exception {
		String classType = getClassType();
		if (StringUtils.isEmpty(classType)) {
			setClassType(getBeanName());
		}

		DefaultComponentTypeRegisterInfo registerInfo = (DefaultComponentTypeRegisterInfo) super
				.getRegisterInfo();

		Class<? extends Component> cl = registerInfo.getClassType();
		Widget widget = null;
		if (cl != null) {
			widget = cl.getAnnotation(Widget.class);
			if (widget != null) {
				dependsPackage = widget.dependsPackage();
			}

			if (outputter == null) {
				ViewObject viewObject = cl.getAnnotation(ViewObject.class);
				if (viewObject != null) {
					String beanId = viewObject.outputter();
					if (StringUtils.isNotEmpty(beanId)) {
						outputter = (Outputter) getBeanFactory()
								.getBean(beanId);
					}
				}
			}

			if (outputter == null) {
				String outputterId;
				if (View.class.isAssignableFrom(cl)) {
					outputterId = VIEW_OUTPUTTER;
				}
				else if (Container.class.isAssignableFrom(cl)) {
					outputterId = CONTAINER_OUTPUTTER;
				}
				else if (Control.class.isAssignableFrom(cl)) {
					outputterId = CONTROL_OUTPUTTER;
				}
				else {
					outputterId = COMPONENT_OUTPUTTER;
				}
				outputter = (Outputter) getBeanFactory().getBean(outputterId);
				Assert.notNull(outputter);
			}
		}

		registerInfo.setDependsPackage(dependsPackage);
		registerInfo.setOutputter(outputter);
		return registerInfo;
	}
}
