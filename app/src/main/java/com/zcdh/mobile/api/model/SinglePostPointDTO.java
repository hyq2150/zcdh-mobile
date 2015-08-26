/** 
*  SinglePostPointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 一个点一个岗位的实体 
*  @author focus, 2014-6-18 下午3:16:04 
*/  
public class SinglePostPointDTO  implements Serializable { 
    //岗位id 
    private Long post_id  ; 
    //经度 
    private Double lon  ; 
    //纬度 
    private Double lat  ; 
    /**
    *设定岗位id
    */
    public void setPost_id(Long post_id) { 
        this.post_id=post_id;
     }
    /**
    *获取岗位id
    */
    public Long getPost_id() { 
        return  this.post_id;
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

 } 

