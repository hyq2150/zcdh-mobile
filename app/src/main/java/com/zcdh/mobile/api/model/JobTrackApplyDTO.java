/** 
*  JobTrackApplyDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-14 上午11:11:23 
*/  
public class JobTrackApplyDTO  implements Serializable { 
    private Long id  ; 
    private String trackContent  ; 
    private Date trackTime  ; 
    /**
    *无功能描述
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *无功能描述
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *无功能描述
    */
    public void setTrackContent(String trackContent) { 
        this.trackContent=trackContent;
     }
    /**
    *无功能描述
    */
    public String getTrackContent() { 
        return  this.trackContent;
     }
    /**
    *无功能描述
    */
    public void setTrackTime(Date trackTime) { 
        this.trackTime=trackTime;
     }
    /**
    *无功能描述
    */
    public Date getTrackTime() { 
        return  this.trackTime;
     }

 } 

