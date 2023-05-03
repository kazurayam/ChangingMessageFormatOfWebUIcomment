package com.kazurayam.ks

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * https://yujiorama.github.io/unofficial-translations/logback-manual/03-configuration.html
 */
public class MyApp3 {

	final static Logger logger = LoggerFactory.getLogger(MyApp3.class)

	public void execute() {
		// SLF4Jがlogbackを使うように設定されていると想定
		LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory()

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context)
			// デフォルトの設定を取り消すために context.reset()を呼び出す
			// context.reset()を呼ばなければ設定を上書きすることになる
			context.reset()
			configurator.doConfigure("src/main/resources/logback-console-file.xml")
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);

		logger.info("Entering Foo application.");
		WebUI.comment("calling Foo")
		Foo foo = new Foo()
		foo.doIt()
		WebUI.comment("called Foo")
		logger.info("Exiting Foo application");
	}
}
