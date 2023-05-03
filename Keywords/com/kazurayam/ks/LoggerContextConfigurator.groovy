package com.kazurayam.ks

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LoggerContextConfigurator {

	public static void configure() {
		// SLF4Jがlogbackを使うように設定されていると想定
		LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory()
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context)
			// デフォルトの設定を取り消したければcontext.reset()を呼び出す
			//context.reset()
			// context.reset()を呼ばなければデフォルトの設定を残し、その上に新しい設定要素を上書きする
			configurator.doConfigure("src/main/resources/logback-file.xml")
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
	}
}
