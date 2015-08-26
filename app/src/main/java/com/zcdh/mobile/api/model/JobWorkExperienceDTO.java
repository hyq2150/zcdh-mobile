/** 
*  JobWorkExperienceDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-15 下午8:21:59 
*/  
public class JobWorkExperienceDTO  implements Serializable { 
    //工作经历的岗位的id 
    private Long wepPostId  ; 
    //工作经历id 
    private Long workExperienceId  ; 
    //工作时间： 月为单位 
    private Integer workMonth  ; 
    //岗位名称 
    private String postName  ; 
    //公司名称 
    private String corName  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    private String postDes  ; 
    /**
    *设定工作经历的岗位的id
    */
    public void setWepPostId(Long wepPostId) { 
        this.wepPostId=wepPostId;
     }
    /**
    *获取工作经历的岗位的id
    */
    public Long getWepPostId() { 
        return  this.wepPostId;
     }
    /**
    *设定工作经历id
    */
    public void setWorkExperienceId(Long workExperienceId) { 
        this.workExperienceId=workExperienceId;
     }
    /**
    *获取工作经历id
    */
    public Long getWorkExperienceId() { 
        return  this.workExperienceId;
     }
    /**
    *设定工作时间： 月为单位
    */
    public void setWorkMonth(Integer workMonth) { 
        this.workMonth=workMonth;
     }
    /**
    *获取工作时间： 月为单位
    */
    public Integer getWorkMonth() { 
        return  this.workMonth;
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
    *无功能描述
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *无功能描述
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *无功能描述
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *无功能描述
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *无功能描述
    */
    public void setPostDes(String postDes) { 
        this.postDes=postDes;
     }
    /**
    *无功能描述
    */
    public String getPostDes() { 
        return  this.postDes;
     }

 } 

