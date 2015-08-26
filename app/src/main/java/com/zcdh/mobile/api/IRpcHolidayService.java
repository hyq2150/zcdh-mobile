/** 
*  IRpcHolidayService 
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
* @author focus, 2014-4-19 下午2:24:01 
*/  
@RpcService("mRpcHolidayService")
public interface IRpcHolidayService   { 
    /** 
    * 地图的banner 和假期工的入口 
    *  @return 
    *  @author focus, 2014-4-19 下午2:27:16 
    */  
    @RpcMethod("findBanner")
    public RequestChannel<BannerDTO> findBanner();
    /** 
    * 取得用户的求职意向 
    *  @return 
    *  @author focus, 2014-4-19 下午2:43:53 
    */  
    @RpcMethod("findUserObjectiveDTO")
    public RequestChannel<SearchHolidayConditionDTO> findUserObjectiveDTO(@Param("userId") Long userId);
    /** 
    * 更新用户的搜索条件 
    *  @param searchConditionDTO 
    *  @return 
    *  @author focus, 2014-4-19 下午3:09:49 
    */  
    @RpcMethod("updateUserObjectiveDTO")
    public RequestChannel<Integer> updateUserObjectiveDTO(@Param("searchConditionDTO") SearchHolidayConditionDTO searchConditionDTO);
    /** 
    * 预约首页 
    *  @param userId 
    *  @param bannerId 
    *  @return 
    *  @author focus, 2014-4-19 下午3:35:31 
    */  
    @RpcMethod("findHolidayHomePageDTO")
    public RequestChannel<HomePageDTO> findHolidayHomePageDTO(@Param("userId") Long userId, @Param("bannerId") Long bannerId);
    /** 
    * 预约 
    *  @param userId 
    *  @return 0 成功， 3 已经预约过了 
    *  @author focus, 2014-4-19 下午4:16:05 
    */  
    @RpcMethod("subscribe")
    public RequestChannel<Integer> subscribe(@Param("userId") Long userId, @Param("bannerId") Long bannerId);
    /** 
    * 查找合适的岗位的列表  
    *  @param userId 
    *  @return		 
    *  @author focus, 2014-4-24 上午12:57:35 
    */  
    @RpcMethod("findFitPostListDTO")
    public RequestChannel<List<FitPostListDTO>> findFitPostListDTO(@Param("userId") Long userId);
    /** 
    * 查询用户搜索条件的头部 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-4-22 上午10:52:42 
    */  
    @RpcMethod("findSearchHolidayTitleDTO")
    public RequestChannel<SearchTitleDTO> findSearchHolidayTitleDTO();
    /** 
    * 搜索列表： 
    *  @param keyWord				关键字 
    *  @param searchConditionDTO	条件 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-19 下午4:03:52 
    */  
    @RpcMethod("findEntPostSearchDTO")
    public RequestChannel<Page<EntPostSearchDTO>> findEntPostSearchDTO(@Param("keyWord") String keyWord, @Param("searchConditionDTO") SearchHolidayConditionDTO searchConditionDTO, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 企业的招聘岗位的各种类型数量统计 
    *  @param entId 
    *  @return 
    *  @author focus, 2014-4-19 下午4:09:29 
    */  
    @RpcMethod("findRecruitmentTypeCountDTO")
    public RequestChannel<RecruitmentTypeCountDTO> findRecruitmentTypeCountDTO(@Param("entId") Long entId);
    /** 
    * 查询企业发布岗位列表 
    *  @param entId		企业id 
    *  @param postProperty 岗位的属性编码： 社会招聘: 1, 假期工: 2,校园招聘 :3,全部：4   
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @throws Exception 
    *  @author focus, 2013-11-11 上午10:06:51 
    */  
    @RpcMethod("findEntPostList")
    public RequestChannel<Page<EntPostSearchDTO>> findEntPostList(@Param("entId") Long entId, @Param("postProperty") Integer postProperty, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 假期工申请岗位 
    *  @param userId 
    *  @param postId 
    *  @return 
    *  @author focus, 2014-4-19 下午5:05:04 
    */  
    @RpcMethod("applyHolidayPost")
    public RequestChannel<PlaceOrderDTO> applyHolidayPost(@Param("userId") Long userId, @Param("postId") Long postId);
    /** 
    * 生成订单  必要条件： 诚意金，代金卷 ， 
    *  @param userId 
    *  @param postId 
    *  @param applyPostCount 
    *  @return 返回订单号， 如果生成订单成功， 则返回订单号， 否则则返回空 
    *  @author focus, 2014-4-19 下午5:08:31 
    */  
    @RpcMethod("generateOrder")
    public RequestChannel<String> generateOrder(@Param("userId") Long userId, @Param("postId") Long postId, @Param("applyPostCount") Integer applyPostCount);
    /** 
    * 确认提交订单:开始支付，通过微信支付 
    *  @param orderNum			订单编号 
    *  @param isUserVoucher 
    *  @return 
    *  @author focus, 2014-4-19 下午5:13:14 
    */  
    @RpcMethod("conformOrderByWeChat")
    public RequestChannel<ConFirmPaymentDTO> conformOrderByWeChat(@Param("orderNum") String ordreNum, @Param("isUserVoucher") Boolean isUserVoucher);
    /** 
    * 查找用户的订单列表 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @param status 0 已付款， 1 未付款 
    *  @return 
    *  @author focus, 2014-4-21 下午9:37:27 
    */  
    @RpcMethod("findMyOrderDTO")
    public RequestChannel<Page<MyOrderDTO>> findMyOrderDTO(@Param("userId") Long userId, @Param("status") Integer status, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找订单详细信息 
    *  @param orderNum 
    *  @return 
    *  @author focus, 2014-4-21 下午9:38:06 
    */  
    @RpcMethod("findOrderDTO")
    public RequestChannel<OrderDTO> findOrderDTO(@Param("orderNum") String orderNum);
    /** 
    * 查看订单跟踪 
    *  @param orderNum 
    *  @return 
    *  @author focus, 2014-4-21 下午9:39:36 
    */  
    @RpcMethod("findTrackOrder")
    public RequestChannel<TrackOrderDTO> findTrackOrder(@Param("orderNum") String orderNum);
    /** 
    * 随便看看 
    *  @param areaCode				地区编码 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-19 下午4:03:52 
    */  
    @RpcMethod("lookAround")
    public RequestChannel<Page<EntPostSearchDTO>> lookAround(@Param("areaCode") String areaCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 随便看看的语音 
    *  @param keyWord 
    *  @param searchConditionDTO 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:19:54 
    */  
    @RpcMethod("lookAroundByVoice")
    public RequestChannel<Page<EntPostSearchDTO>> lookAroundByVoice(@Param("keyWord") String keyWord, @Param("areaCode") String areaCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 随便看看的摇一摇 
    *  @param keyWord 
    *  @param searchConditionDTO 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:20:06 
    */  
    @RpcMethod("lookAroundByShake")
    public RequestChannel<Page<EntPostSearchDTO>> lookAroundByShake(@Param("keyWord") String keyWord, @Param("areaCode") String areaCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 随便看看的移动 
    *  @param keyWord 
    *  @param lbs 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:31:25 
    */  
    @RpcMethod("lookAroundByMove")
    public RequestChannel<Page<EntPostSearchDTO>> lookAroundByMove(@Param("keyWord") String keyWord, @Param("lbs") List<LbsParam> lbs, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 找工作 
    *  @param searchConditionDTO 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:33:54 
    */  
    @RpcMethod("findJob")
    public RequestChannel<Page<EntPostSearchDTO>> findJob(@Param("searchConditionDTO") SearchHolidayConditionDTO searchConditionDTO, @Param("areaCode") String areaCode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 通过语音找工作 
    *  @param keyWord 
    *  @param searchConditionDTO 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:35:21 
    */  
    @RpcMethod("findJobByVoice")
    public RequestChannel<Page<EntPostSearchDTO>> findJobByVoice(@Param("keyWord") String keyWord, @Param("areaCode") String areaCode, @Param("searchConditionDTO") SearchHolidayConditionDTO searchConditionDTO, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 找工作的摇一摇 
    *  @param keyWord 
    *  @param searchConditionDTO 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:20:06 
    */  
    @RpcMethod("findJobByShake")
    public RequestChannel<Page<EntPostSearchDTO>> findJobByShake(@Param("keyWord") String keyWord, @Param("areaCode") String areaCode, @Param("searchConditionDTO") SearchHolidayConditionDTO searchConditionDTO, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 找工作的移动 
    *  @param keyWord 
    *  @param lbs 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-26 上午11:31:25 
    */  
    @RpcMethod("findJobByMove")
    public RequestChannel<Page<EntPostSearchDTO>> findJobByMove(@Param("keyWord") String keyWord, @Param("lbs") List<LbsParam> lbs, @Param("searchConditionDTO") SearchHolidayConditionDTO searchConditionDTO, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 进入准备支付页面 
    *  @param orderNum	订单号 
    *  @return 
    *  @author focus, 2014-4-26 下午3:50:44 
    */  
    @RpcMethod("toPrePayFor")
    public RequestChannel<PayForDTO> toPrePayFor(@Param("orderNum") String orderNum);
    /** 
    * 取消订单 
    *  @param orderNum 
    *  @return 0 取消成功 
    *  @author focus, 2014-4-28 上午9:42:30 
    */  
    @RpcMethod("cancelOrder")
    public RequestChannel<Integer> cancelOrder(@Param("orderNum") String orderNum);
    /** 
    * 取得有效的订单岗位数量 
    *  @param postId 
    *  @return			返回已经招聘的数量 
    *  @author focus, 2014-4-28 上午10:45:15 
    */  
    @RpcMethod("findValidPostCount")
    public RequestChannel<Integer> findValidPostCount(@Param("postId") Long postId);
    /** 
    * 更新订单 
    *  @param order	订单对象 
    *  @return 
    *  @author focus, 2014-4-29 下午12:51:22 
    */  
    @RpcMethod("updateOrder")
    public RequestChannel<Integer> updateOrder(@Param("orderNum") String orderNum, @Param("postCount") Integer postCount);
    /** 
    * 查询个人的求职意向 
    *  @param userId 
    *  @return 
    *  @author focus, 2014-4-29 上午1:25:17 
    */  
    @RpcMethod("findUserObjective")
    public RequestChannel<UserObjectiveDTO> findUserObjective(@Param("userId") Long userId);
    /** 
    * 查找付款的合同条款 
    *  @return 
    *  @author focus, 2014-4-30 下午6:12:11 
    */  
    @RpcMethod("findClause")
    public RequestChannel<String> findClause();
    /** 
    * 取得基本参数 
    *  @return 
    *  @author focus, 2014-5-2 上午10:29:12 
    */  
    @RpcMethod("genBasicWeChatParam")
    public RequestChannel<WeChatParamDTO> genBasicWeChatParam();
    /** 
    * 取得prepayId  
    *  <b>Notice:</b><br> 
    *  @param weChatToken   json字符串 
    *  @param ordreNum		订单编码 
    *  @param isUserVoucher 是否使用代金卷 
    *  @return 
    *  @author focus, 2014-5-2 上午9:26:25 
    */  
    @RpcMethod("genPrePayIdParam")
    public RequestChannel<PrePayParamDTO> genPrePayIdParam(@Param("orderNum") String orderNum, @Param("isUserVoucher") Boolean isUserVoucher, @Param("weChatToken") String weChatToken);
    /** 
    * 这种方式提交， preId 和token 都是手机端生成 
    *  @param preId			预付款id 
    *  @param orderNum		订单编码 
    *  @return 
    *  @author focus, 2014-5-2 下午2:49:23 
    */  
    @RpcMethod("conFirPaymentDTONew")
    public RequestChannel<ConFirmPaymentDTO> conFirPaymentDTONew(@Param("preId") String preId, @Param("isUserVoucher") Boolean isUserVoucher, @Param("orderNum") String orderNum, @Param("otherParams") Map<String, String> otherParams);
    /** 
    * 支付成功 
    *  @param orderNum 
    *  @return 
    *  @author focus, 2014-5-10 上午9:19:26 
    */  
    @RpcMethod("paySuccess")
    public RequestChannel<Integer> paySuccess(@Param("orderNum") String orderNum);
    /** 
    * 查找用户的订单列表:已付款 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-21 下午9:37:27 
    */  
    @RpcMethod("findMyOrderDTOByPaid")
    public RequestChannel<Page<MyOrderDTO>> findMyOrderDTOByPaid(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 查找用户的订单列表：未付款 
    *  @param userId 
    *  @param currentPage 
    *  @param pageSize 
    *  @return 
    *  @author focus, 2014-4-21 下午9:37:27 
    */  
    @RpcMethod("findMyOrderDTOByUnPaid")
    public RequestChannel<Page<MyOrderDTO>> findMyOrderDTOByUnPaid(@Param("userId") Long userId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
    /** 
    * 假期工招聘会根据招聘会id和岗位id取得有效的订单岗位数量 
    *  @param fairId 
    *  @param postId 
    *  @return 
    *  @author liyuan, 2015-6-19 上午11:23:20 
    */  
    @RpcMethod("findValidPostCountForHolidayJob")
    public RequestChannel<Integer> findValidPostCountForHolidayJob(@Param("fairId") Long fairId, @Param("postId") Long postId);
    /** 
    * 生成订单(假期工招聘会) 
    *  @param fairId 
    *  @param userId 
    *  @param postId 
    *  @param applyPostCount  
    *  @return 返回订单号， 如果生成订单成功， 则返回订单号， 否则则返回空 
    *  @author liyuan, 2015-6-19 下午1:22:35 
    */  
    @RpcMethod("generateOrderForHolidayJob")
    public RequestChannel<String> generateOrderForHolidayJob(@Param("fairId") Long fairId, @Param("userId") Long userId, @Param("postId") Long postId, @Param("applyPostCount") Integer applyPostCount);
    /** 
    * 假期工申请岗位(假期工招聘会) 
    *  @param fairId 
    *  @param userId 
    *  @param postId 
    *  @return 
    *  @author liyuan, 2015-6-24 上午11:07:58 
    */  
    @RpcMethod("applyHolidayPostForJobfair")
    public RequestChannel<PlaceOrderDTO> applyHolidayPostForJobfair(@Param("fairId") Long fairId, @Param("userId") Long userId, @Param("postId") Long postId);


 } 

