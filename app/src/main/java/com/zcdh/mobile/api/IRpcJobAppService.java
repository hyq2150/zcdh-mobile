/** 
*  IRpcJobAppService 
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
* @author focus, 2014-6-5 下午10:04:06 
*/  
@RpcService("rpcJobAppService")
public interface IRpcJobAppService   { 
    /** 
    * 查询封面的图片 
    *  @return 
    *  @author focus, 2014-6-5 下午10:32:28 
    */  
    @RpcMethod("findInformationCoverList")
    public RequestChannel<List<InformationCoverDTO>> findInformationCoverList();
    /** 
    * 资讯标题列表 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-5 下午10:33:30 
    */  
    @RpcMethod("findInformationTitleList")
    public RequestChannel<Page<InformationTitleDTO>> findInformationTitleList(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询所有的标题 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-5 下午10:40:15 
    */  
    @RpcMethod("findAllInformationTitleList")
    public RequestChannel<Page<InformationTitleDTO>> findAllInformationTitleList(@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询标题的详细列表： 按天数分页查询 
    *  @param id 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-6-5 下午10:40:23 
    */  
    @RpcMethod("findInformationTitleInfoList")
    public RequestChannel<Page<InformationTitleInfoDTO>> findInformationTitleInfoList(@Param("id") Long id, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 通过关键字搜索查询标题的详细列表：按数量分页查询 
    *  @param keyWord 
    *  @param id 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-10-28 下午1:38:55 
    */  
    @RpcMethod("seachSubInformationListByKeyWord")
    public RequestChannel<Page<InformationTitleInfoDTO>> seachSubInformationListByKeyWord(@Param("keyWord") String keyWord, @Param("InformationTitleInfoId") Long InformationTitleInfoId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询标题的详细列表：按数量分页查询 
    *  @param id 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-10-20 下午5:10:01 
    */  
    @RpcMethod("findSubInformationListByAmount")
    public RequestChannel<Page<InformationTitleInfoDTO>> findSubInformationListByAmount(@Param("id") Long id, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 添加资讯 
    *  @param code 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-5 下午10:38:10 
    */  
    @RpcMethod("updateInformationTitle")
    public RequestChannel<Integer> updateInformationTitle(@Param("id") List<String> id, @Param("userId") Long userId);
    /** 
    * 添加访问次数 
    *  @param id 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-9 上午11:04:25 
    */  
    @RpcMethod("addInformationAccessTimesDetail")
    public RequestChannel<Integer> addInformationAccessTimesDetail(@Param("id") Long id, @Param("userId") Long userId);
    /** 
    * 报名 
    *  @param InformationTitleInfoId		对应应用的明细的条目的id 
    *  @param userId						用户id 
    *  @param signType						001,资讯信息报名,002,广告图片报名 
    *  @return			0 成功，-1 失败，49 用户信息不完整，47 已经报名，50 用户没有登陆 
    *  @author focus, 2014-10-28 下午2:06:01 
    */  
    @RpcMethod("signUp")
    public RequestChannel<Integer> signUp(@Param("InformationTitleInfoId") Long InformationTitleInfoId, @Param("userId") Long userId, @Param("signType") String signType);
    /** 
    * 查询分享的url 
    *  @param userId 
    *  @return 
    *  @author hw, 2014-7-18 下午6:01:56 
    */  
    @RpcMethod("findUrlShare")
    public RequestChannel<List<JobEntShareDTO>> findUrlShare(@Param("InformationTitleInfoId") Long InformationTitleInfoId, @Param("userId") Long userId);
    /** 
    * 是否已经报名 
    *  @param userId	用户id 
    *  @return		    47 已经报名， 48 未报名 ，50 用户没有登陆 
    *  @author focus, 2014-10-29 上午9:51:03 
    */  
    @RpcMethod("isSignUp")
    public RequestChannel<Integer> isSignUp(@Param("InformationTitleInfoId") Long InformationTitleInfoId, @Param("userId") Long userId, @Param("signType") String signType);
    /** 
    * 添加广告访问日志 
    *  @param params 
    *  @return 
    *  @author liyuan, 2015-4-21 下午4:50:49 
    */  
    @RpcMethod("addCoverVisitLog")
    public RequestChannel<Integer> addCoverVisitLog(@Param("params") Map<String, String> params);
    /** 
    * 是否对某手机使用邀请码进行过安装或注册 
    *  @param operation_type 操作类别:1-安装 2-注册 
    *  @param miei 手机的唯一串号 
    *  @return 未安装或注册过,返回0; 安装或注册过,返回邀请码 
    *  @author liyuan, 2015-6-29 下午5:02:54 
    */  
    @RpcMethod("isExistInvitationcodeUse")
    public RequestChannel<String> isExistInvitationcodeUse(@Param("operation_type") String operation_type, @Param("miei") String miei);
    /** 
    * 添加对某手机使用邀请码进行安装或注册的数据记录 
    *  @param invitation_code 邀请码 
    *  @param operation_type 操作类别:1-安装 2-注册 
    *  @param user_id 用户id 
    *  @param account 用户账户 
    *  @param account_type 账号类型：1-手机 2-第三方账号 
    *  @param miei 手机的唯一串号 
    *  @return 成功,返回邀请码; 失败,返回0 
    *  @author liyuan, 2015-6-29 下午5:12:19 
    */  
    @RpcMethod("addInvitationcodeUse")
    public RequestChannel<String> addInvitationcodeUse(@Param("invitation_code") String invitation_code, @Param("operation_type") String operation_type, @Param("user_id") Long user_id, @Param("account") String account, @Param("account_type") String account_type, @Param("miei") String miei);
    /** 
    * 判断邀请码是否存在 
    *  @param invitation_code 邀请码 
    *  @return 存在,返回邀请码; 不存在,返回0 
    *  @author liyuan, 2015-6-30 上午11:06:52 
    */  
    @RpcMethod("isExistInvitationcodeInfo")
    public RequestChannel<String> isExistInvitationcodeInfo(@Param("invitation_code") String invitation_code);
    /** 
    * 是否使用app邀请码推广功能 
    *  @return 1-是, 0-否 
    *  @author liyuan, 2015-6-30 下午6:12:26 
    */  
    @RpcMethod("isUseInvitationcode")
    public RequestChannel<Integer> isUseInvitationcode();
    /** 
    * 根据用户id获取推广人员的邀请码 
    *  @param user_id 
    *  @return 存在,返回邀请码; 不存在,返回0 
    *  @author liyuan, 2015-6-30 下午6:32:55 
    */  
    @RpcMethod("findInvitationcodeByUserId")
    public RequestChannel<String> findInvitationcodeByUserId(@Param("user_id") Long user_id);
    /** 
    * 邀请码处理 
    *  @param invitation_code 
    *  @param user_id 
    *  @param account 
    *  @param miei 
    *  @return 
    *  @author liyuan, 2015-7-1 上午11:25:10 
    */  
    @RpcMethod("dealInvitationcode")
    public RequestChannel<Map<String,String>> dealInvitationcode(@Param("invitation_code") String invitation_code, @Param("user_id") Long user_id, @Param("account") String account, @Param("miei") String miei);


 } 

