/** 
*  InformationDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 消息DTO 
*  @author focus, 2014-6-11 下午3:34:44 
*/  
public class InformationDTO  implements Serializable { 
    //当前id的值 
    private Long id  ; 
    //标题名称 
    private String title  ; 
    //描述 
    private String desc  ; 
    //推送时间 
    private Date pushTime  ; 
    //打开的类型： 
    // 点击类型  1 URL 2 应用跳转 
    private Integer openType  ; 
    //消息数量 
    private Integer totals  ; 
    //androidURL：openType = 1 是URL ，openType = 2 应用名称 
    private String anroidURL  ; 
    //IOSURL ：openType = 1 是URL ，openType = 2 应用名称 
    private String IOSURL  ; 
    //WPSURL：openType = 1 是URL ，openType = 2 应用名称 
    private String WPSURL  ; 
    //IOS 图片 
    private ImgURLDTO iosImg  ; 
    //android 图片 
    private ImgURLDTO andoridImg  ; 
    //自定义参数  参数  格式如：key1 = value1,key2 = value2 
    private String customParam  ; 
    //0 未读，1 已读 
    private Integer isRead  ; 
    //企业名称 
    private String entName  ; 
    /**
    *设定当前id的值
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取当前id的值
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定标题名称
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取标题名称
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定描述
    */
    public void setDesc(String desc) { 
        this.desc=desc;
     }
    /**
    *获取描述
    */
    public String getDesc() { 
        return  this.desc;
     }
    /**
    *设定推送时间
    */
    public void setPushTime(Date pushTime) { 
        this.pushTime=pushTime;
     }
    /**
    *获取推送时间
    */
    public Date getPushTime() { 
        return  this.pushTime;
     }
    /**
    *设定打开的类型： 点击类型  1 URL 2 应用跳转
    */
    public void setOpenType(Integer openType) { 
        this.openType=openType;
     }
    /**
    *获取打开的类型： 点击类型  1 URL 2 应用跳转
    */
    public Integer getOpenType() { 
        return  this.openType;
     }
    /**
    *设定消息数量
    */
    public void setTotals(Integer totals) { 
        this.totals=totals;
     }
    /**
    *获取消息数量
    */
    public Integer getTotals() { 
        return  this.totals;
     }
    /**
    *设定androidURL：openType = 1 是URL ，openType = 2 应用名称
    */
    public void setAnroidURL(String anroidURL) { 
        this.anroidURL=anroidURL;
     }
    /**
    *获取androidURL：openType = 1 是URL ，openType = 2 应用名称
    */
    public String getAnroidURL() { 
        return  this.anroidURL;
     }
    /**
    *设定IOSURL ：openType = 1 是URL ，openType = 2 应用名称
    */
    public void setIOSURL(String IOSURL) { 
        this.IOSURL=IOSURL;
     }
    /**
    *获取IOSURL ：openType = 1 是URL ，openType = 2 应用名称
    */
    public String getIOSURL() { 
        return  this.IOSURL;
     }
    /**
    *设定WPSURL：openType = 1 是URL ，openType = 2 应用名称
    */
    public void setWPSURL(String WPSURL) { 
        this.WPSURL=WPSURL;
     }
    /**
    *获取WPSURL：openType = 1 是URL ，openType = 2 应用名称
    */
    public String getWPSURL() { 
        return  this.WPSURL;
     }
    /**
    *设定IOS 图片
    */
    public void setIosImg(ImgURLDTO iosImg) { 
        this.iosImg=iosImg;
     }
    /**
    *获取IOS 图片
    */
    public ImgURLDTO getIosImg() { 
        return  this.iosImg;
     }
    /**
    *设定android 图片
    */
    public void setAndoridImg(ImgURLDTO andoridImg) { 
        this.andoridImg=andoridImg;
     }
    /**
    *获取android 图片
    */
    public ImgURLDTO getAndoridImg() { 
        return  this.andoridImg;
     }
    /**
    *设定自定义参数  参数  格式如：key1 = value1,key2 = value2
    */
    public void setCustomParam(String customParam) { 
        this.customParam=customParam;
     }
    /**
    *获取自定义参数  参数  格式如：key1 = value1,key2 = value2
    */
    public String getCustomParam() { 
        return  this.customParam;
     }
    /**
    *设定0 未读，1 已读
    */
    public void setIsRead(Integer isRead) { 
        this.isRead=isRead;
     }
    /**
    *获取0 未读，1 已读
    */
    public Integer getIsRead() { 
        return  this.isRead;
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

 } 

