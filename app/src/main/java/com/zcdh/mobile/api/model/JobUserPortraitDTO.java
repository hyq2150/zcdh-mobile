/** 
*  JobUserPortraitDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-7-17 下午5:00:15 
*/  
public class JobUserPortraitDTO  implements Serializable { 
    //个人头像 
    private ImgURLDTO portrait  ; 
    //个人姓名 
    private String userName  ; 
    //0  用户没有头像，也没有名字显示系统默认头像， 
    //  1  头像用名字组装 并且 性别为男，  
    //  2 头像用名字组装，并且性别为女， 
    //  3 用户有头像 
    private Integer status  ; 
    /**
    *设定个人头像
    */
    public void setPortrait(ImgURLDTO portrait) { 
        this.portrait=portrait;
     }
    /**
    *获取个人头像
    */
    public ImgURLDTO getPortrait() { 
        return  this.portrait;
     }
    /**
    *设定个人姓名
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *获取个人姓名
    */
    public String getUserName() { 
        return  this.userName;
     }
    /**
    *设定0  用户没有头像，也没有名字显示系统默认头像，  1  头像用名字组装 并且 性别为男，   2 头像用名字组装，并且性别为女，  3 用户有头像
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取0  用户没有头像，也没有名字显示系统默认头像，  1  头像用名字组装 并且 性别为男，   2 头像用名字组装，并且性别为女，  3 用户有头像
    */
    public Integer getStatus() { 
        return  this.status;
     }

 } 

