package com.bstek.dorado.data.type.property;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
public enum CacheMode {
	noCache, serverSide, clientSide, bothSides;

	public static boolean isCacheableAtServerSide(CacheMode cacheMode) {
		return cacheMode == bothSides || cacheMode == serverSide;
	}

	public static boolean isCacheableAtClientSide(CacheMode cacheMode) {
		return cacheMode == bothSides || cacheMode == clientSide;
	}
}
