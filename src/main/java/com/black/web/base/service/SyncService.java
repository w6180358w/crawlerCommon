package com.black.web.base.service;

import java.util.List;

import com.black.web.base.bean.BaseModel;
import com.black.web.base.collect.ThreadBean;

public interface SyncService<T extends BaseModel> {
	/**
	 * 开始同步数据
	 */
	public void sync(List<T> data,String key,Integer count) throws Exception;
	/**
	 * 停止同步方法
	 */
	public void shutdown();
	
	/**
	 * 获取当前采集类型
	 * @return
	 */
	public String getType();
	
	/**
	 * 处理数据
	 * @param bean
	 * @throws Exception
	 */
	public void handleData(ThreadBean<T> bean) throws Exception;
	
}
