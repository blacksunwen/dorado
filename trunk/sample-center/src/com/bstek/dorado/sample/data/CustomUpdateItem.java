package com.bstek.dorado.sample.data;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.sample.dao.ProductDao;
import com.bstek.dorado.sample.entity.Product;

@Component
public class CustomUpdateItem {
	@Resource
	private ProductDao productDao;

	@DataResolver
	public void save(Collection<Product> products) {
		productDao.persistEntities(products);
	}
}
