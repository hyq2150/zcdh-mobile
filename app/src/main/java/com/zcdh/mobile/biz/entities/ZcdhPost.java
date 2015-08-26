package com.zcdh.mobile.biz.entities;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 职位
 * 
 * @author yangjiannan
 * 
 */
@Table(name="zcdh_post")
public class ZcdhPost implements Serializable {

	
	private int id;
	private String post_category_code;
	private String post_code;
	private String post_description;
	private String post_name;
	private String remark;
	private String technology_param_id;
	private int is_delete;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPost_category_code() {
		return post_category_code;
	}
	public void setPost_category_code(String post_category_code) {
		this.post_category_code = post_category_code;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	public String getPost_description() {
		return post_description;
	}
	public void setPost_description(String post_description) {
		this.post_description = post_description;
	}
	public String getPost_name() {
		return post_name;
	}
	public void setPost_name(String post_name) {
		this.post_name = post_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTechnology_param_id() {
		return technology_param_id;
	}
	public void setTechnology_param_id(String technology_param_id) {
		this.technology_param_id = technology_param_id;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	
	
}
