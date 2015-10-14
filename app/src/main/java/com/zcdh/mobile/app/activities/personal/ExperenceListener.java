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
	void onEduExpItemClick(long id);
	
	void onEduExpItemDelete(long id);

	void onTranningExpItemDelete(long id);
	
	void onTranningExpItemClick(long id);

}
