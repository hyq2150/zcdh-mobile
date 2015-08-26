/** 
*  PrePayParamDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 预订单的参数dto 
*  @author focus, 2014-5-2 下午3:05:29 
*/  
public class PrePayParamDTO  implements Serializable { 
    private String requestPrePayURL  ; 
    private String reqDataParam  ; 
    private Map<String,String> reqMap  ; 
    private Map<String,String> otherParams  ; 
    /**
    *无功能描述
    */
    public void setRequestPrePayURL(String requestPrePayURL) { 
        this.requestPrePayURL=requestPrePayURL;
     }
    /**
    *无功能描述
    */
    public String getRequestPrePayURL() { 
        return  this.requestPrePayURL;
     }
    /**
    *无功能描述
    */
    public void setReqDataParam(String reqDataParam) { 
        this.reqDataParam=reqDataParam;
     }
    /**
    *无功能描述
    */
    public String getReqDataParam() { 
        return  this.reqDataParam;
     }
    /**
    *无功能描述
    */
    public void setReqMap(Map<String,String> reqMap) { 
        this.reqMap=reqMap;
     }
    /**
    *无功能描述
    */
    public Map<String,String> getReqMap() { 
        return  this.reqMap;
     }
    /**
    *无功能描述
    */
    public void setOtherParams(Map<String,String> otherParams) { 
        this.otherParams=otherParams;
     }
    /**
    *无功能描述
    */
    public Map<String,String> getOtherParams() { 
        return  this.otherParams;
     }

 } 

