import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

File commentLog = new File("./comment.log")
if (commentLog.exists()) {
	commentLog.delete()
}

// make a patch to LogBack LoggerFactory object on the fly.
// write messages by WebUI.comment(String) into File in the "level message" format.
CustomKeywords.'com.kazurayam.ksbackyard.CustomLoggerFactory.customizeCommentKeyword4levelAndMsg2File'(commentLog)

def arg0 = 2
def arg1 = 3
def result = arg0 + arg1
WebUI.comment("arg0 + arg1 = ${result}")