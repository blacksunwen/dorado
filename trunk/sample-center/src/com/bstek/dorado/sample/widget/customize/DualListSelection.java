package com.bstek.dorado.sample.widget.customize;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.sample.dao.ProductDao;
import com.bstek.dorado.sample.entity.Product;

@Component
public class DualListSelection {
	@Resource
	private ProductDao productDao;

	@DataProvider
	public Collection<Product> getProducts() {
		return productDao.find("from Product where discontinued=false");
	}

	@DataProvider
	public Collection<Product> getDiscontinuedProducts() {
		return productDao.find("from Product where discontinued=true");
	}

	@Expose
	@Transactional
	public String saveSelection(Collection<Product> added,
			Collection<Product> removed) {
		StringBuffer buf = new StringBuffer();

		buf.append("新增的产品:");
		for (Product product : added) {
			buf.append('\n').append(product.getProductName());
		}

		buf.append("\n\n移除的产品:");
		for (Product product : removed) {
			buf.append('\n').append(product.getProductName());
		}

		return buf.toString();
	}

}
