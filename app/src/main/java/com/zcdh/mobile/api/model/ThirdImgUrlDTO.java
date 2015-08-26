/** 
*  ThirdImgUrlDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-9-26 上午10:16:17 
*/  
public class ThirdImgUrlDTO  implements Serializable { 
    //绑定类型 QQ , weiBo 
    private String bindType  ; 
    //对应的绑定头像 
    private ImgURLDTO bindPortrait  ; 
    /**
    *设定绑定类型 QQ , weiBo
    */
    public void setBindType(String bindType) { 
        this.bindType=bindType;
     }
    /**
    *获取绑定类型 QQ , weiBo
    */
    public String getBindType() { 
        return  this.bindType;
     }
    /**
    *设定对应的绑定头像
    */
    public void setBindPortrait(ImgURLDTO bindPortrait) { 
        this.bindPortrait=bindPortrait;
     }
    /**
    *获取对应的绑定头像
    */
    public ImgURLDTO getBindPortrait() { 
        return  this.bindPortrait;
     }

 } 

