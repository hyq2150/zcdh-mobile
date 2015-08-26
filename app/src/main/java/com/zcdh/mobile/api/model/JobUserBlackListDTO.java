/** 
*  JobUserBlackListDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 个人黑名单列表 
*  @author focus, 2014-5-8 下午3:10:55 
*/  
public class JobUserBlackListDTO  implements Serializable { 
    //企业id 
    private Long entId  ; 
    //企业名称 
    private String entName  ; 
    //用户黑名单的id 
    private Long blackId  ; 
    /**
    *设定企业id
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *获取企业id
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *设定企业名称
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *获取企业名称
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *设定用户黑名单的id
    */
    public void setBlackId(Long blackId) { 
        this.blackId=blackId;
     }
    /**
    *获取用户黑名单的id
    */
    public Long getBlackId() { 
        return  this.blackId;
     }

 } 

