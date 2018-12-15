package com.black.web.base.enums;

public enum ResponseStatusEnum {
    SUCCESS(0),//成功
    UNKNOWERROR(-1),//未知错误 
    
    UNAUTHORITY(30000),//无权限
    ILLEGALTOKEN(20000),//非法TOKEN
	ILLEGALPOST(10000);//非法POST参数
    
	private Integer code;
	private ResponseStatusEnum(Integer code) {  
		this.code = code;
    }
	public Integer getCode() {
		return code;
	}

}