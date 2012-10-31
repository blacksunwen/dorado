/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.widget;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.output.ClientObjectOutputter;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.widget.datacontrol.PropertyDataControl;

/**
 * 默认的视图组件的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 29, 2008
 */
public class ComponentOutputter extends ClientObjectOutputter {
	@Override
	protected void outputObject(Object object, OutputContext context)
			throws IOException, Exception {
		if (object instanceof PropertyDataControl) {
			PropertyDataControl pdc = (PropertyDataControl) object;
			String property = pdc.getProperty();
			if (StringUtils.isNotEmpty(property)) {
				int i = property.lastIndexOf('.');
				if (i > 0 && i < property.length() - 1) {
					String property1 = property.substring(0, i);
					String property2 = property.substring(i + 1);
					String dataPath = pdc.getDataPath();
					if (StringUtils.isNotEmpty(dataPath)) {
						dataPath += ('.' + property1);
					} else {
						dataPath = "#." + property1;
					}
					pdc.setDataPath(dataPath);
					pdc.setProperty(property2);
				}
			}
		}
		super.outputObject(object, context);
	}
}
