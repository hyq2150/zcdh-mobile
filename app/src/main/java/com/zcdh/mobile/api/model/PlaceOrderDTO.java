/** 
*  PlaceOrderDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 工作申请 - 下订单的DTO 
*  @author focus, 2014-4-19 下午4:31:41 
*/  
public class PlaceOrderDTO  implements Serializable { 
    private Long userId  ; 
    private BigDecimal earnestMoney  ; 
    private Boolean isApplied  ; 
    /**
    *无功能描述
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *无功能描述
    */
    public void setEarnestMoney(BigDecimal earnestMoney) { 
        this.earnestMoney=earnestMoney;
     }
    /**
    *无功能描述
    */
    public BigDecimal getEarnestMoney() { 
        return  this.earnestMoney;
     }
    /**
    *无功能描述
    */
    public void setIsApplied(Boolean isApplied) { 
        this.isApplied=isApplied;
     }
    /**
    *无功能描述
    */
    public Boolean getIsApplied() { 
        return  this.isApplied;
     }

 } 

