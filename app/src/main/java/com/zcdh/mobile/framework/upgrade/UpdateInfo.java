/**
 * 
 * @author YJN, 2013-11-29 下午11:37:51
 */
package com.zcdh.mobile.framework.upgrade;

/**
 * @author YJN, 2013-11-29 下午11:37:51
 */
public class UpdateInfo{
	private String version;
	private String versionName;
	private String url;
	private String description;
	private String restype;
	private String resid;
	private String forcedUpdate;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getRestype() {
		return restype;
	}
	public void setRestype(String restype) {
		this.restype = restype;
	}
	public String getResid() {
		return resid;
	}
	public void setResid(String resid) {
		this.resid = resid;
	}
	public String getForcedUpdate() {
		return forcedUpdate;
	}
	public void setForcedUpdate(String forcedUpdate) {
		this.forcedUpdate = forcedUpdate;
	}
	
}
