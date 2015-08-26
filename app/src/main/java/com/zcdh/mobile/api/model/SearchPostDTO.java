/** 
*  SearchPostDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-29 上午1:32:46 
*/  
public class SearchPostDTO  implements Serializable { 
    private Long userId  ; 
    private String code  ; 
    private String parentCode  ; 
    private String name  ; 
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

