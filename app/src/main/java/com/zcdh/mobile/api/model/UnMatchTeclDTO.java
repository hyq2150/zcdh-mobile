/** 
*  UnMatchTeclDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-9-1 下午6:42:40 
*/  
public class UnMatchTeclDTO  implements Serializable { 
    private String param_value  ; 
    private String tel_name  ; 
    /**
    *无功能描述
    */
    public void setParam_value(String param_value) { 
        this.param_value=param_value;
     }
    /**
    *无功能描述
    */
    public String getParam_value() { 
        return  this.param_value;
     }
    /**
    *无功能描述
    */
    public void setTel_name(String tel_name) { 
        this.tel_name=tel_name;
     }
    /**
    *无功能描述
    */
    public String getTel_name() { 
        return  this.tel_name;
     }

 } 

