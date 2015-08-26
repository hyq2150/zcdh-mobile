/** 
*  JobFairDetailDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 招聘会详细信息 
*  @author focus, 2014-6-18 上午11:29:46 
*/  
public class JobFairDetailDTO  implements Serializable { 
    //场次id 
    private Long bannerId  ; 
    //招聘会标题 
    private String title  ; 
    //wap-宣传图片 
    private String wapFileCode  ; 
    //描述 
    private String describes  ; 
    //开始时间 
    private Date startTime  ; 
    //结束时间 
    private Date endTime  ; 
    //简介类型 html,url 
    private String introType  ; 
    //html文件名称 
    private String htmlName  ; 
    //内容描述 
    private String description  ; 
    //联系人 
    private String contacts  ; 
    //联系方式 
    private String phone  ; 
    //主办单位 
    private String organizers  ; 
    //是否报名 1 报名 0 没报名 
    private Integer isApply  ; 
    //地点 
    private String address  ; 
    //1 ： 我们为主办方 
    //   0 ： 我们不是主办方 
    private Integer isMyFair  ; 
    //手动输入url 
    private String url  ; 
    //0自动，1手动 
    private Integer urlType  ; 
    /**
    *设定场次id
    */
    public void setBannerId(Long bannerId) { 
        this.bannerId=bannerId;
     }
    /**
    *获取场次id
    */
    public Long getBannerId() { 
        return  this.bannerId;
     }
    /**
    *设定招聘会标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取招聘会标题
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定wap-宣传图片
    */
    public void setWapFileCode(String wapFileCode) { 
        this.wapFileCode=wapFileCode;
     }
    /**
    *获取wap-宣传图片
    */
    public String getWapFileCode() { 
        return  this.wapFileCode;
     }
    /**
    *设定描述
    */
    public void setDescribes(String describes) { 
        this.describes=describes;
     }
    /**
    *获取描述
    */
    public String getDescribes() { 
        return  this.describes;
     }
    /**
    *设定开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定结束时间
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *获取结束时间
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定简介类型 html,url
    */
    public void setIntroType(String introType) { 
        this.introType=introType;
     }
    /**
    *获取简介类型 html,url
    */
    public String getIntroType() { 
        return  this.introType;
     }
    /**
    *设定html文件名称
    */
    public void setHtmlName(String htmlName) { 
        this.htmlName=htmlName;
     }
    /**
    *获取html文件名称
    */
    public String getHtmlName() { 
        return  this.htmlName;
     }
    /**
    *设定内容描述
    */
    public void setDescription(String description) { 
        this.description=description;
     }
    /**
    *获取内容描述
    */
    public String getDescription() { 
        return  this.description;
     }
    /**
    *设定联系人
    */
    public void setContacts(String contacts) { 
        this.contacts=contacts;
     }
    /**
    *获取联系人
    */
    public String getContacts() { 
        return  this.contacts;
     }
    /**
    *设定联系方式
    */
    public void setPhone(String phone) { 
        this.phone=phone;
     }
    /**
    *获取联系方式
    */
    public String getPhone() { 
        return  this.phone;
     }
    /**
    *设定主办单位
    */
    public void setOrganizers(String organizers) { 
        this.organizers=organizers;
     }
    /**
    *获取主办单位
    */
    public String getOrganizers() { 
        return  this.organizers;
     }
    /**
    *设定是否报名 1 报名 0 没报名
    */
    public void setIsApply(Integer isApply) { 
        this.isApply=isApply;
     }
    /**
    *获取是否报名 1 报名 0 没报名
    */
    public Integer getIsApply() { 
        return  this.isApply;
     }
    /**
    *设定地点
    */
    public void setAddress(String address) { 
        this.address=address;
     }
    /**
    *获取地点
    */
    public String getAddress() { 
        return  this.address;
     }
    /**
    *设定1 ： 我们为主办方   0 ： 我们不是主办方
    */
    public void setIsMyFair(Integer isMyFair) { 
        this.isMyFair=isMyFair;
     }
    /**
    *获取1 ： 我们为主办方   0 ： 我们不是主办方
    */
    public Integer getIsMyFair() { 
        return  this.isMyFair;
     }
    /**
    *设定手动输入url
    */
    public void setUrl(String url) { 
        this.url=url;
     }
    /**
    *获取手动输入url
    */
    public String getUrl() { 
        return  this.url;
     }
    /**
    *设定0自动，1手动
    */
    public void setUrlType(Integer urlType) { 
        this.urlType=urlType;
     }
    /**
    *获取0自动，1手动
    */
    public Integer getUrlType() { 
        return  this.urlType;
     }

 } 

