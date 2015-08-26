package com.zcdh.mobile.biz.entities;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 
 * @author yangjiannan
 * 
 */
@Table(name = "zcdh_industry")
public class ZcdhIndustry implements Serializable {

	private int id;
	private String code;
	private String name;
	private String parent_code;
	private String remark;
	private int is_delete;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent_code() {
		return parent_code;
	}
	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	
}
