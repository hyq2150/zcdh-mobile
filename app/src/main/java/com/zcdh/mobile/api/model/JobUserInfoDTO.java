/** 
*  JobUserInfoDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-11 上午11:14:22 
*/  
public class JobUserInfoDTO  implements Serializable { 
    private Long userId  ; 
    private String userName  ; 
    private String mobile  ; 
    private String email  ; 
    private Date birth  ; 
    private String gerder  ; 
    private String talentTypeCode  ; 
    //人才类型<br> 
    // 021.001 普工 
    // 021.002 应届大学生 
    private String talentTypeName  ; 
    //003.001 男， 003.002  女 003.003 不限 
    private String gerderCode  ; 
    private String isMarried  ; 
    //未婚：008.001， 已婚： 008.002 ,离异：008.003 
    private String isMarriedCode  ; 
    //民族 
    private String nation  ; 
    //证件号码 
    private String credentials  ; 
    //证件类型显示的名称 
    private String credentialTypeName  ; 
    //009.001 身份证 
    // 009.002 军人证 
    // 009.003 护照 
    // 009.004 其他 
    private String credentialTypeCode  ; 
    //工作年限的显示名称 
    private String serviceYearName  ; 
    //005.001 在读学生 
    // 005.002 应届毕业生 
    // 005.003 一年以上 
    // 005.004 二年以上 
    // 005.005 三年以上 
    // 005.006 五年以上 
    // 005.007 八年以上 
    // 005.008 十年以上 
    // 005.009 不限 
    private String serviceYearCode  ; 
    //最高学历的显示的名称 
    private String degreeName  ; 
    //004.001 初中 004.002 高中 004.004 中专 
    // 004.005 大专 004.006 本科 004.007 硕士   
    // 004.009 博士 004.010 培训 004.011 不限 
    private String degreeCode  ; 
    //户口所在地显示的名称 
    private String panmeldenName  ; 
    private String panmeldenCode  ; 
    //贯集显示的名称 
    private String nativePlaceName  ; 
    private String nativePlaceCode  ; 
    //现在居住地显示的名称 
    private String addressName  ; 
    private String addressCode  ; 
    //求职状态名称 
    private String jobStautsName  ; 
    //求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中 
    private String jobStatusCode  ; 
    private String fileCode  ; 
    //语言类型的数量 
    private Integer laguageTypeCount  ; 
    private String school_job  ; 
    private String specialty  ; 
    private String en_level_code  ; 
    private String ots_languages  ; 
    /**
    *无功能描述
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *无功能描述
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *无功能描述
    */
    public String getUserName() { 
        return  this.userName;
     }
    /**
    *无功能描述
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *无功能描述
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *无功能描述
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *无功能描述
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *无功能描述
    */
    public void setBirth(Date birth) { 
        this.birth=birth;
     }
    /**
    *无功能描述
    */
    public Date getBirth() { 
        return  this.birth;
     }
    /**
    *无功能描述
    */
    public void setGerder(String gerder) { 
        this.gerder=gerder;
     }
    /**
    *无功能描述
    */
    public String getGerder() { 
        return  this.gerder;
     }
    /**
    *无功能描述
    */
    public void setTalentTypeCode(String talentTypeCode) { 
        this.talentTypeCode=talentTypeCode;
     }
    /**
    *无功能描述
    */
    public String getTalentTypeCode() { 
        return  this.talentTypeCode;
     }
    /**
    *设定人才类型<br> 021.001 普工 021.002 应届大学生
    */
    public void setTalentTypeName(String talentTypeName) { 
        this.talentTypeName=talentTypeName;
     }
    /**
    *获取人才类型<br> 021.001 普工 021.002 应届大学生
    */
    public String getTalentTypeName() { 
        return  this.talentTypeName;
     }
    /**
    *设定003.001 男， 003.002  女 003.003 不限
    */
    public void setGerderCode(String gerderCode) { 
        this.gerderCode=gerderCode;
     }
    /**
    *获取003.001 男， 003.002  女 003.003 不限
    */
    public String getGerderCode() { 
        return  this.gerderCode;
     }
    /**
    *无功能描述
    */
    public void setIsMarried(String isMarried) { 
        this.isMarried=isMarried;
     }
    /**
    *无功能描述
    */
    public String getIsMarried() { 
        return  this.isMarried;
     }
    /**
    *设定未婚：008.001， 已婚： 008.002 ,离异：008.003
    */
    public void setIsMarriedCode(String isMarriedCode) { 
        this.isMarriedCode=isMarriedCode;
     }
    /**
    *获取未婚：008.001， 已婚： 008.002 ,离异：008.003
    */
    public String getIsMarriedCode() { 
        return  this.isMarriedCode;
     }
    /**
    *设定民族
    */
    public void setNation(String nation) { 
        this.nation=nation;
     }
    /**
    *获取民族
    */
    public String getNation() { 
        return  this.nation;
     }
    /**
    *设定证件号码
    */
    public void setCredentials(String credentials) { 
        this.credentials=credentials;
     }
    /**
    *获取证件号码
    */
    public String getCredentials() { 
        return  this.credentials;
     }
    /**
    *设定证件类型显示的名称
    */
    public void setCredentialTypeName(String credentialTypeName) { 
        this.credentialTypeName=credentialTypeName;
     }
    /**
    *获取证件类型显示的名称
    */
    public String getCredentialTypeName() { 
        return  this.credentialTypeName;
     }
    /**
    *设定009.001 身份证 009.002 军人证 009.003 护照 009.004 其他
    */
    public void setCredentialTypeCode(String credentialTypeCode) { 
        this.credentialTypeCode=credentialTypeCode;
     }
    /**
    *获取009.001 身份证 009.002 军人证 009.003 护照 009.004 其他
    */
    public String getCredentialTypeCode() { 
        return  this.credentialTypeCode;
     }
    /**
    *设定工作年限的显示名称
    */
    public void setServiceYearName(String serviceYearName) { 
        this.serviceYearName=serviceYearName;
     }
    /**
    *获取工作年限的显示名称
    */
    public String getServiceYearName() { 
        return  this.serviceYearName;
     }
    /**
    *设定005.001 在读学生 005.002 应届毕业生 005.003 一年以上 005.004 二年以上 005.005 三年以上 005.006 五年以上 005.007 八年以上 005.008 十年以上 005.009 不限
    */
    public void setServiceYearCode(String serviceYearCode) { 
        this.serviceYearCode=serviceYearCode;
     }
    /**
    *获取005.001 在读学生 005.002 应届毕业生 005.003 一年以上 005.004 二年以上 005.005 三年以上 005.006 五年以上 005.007 八年以上 005.008 十年以上 005.009 不限
    */
    public String getServiceYearCode() { 
        return  this.serviceYearCode;
     }
    /**
    *设定最高学历的显示的名称
    */
    public void setDegreeName(String degreeName) { 
        this.degreeName=degreeName;
     }
    /**
    *获取最高学历的显示的名称
    */
    public String getDegreeName() { 
        return  this.degreeName;
     }
    /**
    *设定004.001 初中 004.002 高中 004.004 中专 004.005 大专 004.006 本科 004.007 硕士   004.009 博士 004.010 培训 004.011 不限
    */
    public void setDegreeCode(String degreeCode) { 
        this.degreeCode=degreeCode;
     }
    /**
    *获取004.001 初中 004.002 高中 004.004 中专 004.005 大专 004.006 本科 004.007 硕士   004.009 博士 004.010 培训 004.011 不限
    */
    public String getDegreeCode() { 
        return  this.degreeCode;
     }
    /**
    *设定户口所在地显示的名称
    */
    public void setPanmeldenName(String panmeldenName) { 
        this.panmeldenName=panmeldenName;
     }
    /**
    *获取户口所在地显示的名称
    */
    public String getPanmeldenName() { 
        return  this.panmeldenName;
     }
    /**
    *无功能描述
    */
    public void setPanmeldenCode(String panmeldenCode) { 
        this.panmeldenCode=panmeldenCode;
     }
    /**
    *无功能描述
    */
    public String getPanmeldenCode() { 
        return  this.panmeldenCode;
     }
    /**
    *设定贯集显示的名称
    */
    public void setNativePlaceName(String nativePlaceName) { 
        this.nativePlaceName=nativePlaceName;
     }
    /**
    *获取贯集显示的名称
    */
    public String getNativePlaceName() { 
        return  this.nativePlaceName;
     }
    /**
    *无功能描述
    */
    public void setNativePlaceCode(String nativePlaceCode) { 
        this.nativePlaceCode=nativePlaceCode;
     }
    /**
    *无功能描述
    */
    public String getNativePlaceCode() { 
        return  this.nativePlaceCode;
     }
    /**
    *设定现在居住地显示的名称
    */
    public void setAddressName(String addressName) { 
        this.addressName=addressName;
     }
    /**
    *获取现在居住地显示的名称
    */
    public String getAddressName() { 
        return  this.addressName;
     }
    /**
    *无功能描述
    */
    public void setAddressCode(String addressCode) { 
        this.addressCode=addressCode;
     }
    /**
    *无功能描述
    */
    public String getAddressCode() { 
        return  this.addressCode;
     }
    /**
    *设定求职状态名称
    */
    public void setJobStautsName(String jobStautsName) { 
        this.jobStautsName=jobStautsName;
     }
    /**
    *获取求职状态名称
    */
    public String getJobStautsName() { 
        return  this.jobStautsName;
     }
    /**
    *设定求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
    */
    public void setJobStatusCode(String jobStatusCode) { 
        this.jobStatusCode=jobStatusCode;
     }
    /**
    *获取求职状态的编码  013.001 ：求职中，013.002：想换岗 ，013.003：在职中
    */
    public String getJobStatusCode() { 
        return  this.jobStatusCode;
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
    *设定语言类型的数量
    */
    public void setLaguageTypeCount(Integer laguageTypeCount) { 
        this.laguageTypeCount=laguageTypeCount;
     }
    /**
    *获取语言类型的数量
    */
    public Integer getLaguageTypeCount() { 
        return  this.laguageTypeCount;
     }
    /**
    *无功能描述
    */
    public void setSchool_job(String school_job) { 
        this.school_job=school_job;
     }
    /**
    *无功能描述
    */
    public String getSchool_job() { 
        return  this.school_job;
     }
    /**
    *无功能描述
    */
    public void setSpecialty(String specialty) { 
        this.specialty=specialty;
     }
    /**
    *无功能描述
    */
    public String getSpecialty() { 
        return  this.specialty;
     }
    /**
    *无功能描述
    */
    public void setEn_level_code(String en_level_code) { 
        this.en_level_code=en_level_code;
     }
    /**
    *无功能描述
    */
    public String getEn_level_code() { 
        return  this.en_level_code;
     }
    /**
    *无功能描述
    */
    public void setOts_languages(String ots_languages) { 
        this.ots_languages=ots_languages;
     }
    /**
    *无功能描述
    */
    public String getOts_languages() { 
        return  this.ots_languages;
     }

 } 

