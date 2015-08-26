/** 
*  JobObjectiveDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 上午10:30:22 
*/  
public class JobObjectiveDTO  implements Serializable { 
    //用户的id 
    private Long userId  ; 
    //最低工资 
    private Integer minSalary  ; 
    //最高工资 
    private Integer maxSalary  ; 
    //面议 TRUE 
    private String negotiableSalary  ; 
    //求职意向岗位 
    private List<JobObjectivePostDTO> posts  ; 
    //求职意向地区 
    private List<JobObjectiveAreaDTO> areas  ; 
    //求职意向行业 
    private List<JobObjectiveIndustryDTO> industries  ; 
    //工作类型的编码 
    private String workPropertyCode  ; 
    //工作类型的名称 
    private String workPropertyName  ; 
    /**
    *设定用户的id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取用户的id
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定最低工资
    */
    public void setMinSalary(Integer minSalary) { 
        this.minSalary=minSalary;
     }
    /**
    *获取最低工资
    */
    public Integer getMinSalary() { 
        return  this.minSalary;
     }
    /**
    *设定最高工资
    */
    public void setMaxSalary(Integer maxSalary) { 
        this.maxSalary=maxSalary;
     }
    /**
    *获取最高工资
    */
    public Integer getMaxSalary() { 
        return  this.maxSalary;
     }
    /**
    *设定面议 TRUE
    */
    public void setNegotiableSalary(String negotiableSalary) { 
        this.negotiableSalary=negotiableSalary;
     }
    /**
    *获取面议 TRUE
    */
    public String getNegotiableSalary() { 
        return  this.negotiableSalary;
     }
    /**
    *设定求职意向岗位
    */
    public void setPosts(List<JobObjectivePostDTO> posts) { 
        this.posts=posts;
     }
    /**
    *获取求职意向岗位
    */
    public List<JobObjectivePostDTO> getPosts() { 
        return  this.posts;
     }
    /**
    *设定求职意向地区
    */
    public void setAreas(List<JobObjectiveAreaDTO> areas) { 
        this.areas=areas;
     }
    /**
    *获取求职意向地区
    */
    public List<JobObjectiveAreaDTO> getAreas() { 
        return  this.areas;
     }
    /**
    *设定求职意向行业
    */
    public void setIndustries(List<JobObjectiveIndustryDTO> industries) { 
        this.industries=industries;
     }
    /**
    *获取求职意向行业
    */
    public List<JobObjectiveIndustryDTO> getIndustries() { 
        return  this.industries;
     }
    /**
    *设定工作类型的编码
    */
    public void setWorkPropertyCode(String workPropertyCode) { 
        this.workPropertyCode=workPropertyCode;
     }
    /**
    *获取工作类型的编码
    */
    public String getWorkPropertyCode() { 
        return  this.workPropertyCode;
     }
    /**
    *设定工作类型的名称
    */
    public void setWorkPropertyName(String workPropertyName) { 
        this.workPropertyName=workPropertyName;
     }
    /**
    *获取工作类型的名称
    */
    public String getWorkPropertyName() { 
        return  this.workPropertyName;
     }

 } 

