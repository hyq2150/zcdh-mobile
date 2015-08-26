/** 
*  JobEducationDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 教育经历 
*  @author focus, 2014-4-15 下午8:35:46 
*/  
public class JobEducationDTO  implements Serializable { 
    private Long eduId  ; 
    private Long userId  ; 
    private String schoolName  ; 
    //学校的编码 
    private String schoolCode  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    //学历编码 
    private String degreeCode  ; 
    //专业编码 
    private String majorCode  ; 
    //学历名称 
    private String degreeName  ; 
    //专业名称 
    private String majorName  ; 
    //补充的内容 
    private String content  ; 
    /**
    *无功能描述
    */
    public void setEduId(Long eduId) { 
        this.eduId=eduId;
     }
    /**
    *无功能描述
    */
    public Long getEduId() { 
        return  this.eduId;
     }
    /**
    *无功能描述
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *无功能描述
    */
    public void setSchoolName(String schoolName) { 
        this.schoolName=schoolName;
     }
    /**
    *无功能描述
    */
    public String getSchoolName() { 
        return  this.schoolName;
     }
    /**
    *设定学校的编码
    */
    public void setSchoolCode(String schoolCode) { 
        this.schoolCode=schoolCode;
     }
    /**
    *获取学校的编码
    */
    public String getSchoolCode() { 
        return  this.schoolCode;
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
    /**
    *设定学历编码
    */
    public void setDegreeCode(String degreeCode) { 
        this.degreeCode=degreeCode;
     }
    /**
    *获取学历编码
    */
    public String getDegreeCode() { 
        return  this.degreeCode;
     }
    /**
    *设定专业编码
    */
    public void setMajorCode(String majorCode) { 
        this.majorCode=majorCode;
     }
    /**
    *获取专业编码
    */
    public String getMajorCode() { 
        return  this.majorCode;
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

