/** 
*  SearchAreaDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-22 上午10:55:16 
*/  
public class SearchAreaDTO  implements Serializable { 
    private Long userId  ; 
    private String code  ; 
    private String name  ; 
    private Integer postCount  ; 
    private String parentCode  ; 
    private String parentName  ; 
    private List<SearchAreaDTO> subAreas  ; 
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
    public void setPostCount(Integer postCount) { 
        this.postCount=postCount;
     }
    /**
    *无功能描述
    */
    public Integer getPostCount() { 
        return  this.postCount;
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
    public void setParentName(String parentName) { 
        this.parentName=parentName;
     }
    /**
    *无功能描述
    */
    public String getParentName() { 
        return  this.parentName;
     }
    /**
    *无功能描述
    */
    public void setSubAreas(List<SearchAreaDTO> subAreas) { 
        this.subAreas=subAreas;
     }
    /**
    *无功能描述
    */
    public List<SearchAreaDTO> getSubAreas() { 
        return  this.subAreas;
     }

 } 

