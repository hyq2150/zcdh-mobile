/** 
*  JobEntTechniclRequireDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业技能要求的DTO 
*  @author focus, 2014-4-9 下午7:24:39 
*/  
public class JobEntTechniclRequireDTO  implements Serializable { 
    private String techniclName  ; 
    private String paramName  ; 
    private String paramCode  ; 
    private String techniclCode  ; 
    private Long postId  ; 
    /**
    *无功能描述
    */
    public void setTechniclName(String techniclName) { 
        this.techniclName=techniclName;
     }
    /**
    *无功能描述
    */
    public String getTechniclName() { 
        return  this.techniclName;
     }
    /**
    *无功能描述
    */
    public void setParamName(String paramName) { 
        this.paramName=paramName;
     }
    /**
    *无功能描述
    */
    public String getParamName() { 
        return  this.paramName;
     }
    /**
    *无功能描述
    */
    public void setParamCode(String paramCode) { 
        this.paramCode=paramCode;
     }
    /**
    *无功能描述
    */
    public String getParamCode() { 
        return  this.paramCode;
     }
    /**
    *无功能描述
    */
    public void setTechniclCode(String techniclCode) { 
        this.techniclCode=techniclCode;
     }
    /**
    *无功能描述
    */
    public String getTechniclCode() { 
        return  this.techniclCode;
     }
    /**
    *无功能描述
    */
    public void setPostId(Long postId) { 
        this.postId=postId;
     }
    /**
    *无功能描述
    */
    public Long getPostId() { 
        return  this.postId;
     }

 } 

