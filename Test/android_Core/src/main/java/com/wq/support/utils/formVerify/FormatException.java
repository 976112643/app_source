package com.wq.support.utils.formVerify;

public class FormatException  extends Exception{
	
	public FormatException(String exceptionStr) {
		super(exceptionStr);
	}
	public FormatException() {
		super("数据格式异常");
	}

}
