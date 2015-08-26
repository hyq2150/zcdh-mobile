/** 
*  IRpcJobEnterpriseService 
* 
*  Created Date: 2015-08-12 17:07:58 
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
* 企业相关接口 
*  @author focus, 2014-4-8 上午11:24:17 
*/  
@RpcService("rpcJobEnterpriseService")
public interface IRpcJobEnterpriseService   { 
    /** 
    * 查询企业详细信息 
    *  @param entId		企业id 
    *  @return 
    *  @author focus, 2014-4-8 上午11:41:36 
    */  
    @RpcMethod("findJobEnterpriseDetailDTO")
    public RequestChannel<JobEnterpriseDetailDTO> findJobEnterpriseDetailDTO(@Param("entId") Long entId);
    /** 
    * 查询企业相册 
    *  @param entId			企业id 
    *  @param currentPage	当前页数 
    *  @param pageSize		每页显示条数 
    *  @return 
    *  @author hw, 2014-4-9 下午2:22:39 
    */  
    @RpcMethod("findEntPhotoByPage")
    public RequestChannel<Page<ImgURLDTO>> findEntPhotoByPage(@Param("entId") Long entId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找系统评价标签列表 
    *  @return 
    *  @author focus, 2014-5-8 下午3:03:43 
    */  
    @RpcMethod("findBasicSysEntCommentTagDTO")
    public RequestChannel<Page<BasicSysCommentTagDTO>> findBasicSysEntCommentTagDTO(@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 添加评价 
    *  @param tagList		标签的列表 
    *  @param comment		用户填写的评价 
    *  @return 
    *  @author hw, 2014-5-12 下午4:09:42 
    */  
    @RpcMethod("addEntComment")
    public RequestChannel<Integer> addEntComment(@Param("userId") Long userId, @Param("entId") Long entId, @Param("isNickName") Integer isNickName, @Param("tagList") List<CommentTagDTO> tagList, @Param("comment") String comment);
    /** 
    * 查看评论列表 
    *  @param entId				企业id 
    *  @param pageSize 			分页的参数： 一页显示多少条数据 
    *  @param currentPage 		当前页 
    *  @return					返回评论列表 
    *  @throws 					ZcdhException   
    *  @author hw, 2013-11-6 下午4:33:54 
    */  
    @RpcMethod("findEntCommentDTO")
    public RequestChannel<Page<CommentDTO>> findEntCommentDTO(@Param("entId") Long entId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 举报企业 
    *  @param userId 
    *  @param entId 
    *  @param informType	举报的类型[001：骗子公司，002：黑社会组织，003：传销组织，004：邪教组织，005：其他举报] 
    *  @param content 
    *  @return				0 成功， -1 失败  14  表示已经举报过了 
    *  @author focus, 2014-4-11 上午9:56:52 
    */  
    @RpcMethod("addInform")
    public RequestChannel<Integer> addInform(@Param("userId") Long userId, @Param("entId") Long entId, @Param("informType") String informType, @Param("content") String content);
    /** 
    * 添加用户黑名单或者 取消黑名单 
    *  @param userId 
    *  @param entId 
    *  @return				0 成功， -1 失败 
    *  @author focus, 2014-4-11 上午10:01:30 
    */  
    @RpcMethod("updateJobBlack")
    public RequestChannel<Integer> updateJobBlack(@Param("userId") Long userId, @Param("entId") Long entId);
    /** 
    * 关注企业 或者取消关注 
    *  @param userId	 
    *  @param entId 
    *  @return				0 成功， -1 失败 
    *  @author focus, 2014-4-11 上午10:13:47 
    */  
    @RpcMethod("updateAttentionEnt")
    public RequestChannel<Integer> updateAttentionEnt(@Param("userId") Long userId, @Param("entId") Long entId);
    /** 
    * 是否已经关注 
    *  @param userId 
    *  @param entId 
    *  @return			0 不关注，1 关注 
    *  @author focus, 2014-4-17 上午9:54:22 
    */  
    @RpcMethod("isAttentionEnt")
    public RequestChannel<Integer> isAttentionEnt(@Param("userId") Long userId, @Param("entId") Long entId);
    /** 
    * 添加留言：纠错，岗位咨询 
    *  @param leaveMessages 
    *  @return 
    *  @author focus, 2014-4-14 下午4:26:40 
    */  
    @RpcMethod("addLeaveMessage")
    public RequestChannel<Integer> addLeaveMessage(@Param("leaveMessages") LeaveMsgDTO leaveMessages);
    /** 
    * 岗位留言 
    *  @param userId 
    *  @param postId 
    *  @return 
    *  @author focus, 2014-6-5 下午5:40:23 
    */  
    @RpcMethod("findPostLeaveMessageDTO")
    public RequestChannel<List<LeaveMessageDTO>> findPostLeaveMessageDTO(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 企业留言 
    *  @param userId 
    *  @param entId 
    *  @param leaveType 
    *  @return 
    *  @author focus, 2014-6-5 下午5:40:58 
    */  
    @RpcMethod("findEntLeaveMessageDTO")
    public RequestChannel<List<LeaveMessageDTO>> findEntLeaveMessageDTO(@Param("userId") Long userId, @Param("entId") Long entId, @Param("leaveType") String leaveType);
    /** 
    * 分享企业 
    *  @param userId 
    *  @param entId 
    *  @param shareTO 分享个的平台：weiBo,QQ 
    *  @return 
    *  @author focus, 2014-4-11 上午10:24:48 
    */  
    @RpcMethod("shareEnt")
    public RequestChannel<Integer> shareEnt(@Param("userId") Long userId, @Param("entId") Long entId, @Param("shareTO") String shareTO);
    /** 
    * 查询企业的岗位列表 
    *  @param entId 
    *  @return 
    *  @author focus, 2014-4-15 下午4:02:36 
    */  
    @RpcMethod("findEntPostDTOByEntId")
    public RequestChannel<Page<JobEntPostDTO>> findEntPostDTOByEntId(@Param("entId") Long entId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查询企业的招聘会岗位列表 
    *  @param entId 
    *  @param fairId 
    *  @return 
    *  @author focus, 2014-4-15 下午4:02:36 
    */  
    @RpcMethod("findEntPostDTOByEntIdFairId")
    public RequestChannel<Page<JobEntPostDTO>> findEntPostDTOByEntIdFairId(@Param("fairId") Long fairId, @Param("entId") Long entId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 产品列表 
    *  @param entId 
    *  @return 
    *  @author focus, 2014-6-4 上午10:37:13 
    */  
    @RpcMethod("findEntProductDTO")
    public RequestChannel<Page<EntProductDTO>> findEntProductDTO(@Param("entId") Long entId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 企业粉丝列表 
    *  @param entId 
    *  @return 
    *  @author focus, 2014-6-4 上午10:37:06 
    */  
    @RpcMethod("findEntFansDTO")
    public RequestChannel<Page<EntFansDTO>> findEntFansDTO(@Param("entId") Long entId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 企业主页 
    *  @param userId 
    *  @param entId 
    *  @return 
    *  @author focus, 2014-6-4 上午10:36:57 
    */  
    @RpcMethod("findEntHomePageDTO")
    public RequestChannel<EntHomePageDTO> findEntHomePageDTO(@Param("userId") Long userId, @Param("entId") Long entId);
    /** 
    * 分享企业 
    *  @param entId 
    *  @return 
    *  @author focus, 2014-12-10 下午2:43:13 
    */  
    @RpcMethod("findEntShareDTO")
    public RequestChannel<List<JobEntShareDTO>> findEntShareDTO(@Param("entId") Long entId);
    /** 
    * 添加企业分享记录 
    *  @param userId 
    *  @param entId 
    *  @return 
    *  @author hw, 2014-12-23 下午3:38:57 
    */  
    @RpcMethod("addEntShareHistory")
    public RequestChannel<Integer> addEntShareHistory(@Param("userId") Long userId, @Param("entId") Long entId);
    /** 
    * 根据短地址查询企业信息 
    *  @param shortURL 
    *  @return 
    *  @author hw, 2014-12-11 上午11:33:59 
    */  
    @RpcMethod("findEntInfoDTOByShortURL")
    public RequestChannel<EntInfoDTO> findEntInfoDTOByShortURL(@Param("shortURL") String shortURL);


 } 

