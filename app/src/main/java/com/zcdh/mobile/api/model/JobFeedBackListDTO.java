/** 
*  JobFeedBackListDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 已经反馈列表 
*  @author focus, 2014-5-8 下午7:02:37 
*/  
public class JobFeedBackListDTO  implements Serializable { 
    //用户id 
    private Long userId  ; 
    //内容 
    private String content  ; 
    //创建时间 
    private Date createTime  ; 
    //0：用户反馈，1：管理员回复 
    private Integer type  ; 
    //feedBack ：id 
    private Long id  ; 
    /**
    *设定用户id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取用户id
    */
    public Long getUserId() { 
        return  this.userId;
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
    *设定创建时间
    */
    public void setCreateTime(Date createTime) { 
        this.createTime=createTime;
     }
    /**
    *获取创建时间
    */
    public Date getCreateTime() { 
        return  this.createTime;
     }
    /**
    *设定0：用户反馈，1：管理员回复
    */
    public void setType(Integer type) { 
        this.type=type;
     }
    /**
    *获取0：用户反馈，1：管理员回复
    */
    public Integer getType() { 
        return  this.type;
     }
    /**
    *设定feedBack ：id
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取feedBack ：id
    */
    public Long getId() { 
        return  this.id;
     }

 } 

