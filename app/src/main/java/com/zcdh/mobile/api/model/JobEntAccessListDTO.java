/** 
*  JobEntAccessListDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午7:43:22 
*/  
public class JobEntAccessListDTO  implements Serializable { 
    private Long id  ; 
    //企业id 
    private Long entId  ; 
    //企业名称 
    private String entName  ; 
    //企业联系人 
    private String contact  ; 
    //行业 
    private String industryName  ; 
    //访问时间 
    private Date accessTime  ; 
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
    *设定企业联系人
    */
    public void setContact(String contact) { 
        this.contact=contact;
     }
    /**
    *获取企业联系人
    */
    public String getContact() { 
        return  this.contact;
     }
    /**
    *设定行业
    */
    public void setIndustryName(String industryName) { 
        this.industryName=industryName;
     }
    /**
    *获取行业
    */
    public String getIndustryName() { 
        return  this.industryName;
     }
    /**
    *设定访问时间
    */
    public void setAccessTime(Date accessTime) { 
        this.accessTime=accessTime;
     }
    /**
    *获取访问时间
    */
    public Date getAccessTime() { 
        return  this.accessTime;
     }

 } 

