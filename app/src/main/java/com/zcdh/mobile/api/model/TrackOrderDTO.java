/** 
*  TrackOrderDTO 
* 
*  Created Date: 2015-10-12 17:31:53 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-25 上午11:11:30 
*/  
public class TrackOrderDTO  implements Serializable { 
    private String customerSrviceName  ; 
    private String publishServiceNum  ; 
    private String customerServiceMobile  ; 
    private String customerServiceQQ  ; 
    private List<TrackOrder> trackOrders  ; 
    /**
    *无功能描述
    */
    public void setCustomerSrviceName(String customerSrviceName) { 
        this.customerSrviceName=customerSrviceName;
     }
    /**
    *无功能描述
    */
    public String getCustomerSrviceName() { 
        return  this.customerSrviceName;
     }
    /**
    *无功能描述
    */
    public void setPublishServiceNum(String publishServiceNum) { 
        this.publishServiceNum=publishServiceNum;
     }
    /**
    *无功能描述
    */
    public String getPublishServiceNum() { 
        return  this.publishServiceNum;
     }
    /**
    *无功能描述
    */
    public void setCustomerServiceMobile(String customerServiceMobile) { 
        this.customerServiceMobile=customerServiceMobile;
     }
    /**
    *无功能描述
    */
    public String getCustomerServiceMobile() { 
        return  this.customerServiceMobile;
     }
    /**
    *无功能描述
    */
    public void setCustomerServiceQQ(String customerServiceQQ) { 
        this.customerServiceQQ=customerServiceQQ;
     }
    /**
    *无功能描述
    */
    public String getCustomerServiceQQ() { 
        return  this.customerServiceQQ;
     }
    /**
    *无功能描述
    */
    public void setTrackOrders(List<TrackOrder> trackOrders) { 
        this.trackOrders=trackOrders;
     }
    /**
    *无功能描述
    */
    public List<TrackOrder> getTrackOrders() { 
        return  this.trackOrders;
     }

 } 

