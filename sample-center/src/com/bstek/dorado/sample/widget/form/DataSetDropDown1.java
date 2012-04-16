package com.bstek.dorado.sample.widget.form;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.OrderDao;
import com.bstek.dorado.sample.entity.Order;

@Component
public class DataSetDropDown1 {

	@Resource
	private OrderDao orderDao;

	@DataProvider
	public void getOrders(Page<Order> page) throws Exception {
		orderDao.find(page, "from Order");

		// 以下两行代码的作用是将一个普通Bean集合转换成一个Dorado数据实体集合。
		Collection<Order> orders = EntityUtils.toEntity(page.getEntities());
		page.setEntities(orders);

		// 此时通过迭代得到的已经是被动态代理过的Dorado数据实体了，虽然类型仍然是Order。
		// 我们可以向数据实体中设置虚拟属性的值
		for (Order order : orders) {
			EntityUtils.setValue(order, "employeeName", order.getEmployee()
					.getFirstName());
		}
	}
}
