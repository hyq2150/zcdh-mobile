/** 
*  JobUserHomePageMiddleDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-14 下午3:49:37 
*/  
public class JobUserHomePageMiddleDTO  implements Serializable { 
    //简历的百分比 
    private String resumePercent  ; 
    //申请的信息 1 有，0 没有 
    private Integer newApply  ; 
    //谁访问我 1 有，0 没有 
    private Integer newVisit  ; 
    //人事经理来信  1 有，0 没有 
    private Integer newLetter  ; 
    //订阅服务  1 有，0 没有 
    private Integer newSubscription  ; 
    //已经反馈信息  1 有，0 没有 
    private Integer newFeedBack  ; 
    //信息中心 
    private Integer newInfocenter  ; 
    /**
    *设定简历的百分比
    */
    public void setResumePercent(String resumePercent) { 
        this.resumePercent=resumePercent;
     }
    /**
    *获取简历的百分比
    */
    public String getResumePercent() { 
        return  this.resumePercent;
     }
    /**
    *设定申请的信息 1 有，0 没有
    */
    public void setNewApply(Integer newApply) { 
        this.newApply=newApply;
     }
    /**
    *获取申请的信息 1 有，0 没有
    */
    public Integer getNewApply() { 
        return  this.newApply;
     }
    /**
    *设定谁访问我 1 有，0 没有
    */
    public void setNewVisit(Integer newVisit) { 
        this.newVisit=newVisit;
     }
    /**
    *获取谁访问我 1 有，0 没有
    */
    public Integer getNewVisit() { 
        return  this.newVisit;
     }
    /**
    *设定人事经理来信  1 有，0 没有
    */
    public void setNewLetter(Integer newLetter) { 
        this.newLetter=newLetter;
     }
    /**
    *获取人事经理来信  1 有，0 没有
    */
    public Integer getNewLetter() { 
        return  this.newLetter;
     }
    /**
    *设定订阅服务  1 有，0 没有
    */
    public void setNewSubscription(Integer newSubscription) { 
        this.newSubscription=newSubscription;
     }
    /**
    *获取订阅服务  1 有，0 没有
    */
    public Integer getNewSubscription() { 
        return  this.newSubscription;
     }
    /**
    *设定已经反馈信息  1 有，0 没有
    */
    public void setNewFeedBack(Integer newFeedBack) { 
        this.newFeedBack=newFeedBack;
     }
    /**
    *获取已经反馈信息  1 有，0 没有
    */
    public Integer getNewFeedBack() { 
        return  this.newFeedBack;
     }
    /**
    *设定信息中心
    */
    public void setNewInfocenter(Integer newInfocenter) { 
        this.newInfocenter=newInfocenter;
     }
    /**
    *获取信息中心
    */
    public Integer getNewInfocenter() { 
        return  this.newInfocenter;
     }

 } 

