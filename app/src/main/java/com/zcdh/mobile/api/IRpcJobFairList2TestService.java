/** 
*  IRpcJobFairList2TestService 
* 
*  Created Date: 2015-08-12 17:07:57 
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
* @author liyuan, 2015-4-13 下午12:00:06 
*/  
@RpcService("rpcJobFairList2TestService")
public interface IRpcJobFairList2TestService   { 
    @RpcMethod("findJobFairList2Test")
    public RequestChannel<Page<JobFairList2TestDTO>> findJobFairList2Test(@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    @RpcMethod("isUserSigninJobfair3")
    public RequestChannel<Integer> isUserSigninJobfair3(@Param("fairId") Long fairId, @Param("userId") Long userId);


 } 

