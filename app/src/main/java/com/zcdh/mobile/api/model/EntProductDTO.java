/** 
*  EntProductDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-4 上午10:30:15 
*/  
public class EntProductDTO  implements Serializable { 
    private Long id  ; 
    //企业id 
    private Long entId  ; 
    //产品标题 
    private String title  ; 
    //产品标题图片 
    private ImgURLDTO titleImg  ; 
    //产品描述 
    private String desc  ; 
    //产品发布的时间 
    private Date publishDate  ; 
    private String url  ; 
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
    *设定企业id
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *获取企业id
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *设定产品标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取产品标题
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定产品标题图片
    */
    public void setTitleImg(ImgURLDTO titleImg) { 
        this.titleImg=titleImg;
     }
    /**
    *获取产品标题图片
    */
    public ImgURLDTO getTitleImg() { 
        return  this.titleImg;
     }
    /**
    *设定产品描述
    */
    public void setDesc(String desc) { 
        this.desc=desc;
     }
    /**
    *获取产品描述
    */
    public String getDesc() { 
        return  this.desc;
     }
    /**
    *设定产品发布的时间
    */
    public void setPublishDate(Date publishDate) { 
        this.publishDate=publishDate;
     }
    /**
    *获取产品发布的时间
    */
    public Date getPublishDate() { 
        return  this.publishDate;
     }
    /**
    *无功能描述
    */
    public void setUrl(String url) { 
        this.url=url;
     }
    /**
    *无功能描述
    */
    public String getUrl() { 
        return  this.url;
     }

 } 

