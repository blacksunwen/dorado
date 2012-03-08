package com.bstek.dorado.util.proxy;

import java.io.ObjectStreamException;

public interface SerializationReplaceable {
	Object writeReplace() throws ObjectStreamException;
}
