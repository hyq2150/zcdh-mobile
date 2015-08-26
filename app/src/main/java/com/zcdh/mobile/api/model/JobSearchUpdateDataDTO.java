/** 
*  JobSearchUpdateDataDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索页面顶部更新信息的dto 
*   
*  @author focus, 2014-4-1 下午12:11:27 
*/  
public class JobSearchUpdateDataDTO  implements Serializable { 
    private Integer addPostCount  ; 
    private Integer totalEntCount  ; 
    private String updateDataBanner  ; 
    /**
    *无功能描述
    */
    public void setAddPostCount(Integer addPostCount) { 
        this.addPostCount=addPostCount;
     }
    /**
    *无功能描述
    */
    public Integer getAddPostCount() { 
        return  this.addPostCount;
     }
    /**
    *无功能描述
    */
    public void setTotalEntCount(Integer totalEntCount) { 
        this.totalEntCount=totalEntCount;
     }
    /**
    *无功能描述
    */
    public Integer getTotalEntCount() { 
        return  this.totalEntCount;
     }
    /**
    *无功能描述
    */
    public void setUpdateDataBanner(String updateDataBanner) { 
        this.updateDataBanner=updateDataBanner;
     }
    /**
    *无功能描述
    */
    public String getUpdateDataBanner() { 
        return  this.updateDataBanner;
     }

 } 

