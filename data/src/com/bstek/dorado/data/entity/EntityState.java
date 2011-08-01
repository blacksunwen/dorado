package com.bstek.dorado.data.entity;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-19
 */
public enum EntityState {
	NONE, NEW, MODIFIED, DELETED;

	public static boolean isDirty(EntityState state) {
		return !NONE.equals(state);
	}

	public static boolean isVisible(EntityState state) {
		return !DELETED.equals(state);
	}

	public static boolean isVisibleDirty(EntityState state) {
		return !NONE.equals(state) && !DELETED.equals(state);
	}

	public static int toInt(EntityState state) {
		if (state == NONE)
			return 0;
		if (state == NEW)
			return 1;
		if (state == MODIFIED)
			return 2;
		if (state == DELETED)
			return 3;
		return 0;
	}

	public static EntityState fromInt(int i) {
		switch (i) {
		case 0:
			return NONE;
		case 1:
			return NEW;
		case 2:
			return MODIFIED;
		case 3:
			return DELETED;
		default:
			return NONE;
		}
	}
}
