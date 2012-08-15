package com.bstek.dorado.sample.data;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.sample.dao.ProductDao;
import com.bstek.dorado.sample.entity.Product;

@Component
public class SimpleCRUD {
	@Resource
	private ProductDao productDao;

	@DataProvider
	public Collection<Product> getAll() {
		return productDao.getAll();
	}

	@DataProvider
	public void getAll(Page<Product> page) {
		productDao.getAll(page);
	}

	@DataResolver
	@Transactional
	public void saveAll(Collection<Product> products) {
		productDao.persistEntities(products);
	}
}