import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// make a patch to LogBack LoggerFactory object runtime
// make messages by WebUI.comment(String) less verbose - message only to Console
CustomKeywords.'com.kazurayam.ksbackyard.CustomLoggerFactory.customizeCommentKeyword4MsgOnly2Console'()

def arg0 = 2
def arg1 = 3
def result = arg0 + arg1
WebUI.comment("arg0 + arg1 = ${result}")
