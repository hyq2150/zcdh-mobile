package com.zcdh.mobile.app.activities.search;

/**
 * 在多选面板中删除已选的项 （多选面板详见：com.zcdh.mobile.app.activities.search.MultSelectionPannel）
 * @author yangjiannan
 *
 */
public interface RemoveItemListner {

	/**
	 * 
	 * @param key 
	 * 删除对象的标识值，标示值可能是职位code， 或者是地区code，等等
	 */
	public void onRemoveItem(Object key);
}
