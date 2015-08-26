/** 
*  IRpcJobPostService 
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
* @author focus, 2014-4-8 上午10:58:57 
*/  
@RpcService("rpcJobPostService")
public interface IRpcJobPostService   { 
    /** 
    * 查找岗位的详细信息 
    *  @param userId 
    *  @param postId 
    *  @return 
    *  @author focus, 2014-4-8 上午11:22:41 
    */  
    @RpcMethod("findJobEntPostDetailDTO")
    public RequestChannel<JobEntPostDetailDTO> findJobEntPostDetailDTO(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 用户发送求职申请 
    *  <blockquote> 
    *  	1.用户提交前，需要现在企业的特殊要求，<br> 
    *  	2.提交个人的补充说明<br><p> 
    *   
    * 	<b> 所以这个方法的操作是：</b><br> 
    * 			a.首先把企业的特殊要求的技能匹配的东西都存入到个人的匹配表中去，<br> 
    * 			b.在把个人补充说明添加到个人求职申请表中 
    *  	 
    *  </blockquote> 
    *  @param entId	企业id 
    *  @param userId	用户id 
    *  @param postId   职位id 
    *  @param content 	个人补充内容 
    *  @param entAbilities 用户匹配的企业特殊技能列表 
    *  @param applyType 用户选择求职意向的来源: 为保留之后做广告统计使用， 岗位详情页面请写入 ："001.003" 
    *  @return 			0 成功 -1 失败 14 表示当前岗位已经申请过了 
    *  @see SvrConstants 
    *  @throws ZcdhException 
    *  @author focus, 2013-11-7 上午9:34:03 
    */  
    @RpcMethod("userApplyPost")
    public RequestChannel<Integer> userApplyPost(@Param("entId") Long entId, @Param("userId") Long userId, @Param("postId") Long postId, @Param("content") String content, @Param("applyType") String applyType, @Param("specialRequire") List<JobSpecialRequirementsMatchDTO> specialRequire);
    /** 
    * 查找岗位的特殊技能的需求列表 
    *  @param postId 
    *  @return 
    *  @author focus, 2014-4-11 上午11:00:39 
    */  
    @RpcMethod("findSpecialRequirementMatchDTO")
    public RequestChannel<ApplyPostInfoDTO> findSpecialRequirementMatchDTO(@Param("postId") Long postId);
    /** 
    * 添加或取消岗位收藏 
    *  @param userId	 
    *  @param postId 
    *  @return			0 成功 -1 失败 
    *  @author focus, 2014-4-11 上午10:28:49 
    */  
    @RpcMethod("updateFavorite")
    public RequestChannel<Integer> updateFavorite(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 分享岗位 
    *  @param userId 
    *  @param postId 
    *  @return			0 成功 -1 失败 
    *  @author focus, 2014-4-11 上午10:30:04 
    */  
    @RpcMethod("sharePost")
    public RequestChannel<Integer> sharePost(@Param("userId") Long userId, @Param("postId") Long postId, @Param("shareTO") String shareTO, @Param("bindId") String bindId);
    /** 
    * 查询岗位分享内容 
    *  @param userId 
    *  @param postId 
    *  @return 
    *  @author hw, 2014-7-17 上午9:59:44 
    */  
    @RpcMethod("findPostShareContent")
    public RequestChannel<List<JobEntShareDTO>> findPostShareContent(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 查询岗位技能与用户技能模板，中不匹配的技能列表 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-9-1 下午6:41:54 
    */  
    @RpcMethod("findUnMatchTeclByUserId")
    public RequestChannel<List<UnMatchTeclDTO>> findUnMatchTeclByUserId(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 查找岗位的详细信息(新) 
    *  @param fairId 
    *  @param userId 
    *  @param postId 
    *  @return 
    *  @author liyuan, 2015-7-9 下午4:55:22 
    */  
    @RpcMethod("findExtJobEntPostDetailDTO")
    public RequestChannel<JobEntPostDetailDTO> findExtJobEntPostDetailDTO(@Param("fairId") Long fairId, @Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 用户发送求职申请(新) 
    *  @param fairId 
    *  @param entId 
    *  @param userId 
    *  @param postId 
    *  @param content 
    *  @param applyType 
    *  @param specialRequire 
    *  @return 
    *  @author liyuan, 2015-7-9 下午6:25:43 
    */  
    @RpcMethod("userApplyPostExt")
    public RequestChannel<Integer> userApplyPostExt(@Param("fairId") Long fairId, @Param("entId") Long entId, @Param("userId") Long userId, @Param("postId") Long postId, @Param("content") String content, @Param("applyType") String applyType, @Param("specialRequire") List<JobSpecialRequirementsMatchDTO> specialRequire);


 } 

