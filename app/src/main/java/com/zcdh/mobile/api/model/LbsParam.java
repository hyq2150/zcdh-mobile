/** 
*  LbsParam 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 传参数的经纬度 
*  @author focus, 2013-11-28 下午2:35:25 
*/  
public class LbsParam  implements Serializable { 
    private Long scope  ; 
    private Double lat  ; 
    private Double lon  ; 
    /**
    *无功能描述
    */
    public void setScope(Long scope) { 
        this.scope=scope;
     }
    /**
    *无功能描述
    */
    public Long getScope() { 
        return  this.scope;
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

