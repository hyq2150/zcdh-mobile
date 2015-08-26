/**
 * 
 * @author focus, 2014-4-25 上午11:11:30
 */
package com.zcdh.mobile.api.model;

import java.io.Serializable;
import java.util.List;

import com.zcdh.core.annotation.RpcEntity;
/**
 * @author focus, 2014-4-25 上午11:11:30
 */
@RpcEntity
public class TrackOrderDTO implements Serializable {

	private String customerSrviceName;		//客服名称
	private String publishServiceNum;		//公众服务号
	private String customerServiceMobile;	//客服的电话
	private String customerServiceQQ;		//客服的QQ
	private List<TrackOrder> trackOrders;	//跟踪列表
	public String getCustomerSrviceName() {
		return customerSrviceName;
	}
	public void setCustomerSrviceName(String customerSrviceName) {
		this.customerSrviceName = customerSrviceName;
	}
	public String getPublishServiceNum() {
		return publishServiceNum;
	}
	public void setPublishServiceNum(String publishServiceNum) {
		this.publishServiceNum = publishServiceNum;
	}
	public String getCustomerServiceMobile() {
		return customerServiceMobile;
	}
	public void setCustomerServiceMobile(String customerServiceMobile) {
		this.customerServiceMobile = customerServiceMobile;
	}
	public String getCustomerServiceQQ() {
		return customerServiceQQ;
	}
	public void setCustomerServiceQQ(String customerServiceQQ) {
		this.customerServiceQQ = customerServiceQQ;
	}
	public List<TrackOrder> getTrackOrders() {
		return trackOrders;
	}
	public void setTrackOrders(List<TrackOrder> trackOrders) {
		this.trackOrders = trackOrders;
	}
	@Override
	public String toString() {
		return "TrackOrderDTO [customerSrviceName=" + customerSrviceName + ", publishServiceNum=" + publishServiceNum + ", customerServiceMobile=" + customerServiceMobile + ", customerServiceQQ=" + customerServiceQQ + ", trackOrders=" + trackOrders + "]";
	}
}
