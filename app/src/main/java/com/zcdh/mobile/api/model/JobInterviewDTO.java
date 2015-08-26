/** 
*  JobInterviewDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午7:43:22 
*/  
public class JobInterviewDTO  implements Serializable { 
    //企业名称 
    private String entName  ; 
    //标题 
    private String title  ; 
    //内容 
    private String content  ; 
    //面试时间 
    private Date sendTime  ; 
    //企业id 
    private Long entId  ; 
    //岗位id 
    private Long postId  ; 
    //岗位给名称 
    private String postName  ; 
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
    *设定标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取标题
    */
    public String getTitle() { 
        return  this.title;
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
    *设定岗位id
    */
    public void setPostId(Long postId) { 
        this.postId=postId;
     }
    /**
    *获取岗位id
    */
    public Long getPostId() { 
        return  this.postId;
     }
    /**
    *设定岗位给名称
    */
    public void setPostName(String postName) { 
        this.postName=postName;
     }
    /**
    *获取岗位给名称
    */
    public String getPostName() { 
        return  this.postName;
     }

 } 

