/**
 * 
 * @author jeason, 2014-5-15 上午10:13:13
 */
package com.zcdh.mobile.biz.entities;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * @author jeason, 2014-5-15 上午10:13:13
 */
@Table(name = "zcdh_school")
public class ZcdhSchool {
	private int id;
	private String remark;
	private String school_code;
	private String school_name;
	private String area_code;
	private String education_approach;
	private String school_dept_code;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSchool_code() {
		return school_code;
	}
	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getEducation_approach() {
		return education_approach;
	}
	public void setEducation_approach(String education_approach) {
		this.education_approach = education_approach;
	}
	public String getSchool_dept_code() {
		return school_dept_code;
	}
	public void setSchool_dept_code(String school_dept_code) {
		this.school_dept_code = school_dept_code;
	}
}
