/**
 * 
 * @author jeason, 2014-4-22 上午9:57:09
 */
package com.zcdh.mobile.app.activities.vacation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jeason, 2014-4-22 上午9:57:09 树结构filter
 */
public class FilterItem extends HashMap<String, String> {

	
	private ArrayList<FilterItem> children;

	public ArrayList<FilterItem> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<FilterItem> children) {
		this.children = children;
	}

	/**
	 * @author jeason, 2014-4-23 下午12:03:16
	 */
	public FilterItem(String name, String value) {
		setNameValue(name, value);
	}

	private void setNameValue(String name, String value) {
		put("name", name);
		put("value", value);
	}
}
