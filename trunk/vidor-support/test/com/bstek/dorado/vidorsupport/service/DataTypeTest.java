package com.bstek.dorado.vidorsupport.service;

import java.util.Collection;

import junit.framework.Assert;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.iapi.IDataTypeWorkshop;

public class DataTypeTest extends AbstractCloudoTestCase {

	public void test_baseNames() throws Exception {
		IDataTypeWorkshop workshop = this.getServiceBean("&cloudo.dataTypeWorkshop");
		Collection<String> names = workshop.baseNames();
		
		Assert.assertEquals(names.size(), 32);
	}
}
