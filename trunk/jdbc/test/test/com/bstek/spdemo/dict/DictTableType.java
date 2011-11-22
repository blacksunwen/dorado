package test.com.bstek.spdemo.dict;

import java.sql.SQLException;
import java.sql.Connection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.Datum;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.jpub.runtime.MutableArray;

public class DictTableType implements ORAData, ORADataFactory {
	public static final String _SQL_NAME = "BSTEK.DICT_TABLE_TYPE";
	public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

	MutableArray _array;

	private static final DictTableType _DictTableTypeFactory = new DictTableType();

	public static ORADataFactory getORADataFactory() {
		return _DictTableTypeFactory;
	}

	/* constructors */
	public DictTableType() {
		this((DictType[]) null);
	}

	public DictTableType(DictType[] a) {
		_array = new MutableArray(2002, a, DictType.getORADataFactory());
	}

	/* ORAData interface */
	public Datum toDatum(Connection c) throws SQLException {
		return _array.toDatum(c, _SQL_NAME);
	}

	/* ORADataFactory interface */
	public ORAData create(Datum d, int sqlType) throws SQLException {
		if (d == null)
			return null;
		DictTableType a = new DictTableType();
		a._array = new MutableArray(2002, (ARRAY) d, DictType.getORADataFactory());
		return a;
	}

	public int length() throws SQLException {
		return _array.length();
	}

	public int getBaseType() throws SQLException {
		return _array.getBaseType();
	}

	public String getBaseTypeName() throws SQLException {
		return _array.getBaseTypeName();
	}

	public ArrayDescriptor getDescriptor() throws SQLException {
		return _array.getDescriptor();
	}

	/* array accessor methods */
	public DictType[] getArray() throws SQLException {
		return (DictType[]) _array.getObjectArray(new DictType[_array.length()]);
	}

	public DictType[] getArray(long index, int count) throws SQLException {
		return (DictType[]) _array.getObjectArray(index,
				new DictType[_array.sliceLength(index, count)]);
	}

	public void setArray(DictType[] a) throws SQLException {
		_array.setObjectArray(a);
	}

	public void setArray(DictType[] a, long index) throws SQLException {
		_array.setObjectArray(a, index);
	}

	public DictType getElement(long index) throws SQLException {
		return (DictType) _array.getObjectElement(index);
	}

	public void setElement(DictType a, long index) throws SQLException {
		_array.setObjectElement(a, index);
	}

}
