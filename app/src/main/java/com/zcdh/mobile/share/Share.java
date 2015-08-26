package com.zcdh.mobile.share;

import java.util.List;

import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.favorite.WechatFavorite;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.app.Constants;

public class Share {
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 *
	 *
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	public static void showShare(Context context,final List<JobEntShareDTO> shareContents,boolean silent, String platform) {
		JobEntShareDTO shareDTO = null;
		if (shareContents != null && shareContents.size() > 0) {
			shareDTO = shareContents.get(0); // 取出第一个，拿到除分享内容外的其他信息
			final OnekeyShare oks = new OnekeyShare();
			oks.setTitle(shareDTO.getShareTitle());
			oks.setTitleUrl(shareDTO.getUrl());
			oks.setText(shareDTO.getShareContent());
			oks.setImageUrl(shareDTO.getImg());
			oks.setUrl(shareDTO.getUrl());
			oks.setSite(context.getString(R.string.app_name));
			oks.setSiteUrl("http://www.zcdhjob.com/");
			oks.setSilent(silent);
//			oks.setShareFromQQAuthSupport(shareFromQQLogin);
			if (platform != null) {
				oks.setPlatform(platform);
			}
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
				
				@Override
				public void onShare(Platform platform, ShareParams paramsToShare) {
					// TODO Auto-generated method stub
					if (shareContents != null && shareContents.size() > 0) {
						for (JobEntShareDTO dto : shareContents) {
							if (platform.getName().equals(dto.getType())) {
								paramsToShare.setText(dto.getShareContent());
							}
						}
					}
				}
			});
			// 令编辑页面显示为Dialog模式
			oks.setDialogMode();
			oks.disableSSOWhenAuthorize();
			oks.addHiddenPlatform(WechatFavorite.NAME);
			oks.show(context,Constants.SHARESDK_APPKEY);
		}
	
	}
}
