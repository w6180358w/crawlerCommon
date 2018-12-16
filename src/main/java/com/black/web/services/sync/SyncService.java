package com.black.web.services.sync;

import java.util.List;

import com.black.web.bean.BaseEntity;
import com.black.web.bean.ThreadBean;

public interface SyncService {
	/**
	 * 开始同步数据
	 */
	public void sync(List<BaseEntity> data,String key,Integer count) throws Exception;
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
	public void handleData(ThreadBean bean) throws Exception;
	
}
