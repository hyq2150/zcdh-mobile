/** 
*  IRpcCUCCSpecialService 
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
* 联通专场 
*  @author focus, 2014-12-31 下午2:32:37 
*/  
@RpcService("rpcOutletsCUCCService")
public interface IRpcCUCCSpecialService   { 
    /** 
    * 联通网点的列表 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-12-31 下午2:45:37 
    */  
    @RpcMethod("findOutletsCUCCByUserId")
    public RequestChannel<List<OutletsCUCCDTO>> findOutletsCUCCByUserId(@Param("userId") Long userId);
    /** 
    * 添加根据经纬度过滤 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2015-1-6 上午10:12:37 
    */  
    @RpcMethod("findInformationTitle1")
    public RequestChannel<Page<InformationTitleDTO>> findInformationTitle1(@Param("userId") Long userId, @Param("lon") Double lon, @Param("lat") Double lat, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 更多的工具或者应用 
    *  @param userId 
    *  @param currentPge 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 下午2:47:59 
    */  
    @RpcMethod("findMoreTools1")
    public RequestChannel<Page<MoreToolsDTO>> findMoreTools1(@Param("userId") Long userId, @Param("lon") Double lon, @Param("lat") Double lat, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 发现中的封面也需要根据地区来调整 
    *  @param userId 	  用户的userId 
    *  @param deviceType 3 安卓设备，4 苹果设备 
    *  @return 
    *  @author focus, 2014-12-31 下午2:36:51 
    */  
    @RpcMethod("findAppCoverDTO")
    public RequestChannel<List<InformationCoverDTO>> findAppCoverDTO(@Param("userId") Long userId, @Param("deviceType") Integer deviceType, @Param("lon") Double lon, @Param("lat") Double lat);
    /** 
    * 地图中的封面(也需要根据地区来调整) 
    *  @param userId 
    *  @param deviceType 
    *  @param lon 
    *  @param lat 
    *  @param entryType 
    *  @return 
    *  @author liyuan, 2015-4-20 下午6:42:49 
    */  
    @RpcMethod("findAppCoverDTOForMap")
    public RequestChannel<List<InformationCoverDTO>> findAppCoverDTOForMap(@Param("userId") Long userId, @Param("deviceType") Integer deviceType, @Param("lon") Double lon, @Param("lat") Double lat, @Param("entryType") String entryType);


 } 

