package com.black.web.base.bean;

import java.io.Serializable;

public class QueryParam implements Serializable
{
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -1820559018603968143L;
	private String fieldName;
	private Object fieldValue;
	
	private String operator;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator);
		sb.append(fieldValue);
		return sb.toString();
	}
	
	public QueryParam(String fieldName, Object fieldValue, String operator) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.operator = operator;
	}
	public QueryParam() {
		super();
	}
	
}
