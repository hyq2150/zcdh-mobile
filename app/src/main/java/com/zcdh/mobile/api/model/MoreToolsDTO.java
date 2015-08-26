/** 
*  MoreToolsDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 更多的工具 应用 
*  @author focus, 2014-6-18 下午2:44:20 
*/  
public class MoreToolsDTO  implements Serializable { 
    //id 
    private Long id  ; 
    //名称 
    private String toolName  ; 
    //是否显示title 
    private Integer isShowTitle  ; 
    //IOS图片 
    private ImgURLDTO iosImg  ; 
    //android 图片 
    private ImgURLDTO andoridImg  ; 
    //ios 跳转路径 
    private String iosURL  ; 
    //android 跳转路径 
    private String androidURL  ; 
    //打开类型 
    private Integer openType  ; 
    //参数 
    private String customParam  ; 
    //状态  1 是系统级别的 
    private Integer status  ; 
    /**
    *设定id
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取id
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定名称
    */
    public void setToolName(String toolName) { 
        this.toolName=toolName;
     }
    /**
    *获取名称
    */
    public String getToolName() { 
        return  this.toolName;
     }
    /**
    *设定是否显示title
    */
    public void setIsShowTitle(Integer isShowTitle) { 
        this.isShowTitle=isShowTitle;
     }
    /**
    *获取是否显示title
    */
    public Integer getIsShowTitle() { 
        return  this.isShowTitle;
     }
    /**
    *设定IOS图片
    */
    public void setIosImg(ImgURLDTO iosImg) { 
        this.iosImg=iosImg;
     }
    /**
    *获取IOS图片
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
    *设定ios 跳转路径
    */
    public void setIosURL(String iosURL) { 
        this.iosURL=iosURL;
     }
    /**
    *获取ios 跳转路径
    */
    public String getIosURL() { 
        return  this.iosURL;
     }
    /**
    *设定android 跳转路径
    */
    public void setAndroidURL(String androidURL) { 
        this.androidURL=androidURL;
     }
    /**
    *获取android 跳转路径
    */
    public String getAndroidURL() { 
        return  this.androidURL;
     }
    /**
    *设定打开类型
    */
    public void setOpenType(Integer openType) { 
        this.openType=openType;
     }
    /**
    *获取打开类型
    */
    public Integer getOpenType() { 
        return  this.openType;
     }
    /**
    *设定参数
    */
    public void setCustomParam(String customParam) { 
        this.customParam=customParam;
     }
    /**
    *获取参数
    */
    public String getCustomParam() { 
        return  this.customParam;
     }
    /**
    *设定状态  1 是系统级别的
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取状态  1 是系统级别的
    */
    public Integer getStatus() { 
        return  this.status;
     }

 } 

