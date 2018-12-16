package com.black.web.bean;

public class LinkedinRecommendEntity {

	private String name;			//推荐人姓名
	private String detail;			//推荐人头衔
	private String content;			//推荐内容
	private String relationship;	//与本人关系
	private String time;			//推荐日期
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
