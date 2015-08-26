/** 
*  UserResumeDTO 
* 
*  Created Date: 2014-05-08 12:17:23 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-5-8 上午11:55:11
 */
public class UserResumeDTO  implements Serializable { 
    /** 
     * 个人基本信息是否填写完整： 0 不完整，1 完整
     */
    private Integer basicInfoFull  ; 
    /** 
     * 自我评价是否填写完整：0 不完整，1 完整
     */
    private Integer commentMyselfFull  ; 
    /** 
     * 教育经历是否填写完整：0 不完整，1 完整
     */
    private Integer eduExperienceFull  ; 
    /** 
     * 邮箱
     */
    private String email  ; 
    /** 
     * 个人头像
     */
    private ImgURLDTO img  ; 
    /** 
     * 求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
     */
    private String jobStatusCode  ; 
    /** 
     * 求职状态名称
     */
    private String jobStautsName  ; 
    /** 
     * 手机号码
     */
    private String mobile  ; 
    /** 
     * 求职意向是否填写完整： 0 不完整，1 完整
     */
    private Integer objectiveFull  ; 
    /** 
     * 简历是否公开：  0 隐藏，1 公开
     */
    private Integer publishResume  ; 
    /** 
     * 个人技能是否是填写完整：0 不完整，1 完整
     */
    private Integer technologyFull  ; 
    /** 
     * 用户id
     */
    private Long userId  ; 
    /** 
     * 用户名称
     */
    private String userName  ; 
    /** 
     * 工作经验是否是填写完整：0 不完整，1 完整
     */
    private Integer workExperienceFull  ; 
    /**
    *设定个人基本信息是否填写完整： 0 不完整，1 完整
    */
    public void setBasicInfoFull(Integer basicInfoFull) { 
        this.basicInfoFull=basicInfoFull;
     }
    /**
    *获取设定个人基本信息是否填写完整： 0 不完整，1 完整
    */
    public Integer getBasicInfoFull() { 
        return  this.basicInfoFull;
     }
    /**
    *设定自我评价是否填写完整：0 不完整，1 完整
    */
    public void setCommentMyselfFull(Integer commentMyselfFull) { 
        this.commentMyselfFull=commentMyselfFull;
     }
    /**
    *获取设定自我评价是否填写完整：0 不完整，1 完整
    */
    public Integer getCommentMyselfFull() { 
        return  this.commentMyselfFull;
     }
    /**
    *设定教育经历是否填写完整：0 不完整，1 完整
    */
    public void setEduExperienceFull(Integer eduExperienceFull) { 
        this.eduExperienceFull=eduExperienceFull;
     }
    /**
    *获取设定教育经历是否填写完整：0 不完整，1 完整
    */
    public Integer getEduExperienceFull() { 
        return  this.eduExperienceFull;
     }
    /**
    *设定邮箱
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *获取设定邮箱
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *设定个人头像
    */
    public void setImg(ImgURLDTO img) { 
        this.img=img;
     }
    /**
    *获取设定个人头像
    */
    public ImgURLDTO getImg() { 
        return  this.img;
     }
    /**
    *设定求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
    */
    public void setJobStatusCode(String jobStatusCode) { 
        this.jobStatusCode=jobStatusCode;
     }
    /**
    *获取设定求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
    */
    public String getJobStatusCode() { 
        return  this.jobStatusCode;
     }
    /**
    *设定求职状态名称
    */
    public void setJobStautsName(String jobStautsName) { 
        this.jobStautsName=jobStautsName;
     }
    /**
    *获取设定求职状态名称
    */
    public String getJobStautsName() { 
        return  this.jobStautsName;
     }
    /**
    *设定手机号码
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *获取设定手机号码
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *设定求职意向是否填写完整： 0 不完整，1 完整
    */
    public void setObjectiveFull(Integer objectiveFull) { 
        this.objectiveFull=objectiveFull;
     }
    /**
    *获取设定求职意向是否填写完整： 0 不完整，1 完整
    */
    public Integer getObjectiveFull() { 
        return  this.objectiveFull;
     }
    /**
    *设定简历是否公开：  0 隐藏，1 公开
    */
    public void setPublishResume(Integer publishResume) { 
        this.publishResume=publishResume;
     }
    /**
    *获取设定简历是否公开：  0 隐藏，1 公开
    */
    public Integer getPublishResume() { 
        return  this.publishResume;
     }
    /**
    *设定个人技能是否是填写完整：0 不完整，1 完整
    */
    public void setTechnologyFull(Integer technologyFull) { 
        this.technologyFull=technologyFull;
     }
    /**
    *获取设定个人技能是否是填写完整：0 不完整，1 完整
    */
    public Integer getTechnologyFull() { 
        return  this.technologyFull;
     }
    /**
    *设定用户id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取设定用户id
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定用户名称
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *获取设定用户名称
    */
    public String getUserName() { 
        return  this.userName;
     }
    /**
    *设定工作经验是否是填写完整：0 不完整，1 完整
    */
    public void setWorkExperienceFull(Integer workExperienceFull) { 
        this.workExperienceFull=workExperienceFull;
     }
    /**
    *获取设定工作经验是否是填写完整：0 不完整，1 完整
    */
    public Integer getWorkExperienceFull() { 
        return  this.workExperienceFull;
     }

 } 

