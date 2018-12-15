package com.black.web.base.bean;

public class PagePo {
	
	public PagePo() {
	}
	public PagePo(PageLimit page) {
		this.page = page;
	}
	
	//是否分页
	public Boolean isNotPage = false;
	//分页信息
	private PageLimit page;
	public PageLimit getPage() {
		int size = 10;
		int index = 1;
		if(page==null){
			return new PageLimit(null,size,index);
		}
		page.setIndex(page.getIndex()!=null?page.getIndex():index);
		page.setSize(page.getSize()!=null?page.getSize():size);
		return page;
	}
	public void setPage(PageLimit page) {
		this.page = page;
	}


	public class PageLimit{

		public PageLimit(Long totalResult, Integer size, Integer index) {
			this.totalResult = totalResult;
			this.size = size;
			this.index = index;
		}
		public PageLimit(PageLimit page) {
			this.totalResult = page.totalResult;
			this.size = page.size;
			this.index = page.index;
		}
		
		//总记录数
		private Long totalResult = 0L;
		private Integer size;
		private Integer index;
		public Long getTotalResult() {
			return totalResult;
		}
		public void setTotalResult(Long totalResult) {
			this.totalResult = totalResult;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		public Integer getIndex() {
			return index;
		}
		public void setIndex(Integer index) {
			this.index = index;
		}

	}

}
