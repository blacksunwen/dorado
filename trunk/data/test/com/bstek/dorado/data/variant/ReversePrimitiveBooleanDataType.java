package com.bstek.dorado.data.variant;

import com.bstek.dorado.data.type.PrimitiveBooleanDataType;

public class ReversePrimitiveBooleanDataType extends PrimitiveBooleanDataType {

	@Override
	public Object fromObject(Object value) {
		Boolean b = (Boolean) super.fromObject(value);
		b = new Boolean(!b.booleanValue());
		return b;
	}

}
