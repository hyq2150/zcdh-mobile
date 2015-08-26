/** 
*  ConFirmPaymentDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 确认支付DTO 
*  @author focus, 2014-4-21 下午8:36:46 
*/  
public class ConFirmPaymentDTO  implements Serializable { 
    private String appId  ; 
    private Map<String,String> reqParam  ; 
    private Boolean isResp  ; 
    private Integer status  ; 
    /**
    *无功能描述
    */
    public void setAppId(String appId) { 
        this.appId=appId;
     }
    /**
    *无功能描述
    */
    public String getAppId() { 
        return  this.appId;
     }
    /**
    *无功能描述
    */
    public void setReqParam(Map<String,String> reqParam) { 
        this.reqParam=reqParam;
     }
    /**
    *无功能描述
    */
    public Map<String,String> getReqParam() { 
        return  this.reqParam;
     }
    /**
    *无功能描述
    */
    public void setIsResp(Boolean isResp) { 
        this.isResp=isResp;
     }
    /**
    *无功能描述
    */
    public Boolean getIsResp() { 
        return  this.isResp;
     }
    /**
    *无功能描述
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *无功能描述
    */
    public Integer getStatus() { 
        return  this.status;
     }

 } 

