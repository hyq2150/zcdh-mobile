/** 
*  JobSearchDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索结果集 
*  @author focus, 2014-4-4 上午9:58:20 
*/  
public class JobSearchDTO  implements Serializable { 
    private Long userId  ; 
    private String keyWord  ; 
    private String areaCode  ; 
    private String tagCode  ; 
    private Double lat  ; 
    private Double lon  ; 
    /**
    *无功能描述
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public Long getUserId() { 
        return  this.userId;
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

 } 

