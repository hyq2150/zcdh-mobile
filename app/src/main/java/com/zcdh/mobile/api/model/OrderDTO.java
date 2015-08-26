/** 
*  OrderDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 订单详情的DTO 
*   
*  @author focus, 2014-4-19 下午5:14:33 
*/  
public class OrderDTO  implements Serializable { 
    private String customerSrviceName  ; 
    private String publishServiceNum  ; 
    private String customerServiceMobile  ; 
    private String customerServiceQQ  ; 
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

 } 

