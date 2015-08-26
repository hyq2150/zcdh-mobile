/** 
*  JobSearchHistoryDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-1 下午5:27:58 
*/  
public class JobSearchHistoryDTO  implements Serializable { 
    private String postAliases  ; 
    private Integer postCount  ; 
    /**
    *无功能描述
    */
    public void setPostAliases(String postAliases) { 
        this.postAliases=postAliases;
     }
    /**
    *无功能描述
    */
    public String getPostAliases() { 
        return  this.postAliases;
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

