/** 
*  BasicSysCommentTagDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 系统标签的dto 
*  @author focus, 2014-5-8 下午3:04:23 
*/  
public class BasicSysCommentTagDTO  implements Serializable { 
    //标签编码 
    private String tagCode  ; 
    //标签名称 
    private String tagName  ; 
    //排序字段 
    private Integer orders  ; 
    /**
    *设定标签编码
    */
    public void setTagCode(String tagCode) { 
        this.tagCode=tagCode;
     }
    /**
    *获取标签编码
    */
    public String getTagCode() { 
        return  this.tagCode;
     }
    /**
    *设定标签名称
    */
    public void setTagName(String tagName) { 
        this.tagName=tagName;
     }
    /**
    *获取标签名称
    */
    public String getTagName() { 
        return  this.tagName;
     }
    /**
    *设定排序字段
    */
    public void setOrders(Integer orders) { 
        this.orders=orders;
     }
    /**
    *获取排序字段
    */
    public Integer getOrders() { 
        return  this.orders;
     }

 } 

