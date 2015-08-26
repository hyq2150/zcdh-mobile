/** 
*  IRpcSearchInNearByService 
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
* 地图附近的搜索 
*  @author focus, 2014-10-31 上午10:58:40 
*/  
@RpcService("rpcSearchInNearByService")
public interface IRpcSearchInNearByService   { 
    /** 
    * 点击附近进入地图 
    *  @param lbs 
    *  @param isMaxScale  1 地图已经是放大到最大了，0 还可以放大 
    *  @return 
    *  @author focus, 2014-6-11 下午5:25:58 
    */  
    @RpcMethod("enterIntoMapFromNearBy")
    public RequestChannel<PointDTO> enterIntoMapFromNearBy(@Param("userId") Long userId, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 点击附近进入地图 
    *  @param lbs 
    *  @param isMaxScale  1 地图已经是放大到最大了，0 还可以放大 
    *  @return 
    *  @author focus, 2014-6-11 下午5:25:58 
    */  
    @RpcMethod("enterIntoMapFromNearByNew")
    public RequestChannel<PointDTO> enterIntoMapFromNearByNew(@Param("userId") Long userId, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale, @Param("postPropertyCode") String postPropertyCode);
    /** 
    * 在地图中移动的时候，查询岗位的标点列表 
    *  @param search	搜索条件 
    *  @param lsb		屏幕的四个点 
    *  @param userId	用户id 
    *  @return 
    *  @author focus, 2014-6-11 下午5:40:22 
    */  
    @RpcMethod("moveInMap")
    public RequestChannel<PointDTO> moveInMap(@Param("userId") Long userId, @Param("search") SearchConditionDTO search, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 通过标签和关键字，搜索地图上的标点 
    *  @param keyWord	关键字 
    *  @param tagCode	标签编码 
    *  @param lbs		经纬度 
    *  @return 
    *  @author focus, 2014-6-11 下午5:43:17 
    */  
    @RpcMethod("findPointByKeyWordOrTag")
    public RequestChannel<PointDTO> findPointByKeyWordOrTag(@Param("userId") Long userId, @Param("keyWord") String keyWord, @Param("tagCode") String tagCode, @Param("postPropertyCode") String postPropertyCode, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 语音搜索 
    *  @param userId 用户id 
    *  @param sayContent 说的内容 
    *  @return 
    *  @author focus, 2014-6-11 下午7:54:22 
    */  
    @RpcMethod("findPointByVoice")
    public RequestChannel<PointDTO> findPointByVoice(@Param("userId") Long userId, @Param("sayContent") String sayContent, @Param("postPropertyCode") String postPropertyCode, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 从地图页面到搜索列表的接口 
    *  @param search 
    *  @param lbs 
    *  @return 
    *  @author focus, 2014-6-11 下午7:26:53 
    */  
    @RpcMethod("fromMapToAdvancedSearchList")
    public RequestChannel<Page<JobEntPostDTO>> fromMapToAdvancedSearchList(@Param("userId") Long userId, @Param("search") SearchConditionDTO search, @Param("lbs") List<LbsParam> lbs, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 筛选 搜索结果列表 
    *  @param userId 
    *  @param search 
    *  @param lbs 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-7-21 上午9:56:13 
    */  
    @RpcMethod("filtrateResultInSearchList")
    public RequestChannel<Page<JobEntPostDTO>> filtrateResultInSearchList(@Param("userId") Long userId, @Param("search") SearchConditionDTO search, @Param("lbs") List<LbsParam> lbs, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 通过岗位id查询查询岗位列表 
    *  @param postIds 岗位id 当且仅仅当 重合的岗位的坐标小于8个点的时候查询， 
    *  @return 
    *  @author focus, 2014-6-20 下午4:25:22 
    */  
    @RpcMethod("findSearchPostDTOByPostIds")
    public RequestChannel<Page<JobEntPostDTO>> findSearchPostDTOByPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 高级搜索，搜索岗位列表 
    *  @param search 
    *  @return 
    *  @author focus, 2014-6-11 下午5:46:17 
    */  
    @RpcMethod("findSearchPostDTOByAdvancedSearch")
    public RequestChannel<Page<JobEntPostDTO>> findSearchPostDTOByAdvancedSearch(@Param("userId") Long userId, @Param("search") SearchConditionDTO search, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 通过企业名称搜索！ 
    *  @param entName	企业名称 
    *  @param lon 
    *  @param lat 
    *  @return 
    *  @author focus, 2014-9-28 下午2:48:04 
    */  
    @RpcMethod("findSearchPostDTOByEntName")
    public RequestChannel<Page<JobEntPostDTO>> findSearchPostDTOByEntName(@Param("userId") Long userId, @Param("entName") String entName, @Param("postPropertyCode") String postPropertyCode, @Param("lon") Double lon, @Param("lat") Double lat, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);


 } 

