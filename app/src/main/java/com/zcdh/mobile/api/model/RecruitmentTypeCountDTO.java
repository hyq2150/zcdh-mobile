/** 
*  RecruitmentTypeCountDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 招聘类型的数量 统计DTO 
*  @author focus, 2014-4-19 下午4:10:09 
*/  
public class RecruitmentTypeCountDTO  implements Serializable { 
    private Long entId  ; 
    private Integer socialPostCount  ; 
    private Integer campusPostCount  ; 
    private Integer holidayPostCount  ; 
    /**
    *无功能描述
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *无功能描述
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *无功能描述
    */
    public void setSocialPostCount(Integer socialPostCount) { 
        this.socialPostCount=socialPostCount;
     }
    /**
    *无功能描述
    */
    public Integer getSocialPostCount() { 
        return  this.socialPostCount;
     }
    /**
    *无功能描述
    */
    public void setCampusPostCount(Integer campusPostCount) { 
        this.campusPostCount=campusPostCount;
     }
    /**
    *无功能描述
    */
    public Integer getCampusPostCount() { 
        return  this.campusPostCount;
     }
    /**
    *无功能描述
    */
    public void setHolidayPostCount(Integer holidayPostCount) { 
        this.holidayPostCount=holidayPostCount;
     }
    /**
    *无功能描述
    */
    public Integer getHolidayPostCount() { 
        return  this.holidayPostCount;
     }

 } 

