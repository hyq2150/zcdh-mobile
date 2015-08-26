/** 
*  JobFairPost 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 岗位列表 
*  @author focus, 2014-6-18 上午11:33:19 
*/  
public class JobFairPost  implements Serializable { 
    private Long postId  ; 
    private String postName  ; 
    private String entName  ; 
    private String areaName  ; 
    private String postNameTemp  ; 
    private Long entId  ; 
    private String boothNo  ; 
    private String psalary  ; 
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
    public void setPostNameTemp(String postNameTemp) { 
        this.postNameTemp=postNameTemp;
     }
    /**
    *无功能描述
    */
    public String getPostNameTemp() { 
        return  this.postNameTemp;
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
    public void setBoothNo(String boothNo) { 
        this.boothNo=boothNo;
     }
    /**
    *无功能描述
    */
    public String getBoothNo() { 
        return  this.boothNo;
     }
    /**
    *无功能描述
    */
    public void setPsalary(String psalary) { 
        this.psalary=psalary;
     }
    /**
    *无功能描述
    */
    public String getPsalary() { 
        return  this.psalary;
     }

 } 

