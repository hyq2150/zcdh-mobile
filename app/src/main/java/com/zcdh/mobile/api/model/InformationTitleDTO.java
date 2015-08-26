/** 
*  InformationTitleDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-5 下午10:39:55 
*/  
public class InformationTitleDTO  implements Serializable { 
    //当前id的值 
    private Long id  ; 
    //消息的子页面的id 
    private Long relId  ; 
    //标题名称 
    private String title  ; 
    //0 不显示标题， 1 显示标题 
    private Integer isShowTitle  ; 
    //打开的类型： 
    // 点击类型  1 URL 2 应用跳转 
    private Integer openType  ; 
    //androidURL：openType = 1 是URL ，openType = 2 应用名称 
    private String anroidURL  ; 
    //IOSURL ：openType = 1 是URL ，openType = 2 应用名称 
    private String IOSURL  ; 
    //WPSURL：openType = 1 是URL ，openType = 2 应用名称 
    private String WPSURL  ; 
    //图片 
    private ImgURLDTO img  ; 
    //自定义参数  参数  格式如：key1 = value1,key2=value2 
    private String customParam  ; 
    private String fileCode  ; 
    //添加消息的Code 
    private String informationCode  ; 
    //状态 
    // 0 普通，31 最新，32 最热门 
    private Integer status  ; 
    //是否是系统的默认App 
    //  
    // 0 不是 
    // 1 是 
    private Integer isSystemApp  ; 
    //排序 
    private Integer orders  ; 
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
    *设定消息的子页面的id
    */
    public void setRelId(Long relId) { 
        this.relId=relId;
     }
    /**
    *获取消息的子页面的id
    */
    public Long getRelId() { 
        return  this.relId;
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
    *设定0 不显示标题， 1 显示标题
    */
    public void setIsShowTitle(Integer isShowTitle) { 
        this.isShowTitle=isShowTitle;
     }
    /**
    *获取0 不显示标题， 1 显示标题
    */
    public Integer getIsShowTitle() { 
        return  this.isShowTitle;
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
    *设定图片
    */
    public void setImg(ImgURLDTO img) { 
        this.img=img;
     }
    /**
    *获取图片
    */
    public ImgURLDTO getImg() { 
        return  this.img;
     }
    /**
    *设定自定义参数  参数  格式如：key1 = value1,key2=value2
    */
    public void setCustomParam(String customParam) { 
        this.customParam=customParam;
     }
    /**
    *获取自定义参数  参数  格式如：key1 = value1,key2=value2
    */
    public String getCustomParam() { 
        return  this.customParam;
     }
    /**
    *无功能描述
    */
    public void setFileCode(String fileCode) { 
        this.fileCode=fileCode;
     }
    /**
    *无功能描述
    */
    public String getFileCode() { 
        return  this.fileCode;
     }
    /**
    *设定添加消息的Code
    */
    public void setInformationCode(String informationCode) { 
        this.informationCode=informationCode;
     }
    /**
    *获取添加消息的Code
    */
    public String getInformationCode() { 
        return  this.informationCode;
     }
    /**
    *设定状态 0 普通，31 最新，32 最热门
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取状态 0 普通，31 最新，32 最热门
    */
    public Integer getStatus() { 
        return  this.status;
     }
    /**
    *设定是否是系统的默认App  0 不是 1 是
    */
    public void setIsSystemApp(Integer isSystemApp) { 
        this.isSystemApp=isSystemApp;
     }
    /**
    *获取是否是系统的默认App  0 不是 1 是
    */
    public Integer getIsSystemApp() { 
        return  this.isSystemApp;
     }
    /**
    *设定排序
    */
    public void setOrders(Integer orders) { 
        this.orders=orders;
     }
    /**
    *获取排序
    */
    public Integer getOrders() { 
        return  this.orders;
     }

 } 

