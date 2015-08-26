/** 
*  JobFairDetailExtDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 招聘会详细信息扩展 
*  @author liyuan, 2015-7-2 下午4:58:14 
*/  
public class JobFairDetailExtDTO  implements Serializable { 
    //招聘会id 
    private Long fairId  ; 
    //招聘会标题 
    private String title  ; 
    //开始时间 
    private Date startTime  ; 
    //结束时间 
    private Date endTime  ; 
    //地点 
    private String address  ; 
    //简介类型 html,url 
    private String introType  ; 
    //手动输入url 
    private String url  ; 
    //0自动，1手动 
    private Integer urlType  ; 
    //招聘会类型 0 社会招聘，1 校园招聘，2 其他, 3 假期工 
    private Integer fairType  ; 
    //招聘会类型名称 
    private String fairName  ; 
    //招聘会状态 
    // 0: 不通过  ，1：生效，上线，-1：审核中,2:草稿中  3：失效，暂停，4：已关闭 5：已结束 
    private Integer status  ; 
    //招聘会状态名称 
    private Integer statusName  ; 
    //是否允许报名 
    // 0:不允许报名，1：允许报名 
    private Integer sign_status  ; 
    //签到方式 (1-按钮签到 2-扫码签到) 
    private String signInType  ; 
    //参会流程url(图片) 
    private String joinFlowUrl  ; 
    //更多详情url 
    private String moreDetailUrl  ; 
    //签到url 
    private String signInUrl  ; 
    //面试结果url 
    private String interviewResultUrl  ; 
    //实时数据url 
    private String realDataUrl  ; 
    //企业登录url 
    private String entLoginUrl  ; 
    //参会职位数量 
    private Integer joinPostCount  ; 
    //参会企业数量 
    private Integer joinEntCount  ; 
    //纬度 
    private Double lat  ; 
    //是否已报名 1-是 0-否 
    private Integer isSignUp  ; 
    //是否已签到  1-是 0-否 
    private Integer isSignIn  ; 
    //时间范围字符串 
    private String timeRange  ; 
    private Double lon  ; 
    /**
    *设定招聘会id
    */
    public void setFairId(Long fairId) { 
        this.fairId=fairId;
     }
    /**
    *获取招聘会id
    */
    public Long getFairId() { 
        return  this.fairId;
     }
    /**
    *设定招聘会标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取招聘会标题
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定结束时间
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *获取结束时间
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定地点
    */
    public void setAddress(String address) { 
        this.address=address;
     }
    /**
    *获取地点
    */
    public String getAddress() { 
        return  this.address;
     }
    /**
    *设定简介类型 html,url
    */
    public void setIntroType(String introType) { 
        this.introType=introType;
     }
    /**
    *获取简介类型 html,url
    */
    public String getIntroType() { 
        return  this.introType;
     }
    /**
    *设定手动输入url
    */
    public void setUrl(String url) { 
        this.url=url;
     }
    /**
    *获取手动输入url
    */
    public String getUrl() { 
        return  this.url;
     }
    /**
    *设定0自动，1手动
    */
    public void setUrlType(Integer urlType) { 
        this.urlType=urlType;
     }
    /**
    *获取0自动，1手动
    */
    public Integer getUrlType() { 
        return  this.urlType;
     }
    /**
    *设定招聘会类型 0 社会招聘，1 校园招聘，2 其他, 3 假期工
    */
    public void setFairType(Integer fairType) { 
        this.fairType=fairType;
     }
    /**
    *获取招聘会类型 0 社会招聘，1 校园招聘，2 其他, 3 假期工
    */
    public Integer getFairType() { 
        return  this.fairType;
     }
    /**
    *设定招聘会类型名称
    */
    public void setFairName(String fairName) { 
        this.fairName=fairName;
     }
    /**
    *获取招聘会类型名称
    */
    public String getFairName() { 
        return  this.fairName;
     }
    /**
    *设定招聘会状态 0: 不通过  ，1：生效，上线，-1：审核中,2:草稿中  3：失效，暂停，4：已关闭 5：已结束
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取招聘会状态 0: 不通过  ，1：生效，上线，-1：审核中,2:草稿中  3：失效，暂停，4：已关闭 5：已结束
    */
    public Integer getStatus() { 
        return  this.status;
     }
    /**
    *设定招聘会状态名称
    */
    public void setStatusName(Integer statusName) { 
        this.statusName=statusName;
     }
    /**
    *获取招聘会状态名称
    */
    public Integer getStatusName() { 
        return  this.statusName;
     }
    /**
    *设定是否允许报名 0:不允许报名，1：允许报名
    */
    public void setSign_status(Integer sign_status) { 
        this.sign_status=sign_status;
     }
    /**
    *获取是否允许报名 0:不允许报名，1：允许报名
    */
    public Integer getSign_status() { 
        return  this.sign_status;
     }
    /**
    *设定签到方式 (1-按钮签到 2-扫码签到)
    */
    public void setSignInType(String signInType) { 
        this.signInType=signInType;
     }
    /**
    *获取签到方式 (1-按钮签到 2-扫码签到)
    */
    public String getSignInType() { 
        return  this.signInType;
     }
    /**
    *设定参会流程url(图片)
    */
    public void setJoinFlowUrl(String joinFlowUrl) { 
        this.joinFlowUrl=joinFlowUrl;
     }
    /**
    *获取参会流程url(图片)
    */
    public String getJoinFlowUrl() { 
        return  this.joinFlowUrl;
     }
    /**
    *设定更多详情url
    */
    public void setMoreDetailUrl(String moreDetailUrl) { 
        this.moreDetailUrl=moreDetailUrl;
     }
    /**
    *获取更多详情url
    */
    public String getMoreDetailUrl() { 
        return  this.moreDetailUrl;
     }
    /**
    *设定签到url
    */
    public void setSignInUrl(String signInUrl) { 
        this.signInUrl=signInUrl;
     }
    /**
    *获取签到url
    */
    public String getSignInUrl() { 
        return  this.signInUrl;
     }
    /**
    *设定面试结果url
    */
    public void setInterviewResultUrl(String interviewResultUrl) { 
        this.interviewResultUrl=interviewResultUrl;
     }
    /**
    *获取面试结果url
    */
    public String getInterviewResultUrl() { 
        return  this.interviewResultUrl;
     }
    /**
    *设定实时数据url
    */
    public void setRealDataUrl(String realDataUrl) { 
        this.realDataUrl=realDataUrl;
     }
    /**
    *获取实时数据url
    */
    public String getRealDataUrl() { 
        return  this.realDataUrl;
     }
    /**
    *设定企业登录url
    */
    public void setEntLoginUrl(String entLoginUrl) { 
        this.entLoginUrl=entLoginUrl;
     }
    /**
    *获取企业登录url
    */
    public String getEntLoginUrl() { 
        return  this.entLoginUrl;
     }
    /**
    *设定参会职位数量
    */
    public void setJoinPostCount(Integer joinPostCount) { 
        this.joinPostCount=joinPostCount;
     }
    /**
    *获取参会职位数量
    */
    public Integer getJoinPostCount() { 
        return  this.joinPostCount;
     }
    /**
    *设定参会企业数量
    */
    public void setJoinEntCount(Integer joinEntCount) { 
        this.joinEntCount=joinEntCount;
     }
    /**
    *获取参会企业数量
    */
    public Integer getJoinEntCount() { 
        return  this.joinEntCount;
     }
    /**
    *设定纬度
    */
    public void setLat(Double lat) { 
        this.lat=lat;
     }
    /**
    *获取纬度
    */
    public Double getLat() { 
        return  this.lat;
     }
    /**
    *设定是否已报名 1-是 0-否
    */
    public void setIsSignUp(Integer isSignUp) { 
        this.isSignUp=isSignUp;
     }
    /**
    *获取是否已报名 1-是 0-否
    */
    public Integer getIsSignUp() { 
        return  this.isSignUp;
     }
    /**
    *设定是否已签到  1-是 0-否
    */
    public void setIsSignIn(Integer isSignIn) { 
        this.isSignIn=isSignIn;
     }
    /**
    *获取是否已签到  1-是 0-否
    */
    public Integer getIsSignIn() { 
        return  this.isSignIn;
     }
    /**
    *设定时间范围字符串
    */
    public void setTimeRange(String timeRange) { 
        this.timeRange=timeRange;
     }
    /**
    *获取时间范围字符串
    */
    public String getTimeRange() { 
        return  this.timeRange;
     }
    /**
    *无功能描述
    */
    public void setLon(Double lon) { 
        this.lon=lon;
     }
    /**
    *无功能描述
    */
    public Double getLon() { 
        return  this.lon;
     }

 } 

