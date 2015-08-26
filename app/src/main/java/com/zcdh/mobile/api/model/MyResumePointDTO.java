/** 
*  MyResumePointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-18 下午3:04:59 
*/  
public class MyResumePointDTO  implements Serializable { 
    //id 
    private Long id  ; 
    //经度 
    private Double lon  ; 
    //纬度 
    private Double lat  ; 
    //有效期 
    private Date expireDate  ; 
    /**
    *设定id
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取id
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定经度
    */
    public void setLon(Double lon) { 
        this.lon=lon;
     }
    /**
    *获取经度
    */
    public Double getLon() { 
        return  this.lon;
     }
    /**
    *设定纬度
    */
    public void setLat(Double lat) { 
        this.lat=lat;
     }
    /**
    *获取纬度
    */
    public Double getLat() { 
        return  this.lat;
     }
    /**
    *设定有效期
    */
    public void setExpireDate(Date expireDate) { 
        this.expireDate=expireDate;
     }
    /**
    *获取有效期
    */
    public Date getExpireDate() { 
        return  this.expireDate;
     }

 } 

