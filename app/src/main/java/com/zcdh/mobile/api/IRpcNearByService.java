/** 
*  IRpcNearByService 
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
* 附近接口 
*  @author focus, 2014-6-11 下午2:18:30 
*/  
@RpcService("rpcNearByService")
public interface IRpcNearByService   { 
    /** 
    * 是否是系统更新 
    *  @param versionCode 版本号 
    *  @return      0 为需要更新系统，27 系统更新错误，28 您的系统已经是最新的系统了 
    *  @author focus, 2014-6-14 上午9:54:10 
    */  
    @RpcMethod("isSystemUpdate")
    public RequestChannel<Integer> isSystemUpdate(@Param("versionCode") Integer versionCode, @Param("pushUserId") String pushUserId);
    /** 
    * 查找左侧的侧边栏 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-11 下午3:10:37 
    */  
    @RpcMethod("findLeftSidebarByUserId")
    public RequestChannel<SidebarDTO> findLeftSidebarByUserId(@Param("userId") Long userId);
    /** 
    * 用户签到  
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-11 下午9:45:31 
    */  
    @RpcMethod("userSignIn")
    public RequestChannel<Integer> userSignIn(@Param("userId") Long userId);
    /** 
    * 通过用户id 查找消息 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-11 下午3:41:44 
    */  
    @RpcMethod("findInformationByUserId")
    public RequestChannel<Page<InformationDTO>> findInformationByUserId(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 通过id 子消息列表查询消息 
    *  @param id 
    *  @return 
    *  @author focus, 2014-6-14 上午9:00:13 
    */  
    @RpcMethod("findSubInformationListById")
    public RequestChannel<Page<InformationDTO>> findSubInformationListById(@Param("id") Long id, @Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 阅读某一条消息 
    *  @param id 
    *  @return 
    *  @author focus, 2014-6-11 下午7:58:59 
    */  
    @RpcMethod("readInformation")
    public RequestChannel<Integer> readInformation(@Param("id") Long id, @Param("userId") Long userId);
    /** 
    * 点击附近进入地图,旧版（没有区分兼职等） 
    *  @param lbs 
    *  @param isMaxScale  1 地图已经是放大到最大了，0 还可以放大 
    *  @return 
    *  @author focus, 2014-6-11 下午5:25:58 
    */  
    @RpcMethod("enterIntoMapFromNearBy")
    public RequestChannel<PointDTO> enterIntoMapFromNearBy(@Param("userId") Long userId, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 点击附近进入地图,新版 
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
    public RequestChannel<PointDTO> findPointByKeyWordOrTag(@Param("userId") Long userId, @Param("keyWord") String keyWord, @Param("tagCode") String tagCode, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 语音搜索 
    *  @param userId 用户id 
    *  @param sayContent 说的内容 
    *  @return 
    *  @author focus, 2014-6-11 下午7:54:22 
    */  
    @RpcMethod("findPointByVoice")
    public RequestChannel<PointDTO> findPointByVoice(@Param("userId") Long userId, @Param("sayContent") String sayContent, @Param("lbs") List<LbsParam> lbs, @Param("isMaxScale") Integer isMaxScale);
    /** 
    * 更新 地图上显示点的类型，  
    *  @param userId			用户id 
    *  @param showPointType		1 只显示岗位的点 ，2 显示岗位的点和 聘， 3 显示岗位的点和 我的简历的点， 4 全部显示 
    *  @return					0 成功 
    *  @author focus, 2014-6-18 下午3:36:01 
    */  
    @RpcMethod("updateShowPointTypeInMap")
    public RequestChannel<Integer> updateShowPointTypeInMap(@Param("userId") Long userId, @Param("showPointType") Integer showPointType);
    /** 
    * 查找地图上显示的类型 
    *  @param userId 
    *  @return			1 只显示岗位的点 ，2 显示岗位的点和 聘， 3 显示岗位的点和 我的简历的点， 4 全部显示 
    *  @author focus, 2014-6-20 下午4:50:03 
    */  
    @RpcMethod("findShowPointTypeInMap")
    public RequestChannel<Integer> findShowPointTypeInMap(@Param("userId") Long userId);
    /** 
    * 删 除投放简历的点 
    *  @param id 
    *  @return	    0 成功 
    *  @author focus, 2014-6-18 下午3:57:00 
    */  
    @RpcMethod("deleteMyResumePointInMap")
    public RequestChannel<Integer> deleteMyResumePointInMap(@Param("id") Long id);
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
    public RequestChannel<Page<JobEntPostDTO>> findSearchPostDTOByEntName(@Param("userId") Long userId, @Param("entName") String entName, @Param("lon") Double lon, @Param("lat") Double lat, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 从列表到地图： 筛选 
    *  @param search 
    *  @return 
    *  @author focus, 2014-6-23 下午5:04:27 
    */  
    @RpcMethod("fromSearchListToMapByAdvanceSearch")
    public RequestChannel<ToMapPointDTO> fromSearchListToMapByAdvanceSearch(@Param("search") SearchConditionDTO search);
    /** 
    * 查找标签类型 
    *  @return 
    *  @author focus, 2014-4-7 上午10:26:37 
    */  
    @RpcMethod("findJobTagTypeDTO")
    public RequestChannel<List<JobTagTypeDTO>> findJobTagTypeDTO();
    /** 
    * 分页查询标签 
    *  @param tagType		标签类型 001:常用，002：应届毕业生，003：人力 
    *  @param currentPge	当前页 
    *  @param pageSize		一页的元素的数量 
    *  @return 
    *  @author focus, 2014-4-7 上午10:14:54 
    */  
    @RpcMethod("findJobSearchTagDTOByPage")
    public RequestChannel<Page<JobSearchTagDTO>> findJobSearchTagDTOByPage(@Param("tagType") String tagType, @Param("currentPge") Integer currentPge, @Param("pageSize") Integer pageSize);
    /** 
    * 查询岗位跟踪 
    *  @param postId 
    *  @return 
    *  @author focus, 2014-6-17 上午9:10:57 
    */  
    @RpcMethod("findTrackPostDetail")
    public RequestChannel<TrackPostDetailDTO> findTrackPostDetail(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 热门城市 
    *  @return 
    *  @author focus, 2014-6-18 下午2:47:50 
    */  
    @RpcMethod("findHotCity")
    public RequestChannel<List<HotCityDTO>> findHotCity();
    /** 
    * 更多的工具或者应用 
    *  @param userId 
    *  @param currentPge 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 下午2:47:59 
    */  
    @RpcMethod("findMoreTools")
    public RequestChannel<Page<MoreToolsDTO>> findMoreTools(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找所有的工具扩展 
    *  @param currentPge 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-7-21 下午6:19:55 
    */  
    @RpcMethod("findAllTools")
    public RequestChannel<Page<MoreToolsDTO>> findAllTools(@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 更新工具 
    *  @param ids 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-7-21 下午6:20:58 
    */  
    @RpcMethod("updateTools")
    public RequestChannel<Integer> updateTools(@Param("ids") List<String> ids, @Param("userId") Long userId);


 } 

