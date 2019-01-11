import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

CustomKeywords.'com.kazurayam.ksbackyard.CustomLoggerFactory.customizeLogger4CommentKeyword'()

def arg0 = 2
def arg1 = 3
def result = arg0 + arg1
WebUI.comment("arg0 + arg1 = ${result}")