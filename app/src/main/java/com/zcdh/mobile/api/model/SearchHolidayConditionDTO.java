/** 
*  SearchHolidayConditionDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索条件的DTO 
*  @author focus, 2014-4-19 下午2:24:59 
*/  
public class SearchHolidayConditionDTO  implements Serializable { 
    private Long user_id  ; 
    private List<String> areaNames  ; 
    private List<String> areaCodes  ; 
    private List<String> industryCodes  ; 
    private List<String> industryNames  ; 
    private List<String> majorCodes  ; 
    private List<String> majorNames  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    private String workPropertyCode  ; 
    private Long bannerId  ; 
    private List<String> postNames  ; 
    private List<String> postCodes  ; 
    private List<LbsParam> lbs  ; 
    /**
    *无功能描述
    */
    public void setUser_id(Long user_id) { 
        this.user_id=user_id;
     }
    /**
    *无功能描述
    */
    public Long getUser_id() { 
        return  this.user_id;
     }
    /**
    *无功能描述
    */
    public void setAreaNames(List<String> areaNames) { 
        this.areaNames=areaNames;
     }
    /**
    *无功能描述
    */
    public List<String> getAreaNames() { 
        return  this.areaNames;
     }
    /**
    *无功能描述
    */
    public void setAreaCodes(List<String> areaCodes) { 
        this.areaCodes=areaCodes;
     }
    /**
    *无功能描述
    */
    public List<String> getAreaCodes() { 
        return  this.areaCodes;
     }
    /**
    *无功能描述
    */
    public void setIndustryCodes(List<String> industryCodes) { 
        this.industryCodes=industryCodes;
     }
    /**
    *无功能描述
    */
    public List<String> getIndustryCodes() { 
        return  this.industryCodes;
     }
    /**
    *无功能描述
    */
    public void setIndustryNames(List<String> industryNames) { 
        this.industryNames=industryNames;
     }
    /**
    *无功能描述
    */
    public List<String> getIndustryNames() { 
        return  this.industryNames;
     }
    /**
    *无功能描述
    */
    public void setMajorCodes(List<String> majorCodes) { 
        this.majorCodes=majorCodes;
     }
    /**
    *无功能描述
    */
    public List<String> getMajorCodes() { 
        return  this.majorCodes;
     }
    /**
    *无功能描述
    */
    public void setMajorNames(List<String> majorNames) { 
        this.majorNames=majorNames;
     }
    /**
    *无功能描述
    */
    public List<String> getMajorNames() { 
        return  this.majorNames;
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
    public void setWorkPropertyCode(String workPropertyCode) { 
        this.workPropertyCode=workPropertyCode;
     }
    /**
    *无功能描述
    */
    public String getWorkPropertyCode() { 
        return  this.workPropertyCode;
     }
    /**
    *无功能描述
    */
    public void setBannerId(Long bannerId) { 
        this.bannerId=bannerId;
     }
    /**
    *无功能描述
    */
    public Long getBannerId() { 
        return  this.bannerId;
     }
    /**
    *无功能描述
    */
    public void setPostNames(List<String> postNames) { 
        this.postNames=postNames;
     }
    /**
    *无功能描述
    */
    public List<String> getPostNames() { 
        return  this.postNames;
     }
    /**
    *无功能描述
    */
    public void setPostCodes(List<String> postCodes) { 
        this.postCodes=postCodes;
     }
    /**
    *无功能描述
    */
    public List<String> getPostCodes() { 
        return  this.postCodes;
     }
    /**
    *无功能描述
    */
    public void setLbs(List<LbsParam> lbs) { 
        this.lbs=lbs;
     }
    /**
    *无功能描述
    */
    public List<LbsParam> getLbs() { 
        return  this.lbs;
     }

 } 

