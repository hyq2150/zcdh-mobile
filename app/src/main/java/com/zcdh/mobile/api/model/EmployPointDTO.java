/** 
*  EmployPointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业招聘的点 
*  @author focus, 2014-6-18 下午3:04:50 
*/  
public class EmployPointDTO  implements Serializable { 
    //经度 
    private Double lon  ; 
    //纬度 
    private Double lat  ; 
    //企业id列表 
    private List<Long> entId  ; 
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
    *设定企业id列表
    */
    public void setEntId(List<Long> entId) { 
        this.entId=entId;
     }
    /**
    *获取企业id列表
    */
    public List<Long> getEntId() { 
        return  this.entId;
     }

 } 

