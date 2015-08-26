/** 
*  IRpcAppConfigService 
* 
*  Created Date: 2015-08-12 17:07:56 
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
* app框架配置相关服务接口 
*  @author liyuan, 2015-6-25 下午4:14:12 
*/  
@RpcService("rpcAppConfigService")
public interface IRpcAppConfigService   { 
    /** 
    * 根据用户id、用户类别、父模块代码查找可用的模块列表 
    *  @param userId 
    *  @param userType 
    *  @param parentModelCode 
    *  @return 
    *  @author liyuan, 2015-6-25 下午4:27:25 
    */  
    @RpcMethod("findAppConfigModelByParentCode")
    public RequestChannel<List<AdminAppConfigModelDTO>> findAppConfigModelByParentCode(@Param("userId") Long userId, @Param("userType") String userType, @Param("parentModelCode") String parentModelCode, @Param("lat") Double lat, @Param("lon") Double lon);


 } 

