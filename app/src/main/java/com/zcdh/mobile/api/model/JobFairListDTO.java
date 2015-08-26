/** 
*  JobFairListDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 招聘会列表 
*  @author focus, 2014-6-18 上午11:30:18 
*/  
public class JobFairListDTO  implements Serializable { 
    //招聘会场次 
    private Long bannerId  ; 
    //开始时间 
    private Date startTime  ; 
    //标题 
    private String title  ; 
    //描述 
    private String decription  ; 
    /**
    *设定招聘会场次
    */
    public void setBannerId(Long bannerId) { 
        this.bannerId=bannerId;
     }
    /**
    *获取招聘会场次
    */
    public Long getBannerId() { 
        return  this.bannerId;
     }
    /**
    *设定开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取标题
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定描述
    */
    public void setDecription(String decription) { 
        this.decription=decription;
     }
    /**
    *获取描述
    */
    public String getDecription() { 
        return  this.decription;
     }

 } 

