package com.black.web.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import com.black.web.base.bean.BaseModel;

@Document(collection="linkedin")
public class BaseEntity extends BaseModel{

	private String name;			//商品名称
	private String source;			//来源（淘宝京东等等）
	private String detail;			//商品详情
	private String subjectName;		//厂家名称
	private String subjectDetail;	//厂家详情
	private String url;				//商品url
	private String subjectUrl;		//厂家url
	private String price;			//价格
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectDetail() {
		return subjectDetail;
	}
	public void setSubjectDetail(String subjectDetail) {
		this.subjectDetail = subjectDetail;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSubjectUrl() {
		return subjectUrl;
	}
	public void setSubjectUrl(String subjectUrl) {
		this.subjectUrl = subjectUrl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "{name:'"+name+"',price:'"+price+"',url:'"+url+"',subjectName:'"+subjectName+"',source:'"+source+"'}";
	}
}
