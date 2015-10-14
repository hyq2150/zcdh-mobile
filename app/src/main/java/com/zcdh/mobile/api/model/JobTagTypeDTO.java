/** 
*  JobTagTypeDTO 
* 
*  Created Date: 2015-10-12 17:31:51 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-7 上午10:25:28 
*/  
public class JobTagTypeDTO  implements Serializable { 
    private String tagTypeName  ; 
    private String tagTypeCode  ; 
    /**
    *无功能描述
    */
    public void setTagTypeName(String tagTypeName) { 
        this.tagTypeName=tagTypeName;
     }
    /**
    *无功能描述
    */
    public String getTagTypeName() { 
        return  this.tagTypeName;
     }
    /**
    *无功能描述
    */
    public void setTagTypeCode(String tagTypeCode) { 
        this.tagTypeCode=tagTypeCode;
     }
    /**
    *无功能描述
    */
    public String getTagTypeCode() { 
        return  this.tagTypeCode;
     }

 } 

