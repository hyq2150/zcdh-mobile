/** 
*  CheckUserRequisiteDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 检查用户的必填项目是否是填写完整的 
*  @author focus, 2014-5-29 下午12:03:13 
*/  
public class CheckUserRequisiteDTO  implements Serializable { 
    //用户名称 0 没有填写， 1 填写完整 
    private Integer isUserNameFilled  ; 
    //性别 0 没有填写， 1 填写完整 
    private Integer isGerderFilled  ; 
    //性别 0 没有填写， 1 填写完整 
    private Integer isServiceYearFilled  ; 
    //0 没有填写， 1 填写完整 
    // 户口所在地 
    private Integer isPanmeldenFilled  ; 
    //0 没有填写， 1 填写完整 
    // 人才类型 
    private Integer isTalentTypeFilled  ; 
    //0 没有完整， 1 填写完整 
    // 用户必填项 是否填写完整 
    private Integer isUserRequisiteFilled  ; 
    /**
    *设定用户名称 0 没有填写， 1 填写完整
    */
    public void setIsUserNameFilled(Integer isUserNameFilled) { 
        this.isUserNameFilled=isUserNameFilled;
     }
    /**
    *获取用户名称 0 没有填写， 1 填写完整
    */
    public Integer getIsUserNameFilled() { 
        return  this.isUserNameFilled;
     }
    /**
    *设定性别 0 没有填写， 1 填写完整
    */
    public void setIsGerderFilled(Integer isGerderFilled) { 
        this.isGerderFilled=isGerderFilled;
     }
    /**
    *获取性别 0 没有填写， 1 填写完整
    */
    public Integer getIsGerderFilled() { 
        return  this.isGerderFilled;
     }
    /**
    *设定性别 0 没有填写， 1 填写完整
    */
    public void setIsServiceYearFilled(Integer isServiceYearFilled) { 
        this.isServiceYearFilled=isServiceYearFilled;
     }
    /**
    *获取性别 0 没有填写， 1 填写完整
    */
    public Integer getIsServiceYearFilled() { 
        return  this.isServiceYearFilled;
     }
    /**
    *设定0 没有填写， 1 填写完整 户口所在地
    */
    public void setIsPanmeldenFilled(Integer isPanmeldenFilled) { 
        this.isPanmeldenFilled=isPanmeldenFilled;
     }
    /**
    *获取0 没有填写， 1 填写完整 户口所在地
    */
    public Integer getIsPanmeldenFilled() { 
        return  this.isPanmeldenFilled;
     }
    /**
    *设定0 没有填写， 1 填写完整 人才类型
    */
    public void setIsTalentTypeFilled(Integer isTalentTypeFilled) { 
        this.isTalentTypeFilled=isTalentTypeFilled;
     }
    /**
    *获取0 没有填写， 1 填写完整 人才类型
    */
    public Integer getIsTalentTypeFilled() { 
        return  this.isTalentTypeFilled;
     }
    /**
    *设定0 没有完整， 1 填写完整 用户必填项 是否填写完整
    */
    public void setIsUserRequisiteFilled(Integer isUserRequisiteFilled) { 
        this.isUserRequisiteFilled=isUserRequisiteFilled;
     }
    /**
    *获取0 没有完整， 1 填写完整 用户必填项 是否填写完整
    */
    public Integer getIsUserRequisiteFilled() { 
        return  this.isUserRequisiteFilled;
     }

 } 

