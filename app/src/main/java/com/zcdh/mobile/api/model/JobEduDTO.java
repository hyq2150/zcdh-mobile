/** 
*  JobEduDTO 
* 
*  Created Date: 2014-05-08 14:44:01 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-4-15 下午8:36:18
 */
public class JobEduDTO  implements Serializable { 
    private String degreeName  ; 
    private Long eduId  ; 
    private String majorName  ; 
    private String school  ; 
    private Long userId  ; 
    /**
    *无功能描述
    */
    public void setDegreeName(String degreeName) { 
        this.degreeName=degreeName;
     }
    /**
    *无功能描述
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
    public void setMajorName(String majorName) { 
        this.majorName=majorName;
     }
    /**
    *无功能描述
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

