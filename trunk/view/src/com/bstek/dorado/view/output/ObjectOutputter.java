package com.bstek.dorado.view.output;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-24
 */
public abstract class ObjectOutputter implements Outputter {

	private Map<String, Object> configProperties = new HashMap<String, Object>();

	/**
	 * 返回要输出的POJO属性的Map集合。
	 * @see #setConfigProperties(Map)
	 */
	public Map<String, Object> getConfigProperties() {
		return configProperties;
	}

	/**
	 * 设置要输出的POJO属性的Map集合。<br>
	 * 其中Map集合的键为属性名，不同类型的键值具有不同的含义：
	 * <ul>
	 * <li>通常情况下键值会被默认为是相应属性的默认值。 即当某个要输出的POJO属性值与默认值相同时，该属性将被忽略而不会输出到JSON中。</li>
	 * <li>当键值是PropertyOutputter的实现类时，键值代表一个子属性的输出器。 此时系统会将该属性的输出任务分派给这个子输出器。</li>
	 * <li>当键值是VirtualPropertyOutputter的实现类时，键值代表一个虚拟属性的输出器。
	 * 虚拟属性是指并不一定真的存在于要输出的Java对象中的属性，只是我们希望在进行输出时输出这样一个属性值。</li>
	 * <li>字符串"#default"是一个特殊的默认值，并且对于不同数据类型的属性而言"#default"又代表不同含义：
	 * <ul>
	 * <li>对String而言"#default"表示null或""。</li>
	 * <li>对boolean而言"#default"表示false。</li>
	 * <li>对int、long、float、double等而言"#default"表示0。</li>
	 * <li>对其他数据类型而言"#default"表示null。</li>
	 * </ul>
	 * </li>
	 * <li>字符串"#ignore"是一个特殊的值，表示忽略该属性的输出操作。</li>
	 * </ul>
	 */
	public void setConfigProperties(Map<String, Object> configProperties) {
		this.configProperties = configProperties;
	}
}
