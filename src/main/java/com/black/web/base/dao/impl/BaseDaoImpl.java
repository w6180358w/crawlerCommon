package com.black.web.base.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.black.web.base.bean.BaseModel;
import com.black.web.base.dao.BaseDao;

@Repository
public class BaseDaoImpl implements BaseDao{
	//子类可以使用
	@Autowired
	protected MongoTemplate mongoTemplate;

	@Override
	public <T extends BaseModel> void save(T a) {
		mongoTemplate.save(a);
	}

	@Override
	public <T extends BaseModel> void update(T a) {
		Class<? extends BaseModel> clazz = a.getClass();
		
	    // 获取实体类的所有属性，返回Field数组
	    Query query = new Query();
	    query.addCriteria(new Criteria("_id").is(a.getId()));
	    Update update = a.getUpdate();
	    this.mongoTemplate.updateFirst(query, update, clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseModel> T queryById(T a) {
		return (T) this.mongoTemplate.findById(a.getId(), a.getClass());
	}

	@Override
	public <T extends BaseModel> void remove(T a) {
		Query query = new Query();
	    query.addCriteria(new Criteria("_id").is(a.getId()));
		this.mongoTemplate.remove(query, a.getClass());
	}

	@Override
	public <T extends BaseModel> void remove(String id) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseModel> List<T> queryList(T t) {
		return (List<T>) this.mongoTemplate.find(t.getQuery(), t.getClass());
	}

	@Override
	public <T extends BaseModel> List<T> queryPage(T t, int pageNumber, int pageSize) {
		return null;
	}
	
}
