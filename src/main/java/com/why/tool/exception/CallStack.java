package com.why.tool.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CallStack {

	private static final Log logger = LogFactory.getLog(CallStack.class);

	public static void warn() {
		Throwable ex = new Throwable();
		StackTraceElement[] stackElements = ex.getStackTrace();
		if (stackElements == null) {
			return;
		}
		int size = Math.min(10, stackElements.length);
		for (int i = 0; i < size; i++) {
			StackTraceElement e = stackElements[i];
			logger.warn(e.getClassName()+"."+e.getMethodName()+"("+e.getFileName()+":"+e.getLineNumber()+")");
		}
	}

}
