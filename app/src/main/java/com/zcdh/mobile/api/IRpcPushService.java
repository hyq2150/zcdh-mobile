/** 
*  IRpcPushService 
* 
*  Created Date: 2015-08-12 17:07:59 
*  
*/  
package com.zcdh.mobile.api;  
import java.util.*; 
import java.math.*; 
import com.zcdh.core.annotation.*; 
import com.zcdh.mobile.api.model.*; 
import com.zcdh.comm.entity.*; 
import com.zcdh.mobile.framework.nio.RequestChannel;
/** 
* 手机端绑定的接口 
*  @author focus, 2014-5-30 上午10:54:32 
*/  
@RpcService("rpcPushService")
public interface IRpcPushService   { 
    /** 
    * 绑定百度的userId 
    *  @param userId		用户id 
    *  @param pushUserId	推送的userId 
    *  @param pushChannelId 推送的channelId 
    *  @return 
    *  @author focus, 2014-5-30 上午11:06:51 
    */  
    @RpcMethod("bindBaiduPushServerUserIdAndChannelId")
    public RequestChannel<Integer> bindBaiduPushServerUserIdAndChannelId(@Param("userId") Long userId, @Param("pushUserId") String pushUserId, @Param("pushChannelId") String pushChannelId);


 } 

