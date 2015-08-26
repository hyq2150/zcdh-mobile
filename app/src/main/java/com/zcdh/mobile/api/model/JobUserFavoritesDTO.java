/** 
*  JobUserFavoritesDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 岗位收藏DTO 
*  @author focus, 2014-5-19 上午10:33:14 
*/  
public class JobUserFavoritesDTO  implements Serializable { 
    private Long postId  ; 
    private String entName  ; 
    private String postName  ; 
    private String salary  ; 
    private String areaName  ; 
    private String degreeName  ; 
    private Date favorityDate  ; 
    private Long endId  ; 
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
    public void setSalary(String salary) { 
        this.salary=salary;
     }
    /**
    *无功能描述
    */
    public String getSalary() { 
        return  this.salary;
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
    public void setDegreeName(String degreeName) { 
        this.degreeName=degreeName;
     }
    /**
    *无功能描述
    */
    public String getDegreeName() { 
        return  this.degreeName;
     }
    /**
    *无功能描述
    */
    public void setFavorityDate(Date favorityDate) { 
        this.favorityDate=favorityDate;
     }
    /**
    *无功能描述
    */
    public Date getFavorityDate() { 
        return  this.favorityDate;
     }
    /**
    *无功能描述
    */
    public void setEndId(Long endId) { 
        this.endId=endId;
     }
    /**
    *无功能描述
    */
    public Long getEndId() { 
        return  this.endId;
     }

 } 

