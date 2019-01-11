package com.kazurayam.ksbackyard

import org.slf4j.LoggerFactory;
import org.slf4j.ILoggerFactory
//import org.slf4j.Logger

import com.kms.katalon.core.annotation.Keyword

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

public class CustomLoggerFactory {

	private static Logger logger = LoggerFactory.getLogger(CustomLoggerFactory.class);

	public static final CLASSNAME_COMMENT_KEYWORD = 'com.kms.katalon.core.keyword.builtin.CommentKeyword'

	/**
	 * @return Logger as defined by the following logback config
	 * <PRE>
	 * <configuration>
	 *   <appender name="STDOUT-msgOnly" class="ch.qos.logback.core.ConsoleAppender">
	 *     <encoder>
	 *       <pattern>%msg%n</pattern>
	 *     </encoder>
	 *   </appender>
	 *   <logger name="com.kms.katalon.core.keyword.builtin.CommentKeyword" level="info" additivity="false">
	 *     <appender-ref ref="STDOUT-msgOnly" />
	 *   </logger>
	 * </configuration>
	 * </PRE>
	 */
	@Keyword
	public static Logger getLogger4CommentKeyword() {
		LoggerContext logCtx = ((ch.qos.logback.classic.Logger)logger).getLoggerContext()
		//
		PatternLayoutEncoder msgOnlyEncoder = new PatternLayoutEncoder()
		msgOnlyEncoder.setContext(logCtx)
		msgOnlyEncoder.setPattern("%msg%n")
		msgOnlyEncoder.start()
		//
		ConsoleAppender msgOnlyAppender = new ConsoleAppender()
		msgOnlyAppender.setContext(logCtx)
		msgOnlyAppender.setName("STDOUT-msgOnly")
		msgOnlyAppender.setEncoder(msgOnlyEncoder)
		msgOnlyAppender.start()
		//
		Logger logger = logCtx.getLogger(CLASSNAME_COMMENT_KEYWORD)
		logger.additive = false
		logger.level = Level.INFO
		logger.addAppender(msgOnlyAppender)
		//
		return logger
	}

	@Keyword
	public static void customizeLogger4CommentKeyword() {
		LoggerFactory.metaClass.static.getLogger = { String name ->
			if (name =~ /com\.kms\.katalon\.core\.keyword\.builtin\.CommentKeyword/) {
				return getLogger4CommentKeyword()
			} else {
				ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory()
				return iLoggerFactory.getLogger(name);
			}
		}
		org.slf4j.Logger log = LoggerFactory.getLogger("com.kms.katalon.core.keyword.builtin.CommentKeyword")
	}

}
