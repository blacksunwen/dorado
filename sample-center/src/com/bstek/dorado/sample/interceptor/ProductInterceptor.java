package com.bstek.dorado.sample.interceptor;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.ProductDao;
import com.bstek.dorado.sample.entity.Product;

@Component
public class ProductInterceptor {

	@Resource
	private ProductDao productDao;

	@DataProvider
	public void getAll(Page<Product> page) {
		productDao.find(page, "from Product");
	}

	@DataProvider
	public Collection<Product> getProductsByCategoryId(Long categoryId) {
		return productDao.find("from Product where category.id=" + categoryId);
	}

	@DataProvider
	public void getProductsByCategoryId(Page<Product> page, Long categoryId) {
		productDao.find(page, "from Product where category.id=" + categoryId);
	}

	@DataProvider
	public Collection<Product> findProductsByName(String filterValue) {
		if (StringUtils.isEmpty(filterValue)) {
			return productDao.getAll();
		} else {
			return productDao.find(
					"from Product where productName like '%'||?||'%'",
					filterValue);
		}
	}

	@DataResolver
	@Transactional
	public void saveAll(Collection<Product> products) {
		productDao.persistEntities(products);
	}
}
