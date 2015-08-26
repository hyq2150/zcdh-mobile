/** 
*  ImgDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-12-31 下午1:44:12 
*/  
public class ImgDTO  implements Serializable { 
    //图片名称 
    private String imgName  ; 
    //地址的前段 
    private String urlPre  ; 
    /**
    *设定图片名称
    */
    public void setImgName(String imgName) { 
        this.imgName=imgName;
     }
    /**
    *获取图片名称
    */
    public String getImgName() { 
        return  this.imgName;
     }
    /**
    *设定地址的前段
    */
    public void setUrlPre(String urlPre) { 
        this.urlPre=urlPre;
     }
    /**
    *获取地址的前段
    */
    public String getUrlPre() { 
        return  this.urlPre;
     }

 } 

