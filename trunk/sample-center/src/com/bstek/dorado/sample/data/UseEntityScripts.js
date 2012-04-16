[
		{
			description : "提示当前Entity",
			script : "alert(currentEntity.get(\"label\"));"
		},
		{
			description : "迭代所有顶层分类",
			script : "var labels = [];\nlist.each(function(entity) {\n\tlabels.push(entity.get(\"label\"));\n});\nalert(labels);"
		},
		{
			description : "修改当前Entity的属性值",
			script : "currentEntity.set({ label: \"<Modified>\", \"new\": true });"
		},
		{
			description : "添加同级对象 - createBrother",
			script : "currentEntity.createBrother({ label: \"<New Entity>\", icon: \">images/favorite.gif\", \"new\": true });"
		},
		{
			description : "在当前Entity之前插入同级对象 - createBrother",
			script : "var newEntity = currentEntity.createBrother({ label: \"<New Entity>\", \"new\": true }, true);\ncurrentEntity.parent.insert(newEntity, \"before\", currentEntity);"
		},
		{
			description : "添加子分类 - createChild",
			script : "currentEntity.createChild(\"categories\", { label: \"<新分类>\", icon: \">images/favorite.gif\", \"new\": true });"
		},
		{
			description : "判断集合子属性是否为空 - isEmpty()",
			script : "alert(currentEntity.get(\"categories\").isEmpty());"
		},
		{
			description : "返回当前Entity所处的集合中的第一个Entity",
			script : "alert(currentEntity.parent.getFirst().get(\"label\"));"
		},
		{
			description : "返回当前Entity的下一个Entity",
			script : "alert(currentEntity.getNext().get(\"label\"));"
		},
		{
			description : "删除当前Entity",
			script : "currentEntity.remove();"
		},
		{
			description : "删除所有同级Entity",
			script : "var entitiesToRemove = currentEntity.parent.toArray();\nentitiesToRemove.each(function(entity) {\n\tentity.remove();\n});"
		},
		{
			description : "上移当前Entity",
			script : "currentEntity.parent.insert(currentEntity, \"before\", currentEntity.getPrevious());"
		},
		{
			description : "下移当前Entity",
			script : "currentEntity.parent.insert(currentEntity, \"after\", currentEntity.getNext());"
		} ]