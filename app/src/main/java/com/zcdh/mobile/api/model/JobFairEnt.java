/** 
*  JobFairEnt 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业列表 
*  @author focus, 2014-6-18 上午11:33:10 
*/  
public class JobFairEnt  implements Serializable { 
    private Long entId  ; 
    private String entName  ; 
    private String entNameTemp  ; 
    private String boothNo  ; 
    private String fileCode  ; 
    private ImgURLDTO entLogo  ; 
    /**
    *无功能描述
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *无功能描述
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *无功能描述
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *无功能描述
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *无功能描述
    */
    public void setEntNameTemp(String entNameTemp) { 
        this.entNameTemp=entNameTemp;
     }
    /**
    *无功能描述
    */
    public String getEntNameTemp() { 
        return  this.entNameTemp;
     }
    /**
    *无功能描述
    */
    public void setBoothNo(String boothNo) { 
        this.boothNo=boothNo;
     }
    /**
    *无功能描述
    */
    public String getBoothNo() { 
        return  this.boothNo;
     }
    /**
    *无功能描述
    */
    public void setFileCode(String fileCode) { 
        this.fileCode=fileCode;
     }
    /**
    *无功能描述
    */
    public String getFileCode() { 
        return  this.fileCode;
     }
    /**
    *无功能描述
    */
    public void setEntLogo(ImgURLDTO entLogo) { 
        this.entLogo=entLogo;
     }
    /**
    *无功能描述
    */
    public ImgURLDTO getEntLogo() { 
        return  this.entLogo;
     }

 } 

