/** 
*  JobEntShareDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 岗位分享内容 
*  @author chw, 2014-4-8 上午11:10:04 
*/  
public class JobEntShareDTO  implements Serializable { 
    private String shareTitle  ; 
    private String shareContent  ; 
    private String img  ; 
    private String url  ; 
    //分享类型  
    // Wechat ：微信 
    // SinaWeibo ：微博 
    // QQ ：QQ/电邮/短信分享内容 
    private String type  ; 
    /**
    *无功能描述
    */
    public void setShareTitle(String shareTitle) { 
        this.shareTitle=shareTitle;
     }
    /**
    *无功能描述
    */
    public String getShareTitle() { 
        return  this.shareTitle;
     }
    /**
    *无功能描述
    */
    public void setShareContent(String shareContent) { 
        this.shareContent=shareContent;
     }
    /**
    *无功能描述
    */
    public String getShareContent() { 
        return  this.shareContent;
     }
    /**
    *无功能描述
    */
    public void setImg(String img) { 
        this.img=img;
     }
    /**
    *无功能描述
    */
    public String getImg() { 
        return  this.img;
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
    /**
    *设定分享类型  Wechat ：微信 SinaWeibo ：微博 QQ ：QQ/电邮/短信分享内容
    */
    public void setType(String type) { 
        this.type=type;
     }
    /**
    *获取分享类型  Wechat ：微信 SinaWeibo ：微博 QQ ：QQ/电邮/短信分享内容
    */
    public String getType() { 
        return  this.type;
     }

 } 

