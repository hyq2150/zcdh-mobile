package com.zcdh.mobile.app.activities.nearby;

import android.widget.ImageButton;

import com.zcdh.mobile.api.model.JobSearchTagDTO;

/**
 * 搜索bar 回调事件
 * @author yangjiannan
 *
 */
public interface SearchBarListener {

	void onTagClick(JobSearchTagDTO tag);
	
	void onKeywordSearch(String keyword);
	
	void onVoiceResult(String sayContent);
	
	void onSwitchMode(ImageButton v);
	
	void onVoiceEnd();
	
	void onShow(boolean show);
	
}
