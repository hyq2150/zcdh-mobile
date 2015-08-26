/** 
*  JobEntRelationPostDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 其他相关职位 
*  @author focus, 2014-4-8 上午11:17:14 
*/  
public class JobEntRelationPostDTO  implements Serializable { 
    private String postName  ; 
    private String tagName  ; 
    private String entName  ; 
    private Long postId  ; 
    private Long entId  ; 
    private Integer orders  ; 
    /**
    *无功能描述
    */
    public void setPostName(String postName) { 
        this.postName=postName;
     }
    /**
    *无功能描述
    */
    public String getPostName() { 
        return  this.postName;
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
    *无功能描述
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *无功能描述
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *无功能描述
    */
    public void setPostId(Long postId) { 
        this.postId=postId;
     }
    /**
    *无功能描述
    */
    public Long getPostId() { 
        return  this.postId;
     }
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
    public void setOrders(Integer orders) { 
        this.orders=orders;
     }
    /**
    *无功能描述
    */
    public Integer getOrders() { 
        return  this.orders;
     }

 } 

