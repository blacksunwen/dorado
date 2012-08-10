package com.bstek.dorado.sample.widget.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;

@Component
public class SearchTree {

	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	/**
	 * 根据名称获取所有节点id 并返回list
	 * 
	 * @param categoryName
	 * @return
	 * @throws Exception
	 */
	@Expose
	public List<Long> getNodePath(String label) throws Exception {
		List<Long> path = new ArrayList<Long>();
		String exampleSql = "select id from Example where label=?";
		Long categoryId = null;

		List<Long> exampleIds = exampleCategoryDao.find(exampleSql, label);
		// 判断是否为Example节点
		if (!exampleIds.isEmpty()) {
			Long exampleId = (Long) exampleIds.get(0);

			String categorySql = "select categoryId from CategoryExampleRelation where exampleId=?";
			List<Long> categoryIds = exampleCategoryDao.find(categorySql, exampleId);
			if (!categoryIds.isEmpty()) {
				path.add(exampleId);
				categoryId = (Long) categoryIds.get(0);
			}
		} else {
			// 判断是否为ExampleCategory节点
			String categorySql = "select id from ExampleCategory where label=?";
			List<Long> categoryIds = exampleCategoryDao
					.find(categorySql, label);
			if (!categoryIds.isEmpty()) {
				categoryId = (Long) categoryIds.get(0);
			}
		}

		// 获取categoryId后开始在Category表中循环
		while (categoryId != null) {
			path.add(categoryId);

			String categorySql = "select categoryId from ExampleCategory where id=?";
			categoryId = exampleCategoryDao.findUnique(categorySql, categoryId);
		}

		Collections.reverse(path);
		return path;
	}

}
