/** 
*  InformationTitleInfoDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-5 下午10:40:06 
*/  
public class InformationTitleInfoDTO  implements Serializable { 
    private Long id  ; 
    //标题 
    private String title  ; 
    //图片 
    private ImgURLDTO titleImg  ; 
    //描述 
    private String desc  ; 
    //时间 
    private Date publishDate  ; 
    //跳转的url 
    private String url  ; 
    //阅读次数 
    private Integer readTimes  ; 
    private String fileCode  ; 
    private String htmlName  ; 
    private String contintType  ; 
    //资讯的属性 
    //  
    // 报名    isSignUp : 1  
    // 分享    isShare  : 1 
    // 评论    isComment: 21(表示有21条评论) 
    private Map<String,Integer> properties  ; 
    /**
    *无功能描述
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *无功能描述
    */
    public Long getId() { 
        return  this.id;
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
    *设定图片
    */
    public void setTitleImg(ImgURLDTO titleImg) { 
        this.titleImg=titleImg;
     }
    /**
    *获取图片
    */
    public ImgURLDTO getTitleImg() { 
        return  this.titleImg;
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
    *设定时间
    */
    public void setPublishDate(Date publishDate) { 
        this.publishDate=publishDate;
     }
    /**
    *获取时间
    */
    public Date getPublishDate() { 
        return  this.publishDate;
     }
    /**
    *设定跳转的url
    */
    public void setUrl(String url) { 
        this.url=url;
     }
    /**
    *获取跳转的url
    */
    public String getUrl() { 
        return  this.url;
     }
    /**
    *设定阅读次数
    */
    public void setReadTimes(Integer readTimes) { 
        this.readTimes=readTimes;
     }
    /**
    *获取阅读次数
    */
    public Integer getReadTimes() { 
        return  this.readTimes;
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
    *无功能描述
    */
    public void setHtmlName(String htmlName) { 
        this.htmlName=htmlName;
     }
    /**
    *无功能描述
    */
    public String getHtmlName() { 
        return  this.htmlName;
     }
    /**
    *无功能描述
    */
    public void setContintType(String contintType) { 
        this.contintType=contintType;
     }
    /**
    *无功能描述
    */
    public String getContintType() { 
        return  this.contintType;
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

