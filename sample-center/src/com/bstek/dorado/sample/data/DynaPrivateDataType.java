package com.bstek.dorado.sample.data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.property.Mapping;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.view.manager.ViewConfig;

@Component
public class DynaPrivateDataType {

	public void onInit(ViewConfig viewCofig) throws Exception {
		PropertyDef propertyDef;

		// Address
		EntityDataType dataTypeAddress = (EntityDataType) viewCofig
				.createDataType("Address");

		propertyDef = new BasePropertyDef("id");
		propertyDef.setDataType(viewCofig.getDataType("long"));
		propertyDef.setLabel("编码");
		propertyDef.setReadOnly(true);
		dataTypeAddress.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("city");
		propertyDef.setLabel("城市");
		dataTypeAddress.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("address");
		propertyDef.setLabel("详细地址");
		dataTypeAddress.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("postCode");
		propertyDef.setLabel("邮编");
		dataTypeAddress.addPropertyDef(propertyDef);

		// People
		EntityDataType dataTypePeople = (EntityDataType) viewCofig
				.getDataType("People");

		propertyDef = new BasePropertyDef("id");
		propertyDef.setDataType(viewCofig.getDataType("long"));
		propertyDef.setLabel("编码");
		propertyDef.setReadOnly(true);
		dataTypePeople.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("name");
		propertyDef.setLabel("姓名");
		dataTypePeople.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("age");
		propertyDef.setDataType(viewCofig.getDataType("Integer"));
		propertyDef.setLabel("年龄");
		dataTypePeople.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("sex");
		propertyDef.setDataType(viewCofig.getDataType("Boolean"));
		propertyDef.setLabel("性别");

		Mapping mapping = new Mapping();
		Map<Boolean, String> map = new LinkedHashMap<Boolean, String>();
		map.put(Boolean.TRUE, "男");
		map.put(Boolean.FALSE, "女");
		mapping.setMapValues(map);
		propertyDef.setMapping(mapping);

		dataTypePeople.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("birthday");
		propertyDef.setDataType(viewCofig.getDataType("Date"));
		propertyDef.setLabel("出生日期");
		propertyDef.setDisplayFormat("Y年m月d日");
		dataTypePeople.addPropertyDef(propertyDef);

		propertyDef = new BasePropertyDef("addresses");
		// 我们已经在前面创建了名为Address的DataType，所以可以通过[Address]来获得Address的集合形式。
		propertyDef.setDataType(viewCofig.getDataType("[Address]"));
		propertyDef.setLabel("地址");
		dataTypePeople.addPropertyDef(propertyDef);
	}

	@DataProvider
	public Record getPeople() throws Exception {
		Record people, address;

		people = new Record();
		people.set("id", 1);
		people.set("name", "小明");
		people.set("age", "25");
		people.set("sex", true);
		people.set("birthday", Date.valueOf("1987-09-01"));

		List<Record> addresses = new ArrayList<Record>();

		address = new Record();
		address.set("id", 1);
		address.set("city", "上海");
		address.set("address", "浦东新区峨山路91弄5号楼");
		address.set("postCode", "201101");
		addresses.add(address);
		address = new Record();

		address.set("id", 2);
		address.set("city", "北京");
		address.set("address", "海淀区西四环北路51号");
		address.set("postCode", "100302");
		addresses.add(address);

		people.set("addresses", addresses);

		return people;
	}
}
