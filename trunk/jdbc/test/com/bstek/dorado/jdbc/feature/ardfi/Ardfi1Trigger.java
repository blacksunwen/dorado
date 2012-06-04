package com.bstek.dorado.jdbc.feature.ardfi;

import com.bstek.dorado.jdbc.model.AbstractDbTableTrigger;
import com.bstek.dorado.jdbc.support.SaveRecordOperation;

public class Ardfi1Trigger extends AbstractDbTableTrigger {

	private static int index = 0;
	
	@Override
	public void doSave(SaveRecordOperation operation) throws Exception {
		operation.getRecord().set("index", ++index);
		super.doSave(operation);
	}

}
