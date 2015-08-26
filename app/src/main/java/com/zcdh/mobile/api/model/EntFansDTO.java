/** 
*  EntFansDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-6-4 上午10:31:18 
*/  
public class EntFansDTO  implements Serializable { 
    //entId 
    private Long endId  ; 
    //用户名称 
    private String userName  ; 
    //用户id 
    private Long userId  ; 
    //男 003.001 
    // 女 003.002 
    private String gender  ; 
    //用户头像 
    private ImgURLDTO img  ; 
    /**
    *设定entId
    */
    public void setEndId(Long endId) { 
        this.endId=endId;
     }
    /**
    *获取entId
    */
    public Long getEndId() { 
        return  this.endId;
     }
    /**
    *设定用户名称
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *获取用户名称
    */
    public String getUserName() { 
        return  this.userName;
     }
    /**
    *设定用户id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取用户id
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定男 003.001 女 003.002
    */
    public void setGender(String gender) { 
        this.gender=gender;
     }
    /**
    *获取男 003.001 女 003.002
    */
    public String getGender() { 
        return  this.gender;
     }
    /**
    *设定用户头像
    */
    public void setImg(ImgURLDTO img) { 
        this.img=img;
     }
    /**
    *获取用户头像
    */
    public ImgURLDTO getImg() { 
        return  this.img;
     }

 } 

