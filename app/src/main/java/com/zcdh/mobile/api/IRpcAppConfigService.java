/** 
*  IRpcAppConfigService 
* 
*  Created Date: 2015-10-12 17:31:49 
*  
*/  
package com.zcdh.mobile.api;  
import com.zcdh.core.annotation.Param;
import com.zcdh.core.annotation.RpcMethod;
import com.zcdh.core.annotation.RpcService;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.framework.nio.RequestChannel;

import java.util.List;
/** 
* app框架配置相关服务接口 
*  @author liyuan, 2015-6-25 下午4:14:12 
*/  
@RpcService("rpcAppConfigService")
public interface IRpcAppConfigService   { 
    /** 
    * 根据用户id、用户类别、父模块代码查找可用的模块列表 
    *  @param userId 
    *  @param parentModelCode 
    *  @param lat 
    *  @param lon 
    *  @param deviceType 3-android, 4-ios 
    *  @param appVersion 
    *  @return 
    *  @author liyuan, 2015-9-9 下午2:54:52 
    */  
    @RpcMethod("findAppConfigModelByParentCode")
    public RequestChannel<List<AdminAppConfigModelDTO>> findAppConfigModelByParentCode(@Param("userId")  Long userId, @Param("parentModelCode")  String parentModelCode, @Param("lat")  Double lat, @Param("lon")  Double lon, @Param("deviceType")  String deviceType, @Param("appVersion")  Integer appVersion);
    /** 
    * 查找可用的非系统模块列表 
    *  @param userId 
    *  @param parentModelId 
    *  @param lat 
    *  @param lon 
    *  @param deviceType 
    *  @param appVersion 
    *  @return 
    *  @author liyuan, 2015-9-14 下午3:46:04 
    */  
    @RpcMethod("findAppConfigModelExt")
    public RequestChannel<List<AdminAppConfigModelDTO>> findAppConfigModelExt(@Param("userId")  Long userId, @Param("parentModelId")  Long parentModelId, @Param("lat")  Double lat, @Param("lon")  Double lon, @Param("deviceType")  String deviceType, @Param("appVersion")  Integer appVersion);
    /** 
    * 根据用户id、父模块id、经纬度、用户类别、版本号、appId查找可用的模块列表 
    *  @param userId 
    *  @param parentModelId 
    *  @param lat 
    *  @param lon 
    *  @param deviceType 
    *  @param appVersion 
    *  @param appId 
    *  @return 
    *  @author liyuan, 2015-9-23 上午11:25:45 
    */  
    @RpcMethod("findAppConfigModelByParentId")
    public RequestChannel<List<AdminAppConfigModelDTO>> findAppConfigModelByParentId(@Param("userId")  Long userId, @Param("parentModelId")  Long parentModelId, @Param("lat")  Double lat, @Param("lon")  Double lon, @Param("deviceType")  String deviceType, @Param("appVersion")  Integer appVersion, @Param("appId")  Long appId);
    /** 
    * 根据用户id、经纬度、用户类别、版本号、appId获取app及所有模块信息 
    *  @param userId 
    *  @param lat 
    *  @param lon 
    *  @param deviceType 
    *  @param appVersion 
    *  @param appId 
    *  @return app的logo图标url、模块列表(List<AdminAppConfigModelDTO>) 
    *  @author liyuan, 2015-9-23 下午3:14:34 
    */  
    @RpcMethod("findAllAppConfigModelInfo")
    public RequestChannel<List<AdminAppConfigModelDTO>> findAllAppConfigModelInfo(@Param("userId")  Long userId, @Param("lat")  Double lat, @Param("lon")  Double lon, @Param("deviceType")  String deviceType, @Param("appVersion")  Integer appVersion, @Param("appId")  Long appId);
    /** 
    * 根据用户id、app模块id列表及相应的选择标记,更新用户扩展模块表 
    *  @param app_model_id app模块列表 
    *  @param isSelect 选择列表,0-未选择,1-已选择(该列表与app_model_id参数按顺序一一对应,表示是否选择了该模块) 
    *  @param userId 
    *  @return 0-更新失败 1-更新成功 
    *  @author liyuan, 2015-9-23 下午5:33:24 
    */  
    @RpcMethod("UpdateAppconfigModelUserext")
    public RequestChannel<Integer> UpdateAppconfigModelUserext(@Param("app_model_id")  List<Long> app_model_id, @Param("isSelect")  List<Integer> isSelect, @Param("userId")  Long userId);


 } 

