/** 
*  TechnicalListDTO 
* 
*  Created Date: 2014-05-21 09:39:16 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-5-14 上午10:51:32
 */
public class TechnicalListDTO  implements Serializable { 
    /** 
     * 技能等级：
     * 002.001	精通
     * 002.002	熟悉
     * 002.003   会一些
     * 002.004   知道
     */
    private Integer paramCode  ; 
    /** 
     * 参数编码对应的名称
     */
    private Integer paramName  ; 
    /** 
     * 技能编码
     */
    private String technicalCode  ; 
    /** 
     * 技能名称
     */
    private String technicalName  ; 
    /**
    *设定技能等级：  002.001	精通  002.002	熟悉  002.003   会一些  002.004   知道
    */
    public void setParamCode(Integer paramCode) { 
        this.paramCode=paramCode;
     }
    /**
    *获取技能等级：  002.001	精通  002.002	熟悉  002.003   会一些  002.004   知道
    */
    public Integer getParamCode() { 
        return  this.paramCode;
     }
    /**
    *设定参数编码对应的名称
    */
    public void setParamName(Integer paramName) { 
        this.paramName=paramName;
     }
    /**
    *获取参数编码对应的名称
    */
    public Integer getParamName() { 
        return  this.paramName;
     }
    /**
    *设定技能编码
    */
    public void setTechnicalCode(String technicalCode) { 
        this.technicalCode=technicalCode;
     }
    /**
    *获取技能编码
    */
    public String getTechnicalCode() { 
        return  this.technicalCode;
     }
    /**
    *设定技能名称
    */
    public void setTechnicalName(String technicalName) { 
        this.technicalName=technicalName;
     }
    /**
    *获取技能名称
    */
    public String getTechnicalName() { 
        return  this.technicalName;
     }

 } 

