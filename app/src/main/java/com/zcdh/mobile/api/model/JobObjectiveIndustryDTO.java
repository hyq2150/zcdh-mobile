/** 
*  JobObjectiveIndustryDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-7 下午5:01:02 
*/  
public class JobObjectiveIndustryDTO  implements Serializable { 
    private String parentCode  ; 
    private String code  ; 
    private String name  ; 
    /**
    *无功能描述
    */
    public void setParentCode(String parentCode) { 
        this.parentCode=parentCode;
     }
    /**
    *无功能描述
    */
    public String getParentCode() { 
        return  this.parentCode;
     }
    /**
    *无功能描述
    */
    public void setCode(String code) { 
        this.code=code;
     }
    /**
    *无功能描述
    */
    public String getCode() { 
        return  this.code;
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

 } 

