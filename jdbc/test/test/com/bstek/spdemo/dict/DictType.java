package test.com.bstek.spdemo.dict;

import java.sql.SQLException;
import java.sql.Connection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.Datum;
import oracle.sql.STRUCT;
import oracle.jpub.runtime.MutableStruct;

public class DictType implements ORAData, ORADataFactory {
	public static final String _SQL_NAME = "BSTEK.DICT_TYPE";
	public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

	protected MutableStruct _struct;

	protected static int[] _sqlType = { 2, 12, 2 };
	protected static ORADataFactory[] _factory = new ORADataFactory[3];
	protected static final DictType _DictTypeFactory = new DictType();

	public static ORADataFactory getORADataFactory() {
		return _DictTypeFactory;
	}

	/* constructors */
	protected void _init_struct(boolean init) {
		if (init)
			_struct = new MutableStruct(new Object[3], _sqlType, _factory);
	}

	public DictType() {
		_init_struct(true);
	}

	public DictType(java.math.BigDecimal id, String name,
			java.math.BigDecimal parentId) throws SQLException {
		_init_struct(true);
		setId(id);
		setName(name);
		setParentId(parentId);
	}

	/* ORAData interface */
	public Datum toDatum(Connection c) throws SQLException {
		return _struct.toDatum(c, _SQL_NAME);
	}

	/* ORADataFactory interface */
	public ORAData create(Datum d, int sqlType) throws SQLException {
		return create(null, d, sqlType);
	}

	protected ORAData create(DictType o, Datum d, int sqlType)
			throws SQLException {
		if (d == null)
			return null;
		if (o == null)
			o = new DictType();
		o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
		return o;
	}

	/* accessor methods */
	public java.math.BigDecimal getId() throws SQLException {
		return (java.math.BigDecimal) _struct.getAttribute(0);
	}

	public void setId(java.math.BigDecimal id) throws SQLException {
		_struct.setAttribute(0, id);
	}

	public String getName() throws SQLException {
		return (String) _struct.getAttribute(1);
	}

	public void setName(String name) throws SQLException {
		_struct.setAttribute(1, name);
	}

	public java.math.BigDecimal getParentId() throws SQLException {
		return (java.math.BigDecimal) _struct.getAttribute(2);
	}

	public void setParentId(java.math.BigDecimal parentId) throws SQLException {
		_struct.setAttribute(2, parentId);
	}

}
