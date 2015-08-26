/** 
*  JobEntPostDetailDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-8 上午11:00:22 
*/  
public class JobEntPostDetailDTO  implements Serializable { 
    //岗位基本信息 
    private JobEntPostDTO entPostDTO  ; 
    //职位要求 
    private String postRequire  ; 
    //技能要求 
    private List<JobEntTechniclRequireDTO> techniclRquire  ; 
    //相关职位 
    private List<JobEntRelationPostDTO> entRelationPostDTO  ; 
    //企业的其他职位 
    private List<JobEntOtherPostDTO> entOtherPostDTO  ; 
    //是否已经收藏	 0未收藏， 1 已经收藏 
    private Integer isFavorite  ; 
    //是否已经申请	 0 未申请，1 已经申请 
    private Integer isApplied  ; 
    //浏览次数 
    private Integer visitCount  ; 
    //用户资料是否完善  1 完善 ,0 不完整 
    private Integer isUserComplete  ; 
    //1 进入预约首页，0 进如假期工订单页面 , 2 普通岗位 
    private Integer postStatus  ; 
    //标签名称 
    private List<String> tagNames  ; 
    /**
    *设定岗位基本信息
    */
    public void setEntPostDTO(JobEntPostDTO entPostDTO) { 
        this.entPostDTO=entPostDTO;
     }
    /**
    *获取岗位基本信息
    */
    public JobEntPostDTO getEntPostDTO() { 
        return  this.entPostDTO;
     }
    /**
    *设定职位要求
    */
    public void setPostRequire(String postRequire) { 
        this.postRequire=postRequire;
     }
    /**
    *获取职位要求
    */
    public String getPostRequire() { 
        return  this.postRequire;
     }
    /**
    *设定技能要求
    */
    public void setTechniclRquire(List<JobEntTechniclRequireDTO> techniclRquire) { 
        this.techniclRquire=techniclRquire;
     }
    /**
    *获取技能要求
    */
    public List<JobEntTechniclRequireDTO> getTechniclRquire() { 
        return  this.techniclRquire;
     }
    /**
    *设定相关职位
    */
    public void setEntRelationPostDTO(List<JobEntRelationPostDTO> entRelationPostDTO) { 
        this.entRelationPostDTO=entRelationPostDTO;
     }
    /**
    *获取相关职位
    */
    public List<JobEntRelationPostDTO> getEntRelationPostDTO() { 
        return  this.entRelationPostDTO;
     }
    /**
    *设定企业的其他职位
    */
    public void setEntOtherPostDTO(List<JobEntOtherPostDTO> entOtherPostDTO) { 
        this.entOtherPostDTO=entOtherPostDTO;
     }
    /**
    *获取企业的其他职位
    */
    public List<JobEntOtherPostDTO> getEntOtherPostDTO() { 
        return  this.entOtherPostDTO;
     }
    /**
    *设定是否已经收藏	 0未收藏， 1 已经收藏
    */
    public void setIsFavorite(Integer isFavorite) { 
        this.isFavorite=isFavorite;
     }
    /**
    *获取是否已经收藏	 0未收藏， 1 已经收藏
    */
    public Integer getIsFavorite() { 
        return  this.isFavorite;
     }
    /**
    *设定是否已经申请	 0 未申请，1 已经申请
    */
    public void setIsApplied(Integer isApplied) { 
        this.isApplied=isApplied;
     }
    /**
    *获取是否已经申请	 0 未申请，1 已经申请
    */
    public Integer getIsApplied() { 
        return  this.isApplied;
     }
    /**
    *设定浏览次数
    */
    public void setVisitCount(Integer visitCount) { 
        this.visitCount=visitCount;
     }
    /**
    *获取浏览次数
    */
    public Integer getVisitCount() { 
        return  this.visitCount;
     }
    /**
    *设定用户资料是否完善  1 完善 ,0 不完整
    */
    public void setIsUserComplete(Integer isUserComplete) { 
        this.isUserComplete=isUserComplete;
     }
    /**
    *获取用户资料是否完善  1 完善 ,0 不完整
    */
    public Integer getIsUserComplete() { 
        return  this.isUserComplete;
     }
    /**
    *设定1 进入预约首页，0 进如假期工订单页面 , 2 普通岗位
    */
    public void setPostStatus(Integer postStatus) { 
        this.postStatus=postStatus;
     }
    /**
    *获取1 进入预约首页，0 进如假期工订单页面 , 2 普通岗位
    */
    public Integer getPostStatus() { 
        return  this.postStatus;
     }
    /**
    *设定标签名称
    */
    public void setTagNames(List<String> tagNames) { 
        this.tagNames=tagNames;
     }
    /**
    *获取标签名称
    */
    public List<String> getTagNames() { 
        return  this.tagNames;
     }

 } 

