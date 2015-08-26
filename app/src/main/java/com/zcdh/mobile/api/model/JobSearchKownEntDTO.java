/** 
*  JobSearchKownEntDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 知名企业 
*  @author focus, 2014-4-1 下午4:22:04 
*/  
public class JobSearchKownEntDTO  implements Serializable { 
    private String kownEnt  ; 
    private Integer postCount  ; 
    /**
    *无功能描述
    */
    public void setKownEnt(String kownEnt) { 
        this.kownEnt=kownEnt;
     }
    /**
    *无功能描述
    */
    public String getKownEnt() { 
        return  this.kownEnt;
     }
    /**
    *无功能描述
    */
    public void setPostCount(Integer postCount) { 
        this.postCount=postCount;
     }
    /**
    *无功能描述
    */
    public Integer getPostCount() { 
        return  this.postCount;
     }

 } 

