package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.view.output.ObjectPropertyOutputter;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.registry.LayoutTypeRegisterInfo;
import com.bstek.dorado.view.registry.LayoutTypeRegistry;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-10-13
 */
public class LayoutPropertyOutputter extends ObjectPropertyOutputter {
	private LayoutTypeRegistry layoutTypeRegistry;

	/**
	 * 设置布局管理器类型的注册管理器。
	 */
	public void setLayoutTypeRegistry(LayoutTypeRegistry layoutTypeRegistry) {
		this.layoutTypeRegistry = layoutTypeRegistry;
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		if (object != null) {
			Layout layout = (Layout) object;
			LayoutTypeRegisterInfo layoutInfo = layoutTypeRegistry
					.getRegisterInfo(layout.getClass());
			layoutInfo.getOutputter().output(layout, context);
		}
	}

}
