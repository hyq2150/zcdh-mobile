/** 
*  EntInfoDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 岗位分享内容 
*  @author chw, 2014-4-8 上午11:10:04 
*/  
public class EntInfoDTO  implements Serializable { 
    private Long entId  ; 
    private String shortName  ; 
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
    public void setShortName(String shortName) { 
        this.shortName=shortName;
     }
    /**
    *无功能描述
    */
    public String getShortName() { 
        return  this.shortName;
     }

 } 

