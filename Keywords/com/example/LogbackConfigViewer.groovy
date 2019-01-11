package com.example

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

import com.kms.katalon.core.annotation.Keyword

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;

public class LogbackConfigViewer {

	private static Logger logger = LoggerFactory.getLogger(LogbackConfigViewer.class);

	@Keyword
	static void show() throws Exception {
		LoggerContext loggerContext = ((ch.qos.logback.classic.Logger)logger).getLoggerContext();
		URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
		System.out.println(mainURL);
	}
}

