/** 
*  AdminUnicomManagerEnterpriseDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2015-3-12 下午1:46:13 
*/  
public class AdminUnicomManagerEnterpriseDTO  implements Serializable { 
    private Long id  ; 
    private Long manager_id  ; 
    private Long ent_id  ; 
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
    *无功能描述
    */
    public void setManager_id(Long manager_id) { 
        this.manager_id=manager_id;
     }
    /**
    *无功能描述
    */
    public Long getManager_id() { 
        return  this.manager_id;
     }
    /**
    *无功能描述
    */
    public void setEnt_id(Long ent_id) { 
        this.ent_id=ent_id;
     }
    /**
    *无功能描述
    */
    public Long getEnt_id() { 
        return  this.ent_id;
     }

 } 

