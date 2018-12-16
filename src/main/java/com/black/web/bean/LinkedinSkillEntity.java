package com.black.web.bean;

import java.util.List;

public class LinkedinSkillEntity {

	private String name;	//技能名称
	private String count;	//认可数量
	private String countStr;					//认可数量字符串
	private List<LinkedinFriendEntity> accepts;	//认可人集合
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCountStr() {
		return countStr;
	}
	public void setCountStr(String countStr) {
		this.countStr = countStr;
	}
	public List<LinkedinFriendEntity> getAccepts() {
		return accepts;
	}
	public void setAccepts(List<LinkedinFriendEntity> accepts) {
		this.accepts = accepts;
	}
	
}
