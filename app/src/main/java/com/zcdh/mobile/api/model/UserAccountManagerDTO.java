/** 
*  UserAccountManagerDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-9-18 下午5:42:44 
*/  
public class UserAccountManagerDTO  implements Serializable { 
    //绑定的类型 
    private String bindType  ; 
    //绑定的值 
    private String bindValue  ; 
    //-1 未绑定，0 已经绑定，1 未验证 
    private Integer status  ; 
    /**
    *设定绑定的类型
    */
    public void setBindType(String bindType) { 
        this.bindType=bindType;
     }
    /**
    *获取绑定的类型
    */
    public String getBindType() { 
        return  this.bindType;
     }
    /**
    *设定绑定的值
    */
    public void setBindValue(String bindValue) { 
        this.bindValue=bindValue;
     }
    /**
    *获取绑定的值
    */
    public String getBindValue() { 
        return  this.bindValue;
     }
    /**
    *设定-1 未绑定，0 已经绑定，1 未验证
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取-1 未绑定，0 已经绑定，1 未验证
    */
    public Integer getStatus() { 
        return  this.status;
     }

 } 

