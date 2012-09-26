package com.bstek.dorado.sample.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.EntityWrapper;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.sample.dao.CategoryExampleRelationDao;
import com.bstek.dorado.sample.dao.ExampleCategoryDao;
import com.bstek.dorado.sample.dao.ExampleDao;
import com.bstek.dorado.sample.dao.ExampleSourceDao;
import com.bstek.dorado.sample.entity.CategoryExampleRelation;
import com.bstek.dorado.sample.entity.Example;
import com.bstek.dorado.sample.entity.ExampleCategory;
import com.bstek.dorado.sample.entity.ExampleSource;

@Component
public class ExampleMaintain {

	@Resource
	private ExampleCategoryDao exampleCategoryDao;

	@Resource
	private ExampleDao exampleDao;

	@Resource
	private ExampleSourceDao exampleSourceDao;

	@Resource
	private CategoryExampleRelationDao categoryExampleRelationDao;

	@DataProvider
	public Collection<ExampleCategory> getCategories(Long parentCategoryId) {
		return exampleCategoryDao.find("from ExampleCategory where categoryId="
				+ parentCategoryId + " order by sortFlag");
	}

	@DataProvider
	@SuppressWarnings("rawtypes")
	public Collection getExamplesByCategoryId(Long categoryId) throws Exception {
		List<Object[]> rows = exampleDao.find("select e, r.sortFlag "
				+ "from Example e, CategoryExampleRelation r "
				+ "where e.id = r.exampleId and r.categoryId = " + categoryId
				+ " order by r.sortFlag");

		List<Example> examples = new ArrayList<Example>();
		for (Object[] row : rows) {
			Example example = EntityUtils.toEntity(row[0]);
			EntityUtils.setValue(example, "sortFlag", row[1]);
			examples.add(example);
		}
		return examples;
	}

	@DataResolver
	@Transactional
	public void saveAll(Collection<ExampleCategory> categories) {
		PersistenceContext persisContext = new PersistenceContext();
		for (ExampleCategory category : EntityUtils.getIterable(categories,
				FilterType.VISIBLE, ExampleCategory.class)) {
			doSaveCategory(null, category, persisContext);
		}
		for (Long exampleId : persisContext.getDetachedExamples()) {
			if (exampleId == 0) {
				continue;
			}
			int relationNum = ((Number) exampleDao.createQuery(
					"select count(*) from CategoryExampleRelation where exampleId="
							+ exampleId).uniqueResult()).intValue();
			if (relationNum == 0) {
				exampleDao.delete(exampleId);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void doSaveCategory(Long parentCategoryId,
			ExampleCategory category, PersistenceContext persisContext) {
		if (EntityState.isVisibleDirty(EntityUtils.getState(category))) {
			category.setCategoryId(parentCategoryId);
		}

		EntityState state = exampleCategoryDao.persistEntity(category);
		if (EntityState.isVisible(state)) {
			long categoryId = category.getId();

			Collection<ExampleCategory> subCategories = category
					.getCategories();
			if (subCategories != null) {
				for (ExampleCategory subCategory : subCategories) {
					doSaveCategory(categoryId, subCategory, persisContext);
				}
			}

			Collection<Example> examples = (Collection<Example>) EntityUtils
					.getValue(category, "examples");
			if (examples != null) {
				for (Example example : examples) {
					doSaveExample(categoryId, example, persisContext);
				}
			}
		}
	}

	private void doSaveExample(long categoryId, Example example,
			PersistenceContext persisContext) {
		EntityState state = EntityUtils.getState(example);
		EntityWrapper exampleEntity = EntityWrapper.create(example);

		long exampleId = example.getId();
		if (EntityState.isDirty(state) && !EntityState.NEW.equals(state)) {
			if (persisContext.getDirtyExamples().contains(exampleId)) {
				throw new IllegalArgumentException(
						"Can not submit duplicate modifications ["
								+ example.getLabel() + "].");
			}
			persisContext.getDirtyExamples().add(exampleId);
		}

		if (EntityState.DELETED.equals(state)) {
			CategoryExampleRelation relation = new CategoryExampleRelation();
			relation.setCategoryId(categoryId);
			relation.setExampleId(exampleId);
			categoryExampleRelationDao.delete(relation);
			persisContext.getDetachedExamples().add(example.getId());
		} else {
			if (EntityState.MODIFIED.equals(state)) {
				exampleDao.save(example);
			} else if (EntityState.MOVED.equals(state)) {
				CategoryExampleRelation relation = new CategoryExampleRelation();
				relation.setCategoryId(categoryId);
				relation.setExampleId(exampleId);
				categoryExampleRelationDao.delete(relation);
				
				exampleDao.save(example);
			} else if (EntityState.NEW.equals(state)) {
				exampleDao.save(example);
				exampleId = example.getId();
			}

			Collection<ExampleSource> sources = example.getSources();
			if (sources != null) {
				for (ExampleSource source : sources) {
					doSaveExampleSource(exampleId, source);
				}
			}

			CategoryExampleRelation relation = new CategoryExampleRelation();
			relation.setCategoryId(categoryId);
			relation.setExampleId(exampleId);
			relation.setSortFlag(exampleEntity.getInt("sortFlag"));
			categoryExampleRelationDao.save(relation);
		}
	}

	private void doSaveExampleSource(long exampleId, ExampleSource source) {
		EntityState state = EntityUtils.getState(source);
		if (EntityState.isVisibleDirty(state)) {
			source.setExampleId(exampleId);
		}
		exampleSourceDao.persistEntity(source);
	}
}

class PersistenceContext {
	Set<Long> dirtyExamples = new HashSet<Long>();
	Set<Long> detachedExamples = new HashSet<Long>();

	public Set<Long> getDirtyExamples() {
		return dirtyExamples;
	}

	public Set<Long> getDetachedExamples() {
		return detachedExamples;
	}
}
