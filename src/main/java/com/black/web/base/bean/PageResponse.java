package com.black.web.base.bean;

import static com.black.web.base.enums.ResponseStatusEnum.SUCCESS;
import static com.black.web.base.enums.ResponseStatusEnum.UNKNOWERROR;

import java.util.List;

import com.black.web.base.exception.RaysException;
import com.black.web.base.msg.RaysMessage;

public class PageResponse<T> extends PagePo {

	private boolean result;
	private Integer code = 0;
	private String msg;
	private T data;
	private List<T> datalist;

	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public List<?> getDatalist() {
		return datalist;
	}
	public void setDatalist(List<T> datalist) {
		this.datalist = datalist;
	}
	public PageResponse(boolean result, Integer code, String msg, T data) {
		this.result = result;
		this.data = data;
		this.code = code;
		this.msg = msg;
		this.isNotPage = null;
	}
	public PageResponse(boolean result, Integer code, String msg, List<T> datalist) {
		this.result = result;
		this.datalist = datalist;
		this.code = code;
		this.msg = msg;
	}
	public PageResponse(boolean result, String msg, T data) {
		this.result = result;
		this.data = data;
		this.msg = msg;
		this.isNotPage = null;
		valCodeMsg(result);
	}
	public PageResponse(boolean result, String msg, List<T> datalist) {
		this.result = result;
		this.datalist = datalist;
		this.msg = msg;
		valCodeMsg(result);
	}
	public PageResponse(boolean result, Integer code, String msg) {
		this.result = result;
		this.code = code;
		this.msg = msg;
		this.isNotPage = null;
	}
	public PageResponse(boolean result, T data) {
		this.result = result;
		this.data = data;
		this.isNotPage = null;
		valCodeMsg(result);
	}
	public PageResponse(boolean result, String msg) {
		this.result = result;
		this.msg = msg;
		this.isNotPage = null;
		valCodeMsg(result);
	}
	public PageResponse(Throwable e) {
		this.code = 0;
		this.result = false;
		this.isNotPage = null;
		if(e instanceof RaysException){
			RaysException rayse = RaysException.class.cast(e);
			this.code = rayse.getCode();
			this.msg = rayse.getMsg();
		}else{
			RaysException rayse = new RaysException();
			this.code = rayse.getCode();
			this.msg = rayse.getMsg();
		}
	}
	public PageResponse(boolean result, List<T> datalist) {
		this.result = result;
		this.datalist = datalist;
		valCodeMsg(result);
	}
	public PageResponse(boolean result) {
		this.result = result;
		this.isNotPage = null;
		valCodeMsg(result);
	}

	private void valCodeMsg(boolean result) {
		if(result){
			this.code = SUCCESS.getCode();
		}else{
			this.code = UNKNOWERROR.getCode();
		}
		if(this.msg==null){
			if(result){
				this.msg = RaysMessage.ACTION_SUCCESS;
			}else{
				this.msg = RaysMessage.UNKONW_ERROR;
			}
		}
	}

}
