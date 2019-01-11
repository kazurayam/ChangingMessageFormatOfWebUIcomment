Changing Message Format of WebUI.comment()
============

## What is this?

This is a small [Katalon Studio](https://www.katalon.com/) project for demostration purpose.
You can down load the zip file from [Releases]() page, unzip it and open with your Katalon Studio.

This project was developed using Katalon Studio version 5.10.1

This project was developed to solve my problem raised in the Katalon Forum:
[How can I change the message format of WebUI.comment(msg)](https://forum.katalon.com/t/how-can-i-change-the-message-format-of-webui-comment-msg/17559)

## Problem to solve

When I run this test case:
```
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
def arg0 = 2
def arg1 = 3
def result = arg0 + arg1
WebUI.comment("arg0 + arg1 = ${result}")
```
I get the following output in the Console:
```
2019-01-11 11:28:35.018 INFO c.k.katalon.core.main.TestCaseExecutor   - --------------------
2019-01-11 11:28:35.034 INFO c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/AdditionDemo
2019-01-11 11:28:35.561 DEBUG testcase.AdditionDemo                    - 1: arg0 = 2
2019-01-11 11:28:35.563 DEBUG testcase.AdditionDemo                    - 2: arg1 = 3
2019-01-11 11:28:35.565 DEBUG testcase.AdditionDemo                    - 3: result = arg0 + arg1
2019-01-11 11:28:35.581 DEBUG testcase.AdditionDemo                    - : comment(arg0 + arg1 = $result)
2019-01-11 11:28:35.710 INFO  c.k.k.c.keyword.builtin.CommentKeyword   - arg0 + arg1 = 5
2019-01-11 11:28:35.710 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/AdditionDemo
```
Please find the output message from `WebUI.comment("arg0 + arg1 = ${result}")`, which is:
```
2019-01-11 11:28:35.710 INFO  c.k.k.c.keyword.builtin.CommentKeyword   - arg0 + arg1 = 5
```
I do not like this. Its too verbose. Rather I want it to be:
```
arg0 + arg1 = 5
```

I mean, I want to bring the format as it was before version 5.7.0 = just the same as `System.out.println(msg)`.

### What I tried (but failed)

I found `<my Katalon home>/configuration/resources/logback/logback.xml`. I thought it is the original logback configuration file. I edited it as follows:
```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <conversionRule conversionWord="clr" converterClass="com.kms.katalon.core.logging.logback.ColorConverter" />
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(%-5level) %cyan(%-40.40logger{39} -) %clr(%msg){}%n</pattern>
    </encoder>
  </appender>
  <appender name="STDOUT-msgOnly" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%msg%n</pattern>
    </encoder>
  </appender>

  <!-- internal APIs -->
  <logger name="com.kms" level="info" />
  <logger name="com.kms.katalon.core.cucumber.keyword.CucumberReporter" level="debug" />
  <logger name="com.kms.katalon.core.logging.KeywordLogger" level="trace" />
  <logger name="com.kms.katalon.core.util.KeywordUtil" level="trace" />

  <!-- test case scripts -->
  <logger name="testcase" level="debug" />

  <!-- generated CustomKeywords file -->
  <logger name="CustomKeywords" level="debug" />

  <!-- built-in keywords -->
  <logger name="com.kms.katalon.core.keyword.builtin.CommentKeyword" level="info" additivity="false">
    <appender-ref ref="STDOUT-msgOnly" />
  </logger>

  <root level="error">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```
The idea here is that messages from `com.kms.katalon.core.keyword.builtin.CommentKeyword` should formatted as "message only".

I stopped and restarted Katalon Studio, and tried the test case. ----- But the message format not changed. Why?

### further investigation

I made a test case as this:
```
CustomKeywords.'com.example.LogbackConfigViewer.show'()
```

and a custom keyword as this (com.example.LogbackConfigViewer):
```
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
```

When I run this test case, I got the following output to the Console:
```
...
file:/C:/Katalon_Studio_Windows_64-5.10.1/configuration/org.eclipse.osgi/65/0/.cp/resources/logback/logback.xml
...
```

By this I learned that Katalon Studio reads `file:/C:/Katalon_Studio_Windows_64-5.10.1/configuration/org.eclipse.osgi/65/0/.cp/resources/logback/logback.xml`, of which content is different from the one I want (WebUI.comment should use appender of msgOnly).

To my surprise, I found that this file is deleted and recreated every time when I stop and restart Katalon Studio. It seems that Katalon Studio recreates the logback.xml. I expected that the `<my Katalon home>/configuration/resources/logback/logback.xml` should be the original for recreation referred by Katalon Studio. But I found it is not the case. Katalon Studio does recreates the logback.xml for runtime, but does not refer to the `<my Katalon home>/configuration/resources/logback/logback.xml`.

So, I can not find the way how to customize the Console message format from WebUI.comment(). How can I achive this?

### logback.xml is loaded from jar file via CLASSPATH

I found that `<my Katalon Home>/plugins/com.kms.katalon.core_1.0.0.201901020244.jar` file contains `/resources.logback/logback.xml` and `/resources.logback/logback-console.xml`.

It is likely that the xml files are loaded from the jar file via CLASSPATH and used by Katalon Studio runtime. Therefore manually editing `<my Katalon home>/configuration/resources/logback/logback.xml` file would not have any effect.

## Solution

Manually editting logback.xml file is not a good idea. I should modify logback LoggerFactory object runtime. I should be able to do it using Groovy's metaprogramming feature.

## Description

I have got a success.

I made a custom keyword [`com.kazurayam.ksbackyard.CustomLoggerFactory`](Keywords/com/kazurayam/ksbackyard/CustomLoggerFactory.groovy). I would not explain this code much. It performs a surgery for LogBack LoggerFactory object using Groovy's ExpandoMetaClass.

I made a test case [`CustomizeLogger4CommentKeywordDemo`](Scripts/CustomizeLogger4CommentKeywordDemo/Script1547184247057.groovy), that is:
```
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// make a patch to the Logback LoggerFactory object runtime
CustomKeywords.'com.kazurayam.ksbackyard.CustomLoggerFactory.customizeLogger4CommentKeyword'()

def arg0 = 2
def arg1 = 3
def result = arg0 + arg1
WebUI.comment("arg0 + arg1 = ${result}")
```

When I ran the test case, in the Console I see the following messages:
```
2019-01-11 14:31:12.826 INFO c.k.katalon.core.main.TestCaseExecutor   - --------------------
2019-01-11 14:31:12.826 INFO c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/CustomizeLogger4CommentKeywordDemo
2019-01-11 14:31:13.579 INFO k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ksbackyard.CustomLoggerFactory.customizeLogger4CommentKeyword is PASSED
arg0 + arg1 = 5
2019-01-11 14:31:13.774 INFO c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/CustomizeLogger4CommentKeywordDemo
```

I like this plain output:
```
arg0 + arg1 = 5
```

I have got a success.

## How to reuse it for you

You can copy the keyword [`com.kazurayam.ksbackyard.CustomLoggerFactory`](Keywords/com/kazurayam/ksbackyard/CustomLoggerFactory.groovy)
and paste it into your Katalon Studio project.

And in your test case, call `CustomKeywords.'com.kazurayam.ksbackyard.CustomLoggerFactory.customizeLogger4CommentKeyword'()`.

Then `WebUI.comment()` will become brief to the point.
