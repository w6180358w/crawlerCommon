package com.black.web.bean;

import java.util.List;

public class LiknedinFollowGroupEntity {

	private String type;	//关注组的类型 company 公司  school 学校
	private String count;	//关注数量
	private List<LinkedinFollowEntity> follows;//关注列表
	
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
	public List<LinkedinFollowEntity> getFollows() {
		return follows;
	}
	public void setFollows(List<LinkedinFollowEntity> follows) {
		this.follows = follows;
	}
}
