package com.bstek.dorado.vidorsupport;

import java.io.File;

public class A {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		File f = new File("c:\\aaa\\bbb.xml");
		System.out.println(f.toURI().toURL());
	}

}
