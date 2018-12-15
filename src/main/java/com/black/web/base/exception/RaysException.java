package com.black.web.base.exception;

import com.black.web.Logger.Logger;
import com.black.web.base.msg.RaysMessage;

public class RaysException extends Exception{
	
	protected Integer code = -1;
	protected String msg = RaysMessage.UNKONW_ERROR;
	
	public RaysException(String msg) {
		Logger.error(msg);
		this.msg = msg;
	}

	public RaysException(Integer code,String msg) {
		Logger.error(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RaysException() {
		super();
	}
	private static final long serialVersionUID = 1L;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
