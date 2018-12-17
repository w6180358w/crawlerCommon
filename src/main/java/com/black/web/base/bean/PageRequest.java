package com.black.web.base.bean;

import java.util.ArrayList;
import java.util.List;

public class PageRequest<A extends BaseModel> extends PagePo {
	private String[] keyWordParam;
	private String keyWord;
	private List<QueryParam> queryParamList;
	private A origin;
	public boolean foreign = true;
	public boolean tree = false;
	public List<String> relevance;
	
	public String[] getKeyWordParam() {
		return keyWordParam;
	}
	public void setKeyWordParam(String[] keyWordParam) {
		this.keyWordParam = keyWordParam;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public List<QueryParam> getQueryParamList() {
		if(queryParamList==null){
			queryParamList=new ArrayList<QueryParam>();
		}
		return queryParamList;
	}
	public void setQueryParamList(List<QueryParam> queryParamList) {
		this.queryParamList = queryParamList;
	}
	public A getOrigin() {
		return origin;
	}
	public void setOrigin(A origin) {
		this.origin = origin;
	}
	
}
