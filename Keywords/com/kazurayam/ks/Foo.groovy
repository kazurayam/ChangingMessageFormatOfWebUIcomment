package com.kazurayam.ks

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Foo {

	final static Logger logger = LoggerFactory.getLogger(Foo.class)

	void doIt() {
		for (int i = 0; i < 1000; i++) {
			StringBuilder sb = new StringBuilder()
			for (int j = 0; j < 300; j++) {
				sb.append("Hello(" + i + "," + j + ") ")
			}
			logger.debug(sb.toString())
		}
	}
}
