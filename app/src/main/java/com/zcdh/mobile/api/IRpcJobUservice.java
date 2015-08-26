/** 
*  IRpcJobUservice 
* 
*  Created Date: 2015-07-09 21:21:00 
*  
*/  
package com.zcdh.mobile.api;  
import java.util.*; 

import com.zcdh.core.annotation.*; 
import com.zcdh.mobile.api.model.*; 
import com.zcdh.comm.entity.*; 
import com.zcdh.mobile.framework.nio.RequestChannel;
/** 
* 用户接口的约定，<br>  
*   
*  <blockquote> 
*  <pre> 
*  1.关于方法名称<br> 
*  	1.1 与实体相结合:包括增删改查<br> 
*  2.实体DTO和变量名称<br> 
*  	2.1 驼峰方式命名，尽量和数据中变量及其实体保持一致<br> 
*  3.返回值<br> 
*   3.1 更新的一常量为准<br> 
*    
*  </pre> 
*  </blockquote> 
*   
*  @author focus, 2014-4-4 下午3:14:46 
*/  
@RpcService("rpcJobUservice")
public interface IRpcJobUservice   { 
    /** 
    * 注册 
    *  @param mobileNum 
    *  @param pwd 
    *  @param email 
    *  @return		2 手机号码已经存在，3 email已经存在 ，0 注册成功 
    *  @author focus, 2014-3-28 下午6:27:21 
    */  
    @RpcMethod("register")
    public RequestChannel<Integer> register(@Param("mobileNum") String mobileNum, @Param("pwd") String pwd, @Param("email") String email);
    /** 
    * 通过QQ无绑定账号直接注册 
    *  @param openId 
    *  @return 0：注册成功，9：该QQ号已经绑定 
    *  @author danny, 2014-9-11 下午3:54:57 
    */  
    @RpcMethod("registerByQQ")
    public RequestChannel<Integer> registerByQQ(@Param("openId") String openId, @Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);
    /** 
    * 通过新浪微博无绑定账号直接注册 
    *  @param weiBoUserId 微博用户id 
    *  @return 0：注册成功，10：该微博号已经绑定 
    *  @author danny, 2014-9-11 下午3:54:57 
    */  
    @RpcMethod("registerByWeibo")
    public RequestChannel<Integer> registerByWeibo(@Param("weiBoUserId") String weiBoUserId, @Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);
    /** 
    * 登陆 
    *  @param mobileNum 
    *  @param pwd 
    *  @return		0 登陆成功，4  账号不存在，5 密码错误，7 账号已经停用，  
    *  @author focus, 2014-3-28 下午6:27:45 
    */  
    @RpcMethod("login")
    public RequestChannel<Integer> login(@Param("account") String account, @Param("pwd") String pwd);
    /** 
    * 无需userId 登陆的  
    *  @return 
    *  @author focus, 2014-6-21 上午10:57:59 
    */  
    @RpcMethod("loginByNoUserId")
    public RequestChannel<Long> loginByNoUserId();
    /** 
    * 使用 QQ 登陆 
    *  @param openId 
    *  @return   0 登陆成功 ,6 QQ第一次登陆，7 账号已经停用，  
    *  @author focus, 2014-4-2 下午11:09:16 
    */  
    @RpcMethod("loginByQQ")
    public RequestChannel<Integer> loginByQQ(@Param("openId") String openId);
    /** 
    * 使用微博登陆：  
    *  @param weiBoUserId  
    *  @return  	 0 登陆成功 ,6 WeiBo第一次登陆，7 账号已经停用，  
    *  @author focus, 2014-4-2 下午11:09:30 
    */  
    @RpcMethod("loginByWeiBo")
    public RequestChannel<Integer> loginByWeiBo(@Param("weiBoUserId") String weiBoUserId);
    /** 
    * 通过QQ 获取userId 
    *  @return   
    *  @author focus, 2014-4-3 上午10:17:57 
    */  
    @RpcMethod("findUserIdByQQ")
    public RequestChannel<Long> findUserIdByQQ(@Param("openId") String openId);
    /** 
    * 通过WeiBo 获取userId 
    *  @return 
    *  @author focus, 2014-4-3 上午10:18:02 
    */  
    @RpcMethod("findUserIdByWeiBo")
    public RequestChannel<Long> findUserIdByWeiBo(@Param("weiBouserId") String weiBouserId);
    /** 
    * 查找用户信息 
    *  @param account 
    *  @return	     	返回用户的 user_id 
    *  @author focus, 2014-3-28 下午6:44:56 
    */  
    @RpcMethod("findUserId")
    public RequestChannel<Long> findUserId(@Param("account") String account);
    /** 
    * 更新密码 
    *  @param account 
    *  @param pwd 
    *  @return			0 成功  ，  5 密码错误  
    *  @author focus, 2014-3-28 下午6:47:23 
    */  
    @RpcMethod("updatePwd")
    public RequestChannel<Integer> updatePwd(@Param("userId") Long userId, @Param("newPwd") String newPwd, @Param("oldPwd") String oldPwd);
    /** 
    * 绑定新的账号 
    *  @param account 
    *  @param loginType	        登陆类型， QQ weiBo 如常量表 
    *  @param warrantyId	授权id  QQ的为 openId  weiBo 的为weiBoUserId 
    *  @return				0 成功， 8 账号已经存在 
    *  @author focus, 2014-4-3 下午2:00:01 
    */  
    @RpcMethod("bindNewAccount")
    public RequestChannel<Integer> bindNewAccount(@Param("account") String account, @Param("email") String email, @Param("pwd") String pwd, @Param("loginType") String loginType, @Param("warrantyId") String warrantyId);
    /** 
    * 绑定已经存在的账号 
    *  @param account 
    *  @param pwd		     
    *  @param loginType		登陆类型， QQ weiBo 如常量表 
    *  @param warrantyId	授权id  QQ的为 openId  weiBo 的为weiBoUserId 
    *  @return				0 成功， 9 QQ 已经绑定账号，10 微博已经绑定， 11 账号已经被绑定 
    *  @author focus, 2014-4-3 下午2:01:03 
    */  
    @RpcMethod("bindExistsAccount")
    public RequestChannel<Integer> bindExistsAccount(@Param("account") String account, @Param("pwd") String pwd, @Param("loginType") String loginType, @Param("warrantyId") String warrantyId, @Param("img") ImgAttachDTO img);
    /** 
    * 取消绑定 
    *  @param account	账号 
    *  @param pwd		密码  
    *  @param bindType	绑定类型   QQ weiBo 如常量表 
    *  @param warrantyId授权id  QQ的为 openId  weiBo 的为weiBoUserId 
    *  @return			-1 失败，0 成功  
    *  @author focus, 2014-4-3 下午8:45:20 
    */  
    @RpcMethod("cancleAccountBind")
    public RequestChannel<Integer> cancleAccountBind(@Param("account") String account, @Param("pwd") String pwd, @Param("bindType") String bindType, @Param("warrantyId") String warrantyId);
    /** 
    * 通过邮箱重置密码 
    *  基于Email的重置密码 
    *  @param account 账号 
    *  @return  -1 失败，0 成功  4  账号不存在 ， 12 用户不存在 ， 13 用户绑定邮箱不存在 
    *  @author focus, 2014-4-6 下午4:45:00 
    */  
    @RpcMethod("resetPwdByEmail")
    public RequestChannel<Integer> resetPwdByEmail(@Param("account") String account);
    /** 
    * 查询用户详细信息的接口 
    *  @param userId 
    *  @return			返回用户的基本资料 
    *  @author focus, 2014-4-12 上午9:23:32 
    */  
    @RpcMethod("findJobUserInfoDTO")
    public RequestChannel<JobUserInfoDTO> findJobUserInfoDTO(@Param("userId") Long userId);
    /** 
    * 更新手机端的用户信息 
    *  @param userInfo	用户资料 
    *  @return			0  成功 
    *  @author focus, 2014-4-11 下午2:58:16 
    */  
    @RpcMethod("updateJobUserInfoDTO")
    public RequestChannel<Integer> updateJobUserInfoDTO(@Param("userInfo") JobUserInfoDTO userInfo);
    /** 
    * 查询工作经历列表 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-4-15 下午7:15:21 
    */  
    @RpcMethod("findJobWorkExperienceDTOByUserId")
    public RequestChannel<List<JobWorkExperienceDTO>> findJobWorkExperienceDTOByUserId(@Param("userId") Long userId);
    /** 
    * 通过id 查找过工作经历岗位 
    *  @param wepPostId 
    *  @return 
    *  @author focus, 2014-4-15 下午8:29:27 
    */  
    @RpcMethod("findJobWorkExperiencePostDTO")
    public RequestChannel<JobWorkExperiencePostDTO> findJobWorkExperiencePostDTO(@Param("wepPostId") Long wepPostId);
    /** 
    * 添加 
    *  @param workExperience 
    *  @return 
    *  @author focus, 2014-4-15 下午7:17:57 
    */  
    @RpcMethod("addJobWorkExperience")
    public RequestChannel<Integer> addJobWorkExperience(@Param("workExperience") JobWorkExperiencePostDTO workExperience);
    /** 
    * 更新 
    *  @param workExperience 
    *  @return 
    *  @author focus, 2014-4-15 下午7:18:44 
    */  
    @RpcMethod("updateJobWorkExperience")
    public RequestChannel<Integer> updateJobWorkExperience(@Param("workExperience") JobWorkExperiencePostDTO workExperience);
    /** 
    * 删除 
    *  @param wepPosId 
    *  @return 
    *  @author focus, 2014-4-15 下午8:45:34 
    */  
    @RpcMethod("removeJobWorkExperiencePost")
    public RequestChannel<Integer> removeJobWorkExperiencePost(@Param("wepPostId") Long wepPostId);
    /** 
    * 查询个人教育经历列表 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-4-15 下午8:47:50 
    */  
    @RpcMethod("findJobEducationByUserId")
    public RequestChannel<JobEducationExperienceDTO> findJobEducationByUserId(@Param("userId") Long userId);
    /** 
    * 查找教育经历的详细 
    *  @param eduId 
    *  @return 
    *  @author focus, 2014-4-15 下午8:48:30 
    */  
    @RpcMethod("findJobEducationByEduId")
    public RequestChannel<JobEducationDTO> findJobEducationByEduId(@Param("eduId") Long eduId);
    /** 
    * 通过id 查找培训经历 
    *  @param trainId 
    *  @return 
    *  @author focus, 2014-5-8 下午2:40:10 
    */  
    @RpcMethod("findTrainDTOByTrainId")
    public RequestChannel<JobTrainDTO> findTrainDTOByTrainId(@Param("trainId") Long trainId);
    /** 
    * 添加教育经历 
    *  @param educationDTO 
    *  @return 
    *  @author focus, 2014-4-15 下午8:50:01 
    */  
    @RpcMethod("addEducation")
    public RequestChannel<Integer> addEducation(@Param("educationDTO") JobEducationDTO educationDTO);
    /** 
    * 添加培训经历 
    *  @param train 
    *  @return 
    *  @author focus, 2014-5-8 下午2:38:44 
    */  
    @RpcMethod("addTrain")
    public RequestChannel<Integer> addTrain(@Param("train") JobTrainDTO train);
    /** 
    * 更新 
    *  @param educationDTO 
    *  @return 
    *  @author focus, 2014-4-15 下午8:50:37 
    */  
    @RpcMethod("updateEducation")
    public RequestChannel<Integer> updateEducation(@Param("educationDTO") JobEducationDTO educationDTO);
    /** 
    * 更新培训经历 
    *  @param train 
    *  @return 
    *  @author focus, 2014-5-8 下午2:37:08 
    */  
    @RpcMethod("updateTrain")
    public RequestChannel<Integer> updateTrain(@Param("train") JobTrainDTO train);
    /** 
    * 删除学校教育 
    *  @param eduId 
    *  @return 
    *  @author focus, 2014-5-13 下午10:05:31 
    */  
    @RpcMethod("removeEdu")
    public RequestChannel<Integer> removeEdu(@Param("eduId") Long eduId);
    /** 
    * 删除培训教育 
    *  @param trainId 
    *  @return 
    *  @author focus, 2014-5-13 下午10:05:43 
    */  
    @RpcMethod("removeTrainId")
    public RequestChannel<Integer> removeTrainId(@Param("trainId") Long trainId);
    /** 
    * 更新设置 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-4-18 下午5:50:53 
    */  
    @RpcMethod("updateSetting")
    public RequestChannel<Integer> updateSetting(@Param("settingDTO") JobSettingDTO settingDTO);
    /** 
    * 查找设置 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-4-18 下午5:50:46 
    */  
    @RpcMethod("findJobSettingByUserId")
    public RequestChannel<List<JobSettingDTO>> findJobSettingByUserId(@Param("userId") Long userId);
    /** 
    * 查找个人的求职申请 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-18 下午9:50:33 
    */  
    @RpcMethod("findJobApplyDTOByUserId")
    public RequestChannel<Page<JobApplyDTO>> findJobApplyDTOByUserId(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 求职申请的状态跟踪  
    *  @param userId 
    *  @return 
    *  @author focus, 2014-6-14 上午11:10:50 
    */  
    @RpcMethod("findJobTrackApplyByUserIdAndPostId")
    public RequestChannel<List<JobTrackApplyDTO>> findJobTrackApplyByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 查询个人求职意向 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-7 下午4:34:28 
    */  
    @RpcMethod("findUserObjectiveDTO")
    public RequestChannel<JobObjectiveDTO> findUserObjectiveDTO(@Param("userId") Long userId);
    /** 
    * 更新个人求职意向 
    *  @param objective 
    *  @return			 0 成功， 
    *  @author focus, 2014-5-7 下午4:38:42 
    */  
    @RpcMethod("updateObjectiveDTO")
    public RequestChannel<Integer> updateObjectiveDTO(@Param("objective") JobObjectiveDTO objective);
    /** 
    * 删除求职意向 
    *  @param objective 
    *  @return			0 成功 
    *  @author focus, 2014-5-12 上午10:00:28 
    */  
    @RpcMethod("removeUserObjective")
    public RequestChannel<Integer> removeUserObjective(@Param("objective") JobObjectiveDTO objective);
    /** 
    * 查询个人主页的DTO 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-14 下午4:29:29 
    */  
    @RpcMethod("findJobUserHomePageDTO")
    public RequestChannel<JobUserHomePageDTO> findJobUserHomePageDTO(@Param("userId") Long userId);
    /** 
    * 查找个人主页的头部 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-8 上午11:31:12 
    */  
    @RpcMethod("findJobUserHomePageTitleDTO")
    public RequestChannel<JobUserHomePageTitleDTO> findJobUserHomePageTitleDTO(@Param("userId") Long userId);
    /** 
    * 查找个人主页的中间部分的内容 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-14 下午3:49:45 
    */  
    @RpcMethod("findJobUserHomePageMiddleDTO")
    public RequestChannel<JobUserHomePageMiddleDTO> findJobUserHomePageMiddleDTO(@Param("userId") Long userId);
    /** 
    * 更新个人主页的头像 
    *  @param imgAttachDTO 图片附件 
    *  @return 
    *  @author focus, 2014-5-8 下午12:21:32 
    */  
    @RpcMethod("updateImgInUserHomePage")
    public RequestChannel<Integer> updateImgInUserHomePage(@Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);
    /** 
    * 签到 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-8 上午11:49:54 
    */  
    @RpcMethod("signIn")
    public RequestChannel<Integer> signIn(@Param("userId") Long userId);
    /** 
    * 个人简历 
    *  @param userId 
    *  @author focus, 2014-5-8 上午11:54:08 
    */  
    @RpcMethod("findJobUserResumeTitleDTO")
    public RequestChannel<JobUserResumeTitleDTO> findJobUserResumeTitleDTO(@Param("userId") Long userId);
    /** 
    * 查找当前页面中间的信息 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-14 下午3:56:02 
    */  
    @RpcMethod("findJobUserResumeMiddleDTO")
    public RequestChannel<JobUserResumeMiddleDTO> findJobUserResumeMiddleDTO(@Param("userId") Long userId);
    /** 
    * 查询个人简历的主页 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-14 下午4:31:51 
    */  
    @RpcMethod("findJobUserResumeHomePageDTO")
    public RequestChannel<JobUserResumeHomePageDTO> findJobUserResumeHomePageDTO(@Param("userId") Long userId);
    /** 
    * 更新个人简历中的头像 
    *  @param imgAttachDTO 
    *  @return 
    *  @author focus, 2014-5-8 下午12:23:09 
    */  
    @RpcMethod("updateImgInUserResume")
    public RequestChannel<Integer> updateImgInUserResume(@Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);
    /** 
    * 查询用户的自我评价 
    *  @param userId 
    *  @return 
    *  @author hw, 2014-5-12 下午4:30:14 
    */  
    @RpcMethod("findUserComment")
    public RequestChannel<UserCommentDTO> findUserComment(@Param("userId") Long userId);
    /** 
    * 查找系统自我评价标签 
    *  @return 
    *  @author focus, 2014-5-8 下午3:03:43 
    */  
    @RpcMethod("findBasicSysCommentTagDTO")
    public RequestChannel<Page<BasicSysCommentTagDTO>> findBasicSysCommentTagDTO(@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 添加自我评价 
    *  @param tagList		标签的列表 
    *  @param comment		用户填写的自我评价 
    *  @return 
    *  @author hw, 2014-5-12 下午4:09:42 
    */  
    @RpcMethod("addUserComment")
    public RequestChannel<Integer> addUserComment(@Param("userId") Long userId, @Param("tagList") List<CommentTagDTO> tagList, @Param("comment") String comment);
    /** 
    * 查找个人的黑名单 
    *  @param userId	 
    *  @return			个人黑名单列表 
    *  @author focus, 2014-5-8 下午3:10:31 
    */  
    @RpcMethod("findUserBlackListDTOByUserId")
    public RequestChannel<List<JobUserBlackListDTO>> findUserBlackListDTOByUserId(@Param("userId") Long userId);
    /** 
    * 移除用户黑名单 
    *  @param userId 
    *  @param entId 
    *  @return 
    *  @author hw, 2014-5-9 下午3:02:37 
    */  
    @RpcMethod("removeUserBlack")
    public RequestChannel<Integer> removeUserBlack(@Param("userId") Long userId, @Param("entId") Long entId);
    /** 
    * 查询意见反馈 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-5-8 下午7:02:52 
    */  
    @RpcMethod("findUserFeedBackDTOByUserId")
    public RequestChannel<Page<JobFeedBackListDTO>> findUserFeedBackDTOByUserId(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 添加意见反馈 
    *  @param userId	用户id 
    *  @param content	反馈内容 
    *  @return 
    *  @author focus, 2014-5-8 下午7:15:47 
    */  
    @RpcMethod("addUserFeedBack")
    public RequestChannel<Integer> addUserFeedBack(@Param("userId") Long userId, @Param("content") String content);
    /** 
    * 邮箱修改 
    *  @param userId	用户id 
    *  @param pwd		用户密码 
    *  @param newEmail	用户邮箱 
    *  @return 0:更新成功    18：密码错误   4：账号不存在 
    *  @author focus, 2014-5-8 下午7:23:47 
    */  
    @RpcMethod("updateUserEmail")
    public RequestChannel<Integer> updateUserEmail(@Param("userId") Long userId, @Param("pwd") String pwd, @Param("newEmail") String newEmail);
    /** 
    * 信息中心，查询面试邀请 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return Page<JobInterviewListDTO>  status:0 未读取， 1 已读 
    *  @author focus, 2014-5-8 下午7:42:48 
    */  
    @RpcMethod("findUserInterview")
    public RequestChannel<Page<JobInterviewListDTO>> findUserInterview(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 信息中心，查询面试邀请详情 
    *  @param interViewId 
    *  @return   
    *  @author focus, 2014-5-8 下午7:42:48 
    */  
    @RpcMethod("findUserInterviewDetail")
    public RequestChannel<JobInterviewDTO> findUserInterviewDetail(@Param("interViewId") Long interViewId);
    /** 
    * 信息中心，查询用户申请岗位列表 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author hw, 2014-5-13 下午3:15:00 
    */  
    @RpcMethod("findUserApply")
    public RequestChannel<Page<JobApplyListDTO>> findUserApply(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 信息中心，查询企业访问个人简历列表 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author hw, 2014-5-13 下午3:20:14 
    */  
    @RpcMethod("findEntAccess")
    public RequestChannel<Page<JobEntAccessListDTO>> findEntAccess(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询用户订阅列表 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author hw, 2014-5-14 下午2:26:10 
    */  
    @RpcMethod("findUserSubscriptionList")
    public RequestChannel<Page<JobUserSubscriptionDTO>> findUserSubscriptionList(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询用户订阅 
    *  @param subId 
    *  @return 
    *  @author hw, 2014-5-14 下午2:26:10 
    */  
    @RpcMethod("findUserSubscription")
    public RequestChannel<JobUserSubscriptionDTO> findUserSubscription(@Param("subId") Long subId);
    /** 
    * 添加用户订阅 
    *  @param jobUserSubscriptionDTO 
    *  @return 
    *  @author hw, 2014-5-14 下午2:47:41 
    */  
    @RpcMethod("addUserSubscription")
    public RequestChannel<Integer> addUserSubscription(@Param("jobUserSubscriptionDTO") JobUserSubscriptionDTO jobUserSubscriptionDTO);
    /** 
    * 更新用户订阅 
    *  @param jobUserSubscriptionDTO 
    *  @return 
    *  @author hw, 2014-5-14 下午2:47:52 
    */  
    @RpcMethod("updateUserSubscription")
    public RequestChannel<Integer> updateUserSubscription(@Param("jobUserSubscriptionDTO") JobUserSubscriptionDTO jobUserSubscriptionDTO);
    /** 
    * 删除用户订阅 
    *  @param subId 
    *  @return 
    *  @author hw, 2014-5-14 下午2:48:04 
    */  
    @RpcMethod("removeUserSubscription")
    public RequestChannel<Integer> removeUserSubscription(@Param("subId") Long subId);
    /** 
    * 查询订阅的岗位的列表 
    *  @param postCode 
    *  @param areaCode 
    *  @param industryCode 
    *  @return 
    *  @author focus, 2014-5-28 下午3:58:46 
    */  
    @RpcMethod("findUserSubcriptionPostList")
    public RequestChannel<Page<JobEntPostDTO>> findUserSubcriptionPostList(@Param("postCode") String postCode, @Param("areaCode") String areaCode, @Param("industryCode") String industryCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 通过关键字查找技能 
    *  @param keyWord	关键字：技能名称，岗位名称 
    *  @param technicalType 技能类型  001 一般要求  002 技术要求 003 素质要求 004 语言要求 
    *  @return 
    *  @author focus, 2014-5-21 上午9:34:14 
    */  
    @RpcMethod("findTechnicalBySearchKeyWord")
    public RequestChannel<Page<JobTechnicalDTO>> findTechnicalBySearchKeyWord(@Param("keyWord") String keyWord, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 根据个人的求职意向查询技能 
    *  @param userId	用户id 
    *  @return 
    *  @author focus, 2014-5-14 上午10:40:43 
    */  
    @RpcMethod("findJobTechnicalByObjective")
    public RequestChannel<Page<JobTechnicalDTO>> findJobTechnicalByObjective(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找用户的个人技能列表 
    *  @param userId	用户id 
    *  @return 
    *  @author focus, 2014-5-14 上午10:40:43 
    */  
    @RpcMethod("findJobTechnicalSelectedByUserId")
    public RequestChannel<Page<JobTechnicalDTO>> findJobTechnicalSelectedByUserId(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 更新技能 
    *  @param technical 
    *  @return 
    *  @author focus, 2014-5-14 上午11:58:31 
    */  
    @RpcMethod("updateJobTechnicalBatch")
    public RequestChannel<Integer> updateJobTechnicalBatch(@Param("technicals") List<JobTechnicalDTO> technicals, @Param("userId") Long userId);
    /** 
    * 添加技能  
    *  @param technologyCode 
    *  @param paramCode 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-22 下午7:41:48 
    */  
    @RpcMethod("addJobTechnical")
    public RequestChannel<Integer> addJobTechnical(@Param("technologyCode") String technologyCode, @Param("paramCode") String paramCode, @Param("userId") Long userId);
    /** 
    * 更新技能 
    *  @param technologyCode 
    *  @param paramCode 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-22 下午7:42:18 
    */  
    @RpcMethod("updateJobTechnical")
    public RequestChannel<Integer> updateJobTechnical(@Param("technologyCode") String technologyCode, @Param("paramCode") String paramCode, @Param("userId") Long userId);
    /** 
    * 删除个人技能 
    *  @param technicalId 
    *  @return 
    *  @author focus, 2014-5-14 上午11:58:39 
    */  
    @RpcMethod("removeJobTechnical")
    public RequestChannel<Integer> removeJobTechnical(@Param("technicalId") Long technicalId);
    /** 
    * 批量删除技能 
    *  @param ids 
    *  @return 
    *  @author focus, 2014-5-21 上午9:32:10 
    */  
    @RpcMethod("removeJobTechnicalBatch")
    public RequestChannel<Integer> removeJobTechnicalBatch(@Param("ids") List<Long> ids);
    /** 
    * 更新个人的求职状态 
    *  @param jobStatusCode	个人求职装的编码 
    *  @param userId		 用户的uesrId 
    *  @return 
    *  @author focus, 2014-5-15 下午3:24:08 
    */  
    @RpcMethod("updateJobStatus")
    public RequestChannel<Integer> updateJobStatus(@Param("jobStatusCode") String jobStatusCode, @Param("userId") Long userId);
    /** 
    * 查询个人的岗位收藏 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-19 上午10:32:42 
    */  
    @RpcMethod("findJobUserFavoritesByUserId")
    public RequestChannel<Page<JobUserFavoritesDTO>> findJobUserFavoritesByUserId(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找个人的语言 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-23 上午11:34:09 
    */  
    @RpcMethod("findJobLanguageDTOByUserId")
    public RequestChannel<List<JobLanguageDTO>> findJobLanguageDTOByUserId(@Param("userId") Long userId);
    /** 
    * 添加语言 
    *  @param language 
    *  @return 
    *  @author focus, 2014-5-23 上午11:39:29 
    */  
    @RpcMethod("addJobLanguage")
    public RequestChannel<Integer> addJobLanguage(@Param("language") JobLanguageDTO language);
    /** 
    * 删除语言 
    *  @param language 
    *  @return 
    *  @author focus, 2014-5-23 上午11:39:40 
    */  
    @RpcMethod("removeJobLanguage")
    public RequestChannel<Integer> removeJobLanguage(@Param("language") JobLanguageDTO language);
    /** 
    * 更新语言 
    *  @param language 
    *  @return 
    *  @author focus, 2014-5-23 上午11:39:51 
    */  
    @RpcMethod("updateJobLanguage")
    public RequestChannel<Integer> updateJobLanguage(@Param("language") JobLanguageDTO language);
    /** 
    * 查找所有的语言 
    *  @return 
    *  @author focus, 2014-5-23 下午4:24:55 
    */  
    @RpcMethod("findAllLanguage")
    public RequestChannel<Page<JobLanguageDTO>> findAllLanguage(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 检查用户信息 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-5-29 下午12:03:57 
    */  
    @RpcMethod("CheckUserRequisite")
    public RequestChannel<CheckUserRequisiteDTO> CheckUserRequisite(@Param("userId") Long userId);
    /** 
    * 是否是最新的系统 
    *  @param versionCode	版本号 
    *  @param deviceType	设备类型，取值范围为：1～5 
    * 	 							云推送支持多种设备，各种设备的类型编号如下： 
    * 	 							1：浏览器设备； 
    * 	 							2：PC设备； 
    * 	 							3：Andriod设备； 
    * 	 							4：iOS设备； 
    * 	 							5：Windows Phone设备； 
    *  @return				0 为需要更新系统，27 系统更新错误，28 您的系统已经是最新的系统了 
    *  @author focus, 2014-7-10 下午12:00:56 
    */  
    @RpcMethod("isTheLastSystem")
    public RequestChannel<Integer> isTheLastSystem(@Param("versionCode") Integer versionCode, @Param("deviceType") Integer deviceType);
    /** 
    * 添加错误日志 
    *  @param userId 
    *  @param modelNumber   手机型号 
    *  @param sysVersion	系统版本 
    *  @param imgAttachDTO 
    *  @return 
    *  @author focus, 2014-7-10 下午5:44:23 
    */  
    @RpcMethod("addErrorLog")
    public RequestChannel<Integer> addErrorLog(@Param("userId") Long userId, @Param("modelNumber") String modelNumber, @Param("sysVersion") String sysVersion, @Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);
    /** 
    * 查找用户的个人头像 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-7-17 下午4:59:45 
    */  
    @RpcMethod("findUserPortrait")
    public RequestChannel<JobUserPortraitDTO> findUserPortrait(@Param("userId") Long userId);
    /** 
    * 查询软件分享的内容 
    *  @param userId 
    *  @return 
    *  @author hw, 2014-7-18 下午6:01:56 
    */  
    @RpcMethod("findAppShareContent")
    public RequestChannel<List<JobEntShareDTO>> findAppShareContent(@Param("userId") Long userId);
    /** 
    * 预览简历 
    *  @param userId	用户的userId 
    *  @return 
    *  @author focus, 2014-8-8 上午10:08:05 
    */  
    @RpcMethod("previewResumeByUserId")
    public RequestChannel<String> previewResumeByUserId(@Param("userId") Long userId);
    /** 
    * 添加登陆的详细信息 
    *  @param userLogin 
    *  @return 
    *  @author focus, 2014-8-15 下午4:01:34 
    */  
    @RpcMethod("addLoginInfo")
    public RequestChannel<Integer> addLoginInfo(@Param("userLogin") JobUserLoginInfo userLogin);
    /** 
    * 发送手机验证码 
    *  @param mobilePhoneNo 手机号码 
    *  @return 0，表示发送成功，-1，发送失败，2 手机号码已经存在 
    *  @author danny, 2014-9-11 下午3:23:37 
    */  
    @RpcMethod("sendSMSVerificationCodeInRegister")
    public RequestChannel<Integer> sendSMSVerificationCodeInRegister(@Param("mobilePhoneNo") String mobilePhoneNo);
    /** 
    * 验证手机验证码是否正确 
    *  @param mobilePhoneNo 手机号码 
    *  @param verificationCode 通过短信获取的验证码 
    *  @return 0 表示验证成功，33，表示手机号码不存在，34：手机短信验证码验证错误 ， 35 验证码过期 
    *  @author danny, 2014-9-11 下午3:42:17 
    */  
    @RpcMethod("validSMSVerificationCodeInRegister")
    public RequestChannel<Integer> validSMSVerificationCodeInRegister(@Param("mobilePhoneNo") String mobilePhoneNo, @Param("verificationCode") String verificationCode);
    /** 
    * 修改密码时，发送的短信验证码 
    *  @param mobilePhoneNo	手机号码 
    *  @return 
    *  @author focus, 2014-9-25 下午2:11:14 
    */  
    @RpcMethod("sendSMSVerificationCodeInResertPwd")
    public RequestChannel<Integer> sendSMSVerificationCodeInResertPwd(@Param("mobilePhoneNo") String mobilePhoneNo);
    /** 
    * 修改密码的短信验证码验证 
    *  @param mobilePhoneNo 
    *  @param verificationCode 
    *  @return	 0 表示验证成功， 34：手机短信验证码验证错误 ， 35 验证码过期 
    *  @author focus, 2014-9-25 下午2:12:53 
    */  
    @RpcMethod("validSMSVerificationCodeInResertPwd")
    public RequestChannel<Integer> validSMSVerificationCodeInResertPwd(@Param("mobilePhoneNo") String mobilePhoneNo, @Param("verificationCode") String verificationCode);
    /** 
    * 账号管理： 绑定 
    *  @param userId	用户id	 
    *  @param bindType	绑定类型 mobile,email,weiBo,weChat,QQ 
    *  @param bindValue 绑定的值 
    *  @return			0 成功,-1 绑定失败,2 手机号码已经存在,3 邮箱已经存在,9 qq 已经绑定， 10 微博已经绑定 
    *  @author focus, 2014-9-18 下午5:37:34 
    */  
    @RpcMethod("bindAccount")
    public RequestChannel<Integer> bindAccount(@Param("userId") Long userId, @Param("bindType") String bindType, @Param("bindValue") String bindValue);
    /** 
    * 账号管理： 解除绑定 
    *  @param userId  		用户id 
    *  @param bindType 		绑定类型 mobile,email,weiBo,weChat,QQ 
    *  @param unBindValue	绑定的值 
    *  @return				0 成功,-1 解除绑定失败 
    *  @author focus, 2014-9-18 下午5:37:48 
    */  
    @RpcMethod("unBindAccount")
    public RequestChannel<Integer> unBindAccount(@Param("userId") Long userId, @Param("bindType") String bindType, @Param("bindValue") String bindValue);
    /** 
    * 更新用户绑定的手机号码 
    *  @param userId	用户id 
    *  @param mobile	手机号码 
    *  @return			0 成功,-1 绑定失败,2 手机号码已经存在 ,34：手机短信验证码验证错误 ， 35 验证码过期 
    *  @author focus, 2014-9-23 上午10:45:27 
    */  
    @RpcMethod("updateBindUserMobile")
    public RequestChannel<Integer> updateBindUserMobile(@Param("userId") Long userId, @Param("mobile") String mobile, @Param("verificationCode") String verificationCode);
    /** 
    * 添加账号密码 : 使用于第一次绑定手机号码，并且添加密码 
    *   
    *  @param account 
    *  @param userId 
    *  @return			0 成功,-1 绑定失败,2 手机号码已经存在 
    *  @author focus, 2014-9-25 下午4:08:26 
    */  
    @RpcMethod("bindUserMobileAndUpdatePwd")
    public RequestChannel<Integer> bindUserMobileAndUpdatePwd(@Param("userId") Long userId, @Param("mobile") String mobile, @Param("pwd") String pwd);
    /** 
    * 更新用户绑定的邮箱 
    *  @param userId	用户id 
    *  @param email		用户的邮箱 
    *  @return			0 成功,-1 绑定失败,3 邮箱已经存在 
    *  @author focus, 2014-9-23 下午4:48:58 
    */  
    @RpcMethod("updateBindUserEmail")
    public RequestChannel<Integer> updateBindUserEmail(@Param("userId") Long userId, @Param("email") String email);
    /** 
    * 账号管理：查询用户的账号管理 
    *  @param userId	用户id 
    *  @return 
    *  @author focus, 2014-9-18 下午5:42:16 
    */  
    @RpcMethod("findUserAccountManagerDTOByUserId")
    public RequestChannel<List<UserAccountManagerDTO>> findUserAccountManagerDTOByUserId(@Param("userId") Long userId);
    /** 
    * 账号管理：查找管理第三方插件的图片 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-9-18 下午5:51:06 
    */  
    @RpcMethod("findThirdImgURLDTOByUserId")
    public RequestChannel<List<ThirdImgUrlDTO>> findThirdImgURLDTOByUserId(@Param("userId") Long userId);
    /** 
    * 绑定头像： 
    *   
    *  <blockquote><pre></pre> 
    *  	<b>使用场景：</b> <br> 
    *  			1 绑定旧账号	bindExistsAccount<br> 
    *  			2 绑定账号		bindAccount<br> 
    *  </blockquote> 
    *  注册绑定就已经有的账号的时候， 
    *  @param bindType	绑定类型 mobile,email,weiBo,weChat,QQ 
    *  @param bindValue 
    *  @param imgAttachDTO 
    *  @return  -1 失败， 0 成功 
    *  @author focus, 2014-9-22 上午10:58:13 
    */  
    @RpcMethod("bindThirdPortrait")
    public RequestChannel<Integer> bindThirdPortrait(@Param("bindType") String bindType, @Param("bindValue") String bindValue, @Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);
    /** 
    * 是否使用shareSDKMobile 发送短信 
    *  @return	 39 使用shareSDKMobile 发送短信 并且手机不存在, 40 使用服务端发送短信 并且手机号码不存在， <br>   
    *  			 41 shareSDKMobile 并且手机号码已经存在, 42 使用服务端发送短信 并且手机号码已经存在  
    *  @author focus, 2014-9-26 下午2:10:38 
    */  
    @RpcMethod("isUserShareSDKMobileSendSM")
    public RequestChannel<Integer> isUserShareSDKMobileSendSM(@Param("mobile") String mobile);
    /** 
    * 是否打开匹配度设置 
    *  @param userId 
    *  @return			37 打开匹配模式， 38 关闭匹配模式 
    *  @author focus, 2014-9-30 下午2:39:47 
    */  
    @RpcMethod("isOpenUserMatchSet")
    public RequestChannel<Integer> isOpenUserMatchSet(@Param("userId") Long userId);
    /** 
    * 通过手机号码修改密码 
    *  @param userId 	用户id 
    *  @param pwd		密码 
    *  @return			0 修改密码成功, 4 账号不存在, -1 修改密码失败 ，34：手机短信验证码验证错误 ， 35 验证码过期 
    *  @author focus, 2014-9-30 下午2:58:56 
    */  
    @RpcMethod("updatePWDByMobile")
    public RequestChannel<Integer> updatePWDByMobile(@Param("mobile") String mobile, @Param("pwd") String pwd, @Param("verificationCode") String verificationCode);
    /** 
    * 请求的设备的属性  
    *  手机型号		DECEIVE_NAME	 
    *  手机类型		DECEIVE_TYPE：IOS,Android 
    *  系统版本号  	SYS_VERSION 
    *  app版本号		APP_VERSION 
    *  网络连接方式	NETWORK_STATUS 
    *  网络运营商		MOBILE_NETWORK_OPERATORS： CMCC 移动， CTCC 电信，CUCC 联通 ，UNNO 未知网络 
    *  登陆类型		LOGIN_TYPE: weiBo ,Mobile,QQ,None 
    *  登陆账号：		LOGIN_ACCOUNT :如果是手机号码或者邮箱登陆： 则记录手机号码或者邮箱， 如果是第三方的账号，则是 openId,如果没有登陆，则为None 
    *  手机串号：		MIEI 手机的唯一串号 
    *  用户ID		USER_ID 
    *  是否第一次装	IS_FIRST_INSTALL : 0 不是第一次安装, 1 第一次安装。 
    *  应用ID		APP_ID ： ZCDH_JOB 职场导航求职端,ZCDH_ENT 职场导航企业端 
    *  Lat			LAT 
    *  Lon			LON 
    *  城市			CITY_NAME 
    *  详细地址		ADDRESS 
    *  @param params 
    *  @author focus 
    *  @return 0 成功 
    */  
    @RpcMethod("addUserLog")
    public RequestChannel<Integer> addUserLog(@Param("params") Map<String, String> params);
    /** 
    * 使用微信登陆 
    *  @param openId 
    *  @return   0 登陆成功 ,6 QQ第一次登陆，7 账号已经停用 
    *  @author liyuan, 2015-5-7 下午1:06:24 
    */  
    @RpcMethod("loginByWeChat")
    public RequestChannel<Integer> loginByWeChat(@Param("openId") String openId);
    /** 
    * 通过微信获取userId 
    *  @param openId 
    *  @return 
    *  @author liyuan, 2015-5-7 下午5:59:06 
    */  
    @RpcMethod("findUserIdByWeChat")
    public RequestChannel<Long> findUserIdByWeChat(@Param("openId") String openId);
    /** 
    * 通过微信无绑定账号直接注册 
    *  @param openId 
    *  @param imgAttachDTO 
    *  @return 0：注册成功，44：该微信号已经绑定 
    *  @author liyuan, 2015-5-12 上午10:54:46 
    */  
    @RpcMethod("registerByWeChat")
    public RequestChannel<Integer> registerByWeChat(@Param("openId") String openId, @Param("imgAttachDTO") ImgAttachDTO imgAttachDTO);


    

    
    
	/**
	 * 根据用户id获取实践经历列表
	 * @param user_id
	 * @return
	 * @author liyuan, 2015-7-15 下午6:15:30
	 */
	@RpcMethod("findJobPracticeList")
	public RequestChannel<List<JobPracticeDTO>> findJobPracticeList(@Param("user_id") Long user_id);
	
	/**
	 * 删除实践经历实体
	 * @param practice_id
	 * @return 0-成功 其它-不成功
	 * @author liyuan, 2015-7-15 下午6:21:15
	 */
	@RpcMethod("removeJobPractice")
	public RequestChannel<Integer> removeJobPractice(@Param("practice_id") Long practice_id);
	
	/**
	 * 添加实践经历实体
	 * @param practice
	 * @return 0-成功 其它-不成功
	 * @author liyuan, 2015-7-15 下午6:24:05
	 */
	@RpcMethod("addJobPractice")
	public RequestChannel<Integer> addJobPractice(@Param("practice") JobPracticeDTO practice);
	
	/**
	 * 修改实践经历实体
	 * @param practice
	 * @return 0-成功 其它-不成功
	 * @author liyuan, 2015-7-15 下午6:25:07
	 */
	@RpcMethod("updateJobPractice")
	public RequestChannel<Integer> updateJobPractice(@Param("practice") JobPracticeDTO practice);

	
	/**
	 * 根据用户id获取获奖经历列表
	 * @param user_id
	 * @return
	 * @author liyuan, 2015-7-15 下午6:33:55
	 */
	@RpcMethod("findJobPrizesList")
	public RequestChannel<List<JobPrizesDTO>> findJobPrizesList(@Param("user_id") Long user_id);
	
	/**
	 * 删除获奖经历实体
	 * @param prizes_id
	 * @return 0-成功 其它-不成功
	 * @author liyuan, 2015-7-15 下午6:34:44
	 */
	@RpcMethod("removeJobPrizes")
	public RequestChannel<Integer> removeJobPrizes(@Param("prizes_id") Long prizes_id);
	
	/**
	 * 添加获奖经历实体
	 * @param prizes
	 * @return 0-成功 其它-不成功
	 * @author liyuan, 2015-7-15 下午6:36:17
	 */
	@RpcMethod("addJobPrizes")
	public RequestChannel<Integer> addJobPrizes(@Param("prizes") JobPrizesDTO prizes);
	
	/**
	 * 修改获奖经历实体
	 * @param prizes
	 * @return 0-成功 其它-不成功
	 * @author liyuan, 2015-7-15 下午6:36:59
	 */
	@RpcMethod("updateJobPrizes")
	public RequestChannel<Integer> updateJobPrizes(@Param("prizes") JobPrizesDTO prizes);
    
	/**
	 * 判断给定的手机号码是否合法
	 * @param mobile
	 * @return 1- 合法 0-不合法
	 * @author liyuan, 2015-7-16 下午4:02:35
	 */
	@RpcMethod("isValidMobilePhone")
	public RequestChannel<Integer> isValidMobilePhone(@Param("mobile") String mobile);
	

	/**
	 * 判断给定的邮箱号码是否合法
	 * @param email
	 * @return 1- 合法 0-不合法
	 * @author liyuan, 2015-7-17 上午11:31:58
	 */
	@RpcMethod("isValidEmail")
	public RequestChannel<Integer> isValidEmail(@Param("email") String email);

    
	/**
	 * 获取手机和email的正则表达式
	 * @return regex_phone-手机正则表达式 regex_email-邮箱正则表达式
	 * @author liyuan, 2015-7-17 下午4:58:47
	 */
	@RpcMethod("getMobileAndEmailRegex")
	public RequestChannel<Map<String,String>> getMobileAndEmailRegex();

	
	/**
	 * 根据用户id获取求职用户对象信息
	 * @param userId
	 * @return
	 * @author liyuan, 2015-8-12 下午1:39:15
	 */
	public JobUserDTO findJobUserDTOByUserId(@Param("userId") Long userId);

	
 } 

