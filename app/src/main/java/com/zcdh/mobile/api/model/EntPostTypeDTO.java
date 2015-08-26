/** 
*  EntPostTypeDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 岗位类别 
*  @author focus, 2015-2-9 下午2:55:42 
*/  
public class EntPostTypeDTO  implements Serializable { 
    private String postTypeName  ; 
    private String postTypeCode  ; 
    private String postTotals  ; 
    private String parentCode  ; 
    private List<EntPostTypeDTO> subPostTypes  ; 
    /**
    *无功能描述
    */
    public void setPostTypeName(String postTypeName) { 
        this.postTypeName=postTypeName;
     }
    /**
    *无功能描述
    */
    public String getPostTypeName() { 
        return  this.postTypeName;
     }
    /**
    *无功能描述
    */
    public void setPostTypeCode(String postTypeCode) { 
        this.postTypeCode=postTypeCode;
     }
    /**
    *无功能描述
    */
    public String getPostTypeCode() { 
        return  this.postTypeCode;
     }
    /**
    *无功能描述
    */
    public void setPostTotals(String postTotals) { 
        this.postTotals=postTotals;
     }
    /**
    *无功能描述
    */
    public String getPostTotals() { 
        return  this.postTotals;
     }
    /**
    *无功能描述
    */
    public void setParentCode(String parentCode) { 
        this.parentCode=parentCode;
     }
    /**
    *无功能描述
    */
    public String getParentCode() { 
        return  this.parentCode;
     }
    /**
    *无功能描述
    */
    public void setSubPostTypes(List<EntPostTypeDTO> subPostTypes) { 
        this.subPostTypes=subPostTypes;
     }
    /**
    *无功能描述
    */
    public List<EntPostTypeDTO> getSubPostTypes() { 
        return  this.subPostTypes;
     }

 } 

