/** 
*  JobSearchTagDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 标签的DTO 
*  @author focus, 2014-4-1 下午4:27:49 
*/  
public class JobSearchTagDTO  implements Serializable { 
    private String tagCode  ; 
    private String tagName  ; 
    //标签类型 
    private String propertyType  ; 
    /**
    *无功能描述
    */
    public void setTagCode(String tagCode) { 
        this.tagCode=tagCode;
     }
    /**
    *无功能描述
    */
    public String getTagCode() { 
        return  this.tagCode;
     }
    /**
    *无功能描述
    */
    public void setTagName(String tagName) { 
        this.tagName=tagName;
     }
    /**
    *无功能描述
    */
    public String getTagName() { 
        return  this.tagName;
     }
    /**
    *设定标签类型
    */
    public void setPropertyType(String propertyType) { 
        this.propertyType=propertyType;
     }
    /**
    *获取标签类型
    */
    public String getPropertyType() { 
        return  this.propertyType;
     }

 } 

