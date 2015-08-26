/** 
*  JobEntPostShareDTO 
* 
*  Created Date: 2014-07-22 11:42:07 
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
public class JobEntPostShareDTO  implements Serializable { 
    private String shareTitle  ; 
    private String shareContent  ; 
    private String img  ; 
    private String url  ; 
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

 } 

