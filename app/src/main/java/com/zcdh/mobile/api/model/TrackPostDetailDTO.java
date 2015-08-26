/** 
*  TrackPostDetailDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-17 上午9:11:11 
*/  
public class TrackPostDetailDTO  implements Serializable { 
    //企业id 
    private Long entId  ; 
    //岗位id 
    private Long postId  ; 
    //岗位名称 
    private String postName  ; 
    //工资 
    private String salary  ; 
    //岗位给地址 
    private String postAddress  ; 
    //求职状态 
    private String postStatus  ; 
    //工作年限 
    private String serviceYear  ; 
    //学历 
    private String degreeName  ; 
    //申请数量 
    private Integer applyCount  ; 
    //访问数量 
    private Integer visitCount  ; 
    //招聘人数 
    private Integer employCount  ; 
    //发布时间 
    private Date publishDate  ; 
    //企业名称 
    private String entName  ; 
    //岗位标签 
    private List<String> tagNames  ; 
    //订单跟踪明细 
    private List<TrackPostDTO> tarckPosts  ; 
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
    *设定工资
    */
    public void setSalary(String salary) { 
        this.salary=salary;
     }
    /**
    *获取工资
    */
    public String getSalary() { 
        return  this.salary;
     }
    /**
    *设定岗位给地址
    */
    public void setPostAddress(String postAddress) { 
        this.postAddress=postAddress;
     }
    /**
    *获取岗位给地址
    */
    public String getPostAddress() { 
        return  this.postAddress;
     }
    /**
    *设定求职状态
    */
    public void setPostStatus(String postStatus) { 
        this.postStatus=postStatus;
     }
    /**
    *获取求职状态
    */
    public String getPostStatus() { 
        return  this.postStatus;
     }
    /**
    *设定工作年限
    */
    public void setServiceYear(String serviceYear) { 
        this.serviceYear=serviceYear;
     }
    /**
    *获取工作年限
    */
    public String getServiceYear() { 
        return  this.serviceYear;
     }
    /**
    *设定学历
    */
    public void setDegreeName(String degreeName) { 
        this.degreeName=degreeName;
     }
    /**
    *获取学历
    */
    public String getDegreeName() { 
        return  this.degreeName;
     }
    /**
    *设定申请数量
    */
    public void setApplyCount(Integer applyCount) { 
        this.applyCount=applyCount;
     }
    /**
    *获取申请数量
    */
    public Integer getApplyCount() { 
        return  this.applyCount;
     }
    /**
    *设定访问数量
    */
    public void setVisitCount(Integer visitCount) { 
        this.visitCount=visitCount;
     }
    /**
    *获取访问数量
    */
    public Integer getVisitCount() { 
        return  this.visitCount;
     }
    /**
    *设定招聘人数
    */
    public void setEmployCount(Integer employCount) { 
        this.employCount=employCount;
     }
    /**
    *获取招聘人数
    */
    public Integer getEmployCount() { 
        return  this.employCount;
     }
    /**
    *设定发布时间
    */
    public void setPublishDate(Date publishDate) { 
        this.publishDate=publishDate;
     }
    /**
    *获取发布时间
    */
    public Date getPublishDate() { 
        return  this.publishDate;
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
    *设定岗位标签
    */
    public void setTagNames(List<String> tagNames) { 
        this.tagNames=tagNames;
     }
    /**
    *获取岗位标签
    */
    public List<String> getTagNames() { 
        return  this.tagNames;
     }
    /**
    *设定订单跟踪明细
    */
    public void setTarckPosts(List<TrackPostDTO> tarckPosts) { 
        this.tarckPosts=tarckPosts;
     }
    /**
    *获取订单跟踪明细
    */
    public List<TrackPostDTO> getTarckPosts() { 
        return  this.tarckPosts;
     }

 } 

