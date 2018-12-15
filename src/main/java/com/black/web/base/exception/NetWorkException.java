package com.black.web.base.exception;

import com.black.web.base.msg.RaysMessage;

public class NetWorkException  extends RaysException{
	private static final long serialVersionUID = -6078731114637735054L;
	
	public NetWorkException(String msg) {
		super(msg);
		this.code = 404;
	}
	public NetWorkException() {
		super(RaysMessage.NEWWORK_ERROR);
		this.code = 404;
	}
}
