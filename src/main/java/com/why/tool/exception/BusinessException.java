package com.why.tool.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

	private final int errorCode;
	
//	public BusinessException(ErrorCode errorCode){
//		super(errorCode.msg);
//		this.errorCode = errorCode.code;
//	}
	
	public BusinessException(int code, String msg){
		super(msg);
		this.errorCode = code;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
}
