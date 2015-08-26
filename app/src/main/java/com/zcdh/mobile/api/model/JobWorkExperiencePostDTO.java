/** 
*  JobWorkExperiencePostDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-15 下午7:33:24 
*/  
public class JobWorkExperiencePostDTO  implements Serializable { 
    private Long userId  ; 
    //工作经历岗位id 
    private Long wepPostId  ; 
    //岗位名称 
    private String postName  ; 
    //公司名称 
    private String corName  ; 
    //开始时间 
    private Date startTime  ; 
    //结束时间 
    private Date endTime  ; 
    //岗位编码 
    private String postCode  ; 
    //行业 
    private String industryCode  ; 
    //行业名称 
    private String industryName  ; 
    //岗位详情 
    private String postDesc  ; 
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
    *设定工作经历岗位id
    */
    public void setWepPostId(Long wepPostId) { 
        this.wepPostId=wepPostId;
     }
    /**
    *获取工作经历岗位id
    */
    public Long getWepPostId() { 
        return  this.wepPostId;
     }
    /**
    *设定岗位名称
    */
    public void setPostName(String postName) { 
        this.postName=postName;
     }
    /**
    *获取岗位名称
    */
    public String getPostName() { 
        return  this.postName;
     }
    /**
    *设定公司名称
    */
    public void setCorName(String corName) { 
        this.corName=corName;
     }
    /**
    *获取公司名称
    */
    public String getCorName() { 
        return  this.corName;
     }
    /**
    *设定开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定结束时间
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *获取结束时间
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定岗位编码
    */
    public void setPostCode(String postCode) { 
        this.postCode=postCode;
     }
    /**
    *获取岗位编码
    */
    public String getPostCode() { 
        return  this.postCode;
     }
    /**
    *设定行业
    */
    public void setIndustryCode(String industryCode) { 
        this.industryCode=industryCode;
     }
    /**
    *获取行业
    */
    public String getIndustryCode() { 
        return  this.industryCode;
     }
    /**
    *设定行业名称
    */
    public void setIndustryName(String industryName) { 
        this.industryName=industryName;
     }
    /**
    *获取行业名称
    */
    public String getIndustryName() { 
        return  this.industryName;
     }
    /**
    *设定岗位详情
    */
    public void setPostDesc(String postDesc) { 
        this.postDesc=postDesc;
     }
    /**
    *获取岗位详情
    */
    public String getPostDesc() { 
        return  this.postDesc;
     }

 } 

