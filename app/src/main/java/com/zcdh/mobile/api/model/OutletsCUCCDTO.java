/** 
*  OutletsCUCCDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 联通网点的DTO 
*  @author focus, 2014-12-31 下午2:40:00 
*/  
public class OutletsCUCCDTO  implements Serializable { 
    private Long lon  ; 
    private Long lat  ; 
    private String contact  ; 
    private String phone  ; 
    private String outletsAddress  ; 
    private String outletsName  ; 
    private String ZIP  ; 
    /**
    *无功能描述
    */
    public void setLon(Long lon) { 
        this.lon=lon;
     }
    /**
    *无功能描述
    */
    public Long getLon() { 
        return  this.lon;
     }
    /**
    *无功能描述
    */
    public void setLat(Long lat) { 
        this.lat=lat;
     }
    /**
    *无功能描述
    */
    public Long getLat() { 
        return  this.lat;
     }
    /**
    *无功能描述
    */
    public void setContact(String contact) { 
        this.contact=contact;
     }
    /**
    *无功能描述
    */
    public String getContact() { 
        return  this.contact;
     }
    /**
    *无功能描述
    */
    public void setPhone(String phone) { 
        this.phone=phone;
     }
    /**
    *无功能描述
    */
    public String getPhone() { 
        return  this.phone;
     }
    /**
    *无功能描述
    */
    public void setOutletsAddress(String outletsAddress) { 
        this.outletsAddress=outletsAddress;
     }
    /**
    *无功能描述
    */
    public String getOutletsAddress() { 
        return  this.outletsAddress;
     }
    /**
    *无功能描述
    */
    public void setOutletsName(String outletsName) { 
        this.outletsName=outletsName;
     }
    /**
    *无功能描述
    */
    public String getOutletsName() { 
        return  this.outletsName;
     }
    /**
    *无功能描述
    */
    public void setZIP(String ZIP) { 
        this.ZIP=ZIP;
     }
    /**
    *无功能描述
    */
    public String getZIP() { 
        return  this.ZIP;
     }

 } 

