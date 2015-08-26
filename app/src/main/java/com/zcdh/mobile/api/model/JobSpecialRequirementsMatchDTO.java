/** 
*  JobSpecialRequirementsMatchDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 特殊技能DTO 
*  @author focus, 2014-4-11 上午10:33:12 
*/  
public class JobSpecialRequirementsMatchDTO  implements Serializable { 
    //特殊技能的值 
    private String specailValue  ; 
    private String code  ; 
    /**
    *设定特殊技能的值
    */
    public void setSpecailValue(String specailValue) { 
        this.specailValue=specailValue;
     }
    /**
    *获取特殊技能的值
    */
    public String getSpecailValue() { 
        return  this.specailValue;
     }
    /**
    *无功能描述
    */
    public void setCode(String code) { 
        this.code=code;
     }
    /**
    *无功能描述
    */
    public String getCode() { 
        return  this.code;
     }

 } 

