package com.bstek.dorado.hibernate.criteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * TopCriteria关于associationPath的帮助类
 * @author mark
 *
 */
public class AssociationPathHelper {

	private BaseCriteria criteria;
	private Map<String, String> fieldPathMap; //<associationPath, alias>
	private AssociationPathHelper parent;
	private String globalPath;
	
	public AssociationPathHelper(TopCriteria criteria) {
		this.criteria = criteria;
		this.reset();
	}
	
	/**
	 * 获得filed的别名
	 * @param field
	 * @return
	 */
	public String getFieldAlias(String field) {
		Map<String, String> fieldPathMap = getFieldAliasMap();
		
		String alias = fieldPathMap.get(field);
		if (alias != null) {
			return alias;
		} else {
			String path1 ,path2;
			int pot = field.lastIndexOf('.');
			while (pot > 0) {
				path1 = field.substring(0, pot);
				path2 = field.substring(pot + 1);
				alias = fieldPathMap.get(path1);
				if (alias != null) {
					return alias + "." +path2;
				} else {
					pot = field.lastIndexOf('.', pot - 1);
				}
			}
		}
		return field;
	}

	private AssociationPathHelper (AssociationPathHelper parent, 
			SubCriteria criteria) {
		this.parent = parent;
		this.criteria = criteria;
		
		//计算globalPath
		Map<String, String> fieldPathMap = getFieldAliasMap();
		String subPath = criteria.getAssociationPath();
		globalPath = (parent.globalPath == null) ? 
				subPath: 
				parent.globalPath + "." + subPath;
		String criteriaAlias = criteria.getAlias();
		fieldPathMap.put(globalPath, criteriaAlias);
	}
	
	private void reset() {
		Map<String, String> fieldPathMap = getFieldAliasMap();
		
		//填充fieldPathMap
		List<Alias> aliases = criteria.getAliases();
		if (aliases != null && aliases.size() > 0) {
			for (Alias alias: aliases) {
				String associationPath = alias.getAssociationPath();
				if (globalPath != null) {
					associationPath = globalPath + "." + associationPath;
				}
				
				String aliasName = alias.getAlias();
				fieldPathMap.put(associationPath, StringUtils.defaultIfEmpty(aliasName, associationPath));
			}
		}
		
		List<SubCriteria> subCriterias = criteria.getSubCriterias();
		if (subCriterias != null && subCriterias.size() > 0) {
			for (SubCriteria sub: subCriterias) {
				AssociationPathHelper helper = new AssociationPathHelper(this, sub);
				helper.reset();
			}
		}
	}
	
	private Map<String, String> getFieldAliasMap() {
		if (parent != null) {
			return parent.getFieldAliasMap();
		} else {
			if (fieldPathMap == null) {
				fieldPathMap = new HashMap<String, String>();
			}
			return fieldPathMap;
		}
	}
}
