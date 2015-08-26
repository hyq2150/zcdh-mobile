/** 
*  JobLanguageDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-23 上午11:35:05 
*/  
public class JobLanguageDTO  implements Serializable { 
    //主键id 
    private Long id  ; 
    //userId 
    private Long userId  ; 
    //语言编码 
    private String languageCode  ; 
    //语言名称 
    private String languageName  ; 
    //读写熟练度的编码 
    private String readWriteParamCode  ; 
    //读写熟练度的名称 
    private String headWriteParamName  ; 
    //听说熟练度的编码 
    private String hearSpeakParamCode  ; 
    //听说熟练度的名称 
    private String hearSpeakParamName  ; 
    //0 不选中 ， 1 选中 
    private Integer isSelectedLanguage  ; 
    /**
    *设定主键id
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取主键id
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定userId
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取userId
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定语言编码
    */
    public void setLanguageCode(String languageCode) { 
        this.languageCode=languageCode;
     }
    /**
    *获取语言编码
    */
    public String getLanguageCode() { 
        return  this.languageCode;
     }
    /**
    *设定语言名称
    */
    public void setLanguageName(String languageName) { 
        this.languageName=languageName;
     }
    /**
    *获取语言名称
    */
    public String getLanguageName() { 
        return  this.languageName;
     }
    /**
    *设定读写熟练度的编码
    */
    public void setReadWriteParamCode(String readWriteParamCode) { 
        this.readWriteParamCode=readWriteParamCode;
     }
    /**
    *获取读写熟练度的编码
    */
    public String getReadWriteParamCode() { 
        return  this.readWriteParamCode;
     }
    /**
    *设定读写熟练度的名称
    */
    public void setHeadWriteParamName(String headWriteParamName) { 
        this.headWriteParamName=headWriteParamName;
     }
    /**
    *获取读写熟练度的名称
    */
    public String getHeadWriteParamName() { 
        return  this.headWriteParamName;
     }
    /**
    *设定听说熟练度的编码
    */
    public void setHearSpeakParamCode(String hearSpeakParamCode) { 
        this.hearSpeakParamCode=hearSpeakParamCode;
     }
    /**
    *获取听说熟练度的编码
    */
    public String getHearSpeakParamCode() { 
        return  this.hearSpeakParamCode;
     }
    /**
    *设定听说熟练度的名称
    */
    public void setHearSpeakParamName(String hearSpeakParamName) { 
        this.hearSpeakParamName=hearSpeakParamName;
     }
    /**
    *获取听说熟练度的名称
    */
    public String getHearSpeakParamName() { 
        return  this.hearSpeakParamName;
     }
    /**
    *设定0 不选中 ， 1 选中
    */
    public void setIsSelectedLanguage(Integer isSelectedLanguage) { 
        this.isSelectedLanguage=isSelectedLanguage;
     }
    /**
    *获取0 不选中 ， 1 选中
    */
    public Integer getIsSelectedLanguage() { 
        return  this.isSelectedLanguage;
     }

 } 

