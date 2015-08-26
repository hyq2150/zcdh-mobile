package com.zcdh.mobile.biz.entities;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

/**
 * 参数表
 * 
 * @author yangjiannan
 * 
 */
@Table(name = "zcdh_param")
public class ZcdhParam implements Serializable {

	@Transient
	private int id;
	@Id
	private String param_category_code;
	private String param_code;
	private String param_name;
	private String param_value;
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParam_category_code() {
		return param_category_code;
	}

	public void setParam_category_code(String param_category_code) {
		this.param_category_code = param_category_code;
	}

	public String getParam_code() {
		return param_code;
	}

	public void setParam_code(String param_code) {
		this.param_code = param_code;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
