/** 
*  HotCityDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-18 下午2:42:36 
*/  
public class HotCityDTO  implements Serializable { 
    //地区名称 
    private String areaName  ; 
    //地区编码 
    private String areaCode  ; 
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

 } 

