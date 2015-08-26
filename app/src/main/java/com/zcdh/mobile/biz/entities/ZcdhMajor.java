/**
 * 
 * @author jeason, 2014-4-10 下午3:55:32
 */
package com.zcdh.mobile.biz.entities;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * @author jeason, 2014-4-10 下午3:55:32
 */
@Table(name = "zcdh_jobhunte_major")
public class ZcdhMajor implements Serializable {

	@Id(column = "major_id")
	private int major_id;
	private String code;
	private String edu_type;
	private String major_name;
	private String parent_code;

	public int getMajor_id() {
		return major_id;
	}

	public void setMajor_id(int major_id) {
		this.major_id = major_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEdu_type() {
		return edu_type;
	}

	public void setEdu_type(String edu_type) {
		this.edu_type = edu_type;
	}

	public String getMajor_name() {
		return major_name;
	}

	public void setMajor_name(String major_name) {
		this.major_name = major_name;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

}
