/** 
*  FitPostListDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-22 下午4:03:28 
*/  
public class FitPostListDTO  implements Serializable { 
    private String areaCode  ; 
    private String areaName  ; 
    private Integer fitPostTotals  ; 
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
    public void setAreaName(String areaName) { 
        this.areaName=areaName;
     }
    /**
    *无功能描述
    */
    public String getAreaName() { 
        return  this.areaName;
     }
    /**
    *无功能描述
    */
    public void setFitPostTotals(Integer fitPostTotals) { 
        this.fitPostTotals=fitPostTotals;
     }
    /**
    *无功能描述
    */
    public Integer getFitPostTotals() { 
        return  this.fitPostTotals;
     }

 } 

