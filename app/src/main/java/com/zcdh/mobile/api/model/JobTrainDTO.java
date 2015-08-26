/** 
*  JobTrainDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午2:26:50 
*/  
public class JobTrainDTO  implements Serializable { 
    private Long trainId  ; 
    //用户id 
    private Long userId  ; 
    //培训的开始时间 
    private Date startTime  ; 
    //培训的结束时间 
    private Date endTime  ; 
    //培训机构 
    private String trainInstitution  ; 
    //培训的课程的名称 
    private String courseName  ; 
    //培训的课程的编码 
    private String courseCode  ; 
    //培训获得的证书的名称 
    private String credentialName  ; 
    //培训获得的证书的编码 
    private String credentialCode  ; 
    //补充的内容 
    private String content  ; 
    /**
    *无功能描述
    */
    public void setTrainId(Long trainId) { 
        this.trainId=trainId;
     }
    /**
    *无功能描述
    */
    public Long getTrainId() { 
        return  this.trainId;
     }
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
    *设定培训的开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取培训的开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定培训的结束时间
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *获取培训的结束时间
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定培训机构
    */
    public void setTrainInstitution(String trainInstitution) { 
        this.trainInstitution=trainInstitution;
     }
    /**
    *获取培训机构
    */
    public String getTrainInstitution() { 
        return  this.trainInstitution;
     }
    /**
    *设定培训的课程的名称
    */
    public void setCourseName(String courseName) { 
        this.courseName=courseName;
     }
    /**
    *获取培训的课程的名称
    */
    public String getCourseName() { 
        return  this.courseName;
     }
    /**
    *设定培训的课程的编码
    */
    public void setCourseCode(String courseCode) { 
        this.courseCode=courseCode;
     }
    /**
    *获取培训的课程的编码
    */
    public String getCourseCode() { 
        return  this.courseCode;
     }
    /**
    *设定培训获得的证书的名称
    */
    public void setCredentialName(String credentialName) { 
        this.credentialName=credentialName;
     }
    /**
    *获取培训获得的证书的名称
    */
    public String getCredentialName() { 
        return  this.credentialName;
     }
    /**
    *设定培训获得的证书的编码
    */
    public void setCredentialCode(String credentialCode) { 
        this.credentialCode=credentialCode;
     }
    /**
    *获取培训获得的证书的编码
    */
    public String getCredentialCode() { 
        return  this.credentialCode;
     }
    /**
    *设定补充的内容
    */
    public void setContent(String content) { 
        this.content=content;
     }
    /**
    *获取补充的内容
    */
    public String getContent() { 
        return  this.content;
     }

 } 

