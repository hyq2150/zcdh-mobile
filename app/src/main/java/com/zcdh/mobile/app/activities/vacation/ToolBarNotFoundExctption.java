/**
 * 
 * @author YJN, 2013-10-29 下午10:44:34
 */
package com.zcdh.mobile.app.activities.vacation;

/**
 * @author YJN, 2013-10-29 下午10:44:34
 * 没有在布局文件中包含 myToolbar.xml ，而找不到Toolbar
 * 1) 设置title 时
 * 2） 设置左右Button属性时
 * 如果不存在会引起
 */
public class ToolBarNotFoundExctption extends Exception {

	/**
	 * 
	 * @author YJN, 2013-10-29 下午10:47:08
	 */
	private static final long serialVersionUID = 100L;

	public ToolBarNotFoundExctption(String error){
		super(error);
	}
}
