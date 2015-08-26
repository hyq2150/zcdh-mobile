/** 
*  ApplyPostInfoDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-10-10 上午11:12:35 
*/  
public class ApplyPostInfoDTO  implements Serializable { 
    //岗位的特殊技能需求列表 
    private List<JobSpecialRequirementsMatchDTO> specialKill  ; 
    //企业logo 
    private ImgURLDTO entLogo  ; 
    /**
    *设定岗位的特殊技能需求列表
    */
    public void setSpecialKill(List<JobSpecialRequirementsMatchDTO> specialKill) { 
        this.specialKill=specialKill;
     }
    /**
    *获取岗位的特殊技能需求列表
    */
    public List<JobSpecialRequirementsMatchDTO> getSpecialKill() { 
        return  this.specialKill;
     }
    /**
    *设定企业logo
    */
    public void setEntLogo(ImgURLDTO entLogo) { 
        this.entLogo=entLogo;
     }
    /**
    *获取企业logo
    */
    public ImgURLDTO getEntLogo() { 
        return  this.entLogo;
     }

 } 

