package com.black.web.bean;

import java.util.List;

public class LinkedinEntity extends BaseEntity{

	private String userName;	//姓名
	private String desc;		//简介
	private String address;		//居住地址
	private String location;	//现单位
	private String education;	//最高学历
	private String friendStr;	//好友数
	private String callFollow;		//文章和动态被关注
	private List<String> contacts;//联系方式
	private List<LinkedinCompanyEntity> companys;//公司
	private List<LinkedinSchoolEntity> schools;//院校
	private List<LinkedinSkillGroupEntity> skillGroups;//技能组
	private List<LinkedinRecommendGroupEntity> tjGroup;//推荐组
	private List<LinkedinAchievementEntity> achievements;//成就
	private List<LiknedinFollowGroupEntity> followGroups;//关注组
	
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getFriendStr() {
		return friendStr;
	}
	public void setFriendStr(String friendStr) {
		this.friendStr = friendStr;
	}
	public List<String> getContacts() {
		return contacts;
	}
	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}
	public String getCallFollow() {
		return callFollow;
	}
	public void setCallFollow(String callFollow) {
		this.callFollow = callFollow;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<LinkedinCompanyEntity> getCompanys() {
		return companys;
	}
	public void setCompanys(List<LinkedinCompanyEntity> companys) {
		this.companys = companys;
	}
	public List<LinkedinSchoolEntity> getSchools() {
		return schools;
	}
	public void setSchools(List<LinkedinSchoolEntity> schools) {
		this.schools = schools;
	}
	public List<LinkedinSkillGroupEntity> getSkillGroups() {
		return skillGroups;
	}
	public void setSkillGroups(List<LinkedinSkillGroupEntity> skillGroups) {
		this.skillGroups = skillGroups;
	}
	public List<LinkedinRecommendGroupEntity> getTjGroup() {
		return tjGroup;
	}
	public void setTjGroup(List<LinkedinRecommendGroupEntity> tjGroup) {
		this.tjGroup = tjGroup;
	}
	public List<LinkedinAchievementEntity> getAchievements() {
		return achievements;
	}
	public void setAchievements(List<LinkedinAchievementEntity> achievements) {
		this.achievements = achievements;
	}
	public List<LiknedinFollowGroupEntity> getFollowGroups() {
		return followGroups;
	}
	public void setFollowGroups(List<LiknedinFollowGroupEntity> followGroups) {
		this.followGroups = followGroups;
	}
	
}
