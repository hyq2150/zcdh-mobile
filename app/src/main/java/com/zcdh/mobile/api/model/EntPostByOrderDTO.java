/** 
*  EntPostByOrderDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 订单中的岗位信息 
*  @author focus, 2014-4-21 下午9:03:36 
*/  
public class EntPostByOrderDTO  implements Serializable { 
    private Long postId  ; 
    private String postName  ; 
    private String entName  ; 
    private String areaName  ; 
    private String workTime  ; 
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
    public void setAreaName(String areaName) { 
        this.areaName=areaName;
     }
    /**
    *无功能描述
    */
    public String getAreaName() { 
        return  this.areaName;
     }
    /**
    *无功能描述
    */
    public void setWorkTime(String workTime) { 
        this.workTime=workTime;
     }
    /**
    *无功能描述
    */
    public String getWorkTime() { 
        return  this.workTime;
     }

 } 

