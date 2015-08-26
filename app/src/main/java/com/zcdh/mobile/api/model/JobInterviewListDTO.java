/** 
*  JobInterviewListDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午7:43:22 
*/  
public class JobInterviewListDTO  implements Serializable { 
    //面试邀请id 
    private Long interviewId  ; 
    //状态，是否已经读取 0 未读取， 1 已读 
    private Integer status  ; 
    //企业id 
    private Long entId  ; 
    //企业名称 
    private String entName  ; 
    //内容 
    private String content  ; 
    //面试时间 
    private Date sendTime  ; 
    /**
    *设定面试邀请id
    */
    public void setInterviewId(Long interviewId) { 
        this.interviewId=interviewId;
     }
    /**
    *获取面试邀请id
    */
    public Long getInterviewId() { 
        return  this.interviewId;
     }
    /**
    *设定状态，是否已经读取 0 未读取， 1 已读
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取状态，是否已经读取 0 未读取， 1 已读
    */
    public Integer getStatus() { 
        return  this.status;
     }
    /**
    *设定企业id
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *获取企业id
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *设定企业名称
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *获取企业名称
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *设定内容
    */
    public void setContent(String content) { 
        this.content=content;
     }
    /**
    *获取内容
    */
    public String getContent() { 
        return  this.content;
     }
    /**
    *设定面试时间
    */
    public void setSendTime(Date sendTime) { 
        this.sendTime=sendTime;
     }
    /**
    *获取面试时间
    */
    public Date getSendTime() { 
        return  this.sendTime;
     }

 } 

