package com.black.web.base.bean;

import java.lang.reflect.Field;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class BaseModel{
	
	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Query getQuery() {
		Query query = new Query();
		// 利用反射获取部位空的属性值
	    Field[] field = this.getClass().getDeclaredFields();
	    for (int i = 0; i < field.length; i++) {
	      Field f = field[i];
	      // 设置些属性是可以访问的
	      f.setAccessible(true);
	      try {
	        Object object = f.get(this);
	        if (object != null) {
	        	query.addCriteria(new Criteria(f.getName()).is(object));
	        }
	      } catch (IllegalAccessException e) {
	        e.printStackTrace();
	      }
	      f.setAccessible(false);
	    }
	    return query;
	}
	
	public Update getUpdate() {
		Update update = new Update();
		// 利用反射获取部位空的属性值
	    Field[] field = this.getClass().getDeclaredFields();
	    for (int i = 0; i < field.length; i++) {
	      Field f = field[i];
	      // 设置些属性是可以访问的
	      f.setAccessible(true);
	      try {
	        Object object = f.get(this);
	        if (object != null) {
	        	update.set(f.getName(), object);
	        }
	      } catch (IllegalAccessException e) {
	        e.printStackTrace();
	      }
	      f.setAccessible(false);
	    }
	    return update;
	}
}

