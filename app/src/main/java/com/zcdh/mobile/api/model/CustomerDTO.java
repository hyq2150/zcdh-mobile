/** 
*  CustomerDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-25 下午2:33:01 
*/  
public class CustomerDTO  implements Serializable { 
    private Integer totals  ; 
    private Long accountId  ; 
    private String name  ; 
    /**
    *无功能描述
    */
    public void setTotals(Integer totals) { 
        this.totals=totals;
     }
    /**
    *无功能描述
    */
    public Integer getTotals() { 
        return  this.totals;
     }
    /**
    *无功能描述
    */
    public void setAccountId(Long accountId) { 
        this.accountId=accountId;
     }
    /**
    *无功能描述
    */
    public Long getAccountId() { 
        return  this.accountId;
     }
    /**
    *无功能描述
    */
    public void setName(String name) { 
        this.name=name;
     }
    /**
    *无功能描述
    */
    public String getName() { 
        return  this.name;
     }

 } 

