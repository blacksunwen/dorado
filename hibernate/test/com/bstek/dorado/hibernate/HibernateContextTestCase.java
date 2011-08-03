package com.bstek.dorado.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.DataContextTestCase;

public abstract class HibernateContextTestCase extends DataContextTestCase {
	public HibernateContextTestCase() {
		addExtensionContextConfigLocation("com/bstek/dorado/hibernate/test-context.xml");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
