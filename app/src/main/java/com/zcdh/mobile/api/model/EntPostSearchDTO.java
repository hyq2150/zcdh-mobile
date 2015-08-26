/** 
*  EntPostSearchDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索岗位的DTO 
*  @author focus, 2014-4-19 下午3:59:43 
*/  
public class EntPostSearchDTO  implements Serializable { 
    private Long postId  ; 
    private Long entId  ; 
    private String postName  ; 
    private String entName  ; 
    private String areaName  ; 
    private Date publishDate  ; 
    private String salaryName  ; 
    private String degreeName  ; 
    private Integer headCounts  ; 
    private Integer headTotalCount  ; 
    private String workProperty  ; 
    private Boolean isFilled  ; 
    private Double lat  ; 
    private Double lon  ; 
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
    public void setPublishDate(Date publishDate) { 
        this.publishDate=publishDate;
     }
    /**
    *无功能描述
    */
    public Date getPublishDate() { 
        return  this.publishDate;
     }
    /**
    *无功能描述
    */
    public void setSalaryName(String salaryName) { 
        this.salaryName=salaryName;
     }
    /**
    *无功能描述
    */
    public String getSalaryName() { 
        return  this.salaryName;
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
    public void setHeadCounts(Integer headCounts) { 
        this.headCounts=headCounts;
     }
    /**
    *无功能描述
    */
    public Integer getHeadCounts() { 
        return  this.headCounts;
     }
    /**
    *无功能描述
    */
    public void setHeadTotalCount(Integer headTotalCount) { 
        this.headTotalCount=headTotalCount;
     }
    /**
    *无功能描述
    */
    public Integer getHeadTotalCount() { 
        return  this.headTotalCount;
     }
    /**
    *无功能描述
    */
    public void setWorkProperty(String workProperty) { 
        this.workProperty=workProperty;
     }
    /**
    *无功能描述
    */
    public String getWorkProperty() { 
        return  this.workProperty;
     }
    /**
    *无功能描述
    */
    public void setIsFilled(Boolean isFilled) { 
        this.isFilled=isFilled;
     }
    /**
    *无功能描述
    */
    public Boolean getIsFilled() { 
        return  this.isFilled;
     }
    /**
    *无功能描述
    */
    public void setLat(Double lat) { 
        this.lat=lat;
     }
    /**
    *无功能描述
    */
    public Double getLat() { 
        return  this.lat;
     }
    /**
    *无功能描述
    */
    public void setLon(Double lon) { 
        this.lon=lon;
     }
    /**
    *无功能描述
    */
    public Double getLon() { 
        return  this.lon;
     }

 } 

