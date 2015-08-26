package com.zcdh.mobile.app.activities.nearby;

import android.widget.ImageButton;

import com.zcdh.mobile.api.model.JobSearchTagDTO;

/**
 * 搜索bar 回调事件
 * @author yangjiannan
 *
 */
public interface SearchBarListener {

	public void onTagClick(JobSearchTagDTO tag);
	
	public void onKeywordSearch(String keyword);
	
	public void onVoiceResult(String sayContent);
	
	public void onSwitchMode(ImageButton v);
	
	public void onVoiceEnd();
	
	public void onShow(boolean show);
	
}
