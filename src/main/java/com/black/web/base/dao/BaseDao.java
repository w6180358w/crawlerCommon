package com.black.web.base.dao;

import java.util.List;

import com.black.web.base.bean.BaseModel;

public interface BaseDao {

	public <A extends BaseModel> void save(A a);
	
	public <A extends BaseModel> void update(A a);
	
	public <A extends BaseModel> A queryById(A a);
	
	public <A extends BaseModel> void remove(A a);
	
	public <A extends BaseModel> void remove(String id);
	
	public <A extends BaseModel> List<A> queryList(A t);
	
	public <A extends BaseModel> List<A> queryPage(A t, int pageNumber,int pageSize);
	
}
