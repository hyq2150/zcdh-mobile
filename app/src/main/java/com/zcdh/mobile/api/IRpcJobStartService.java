/** 
*  IRpcJobStartService 
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
* 引导页 
*  @author focus, 2014-12-31 上午11:11:54 
*/  
@RpcService("rpcJobStartService")
public interface IRpcJobStartService   { 
    /** 
    * html 引导页 
    *  @param lat 
    *  @param lon 
    *  @return 
    *  @author focus, 2014-12-31 上午11:28:03 
    */  
    @RpcMethod("findGuideViewByHtml")
    public RequestChannel<List<String>> findGuideViewByHtml(@Param("lat") Double lat, @Param("lon") Double lon, @Param("deviceType") Integer deviceType);
    /** 
    * 图片 引导页 
    *  @param lat 
    *  @param lon 
    *  @return 
    *  @author focus, 2014-12-31 上午11:28:12 
    */  
    @RpcMethod("findGuideViewByImg")
    public RequestChannel<List<ImgDTO>> findGuideViewByImg(@Param("lat") Double lat, @Param("lon") Double lon, @Param("deviceType") Integer deviceType);
    /** 
    * html 闪屏 
    *  @param lat 
    *  @param lon 
    *  @return 
    *  @author focus, 2014-12-31 上午11:27:30 
    */  
    @RpcMethod("findFlashScreenByHtml")
    public RequestChannel<List<String>> findFlashScreenByHtml(@Param("lat") Double lat, @Param("lon") Double lon, @Param("deviceType") Integer deviceType);
    /** 
    * 图片 闪屏 
    *  @param lat 
    *  @param lon 
    *  @param deviceType 3 表示安卓， 4 表示苹果 
    *  @return 
    *  @author focus, 2014-12-31 上午11:27:42 
    */  
    @RpcMethod("findFlashScreenByImg")
    public RequestChannel<List<ImgDTO>> findFlashScreenByImg(@Param("lat") Double lat, @Param("lon") Double lon, @Param("deviceType") Integer deviceType);
    /** 
    * 根据所在城市等获取闪屏广告图片 
    *  @param lat 纬度 
    *  @param lon  经度 
    *  @param province  省名 
    *  @param city  城市名 
    *  @param district  区县名 
    *  @param street  街道名 
    *  @param deviceType  设备类型  3-android,4-ios 
    *  @return 
    *  @author liyuan, 2015-5-20 下午2:43:51 
    */  
    @RpcMethod("findFlashScreenByImgForCity")
    public RequestChannel<List<ImgDTO>> findFlashScreenByImgForCity(@Param("lat") Double lat, @Param("lon") Double lon, @Param("province") String province, @Param("city") String city, @Param("district") String district, @Param("street") String street, @Param("deviceType") Integer deviceType);


 } 

