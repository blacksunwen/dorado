package com.bstek.dorado.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.provider.filter.AdvanceFilterCriterionParser;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.type.DataType;

public abstract class HibernateContextTestCase extends ConfigManagerTestSupport {
	public HibernateContextTestCase() {
		super();
		addExtensionContextConfigLocation("com/bstek/dorado/hibernate/context.xml");
		addExtensionContextConfigLocation("com/bstek/dorado/hibernate/test-context.xml");
	}

	protected SingleValueFilterCriterion createFilterCriterion(String property, DataType dataType,
			String expression) throws Exception {
		Context context = Context.getCurrent();
		AdvanceFilterCriterionParser parser = (AdvanceFilterCriterionParser)context.getServiceBean("filterCriterionParser");
		
		return (SingleValueFilterCriterion)parser.createFilterCriterion(property, dataType, expression);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	protected void buildOrders(Map parameter, Object[][] orders) {
		if (orders == null)
			return;

		for (Object[] order : orders) {
			String property = (String) order[0];
			boolean desc = (Boolean) order[1];
			Map map = new HashMap(2);
			map.put("property", property);
			map.put("desc", desc);

			List $orders = (List) parameter.get("$orders");
			if ($orders == null) {
				$orders = new ArrayList();
				parameter.put("$orders", $orders);
			}
			$orders.add(map);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	protected void buildCriterions(Map parameter, String[][] criterions) {
		if (criterions == null)
			return;

		for (String[] cri : criterions) {
			String property = cri[0];
			String expression = cri[1];
			Map map = new HashMap(2);
			map.put("property", property);
			map.put("expression", expression);

			List $criterions = (List) parameter.get("$criterions");
			if ($criterions == null) {
				$criterions = new ArrayList();
				parameter.put("$criterions", $criterions);
			}
			$criterions.add(map);
		}
	}
}
