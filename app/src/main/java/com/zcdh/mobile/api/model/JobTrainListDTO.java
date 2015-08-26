/** 
*  JobTrainListDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午2:42:00 
*/  
public class JobTrainListDTO  implements Serializable { 
    //培训经历的id 
    private Long trainId  ; 
    //培训机构 
    private String trainInstitution  ; 
    //证书的名称 
    private String credentialName  ; 
    //课程名称 
    private String courseName  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    /**
    *设定培训经历的id
    */
    public void setTrainId(Long trainId) { 
        this.trainId=trainId;
     }
    /**
    *获取培训经历的id
    */
    public Long getTrainId() { 
        return  this.trainId;
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
    *设定证书的名称
    */
    public void setCredentialName(String credentialName) { 
        this.credentialName=credentialName;
     }
    /**
    *获取证书的名称
    */
    public String getCredentialName() { 
        return  this.credentialName;
     }
    /**
    *设定课程名称
    */
    public void setCourseName(String courseName) { 
        this.courseName=courseName;
     }
    /**
    *获取课程名称
    */
    public String getCourseName() { 
        return  this.courseName;
     }
    /**
    *无功能描述
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *无功能描述
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *无功能描述
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *无功能描述
    */
    public Date getEndTime() { 
        return  this.endTime;
     }

 } 

