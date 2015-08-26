/** 
*  BannerDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-30 下午4:22:07 
*/  
public class BannerDTO  implements Serializable { 
    private Long bannerId  ; 
    private boolean toSubcribe  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    /**
    *无功能描述
    */
    public void setBannerId(Long bannerId) { 
        this.bannerId=bannerId;
     }
    /**
    *无功能描述
    */
    public Long getBannerId() { 
        return  this.bannerId;
     }
    /**
    *无功能描述
    */
    public void setToSubcribe(boolean toSubcribe) { 
        this.toSubcribe=toSubcribe;
     }
    /**
    *无功能描述
    */
    public boolean getToSubcribe() { 
        return  this.toSubcribe;
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

