/** 
*  EntIndustryTypeDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业的行业类别 
*  @author focus, 2015-2-9 下午2:55:33 
*/  
public class EntIndustryTypeDTO  implements Serializable { 
    private String entIndustryName  ; 
    private String entIndustryCode  ; 
    private String entTotals  ; 
    private String parentCode  ; 
    private List<EntIndustryTypeDTO> subEntIndustries  ; 
    /**
    *无功能描述
    */
    public void setEntIndustryName(String entIndustryName) { 
        this.entIndustryName=entIndustryName;
     }
    /**
    *无功能描述
    */
    public String getEntIndustryName() { 
        return  this.entIndustryName;
     }
    /**
    *无功能描述
    */
    public void setEntIndustryCode(String entIndustryCode) { 
        this.entIndustryCode=entIndustryCode;
     }
    /**
    *无功能描述
    */
    public String getEntIndustryCode() { 
        return  this.entIndustryCode;
     }
    /**
    *无功能描述
    */
    public void setEntTotals(String entTotals) { 
        this.entTotals=entTotals;
     }
    /**
    *无功能描述
    */
    public String getEntTotals() { 
        return  this.entTotals;
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
    public void setSubEntIndustries(List<EntIndustryTypeDTO> subEntIndustries) { 
        this.subEntIndustries=subEntIndustries;
     }
    /**
    *无功能描述
    */
    public List<EntIndustryTypeDTO> getSubEntIndustries() { 
        return  this.subEntIndustries;
     }

 } 

