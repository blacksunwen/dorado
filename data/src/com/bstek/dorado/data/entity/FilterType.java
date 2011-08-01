package com.bstek.dorado.data.entity;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
public enum FilterType {
	NONE, NEW, MODIFIED, DELETED, VISIBLE, DIRTY, VISIBLE_DIRTY, ALL;

	public static int toInt(FilterType type) {
		if (type == NONE)
			return 0;
		if (type == NEW)
			return 1;
		if (type == MODIFIED)
			return 2;
		if (type == DELETED)
			return 3;
		if (type == VISIBLE)
			return 96;
		if (type == DIRTY)
			return 97;
		if (type == VISIBLE_DIRTY)
			return 98;
		return 99;
	}

	public static FilterType fromInt(int i) {
		switch (i) {
		case 0:
			return NONE;
		case 1:
			return NEW;
		case 2:
			return MODIFIED;
		case 3:
			return DELETED;
		case 96:
			return VISIBLE;
		case 97:
			return DIRTY;
		case 98:
			return VISIBLE_DIRTY;
		default:
			return ALL;
		}
	}
}
