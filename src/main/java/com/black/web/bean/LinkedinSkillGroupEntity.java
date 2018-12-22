package com.black.web.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class LinkedinSkillGroupEntity {

	private String name;		//技能组名称
	private List<LinkedinSkillEntity> skills;//技能组
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(StringUtils.isNotEmpty(name) && name.indexOf("其他技能")>-1) {
			this.name = "其他技能";
		}else {
			this.name = name;
		}
	}
	public List<LinkedinSkillEntity> getSkills() {
		return skills;
	}
	public void setSkills(List<LinkedinSkillEntity> skills) {
		this.skills = skills;
	}
	
}
