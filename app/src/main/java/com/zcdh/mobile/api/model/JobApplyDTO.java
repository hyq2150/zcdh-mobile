/** 
*  JobApplyDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-18 下午7:27:42 
*/  
public class JobApplyDTO  implements Serializable { 
    private Long userId  ; 
    private String entName  ; 
    private Date applyDate  ; 
    private String postName  ; 
    //最后一条 
    private String applyStatus  ; 
    private Long postId  ; 
    private Long entId  ; 
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
    public void setApplyDate(Date applyDate) { 
        this.applyDate=applyDate;
     }
    /**
    *无功能描述
    */
    public Date getApplyDate() { 
        return  this.applyDate;
     }
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
    *设定最后一条
    */
    public void setApplyStatus(String applyStatus) { 
        this.applyStatus=applyStatus;
     }
    /**
    *获取最后一条
    */
    public String getApplyStatus() { 
        return  this.applyStatus;
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

 } 

