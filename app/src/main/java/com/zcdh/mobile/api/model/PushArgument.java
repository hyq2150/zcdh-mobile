/** 
*  PushArgument 
* 
*  Created Date: 2015-08-12 17:08:07 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 推送服务的参数的实体 
*  @author focus, 2014-5-30 上午11:43:38 
*/  
public class PushArgument  implements Serializable { 
    //控制跳转Action的字段  
    //  
    // 001 表示订阅的岗位列表页面 
    private String skipActionCode  ; 
    //1 打开URL ，2 打开应用 
    private Integer openType  ; 
    //跳转的URL 
    private String openURL  ; 
    //标题 
    private String title  ; 
    //推送内容 
    private String description  ; 
    //岗位编码 
    private String postCode  ; 
    //地区编码 
    private String areaCode  ; 
    //行业编码 
    private String industryCode  ; 
    /**
    *设定控制跳转Action的字段   001 表示订阅的岗位列表页面
    */
    public void setSkipActionCode(String skipActionCode) { 
        this.skipActionCode=skipActionCode;
     }
    /**
    *获取控制跳转Action的字段   001 表示订阅的岗位列表页面
    */
    public String getSkipActionCode() { 
        return  this.skipActionCode;
     }
    /**
    *设定1 打开URL ，2 打开应用
    */
    public void setOpenType(Integer openType) { 
        this.openType=openType;
     }
    /**
    *获取1 打开URL ，2 打开应用
    */
    public Integer getOpenType() { 
        return  this.openType;
     }
    /**
    *设定跳转的URL
    */
    public void setOpenURL(String openURL) { 
        this.openURL=openURL;
     }
    /**
    *获取跳转的URL
    */
    public String getOpenURL() { 
        return  this.openURL;
     }
    /**
    *设定标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取标题
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定推送内容
    */
    public void setDescription(String description) { 
        this.description=description;
     }
    /**
    *获取推送内容
    */
    public String getDescription() { 
        return  this.description;
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
    *设定地区编码
    */
    public void setAreaCode(String areaCode) { 
        this.areaCode=areaCode;
     }
    /**
    *获取地区编码
    */
    public String getAreaCode() { 
        return  this.areaCode;
     }
    /**
    *设定行业编码
    */
    public void setIndustryCode(String industryCode) { 
        this.industryCode=industryCode;
     }
    /**
    *获取行业编码
    */
    public String getIndustryCode() { 
        return  this.industryCode;
     }

 } 

