/** 
*  EntHomePageDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业主页 
*  @author focus, 2014-6-4 上午10:35:36 
*/  
public class EntHomePageDTO  implements Serializable { 
    //企业详细信息 
    private JobEnterpriseDetailDTO entDetailDTO  ; 
    //企业logo 
    private ImgURLDTO entLogo  ; 
    //企业二维码 
    private String erCode  ; 
    //是否已经是黑名单 
    //  
    // 0 不是黑名单， 1 加入黑名单 
    private Integer isAddBlack  ; 
    //是否已关注    0 表示 取消关注， 1 表示关注 
    private Integer isAttention  ; 
    //标签 
    private List<String> tagNames  ; 
    /**
    *设定企业详细信息
    */
    public void setEntDetailDTO(JobEnterpriseDetailDTO entDetailDTO) { 
        this.entDetailDTO=entDetailDTO;
     }
    /**
    *获取企业详细信息
    */
    public JobEnterpriseDetailDTO getEntDetailDTO() { 
        return  this.entDetailDTO;
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
    /**
    *设定企业二维码
    */
    public void setErCode(String erCode) { 
        this.erCode=erCode;
     }
    /**
    *获取企业二维码
    */
    public String getErCode() { 
        return  this.erCode;
     }
    /**
    *设定是否已经是黑名单  0 不是黑名单， 1 加入黑名单
    */
    public void setIsAddBlack(Integer isAddBlack) { 
        this.isAddBlack=isAddBlack;
     }
    /**
    *获取是否已经是黑名单  0 不是黑名单， 1 加入黑名单
    */
    public Integer getIsAddBlack() { 
        return  this.isAddBlack;
     }
    /**
    *设定是否已关注    0 表示 取消关注， 1 表示关注
    */
    public void setIsAttention(Integer isAttention) { 
        this.isAttention=isAttention;
     }
    /**
    *获取是否已关注    0 表示 取消关注， 1 表示关注
    */
    public Integer getIsAttention() { 
        return  this.isAttention;
     }
    /**
    *设定标签
    */
    public void setTagNames(List<String> tagNames) { 
        this.tagNames=tagNames;
     }
    /**
    *获取标签
    */
    public List<String> getTagNames() { 
        return  this.tagNames;
     }

 } 

