/** 
*  TestJobEducationDTO 
* 
*  Created Date: 2014-04-16 11:03:43 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-4-15 下午8:35:46
 */
public class TestJobEducationDTO  implements Serializable { 
    /** 
     * 学历编码
     */
    private String degreeCode  ; 
    /** 
     * 学历名称
     */
    private String degreeName  ; 
    private Long eduId  ; 
    private ArrayList endTime  ; 
    /** 
     * 专业编码
     */
    private String majorCode  ; 
    /** 
     * 专业名称
     */
    private String majorName  ; 
    private String school  ; 
    private ArrayList startTime  ; 
    private Long userId  ; 
    /**
    *设定学历编码
    */
    public void setDegreeCode(String degreeCode) { 
        this.degreeCode=degreeCode;
     }
    /**
    *获取设定学历编码
    */
    public String getDegreeCode() { 
        return  this.degreeCode;
     }
    /**
    *设定学历名称
    */
    public void setDegreeName(String degreeName) { 
        this.degreeName=degreeName;
     }
    /**
    *获取设定学历名称
    */
    public String getDegreeName() { 
        return  this.degreeName;
     }
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
    public void setEndTime(ArrayList endTime) { 
        this.endTime=endTime;
     }
    /**
    *无功能描述
    */
    public ArrayList getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定专业编码
    */
    public void setMajorCode(String majorCode) { 
        this.majorCode=majorCode;
     }
    /**
    *获取设定专业编码
    */
    public String getMajorCode() { 
        return  this.majorCode;
     }
    /**
    *设定专业名称
    */
    public void setMajorName(String majorName) { 
        this.majorName=majorName;
     }
    /**
    *获取设定专业名称
    */
    public String getMajorName() { 
        return  this.majorName;
     }
    /**
    *无功能描述
    */
    public void setSchool(String school) { 
        this.school=school;
     }
    /**
    *无功能描述
    */
    public String getSchool() { 
        return  this.school;
     }
    /**
    *无功能描述
    */
    public void setStartTime(ArrayList startTime) { 
        this.startTime=startTime;
     }
    /**
    *无功能描述
    */
    public ArrayList getStartTime() { 
        return  this.startTime;
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

 } 

