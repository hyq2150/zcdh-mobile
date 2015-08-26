/** 
*  IRpcJobFairService 
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
* @author focus, 2014-6-18 上午11:29:02 
*/  
@RpcService("rpcJobFairService")
public interface IRpcJobFairService   { 
    /** 
    * 招聘会列表 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 上午11:47:13 
    */  
    @RpcMethod("findJobFairList")
    public RequestChannel<Page<JobFairListDTO>> findJobFairList(@Param("keyWord") String keyWord, @Param("areaCode") String areaCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询招聘会的详细 
    *  @param bannerId 
    *  @return 
    *  @author focus, 2014-6-18 上午11:46:54 
    */  
    @RpcMethod("findJobFairDetailByBannerId")
    public RequestChannel<JobFairDetailDTO> findJobFairDetailByBannerId(@Param("bannerId") Long bannerId);
    /** 
    * 查找招聘会的企业  
    *  @param bannerId 
    *  @param keyWord 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 上午11:47:21 
    */  
    @RpcMethod("findJobFairEnt")
    public RequestChannel<Page<JobFairEnt>> findJobFairEnt(@Param("bannerId") Long bannerId, @Param("keyWord") String keyWord, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找招聘会对应的岗位 
    *  @param bannerId 
    *  @param keyWord 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 上午11:47:46 
    */  
    @RpcMethod("findJobFairPost")
    public RequestChannel<Page<JobFairPost>> findJobFairPost(@Param("bannerId") Long bannerId, @Param("keyWord") String keyWord, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 是否已经报名招聘会 
    *  @param userId 
    *  @param bannerId 
    *  @return				1 已经报名，0 没有报名 
    *  @author focus, 2014-6-19 下午8:49:56 
    */  
    @RpcMethod("isJoinInJobFair")
    public RequestChannel<Integer> isJoinInJobFair(@Param("userId") Long userId, @Param("bannerId") Long bannerId);
    /** 
    * 报名招聘会 
    *  @param bannerId 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-18 下午4:58:20 
    */  
    @RpcMethod("joinInFair")
    public RequestChannel<Integer> joinInFair(@Param("bannerId") Long bannerId, @Param("userId") Long userId);
    /** 
    * 分享招聘会 
    *  @param userId 
    *  @param bannerId 
    *  @return 
    *  @author focus, 2014-6-18 下午4:59:14 
    */  
    @RpcMethod("shareFair")
    public RequestChannel<Integer> shareFair(@Param("bannerId") Long bannerId, @Param("userId") Long userId);
    /** 
    * 查询招聘会分享的内容 
    *  @param bannerId 
    *  @return 
    *  @author hw, 2014-7-18 下午6:01:56 
    */  
    @RpcMethod("findFairShareContent")
    public RequestChannel<List<JobEntShareDTO>> findFairShareContent(@Param("bannerId") Long bannerId);
    /** 
    * 查找行业类别 
    *  @param bannerId		 
    *  @return 
    *  @author focus, 2015-2-9 下午2:52:03 
    */  
    @RpcMethod("findEntIndustryType")
    public RequestChannel<List<EntIndustryTypeDTO>> findEntIndustryType(@Param("bannerId") Long bannerId);
    /** 
    * 查找岗位类别 
    *  @param bannerId 
    *  @return 
    *  @author focus, 2015-2-9 下午2:53:37 
    */  
    @RpcMethod("findEntPostType")
    public RequestChannel<List<EntPostTypeDTO>> findEntPostType(@Param("bannerId") Long bannerId);
    /** 
    * 查找招聘会的企业  
    *  @param bannerId 
    *  @param keyWord 
    *  @param IndustryCode 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 上午11:47:21 
    */  
    @RpcMethod("findJobFairEntByIndustryCode")
    public RequestChannel<Page<JobFairEnt>> findJobFairEntByIndustryCode(@Param("bannerId") Long bannerId, @Param("keyWord") String keyWord, @Param("IndustryCode") String IndustryCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 根据岗位类别查找招聘会对应的岗位 
    *  @param bannerId 
    *  @param keyWord 
    *  @param postTypeCode 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-18 上午11:47:46 
    */  
    @RpcMethod("findJobFairPostByPostTypeCode")
    public RequestChannel<Page<JobFairPost>> findJobFairPostByPostTypeCode(@Param("bannerId") Long bannerId, @Param("keyWord") String keyWord, @Param("postTypeCode") String postTypeCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 招聘会列表扩展 
    *  @param keyWord 
    *  @param areaCode 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author liyuan, 2015-5-5 下午6:05:37 
    */  
    @RpcMethod("findJobFairExtList")
    public RequestChannel<Page<JobFairExtListDTO>> findJobFairExtList(@Param("keyWord") String keyWord, @Param("areaCode") String areaCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 获取我的招聘会列表 
    *  @param keyWord 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author liyuan, 2015-7-2 下午3:33:07 
    */  
    @RpcMethod("findMyJobFairExtList")
    public RequestChannel<Page<JobFairExtListDTO>> findMyJobFairExtList(@Param("keyWord") String keyWord, @Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 获取招聘会详情信息 
    *  @param fairId 
    *  @return 
    *  @author liyuan, 2015-7-2 下午6:47:16 
    */  
    @RpcMethod("findJobFairDetailExtByFairId")
    public RequestChannel<JobFairDetailExtDTO> findJobFairDetailExtByFairId(@Param("fairId") Long fairId, @Param("userId") Long userId);
    /** 
    * 根据条件获取招聘会参会企业 
    *  @param fairId 
    *  @param keyWord 
    *  @param industryCode 行业代码 
    *  @param isSearchOffline  是否选择了查询现场企业 1-是,0-否 
    *  @param isSearchOnline   是否选择了查询线上企业 1-是,0-否 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author liyuan, 2015-7-5 下午12:04:44 
    */  
    @RpcMethod("findJobFairEntBySearchType")
    public RequestChannel<Page<JobFairEnt>> findJobFairEntBySearchType(@Param("fairId") Long fairId, @Param("keyWord") String keyWord, @Param("industryCode") String industryCode, @Param("isSearchOffline") Integer isSearchOffline, @Param("isSearchOnline") Integer isSearchOnline, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 根据条件获取招聘会参会职位 
    *  @param fairId 
    *  @param keyWord 
    *  @param industryCode 行业代码 
    *  @param isSearchOffline  是否选择了查询现场职位 1-是,0-否 
    *  @param isSearchOnline   是否选择了查询线上职位 1-是,0-否 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author liyuan, 2015-7-5 下午12:11:01 
    */  
    @RpcMethod("findJobFairPostBySearchType")
    public RequestChannel<Page<JobFairPost>> findJobFairPostBySearchType(@Param("fairId") Long fairId, @Param("keyWord") String keyWord, @Param("industryCode") String industryCode, @Param("isSearchOffline") Integer isSearchOffline, @Param("isSearchOnline") Integer isSearchOnline, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询招聘会报名人数 
    *  @param fairId 
    *  @return 
    *  @author liyuan, 2015-7-6 上午11:17:35 
    */  
    @RpcMethod("findFairUserSignUpAndSchoolCountByFairId")
    public RequestChannel<Long> findFairUserSignUpAndSchoolCountByFairId(@Param("fairId") Long fairId);
    /** 
    * 查询找招聘会报名人员(包含名称，学校) 
    *  @param fairId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author liyuan, 2015-7-6 上午11:34:59 
    */  
    @RpcMethod("findFairUserSignUpAndSchoolListByFairId")
    public RequestChannel<Page<UserSignUpDTO>> findFairUserSignUpAndSchoolListByFairId(@Param("fairId") Long fairId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 判断用户是否已签到招聘会 
    *  @param fairId 
    *  @param userId 
    *  @return 
    *  @author liyuan, 2015-7-7 下午2:56:23 
    */  
    @RpcMethod("isUserSigninJobfair")
    public RequestChannel<Integer> isUserSigninJobfair(@Param("fairId") Long fairId, @Param("userId") Long userId);


 } 

