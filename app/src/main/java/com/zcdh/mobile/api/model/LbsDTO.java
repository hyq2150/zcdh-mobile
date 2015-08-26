/** 
*  LbsDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-26 下午6:08:38 
*/  
public class LbsDTO  implements Serializable { 
    private double minLon  ; 
    private double minLat  ; 
    private double maxLon  ; 
    private double maxLat  ; 
    /**
    *无功能描述
    */
    public void setMinLon(double minLon) { 
        this.minLon=minLon;
     }
    /**
    *无功能描述
    */
    public double getMinLon() { 
        return  this.minLon;
     }
    /**
    *无功能描述
    */
    public void setMinLat(double minLat) { 
        this.minLat=minLat;
     }
    /**
    *无功能描述
    */
    public double getMinLat() { 
        return  this.minLat;
     }
    /**
    *无功能描述
    */
    public void setMaxLon(double maxLon) { 
        this.maxLon=maxLon;
     }
    /**
    *无功能描述
    */
    public double getMaxLon() { 
        return  this.maxLon;
     }
    /**
    *无功能描述
    */
    public void setMaxLat(double maxLat) { 
        this.maxLat=maxLat;
     }
    /**
    *无功能描述
    */
    public double getMaxLat() { 
        return  this.maxLat;
     }

 } 

