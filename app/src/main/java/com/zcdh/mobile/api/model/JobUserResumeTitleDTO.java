/** 
*  JobUserResumeTitleDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 上午11:55:11 
*/  
public class JobUserResumeTitleDTO  implements Serializable { 
    //用户id 
    private Long userId  ; 
    //个人头像 
    private ImgURLDTO img  ; 
    //用户名称 
    private String userName  ; 
    //邮箱 
    private String email  ; 
    //手机号码 
    private String mobile  ; 
    //求职状态名称 
    private String jobStautsName  ; 
    //求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中 
    private String jobStatusCode  ; 
    //简历是否公开：  0 隐藏，1 公开 
    private Integer publishResume  ; 
    //被访问的数量 
    private Integer visitedCount  ; 
    //人才类型 
    private String talentTypeName  ; 
    /**
    *设定用户id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取用户id
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定个人头像
    */
    public void setImg(ImgURLDTO img) { 
        this.img=img;
     }
    /**
    *获取个人头像
    */
    public ImgURLDTO getImg() { 
        return  this.img;
     }
    /**
    *设定用户名称
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *获取用户名称
    */
    public String getUserName() { 
        return  this.userName;
     }
    /**
    *设定邮箱
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *获取邮箱
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *设定手机号码
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *获取手机号码
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *设定求职状态名称
    */
    public void setJobStautsName(String jobStautsName) { 
        this.jobStautsName=jobStautsName;
     }
    /**
    *获取求职状态名称
    */
    public String getJobStautsName() { 
        return  this.jobStautsName;
     }
    /**
    *设定求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
    */
    public void setJobStatusCode(String jobStatusCode) { 
        this.jobStatusCode=jobStatusCode;
     }
    /**
    *获取求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
    */
    public String getJobStatusCode() { 
        return  this.jobStatusCode;
     }
    /**
    *设定简历是否公开：  0 隐藏，1 公开
    */
    public void setPublishResume(Integer publishResume) { 
        this.publishResume=publishResume;
     }
    /**
    *获取简历是否公开：  0 隐藏，1 公开
    */
    public Integer getPublishResume() { 
        return  this.publishResume;
     }
    /**
    *设定被访问的数量
    */
    public void setVisitedCount(Integer visitedCount) { 
        this.visitedCount=visitedCount;
     }
    /**
    *获取被访问的数量
    */
    public Integer getVisitedCount() { 
        return  this.visitedCount;
     }
    /**
    *设定人才类型
    */
    public void setTalentTypeName(String talentTypeName) { 
        this.talentTypeName=talentTypeName;
     }
    /**
    *获取人才类型
    */
    public String getTalentTypeName() { 
        return  this.talentTypeName;
     }

 } 

