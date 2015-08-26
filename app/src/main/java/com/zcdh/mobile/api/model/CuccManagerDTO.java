/** 
*  CuccManagerDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-5 下午10:40:06 
*/  
public class CuccManagerDTO  implements Serializable { 
    private Long managerId  ; 
    private String name  ; 
    private String mobile  ; 
    private String url  ; 
    private String areaCode  ; 
    private Integer ignoreClaim  ; 
    /**
    *无功能描述
    */
    public void setManagerId(Long managerId) { 
        this.managerId=managerId;
     }
    /**
    *无功能描述
    */
    public Long getManagerId() { 
        return  this.managerId;
     }
    /**
    *无功能描述
    */
    public void setName(String name) { 
        this.name=name;
     }
    /**
    *无功能描述
    */
    public String getName() { 
        return  this.name;
     }
    /**
    *无功能描述
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *无功能描述
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *无功能描述
    */
    public void setUrl(String url) { 
        this.url=url;
     }
    /**
    *无功能描述
    */
    public String getUrl() { 
        return  this.url;
     }
    /**
    *无功能描述
    */
    public void setAreaCode(String areaCode) { 
        this.areaCode=areaCode;
     }
    /**
    *无功能描述
    */
    public String getAreaCode() { 
        return  this.areaCode;
     }
    /**
    *无功能描述
    */
    public void setIgnoreClaim(Integer ignoreClaim) { 
        this.ignoreClaim=ignoreClaim;
     }
    /**
    *无功能描述
    */
    public Integer getIgnoreClaim() { 
        return  this.ignoreClaim;
     }

 } 

