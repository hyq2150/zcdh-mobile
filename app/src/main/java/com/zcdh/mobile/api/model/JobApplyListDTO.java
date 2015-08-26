/** 
*  JobApplyListDTO 
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
public class JobApplyListDTO  implements Serializable { 
    private Long id  ; 
    //岗位id 
    private Long postId  ; 
    //企业名称 
    private String entName  ; 
    //岗位名称 
    private String postName  ; 
    //面试时间 
    private Date applyTime  ; 
    //流程最后一条状态 
    private Integer status  ; 
    /**
    *无功能描述
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *无功能描述
    */
    public Long getId() { 
        return  this.id;
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
    *设定面试时间
    */
    public void setApplyTime(Date applyTime) { 
        this.applyTime=applyTime;
     }
    /**
    *获取面试时间
    */
    public Date getApplyTime() { 
        return  this.applyTime;
     }
    /**
    *设定流程最后一条状态
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取流程最后一条状态
    */
    public Integer getStatus() { 
        return  this.status;
     }

 } 

