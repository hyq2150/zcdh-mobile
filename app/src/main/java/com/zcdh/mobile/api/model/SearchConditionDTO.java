/** 
*  SearchConditionDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索条件的DTO 
*  @author focus, 2014-4-8 下午8:29:29 
*/  
public class SearchConditionDTO  implements Serializable { 
    private long userId  ; 
    private String postAliases  ; 
    private String techniclName  ; 
    private String entName  ; 
    private String postCode  ; 
    private String industryCode  ; 
    private String propertyCode  ; 
    private String employNum  ; 
    private String areaCode  ; 
    private String timeBucket  ; 
    private String salaryCode  ; 
    private String tagCode  ; 
    private Double lat  ; 
    private Double lon  ; 
    private Double distance  ; 
    private String keyWord  ; 
    private String majorCode  ; 
    private String postPropertyCode  ; 
    private List<String> areaCodes  ; 
    private String areaCodesStr  ; 
    private List<String> weekdays  ; 
    private String weekdaysStr  ; 
    private List<String> postCodes  ; 
    /**
    *无功能描述
    */
    public void setUserId(long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public long getUserId() { 
        return  this.userId;
     }
    /**
    *无功能描述
    */
    public void setPostAliases(String postAliases) { 
        this.postAliases=postAliases;
     }
    /**
    *无功能描述
    */
    public String getPostAliases() { 
        return  this.postAliases;
     }
    /**
    *无功能描述
    */
    public void setTechniclName(String techniclName) { 
        this.techniclName=techniclName;
     }
    /**
    *无功能描述
    */
    public String getTechniclName() { 
        return  this.techniclName;
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
    public void setPostCode(String postCode) { 
        this.postCode=postCode;
     }
    /**
    *无功能描述
    */
    public String getPostCode() { 
        return  this.postCode;
     }
    /**
    *无功能描述
    */
    public void setIndustryCode(String industryCode) { 
        this.industryCode=industryCode;
     }
    /**
    *无功能描述
    */
    public String getIndustryCode() { 
        return  this.industryCode;
     }
    /**
    *无功能描述
    */
    public void setPropertyCode(String propertyCode) { 
        this.propertyCode=propertyCode;
     }
    /**
    *无功能描述
    */
    public String getPropertyCode() { 
        return  this.propertyCode;
     }
    /**
    *无功能描述
    */
    public void setEmployNum(String employNum) { 
        this.employNum=employNum;
     }
    /**
    *无功能描述
    */
    public String getEmployNum() { 
        return  this.employNum;
     }
    /**
    *无功能描述
    */
    public void setAreaCode(String areaCode) { 
        this.areaCode=areaCode;
     }
    /**
    *无功能描述
    */
    public String getAreaCode() { 
        return  this.areaCode;
     }
    /**
    *无功能描述
    */
    public void setTimeBucket(String timeBucket) { 
        this.timeBucket=timeBucket;
     }
    /**
    *无功能描述
    */
    public String getTimeBucket() { 
        return  this.timeBucket;
     }
    /**
    *无功能描述
    */
    public void setSalaryCode(String salaryCode) { 
        this.salaryCode=salaryCode;
     }
    /**
    *无功能描述
    */
    public String getSalaryCode() { 
        return  this.salaryCode;
     }
    /**
    *无功能描述
    */
    public void setTagCode(String tagCode) { 
        this.tagCode=tagCode;
     }
    /**
    *无功能描述
    */
    public String getTagCode() { 
        return  this.tagCode;
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
    /**
    *无功能描述
    */
    public void setDistance(Double distance) { 
        this.distance=distance;
     }
    /**
    *无功能描述
    */
    public Double getDistance() { 
        return  this.distance;
     }
    /**
    *无功能描述
    */
    public void setKeyWord(String keyWord) { 
        this.keyWord=keyWord;
     }
    /**
    *无功能描述
    */
    public String getKeyWord() { 
        return  this.keyWord;
     }
    /**
    *无功能描述
    */
    public void setMajorCode(String majorCode) { 
        this.majorCode=majorCode;
     }
    /**
    *无功能描述
    */
    public String getMajorCode() { 
        return  this.majorCode;
     }
    /**
    *无功能描述
    */
    public void setPostPropertyCode(String postPropertyCode) { 
        this.postPropertyCode=postPropertyCode;
     }
    /**
    *无功能描述
    */
    public String getPostPropertyCode() { 
        return  this.postPropertyCode;
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
    public void setAreaCodesStr(String areaCodesStr) { 
        this.areaCodesStr=areaCodesStr;
     }
    /**
    *无功能描述
    */
    public String getAreaCodesStr() { 
        return  this.areaCodesStr;
     }
    /**
    *无功能描述
    */
    public void setWeekdays(List<String> weekdays) { 
        this.weekdays=weekdays;
     }
    /**
    *无功能描述
    */
    public List<String> getWeekdays() { 
        return  this.weekdays;
     }
    /**
    *无功能描述
    */
    public void setWeekdaysStr(String weekdaysStr) { 
        this.weekdaysStr=weekdaysStr;
     }
    /**
    *无功能描述
    */
    public String getWeekdaysStr() { 
        return  this.weekdaysStr;
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

 } 

