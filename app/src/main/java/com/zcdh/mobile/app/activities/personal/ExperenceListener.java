/**
 * 
 * @author jeason, 2014-5-9 下午4:10:24
 */
package com.zcdh.mobile.app.activities.personal;

/**
 * @author jeason, 2014-5-9 下午4:10:24
 * 教育经历 培训经历操作监听器
 */
public interface ExperenceListener {
	public void onEduExpItemClick(long id);
	
	public void onEduExpItemDelete(long id);

	public void onTranningExpItemDelete(long id);
	
	public void onTranningExpItemClick(long id);

}
