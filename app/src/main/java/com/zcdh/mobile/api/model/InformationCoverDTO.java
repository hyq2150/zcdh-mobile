/** 
*  InformationCoverDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-5 下午10:07:07 
*/  
public class InformationCoverDTO  implements Serializable { 
    private Long coverId  ; 
    //标题 
    private String title  ; 
    //打开的类型： 
    // 点击类型  1 URL 2 应用跳转 
    private Integer openType  ; 
    //参数  格式如：key1 = value1,key2=value2 
    private String customParam  ; 
    //androidURL：openType = 1 URL ，openType = 2 应用名称 
    private String anroidURL  ; 
    //IOSURL ：openType = 1 URL ，openType = 2 应用名称 
    private String IOSURL  ; 
    //WPSURL：openType = 1 URL ，openType = 2 应用名称 
    private String WPSURL  ; 
    //封面图片 
    private ImgURLDTO cover  ; 
    private String fileCode  ; 
    //资讯的属性 
    //  
    // 报名    isSignUp : 1  
    // 分享    isShare  : 1 
    // 评论    isComment: 21(表示有21条评论) 
    private Map<String,Integer> properties  ; 
    /**
    *无功能描述
    */
    public void setCoverId(Long coverId) { 
        this.coverId=coverId;
     }
    /**
    *无功能描述
    */
    public Long getCoverId() { 
        return  this.coverId;
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
    *设定参数  格式如：key1 = value1,key2=value2
    */
    public void setCustomParam(String customParam) { 
        this.customParam=customParam;
     }
    /**
    *获取参数  格式如：key1 = value1,key2=value2
    */
    public String getCustomParam() { 
        return  this.customParam;
     }
    /**
    *设定androidURL：openType = 1 URL ，openType = 2 应用名称
    */
    public void setAnroidURL(String anroidURL) { 
        this.anroidURL=anroidURL;
     }
    /**
    *获取androidURL：openType = 1 URL ，openType = 2 应用名称
    */
    public String getAnroidURL() { 
        return  this.anroidURL;
     }
    /**
    *设定IOSURL ：openType = 1 URL ，openType = 2 应用名称
    */
    public void setIOSURL(String IOSURL) { 
        this.IOSURL=IOSURL;
     }
    /**
    *获取IOSURL ：openType = 1 URL ，openType = 2 应用名称
    */
    public String getIOSURL() { 
        return  this.IOSURL;
     }
    /**
    *设定WPSURL：openType = 1 URL ，openType = 2 应用名称
    */
    public void setWPSURL(String WPSURL) { 
        this.WPSURL=WPSURL;
     }
    /**
    *获取WPSURL：openType = 1 URL ，openType = 2 应用名称
    */
    public String getWPSURL() { 
        return  this.WPSURL;
     }
    /**
    *设定封面图片
    */
    public void setCover(ImgURLDTO cover) { 
        this.cover=cover;
     }
    /**
    *获取封面图片
    */
    public ImgURLDTO getCover() { 
        return  this.cover;
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
    *设定资讯的属性  报名    isSignUp : 1  分享    isShare  : 1 评论    isComment: 21(表示有21条评论)
    */
    public void setProperties(Map<String,Integer> properties) { 
        this.properties=properties;
     }
    /**
    *获取资讯的属性  报名    isSignUp : 1  分享    isShare  : 1 评论    isComment: 21(表示有21条评论)
    */
    public Map<String,Integer> getProperties() { 
        return  this.properties;
     }

 } 

