package com.black.web.bean;

import java.util.List;

public class LinkedinRecommendGroupEntity {

	private String type;//推荐信类型，in已收到，out已发出
	
	private String count;//推荐信数量
	
	private List<LinkedinRecommendEntity> users;	//推荐人

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<LinkedinRecommendEntity> getUsers() {
		return users;
	}

	public void setUsers(List<LinkedinRecommendEntity> users) {
		this.users = users;
	}
	
}
