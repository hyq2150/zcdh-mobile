/** 
*  JobTechnicalDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-14 上午10:48:10 
*/  
public class JobTechnicalDTO  implements Serializable { 
    //技能id 
    private Long id  ; 
    //技能编码 
    private String technicalCode  ; 
    //技能名称 
    private String technicalName  ; 
    //技能等级： 
    // 002.001	精通 
    // 002.002	熟悉 
    // 002.003   会一些 
    // 002.004   知道 
    private String paramCode  ; 
    //参数编码对应的名称 
    private String paramName  ; 
    //选中的技能 1 选中的， 0 没有选中的 
    private Integer isSelectedTechnical  ; 
    /**
    *设定技能id
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取技能id
    */
    public Long getId() { 
        return  this.id;
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
    /**
    *设定技能等级： 002.001	精通 002.002	熟悉 002.003   会一些 002.004   知道
    */
    public void setParamCode(String paramCode) { 
        this.paramCode=paramCode;
     }
    /**
    *获取技能等级： 002.001	精通 002.002	熟悉 002.003   会一些 002.004   知道
    */
    public String getParamCode() { 
        return  this.paramCode;
     }
    /**
    *设定参数编码对应的名称
    */
    public void setParamName(String paramName) { 
        this.paramName=paramName;
     }
    /**
    *获取参数编码对应的名称
    */
    public String getParamName() { 
        return  this.paramName;
     }
    /**
    *设定选中的技能 1 选中的， 0 没有选中的
    */
    public void setIsSelectedTechnical(Integer isSelectedTechnical) { 
        this.isSelectedTechnical=isSelectedTechnical;
     }
    /**
    *获取选中的技能 1 选中的， 0 没有选中的
    */
    public Integer getIsSelectedTechnical() { 
        return  this.isSelectedTechnical;
     }

 } 

