/** 
*  TrackPostDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 订单跟踪 
*  @author focus, 2014-6-17 上午9:20:19 
*/  
public class TrackPostDTO  implements Serializable { 
    //跟踪内容 
    private String trackContent  ; 
    //跟踪时间 
    private Date trackDate  ; 
    /**
    *设定跟踪内容
    */
    public void setTrackContent(String trackContent) { 
        this.trackContent=trackContent;
     }
    /**
    *获取跟踪内容
    */
    public String getTrackContent() { 
        return  this.trackContent;
     }
    /**
    *设定跟踪时间
    */
    public void setTrackDate(Date trackDate) { 
        this.trackDate=trackDate;
     }
    /**
    *获取跟踪时间
    */
    public Date getTrackDate() { 
        return  this.trackDate;
     }

 } 

