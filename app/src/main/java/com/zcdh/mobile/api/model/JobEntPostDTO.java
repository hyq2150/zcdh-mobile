/** 
*  JobEntPostDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索岗位列表DTO 
*  @author focus, 2014-4-1 下午5:19:47 
*/  
public class JobEntPostDTO  implements Serializable { 
    private Long postId  ; 
    private Long entId  ; 
    //岗位名称 
    private String postAliases  ; 
    //工作性质 
    private String JobCategory  ; 
    //工作性质编码 
    private String postPropertyCode  ; 
    //工作经验 
    private String WorkExperience  ; 
    //岗位所在的详细地址 
    private String postAddress  ; 
    //学历 
    private String degree  ; 
    //企业名称 
    private String entName  ; 
    private String fullEntName  ; 
    //是否认证，0：没认证，1：认证 
    private Integer isLegalize  ; 
    //薪水 
    private String salary  ; 
    //发布时间 
    private Date publishDate  ; 
    //岗位的招聘数量 
    private Integer employCount  ; 
    private String employCountStr  ; 
    //求职申请的数量 
    private Integer applyCount  ; 
    //公司福利 
    private List<String> welfares  ; 
    //公司的标签 
    private List<String> tagNames  ; 
    //专业名称 
    private String majorName  ; 
    //工作时间 
    private String workDate  ; 
    //岗位编码 
    private String postCode  ; 
    private Double lat  ; 
    private Double lon  ; 
    //是否计算距离的标记： 关于岗位列表的距离计算 。<br> 
    //  
    // true 计算， false 不计算距离 
    private Boolean isCountDistance  ; 
    //距离， 单位千米 
    private Double distance  ; 
    //岗位多在的城市， 地区 
    private String areaName  ; 
    //岗位工作的开始时间 
    private Date startTime  ; 
    //岗位工作的结束时间 
    private Date endTime  ; 
    //岗位的匹配度 
    private int matchRate  ; 
    //企业短名 
    private String entShortName  ; 
    private Integer visitTimes  ; 
    private String postdesc  ; 
    private Double sincerity_gold  ; 
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
    *设定岗位名称
    */
    public void setPostAliases(String postAliases) { 
        this.postAliases=postAliases;
     }
    /**
    *获取岗位名称
    */
    public String getPostAliases() { 
        return  this.postAliases;
     }
    /**
    *设定工作性质
    */
    public void setJobCategory(String JobCategory) { 
        this.JobCategory=JobCategory;
     }
    /**
    *获取工作性质
    */
    public String getJobCategory() { 
        return  this.JobCategory;
     }
    /**
    *设定工作性质编码
    */
    public void setPostPropertyCode(String postPropertyCode) { 
        this.postPropertyCode=postPropertyCode;
     }
    /**
    *获取工作性质编码
    */
    public String getPostPropertyCode() { 
        return  this.postPropertyCode;
     }
    /**
    *设定工作经验
    */
    public void setWorkExperience(String WorkExperience) { 
        this.WorkExperience=WorkExperience;
     }
    /**
    *获取工作经验
    */
    public String getWorkExperience() { 
        return  this.WorkExperience;
     }
    /**
    *设定岗位所在的详细地址
    */
    public void setPostAddress(String postAddress) { 
        this.postAddress=postAddress;
     }
    /**
    *获取岗位所在的详细地址
    */
    public String getPostAddress() { 
        return  this.postAddress;
     }
    /**
    *设定学历
    */
    public void setDegree(String degree) { 
        this.degree=degree;
     }
    /**
    *获取学历
    */
    public String getDegree() { 
        return  this.degree;
     }
    /**
    *设定企业名称
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *获取企业名称
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *无功能描述
    */
    public void setFullEntName(String fullEntName) { 
        this.fullEntName=fullEntName;
     }
    /**
    *无功能描述
    */
    public String getFullEntName() { 
        return  this.fullEntName;
     }
    /**
    *设定是否认证，0：没认证，1：认证
    */
    public void setIsLegalize(Integer isLegalize) { 
        this.isLegalize=isLegalize;
     }
    /**
    *获取是否认证，0：没认证，1：认证
    */
    public Integer getIsLegalize() { 
        return  this.isLegalize;
     }
    /**
    *设定薪水
    */
    public void setSalary(String salary) { 
        this.salary=salary;
     }
    /**
    *获取薪水
    */
    public String getSalary() { 
        return  this.salary;
     }
    /**
    *设定发布时间
    */
    public void setPublishDate(Date publishDate) { 
        this.publishDate=publishDate;
     }
    /**
    *获取发布时间
    */
    public Date getPublishDate() { 
        return  this.publishDate;
     }
    /**
    *设定岗位的招聘数量
    */
    public void setEmployCount(Integer employCount) { 
        this.employCount=employCount;
     }
    /**
    *获取岗位的招聘数量
    */
    public Integer getEmployCount() { 
        return  this.employCount;
     }
    /**
    *无功能描述
    */
    public void setEmployCountStr(String employCountStr) { 
        this.employCountStr=employCountStr;
     }
    /**
    *无功能描述
    */
    public String getEmployCountStr() { 
        return  this.employCountStr;
     }
    /**
    *设定求职申请的数量
    */
    public void setApplyCount(Integer applyCount) { 
        this.applyCount=applyCount;
     }
    /**
    *获取求职申请的数量
    */
    public Integer getApplyCount() { 
        return  this.applyCount;
     }
    /**
    *设定公司福利
    */
    public void setWelfares(List<String> welfares) { 
        this.welfares=welfares;
     }
    /**
    *获取公司福利
    */
    public List<String> getWelfares() { 
        return  this.welfares;
     }
    /**
    *设定公司的标签
    */
    public void setTagNames(List<String> tagNames) { 
        this.tagNames=tagNames;
     }
    /**
    *获取公司的标签
    */
    public List<String> getTagNames() { 
        return  this.tagNames;
     }
    /**
    *设定专业名称
    */
    public void setMajorName(String majorName) { 
        this.majorName=majorName;
     }
    /**
    *获取专业名称
    */
    public String getMajorName() { 
        return  this.majorName;
     }
    /**
    *设定工作时间
    */
    public void setWorkDate(String workDate) { 
        this.workDate=workDate;
     }
    /**
    *获取工作时间
    */
    public String getWorkDate() { 
        return  this.workDate;
     }
    /**
    *设定岗位编码
    */
    public void setPostCode(String postCode) { 
        this.postCode=postCode;
     }
    /**
    *获取岗位编码
    */
    public String getPostCode() { 
        return  this.postCode;
     }
    /**
    *无功能描述
    */
    public void setLat(Double lat) { 
        this.lat=lat;
     }
    /**
    *无功能描述
    */
    public Double getLat() { 
        return  this.lat;
     }
    /**
    *无功能描述
    */
    public void setLon(Double lon) { 
        this.lon=lon;
     }
    /**
    *无功能描述
    */
    public Double getLon() { 
        return  this.lon;
     }
    /**
    *设定是否计算距离的标记： 关于岗位列表的距离计算 。<br>  true 计算， false 不计算距离
    */
    public void setIsCountDistance(Boolean isCountDistance) { 
        this.isCountDistance=isCountDistance;
     }
    /**
    *获取是否计算距离的标记： 关于岗位列表的距离计算 。<br>  true 计算， false 不计算距离
    */
    public Boolean getIsCountDistance() { 
        return  this.isCountDistance;
     }
    /**
    *设定距离， 单位千米
    */
    public void setDistance(Double distance) { 
        this.distance=distance;
     }
    /**
    *获取距离， 单位千米
    */
    public Double getDistance() { 
        return  this.distance;
     }
    /**
    *设定岗位多在的城市， 地区
    */
    public void setAreaName(String areaName) { 
        this.areaName=areaName;
     }
    /**
    *获取岗位多在的城市， 地区
    */
    public String getAreaName() { 
        return  this.areaName;
     }
    /**
    *设定岗位工作的开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取岗位工作的开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定岗位工作的结束时间
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *获取岗位工作的结束时间
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定岗位的匹配度
    */
    public void setMatchRate(int matchRate) { 
        this.matchRate=matchRate;
     }
    /**
    *获取岗位的匹配度
    */
    public int getMatchRate() { 
        return  this.matchRate;
     }
    /**
    *设定企业短名
    */
    public void setEntShortName(String entShortName) { 
        this.entShortName=entShortName;
     }
    /**
    *获取企业短名
    */
    public String getEntShortName() { 
        return  this.entShortName;
     }
    /**
    *无功能描述
    */
    public void setVisitTimes(Integer visitTimes) { 
        this.visitTimes=visitTimes;
     }
    /**
    *无功能描述
    */
    public Integer getVisitTimes() { 
        return  this.visitTimes;
     }
    /**
    *无功能描述
    */
    public void setPostdesc(String postdesc) { 
        this.postdesc=postdesc;
     }
    /**
    *无功能描述
    */
    public String getPostdesc() { 
        return  this.postdesc;
     }
    /**
    *无功能描述
    */
    public void setSincerity_gold(Double sincerity_gold) { 
        this.sincerity_gold=sincerity_gold;
     }
    /**
    *无功能描述
    */
    public Double getSincerity_gold() { 
        return  this.sincerity_gold;
     }

 } 

