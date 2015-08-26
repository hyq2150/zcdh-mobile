/**
 * 
 * @author jeason, 2014-4-10 下午3:55:32
 */
package com.zcdh.mobile.biz.entities;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

/**
 * @author jeason, 2014-4-10 下午3:55:32
 */
@Table(name = "zcdh_area")
public class ZcdhArea {
	
	@Transient
	private int id;
	@Id
	private String code;
	@Transient
	private String fullname;
	private String name;
	private String name_sort;
	private String parent_code;
	private String remark;
	@Transient
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

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_sort() {
		return name_sort;
	}

	public void setName_sort(String name_sort) {
		this.name_sort = name_sort;
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
