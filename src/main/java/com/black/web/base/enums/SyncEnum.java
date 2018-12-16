package com.black.web.base.enums;

public enum SyncEnum {

	TAOBAO("com.black.web.services.sync.impl.TaobaoSyncServiceImpl"),
	LINKEDIN("com.black.web.services.sync.impl.LinkedinSyncServiceImpl");
	
	private SyncEnum(String className) {
		this.className = className;
	}
	private String className;

	public String getClassName() {
		return className;
	}

}
