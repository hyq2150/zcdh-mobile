/** 
*  JobUserSubscriptionDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 用户订阅 
*  @author hw, 2014-5-14 下午2:30:53 
*/  
public class JobUserSubscriptionDTO  implements Serializable { 
    //订阅id 
    private Long subId  ; 
    //用户id 
    private Long userId  ; 
    //行业编码 
    private String industryCode  ; 
    //行业名称 
    private String industryName  ; 
    //岗位编码 
    private String postCode  ; 
    //岗位名称 
    private String postName  ; 
    //地区编码 
    private String areaCode  ; 
    //地区名称 
    private String areaName  ; 
    //是否已读 
    //  1 已读， 0 未读 
    private Integer is_read  ; 
    /**
    *设定订阅id
    */
    public void setSubId(Long subId) { 
        this.subId=subId;
     }
    /**
    *获取订阅id
    */
    public Long getSubId() { 
        return  this.subId;
     }
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
    *设定行业编码
    */
    public void setIndustryCode(String industryCode) { 
        this.industryCode=industryCode;
     }
    /**
    *获取行业编码
    */
    public String getIndustryCode() { 
        return  this.industryCode;
     }
    /**
    *设定行业名称
    */
    public void setIndustryName(String industryName) { 
        this.industryName=industryName;
     }
    /**
    *获取行业名称
    */
    public String getIndustryName() { 
        return  this.industryName;
     }
    /**
    *设定岗位编码
    */
    public void setPostCode(String postCode) { 
        this.postCode=postCode;
     }
    /**
    *获取岗位编码
    */
    public String getPostCode() { 
        return  this.postCode;
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
    *设定地区编码
    */
    public void setAreaCode(String areaCode) { 
        this.areaCode=areaCode;
     }
    /**
    *获取地区编码
    */
    public String getAreaCode() { 
        return  this.areaCode;
     }
    /**
    *设定地区名称
    */
    public void setAreaName(String areaName) { 
        this.areaName=areaName;
     }
    /**
    *获取地区名称
    */
    public String getAreaName() { 
        return  this.areaName;
     }
    /**
    *设定是否已读  1 已读， 0 未读
    */
    public void setIs_read(Integer is_read) { 
        this.is_read=is_read;
     }
    /**
    *获取是否已读  1 已读， 0 未读
    */
    public Integer getIs_read() { 
        return  this.is_read;
     }

 } 

