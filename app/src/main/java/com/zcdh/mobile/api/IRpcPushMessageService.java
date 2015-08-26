/** 
*  IRpcPushMessageService 
* 
*  Created Date: 2015-08-12 17:07:58 
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
* @author focus, 2014-9-2 下午5:01:32 
*/  
@RpcService("pushMessageTest")
public interface IRpcPushMessageService   { 
    @RpcMethod("testEntReply")
    public RequestChannel<Integer> testEntReply(@Param("userId") Long userId);
    @RpcMethod("testInteview")
    public RequestChannel<Integer> testInteview(@Param("userId") Long userId);
    @RpcMethod("testTrackPost")
    public RequestChannel<Integer> testTrackPost(@Param("userId") Long userId);


 } 

