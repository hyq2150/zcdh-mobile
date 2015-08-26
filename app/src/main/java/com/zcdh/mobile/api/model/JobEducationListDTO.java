/** 
*  JobEducationListDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午2:41:28 
*/  
public class JobEducationListDTO  implements Serializable { 
    //教育经历id 
    private Long eduId  ; 
    //用户id 
    private Long userId  ; 
    //学校名称 
    private String school  ; 
    //学历名称 
    private String degreeName  ; 
    //专业名称 
    private String majorName  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    /**
    *设定教育经历id
    */
    public void setEduId(Long eduId) { 
        this.eduId=eduId;
     }
    /**
    *获取教育经历id
    */
    public Long getEduId() { 
        return  this.eduId;
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
    *设定学校名称
    */
    public void setSchool(String school) { 
        this.school=school;
     }
    /**
    *获取学校名称
    */
    public String getSchool() { 
        return  this.school;
     }
    /**
    *设定学历名称
    */
    public void setDegreeName(String degreeName) { 
        this.degreeName=degreeName;
     }
    /**
    *获取学历名称
    */
    public String getDegreeName() { 
        return  this.degreeName;
     }
    /**
    *设定专业名称
    */
    public void setMajorName(String majorName) { 
        this.majorName=majorName;
     }
    /**
    *获取专业名称
    */
    public String getMajorName() { 
        return  this.majorName;
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

