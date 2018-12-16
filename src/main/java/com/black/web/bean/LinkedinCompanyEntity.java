package com.black.web.bean;

import java.util.List;

public class LinkedinCompanyEntity {

	private String name;
	private String entry;//入职日期
	private String duration;//任职时长
	private String address;//所在地区
	private String detail;//详情
	
	private List<LinkedinjobEntity> jobs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<LinkedinjobEntity> getJobs() {
		return jobs;
	}

	public void setJobs(List<LinkedinjobEntity> jobs) {
		this.jobs = jobs;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}
	
}
