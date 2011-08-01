package com.bstek.dorado.util;

import com.bstek.dorado.util.PathUtils;

import junit.framework.TestCase;

public class PathMatchingUtilsTest extends TestCase {
	public void test() {
		assertFalse(PathUtils.match("*", "abc/ab"));
		assertFalse(PathUtils.match("*", "/abc/ab"));
		assertTrue(PathUtils.match("**", "abc/ab"));
		assertFalse(PathUtils.match("**", "/abc/ab"));
		assertTrue(PathUtils.match("**", "abc/ab.abc"));
		assertTrue(PathUtils.match("/**", "/abc/ab"));
		assertTrue(PathUtils.match("**/*.*", "abc.abc"));
		assertTrue(PathUtils.match("**/*.*", "abc.abc"));
		assertTrue(PathUtils.match("**", "abc"));
		assertFalse(PathUtils.match("*/*", "abc"));
		assertTrue(PathUtils.match("*/*", "abc/ab"));
		assertTrue(PathUtils.match("*/ab", "abc/ab"));
		assertFalse(PathUtils.match("*/ab", "abc/abc"));
		assertTrue(PathUtils.match("*/ab*", "abc/ab"));
		assertTrue(PathUtils.match("*.*", "abc.ab"));
		assertTrue(PathUtils.match("abc/ab*", "abc/ab"));
		assertTrue(PathUtils.match("abc/*", "abc/ab"));
		assertFalse(PathUtils.match("abc/*", "abc/ab/abc"));
	}
}
