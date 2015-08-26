/** 
*  ImgURLDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 上午11:10:03 
*/  
public class ImgURLDTO  implements Serializable { 
    //小图片 
    private String small  ; 
    //中等图片 
    private String medium  ; 
    //大图片 
    private String big  ; 
    //原始图片 
    private String original  ; 
    /**
    *设定小图片
    */
    public void setSmall(String small) { 
        this.small=small;
     }
    /**
    *获取小图片
    */
    public String getSmall() { 
        return  this.small;
     }
    /**
    *设定中等图片
    */
    public void setMedium(String medium) { 
        this.medium=medium;
     }
    /**
    *获取中等图片
    */
    public String getMedium() { 
        return  this.medium;
     }
    /**
    *设定大图片
    */
    public void setBig(String big) { 
        this.big=big;
     }
    /**
    *获取大图片
    */
    public String getBig() { 
        return  this.big;
     }
    /**
    *设定原始图片
    */
    public void setOriginal(String original) { 
        this.original=original;
     }
    /**
    *获取原始图片
    */
    public String getOriginal() { 
        return  this.original;
     }

 } 

