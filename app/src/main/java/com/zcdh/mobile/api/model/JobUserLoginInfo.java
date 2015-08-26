/** 
*  JobUserLoginInfo 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 登陆日志 
*  @author focus, 2014-8-15 下午3:52:03 
*/  
public class JobUserLoginInfo  implements Serializable { 
    //用户id 
    private Long userId  ; 
    //经度 
    private Double lon  ; 
    //纬度 
    private Double lat  ; 
    //手机型号 
    private String modelNumber  ; 
    //系统版本： 
    private String sysVersion  ; 
    //3：Andriod设备； 4：iOS设备 
    private Integer sysType  ; 
    private String miei  ; 
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
    *设定手机型号
    */
    public void setModelNumber(String modelNumber) { 
        this.modelNumber=modelNumber;
     }
    /**
    *获取手机型号
    */
    public String getModelNumber() { 
        return  this.modelNumber;
     }
    /**
    *设定系统版本：
    */
    public void setSysVersion(String sysVersion) { 
        this.sysVersion=sysVersion;
     }
    /**
    *获取系统版本：
    */
    public String getSysVersion() { 
        return  this.sysVersion;
     }
    /**
    *设定3：Andriod设备； 4：iOS设备
    */
    public void setSysType(Integer sysType) { 
        this.sysType=sysType;
     }
    /**
    *获取3：Andriod设备； 4：iOS设备
    */
    public Integer getSysType() { 
        return  this.sysType;
     }
    /**
    *无功能描述
    */
    public void setMiei(String miei) { 
        this.miei=miei;
     }
    /**
    *无功能描述
    */
    public String getMiei() { 
        return  this.miei;
     }

 } 

