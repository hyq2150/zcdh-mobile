/** 
*  JobSettingDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-18 下午5:54:25 
*/  
public class JobSettingDTO  implements Serializable { 
    private Long id  ; 
    //用户id 
    private Long userId  ; 
    //编码 
    private String code  ; 
    //值 
    private String value  ; 
    //状态       0 无效， 1 有效 
    private Integer status  ; 
    /**
    *无功能描述
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *无功能描述
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定用户id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取用户id
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定编码
    */
    public void setCode(String code) { 
        this.code=code;
     }
    /**
    *获取编码
    */
    public String getCode() { 
        return  this.code;
     }
    /**
    *设定值
    */
    public void setValue(String value) { 
        this.value=value;
     }
    /**
    *获取值
    */
    public String getValue() { 
        return  this.value;
     }
    /**
    *设定状态       0 无效， 1 有效
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取状态       0 无效， 1 有效
    */
    public Integer getStatus() { 
        return  this.status;
     }

 } 

