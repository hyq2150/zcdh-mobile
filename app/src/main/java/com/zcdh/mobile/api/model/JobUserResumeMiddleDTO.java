/** 
*  JobUserResumeMiddleDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 个人简历页面的中间的信息 
*  @author focus, 2013-5-13 下午3:52:09 
*/  
public class JobUserResumeMiddleDTO  implements Serializable { 
    //提示信息 
    private String promptContent  ; 
    //个人基本信息是否填写完整： 0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer basicInfoFull  ; 
    //求职意向是否填写完整： 0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer objectiveFull  ; 
    //工作经验是否是填写完整：0 不完整，1 完整 ， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer workExperienceFull  ; 
    //教育经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer eduExperienceFull  ; 
    //个人技能是否是填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer technologyFull  ; 
    //自我评价是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer commentMyselfFull  ; 
    //实践经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer practiceFull  ; 
    //获奖经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整 
    private Integer prizesFull  ; 
    //简历完整度的百分比 
    private String resumePercent  ; 
    //是否允许投递简历 
    // 1-允许投简历 0-不允许投简历 
    private Integer isCanPostResume  ; 
    //是否允许报名招聘会 
    // 1-允许报名 0-不允许报名 
    private Integer isCanSignUp  ; 
    /**
    *设定提示信息
    */
    public void setPromptContent(String promptContent) { 
        this.promptContent=promptContent;
     }
    /**
    *获取提示信息
    */
    public String getPromptContent() { 
        return  this.promptContent;
     }
    /**
    *设定个人基本信息是否填写完整： 0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setBasicInfoFull(Integer basicInfoFull) { 
        this.basicInfoFull=basicInfoFull;
     }
    /**
    *获取个人基本信息是否填写完整： 0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getBasicInfoFull() { 
        return  this.basicInfoFull;
     }
    /**
    *设定求职意向是否填写完整： 0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setObjectiveFull(Integer objectiveFull) { 
        this.objectiveFull=objectiveFull;
     }
    /**
    *获取求职意向是否填写完整： 0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getObjectiveFull() { 
        return  this.objectiveFull;
     }
    /**
    *设定工作经验是否是填写完整：0 不完整，1 完整 ， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setWorkExperienceFull(Integer workExperienceFull) { 
        this.workExperienceFull=workExperienceFull;
     }
    /**
    *获取工作经验是否是填写完整：0 不完整，1 完整 ， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getWorkExperienceFull() { 
        return  this.workExperienceFull;
     }
    /**
    *设定教育经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setEduExperienceFull(Integer eduExperienceFull) { 
        this.eduExperienceFull=eduExperienceFull;
     }
    /**
    *获取教育经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getEduExperienceFull() { 
        return  this.eduExperienceFull;
     }
    /**
    *设定个人技能是否是填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setTechnologyFull(Integer technologyFull) { 
        this.technologyFull=technologyFull;
     }
    /**
    *获取个人技能是否是填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getTechnologyFull() { 
        return  this.technologyFull;
     }
    /**
    *设定自我评价是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setCommentMyselfFull(Integer commentMyselfFull) { 
        this.commentMyselfFull=commentMyselfFull;
     }
    /**
    *获取自我评价是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getCommentMyselfFull() { 
        return  this.commentMyselfFull;
     }
    /**
    *设定实践经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setPracticeFull(Integer practiceFull) { 
        this.practiceFull=practiceFull;
     }
    /**
    *获取实践经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getPracticeFull() { 
        return  this.practiceFull;
     }
    /**
    *设定获奖经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public void setPrizesFull(Integer prizesFull) { 
        this.prizesFull=prizesFull;
     }
    /**
    *获取获奖经历是否填写完整：0 不完整，1 完整， 2 必填项并且不完整， 3 必填项并且完整
    */
    public Integer getPrizesFull() { 
        return  this.prizesFull;
     }
    /**
    *设定简历完整度的百分比
    */
    public void setResumePercent(String resumePercent) { 
        this.resumePercent=resumePercent;
     }
    /**
    *获取简历完整度的百分比
    */
    public String getResumePercent() { 
        return  this.resumePercent;
     }
    /**
    *设定是否允许投递简历 1-允许投简历 0-不允许投简历
    */
    public void setIsCanPostResume(Integer isCanPostResume) { 
        this.isCanPostResume=isCanPostResume;
     }
    /**
    *获取是否允许投递简历 1-允许投简历 0-不允许投简历
    */
    public Integer getIsCanPostResume() { 
        return  this.isCanPostResume;
     }
    /**
    *设定是否允许报名招聘会 1-允许报名 0-不允许报名
    */
    public void setIsCanSignUp(Integer isCanSignUp) { 
        this.isCanSignUp=isCanSignUp;
     }
    /**
    *获取是否允许报名招聘会 1-允许报名 0-不允许报名
    */
    public Integer getIsCanSignUp() { 
        return  this.isCanSignUp;
     }

 } 

