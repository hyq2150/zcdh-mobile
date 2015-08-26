/** 
*  WeChatParamDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 微信参数的实体 
*  @author focus, 2014-5-2 上午9:27:52 
*/  
public class WeChatParamDTO  implements Serializable { 
    public String app_id  ; 
    public String app_secret  ; 
    public String app_key  ; 
    public String partner  ; 
    public String partner_key  ; 
    private String requestTokenUrl  ; 
    private String rquestPreIdUrl  ; 
    /**
    *无功能描述
    */
    public void setApp_id(String app_id) { 
        this.app_id=app_id;
     }
    /**
    *无功能描述
    */
    public String getApp_id() { 
        return  this.app_id;
     }
    /**
    *无功能描述
    */
    public void setApp_secret(String app_secret) { 
        this.app_secret=app_secret;
     }
    /**
    *无功能描述
    */
    public String getApp_secret() { 
        return  this.app_secret;
     }
    /**
    *无功能描述
    */
    public void setApp_key(String app_key) { 
        this.app_key=app_key;
     }
    /**
    *无功能描述
    */
    public String getApp_key() { 
        return  this.app_key;
     }
    /**
    *无功能描述
    */
    public void setPartner(String partner) { 
        this.partner=partner;
     }
    /**
    *无功能描述
    */
    public String getPartner() { 
        return  this.partner;
     }
    /**
    *无功能描述
    */
    public void setPartner_key(String partner_key) { 
        this.partner_key=partner_key;
     }
    /**
    *无功能描述
    */
    public String getPartner_key() { 
        return  this.partner_key;
     }
    /**
    *无功能描述
    */
    public void setRequestTokenUrl(String requestTokenUrl) { 
        this.requestTokenUrl=requestTokenUrl;
     }
    /**
    *无功能描述
    */
    public String getRequestTokenUrl() { 
        return  this.requestTokenUrl;
     }
    /**
    *无功能描述
    */
    public void setRquestPreIdUrl(String rquestPreIdUrl) { 
        this.rquestPreIdUrl=rquestPreIdUrl;
     }
    /**
    *无功能描述
    */
    public String getRquestPreIdUrl() { 
        return  this.rquestPreIdUrl;
     }

 } 

