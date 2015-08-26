/**
 * 
 * @author jeason, 2014-5-29 上午9:54:28
 */
package com.zcdh.mobile.biz.entities;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * @author jeason, 2014-5-29 上午9:54:28
 */
@Table(name = "zcdh_jobhunte_certificate")
public class ZcdhCertificate {
	
	private int id;
	private String cer_code;
	private String cer_name;
	private String parent_code;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCer_code() {
		return cer_code;
	}

	public void setCer_code(String cer_code) {
		this.cer_code = cer_code;
	}

	public String getCer_name() {
		return cer_name;
	}

	public void setCer_name(String cer_name) {
		this.cer_name = cer_name;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}
}
