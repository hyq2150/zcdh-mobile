/** 
*  JobUserHomePageTitleDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-14 下午4:06:08 
*/  
public class JobUserHomePageTitleDTO  implements Serializable { 
    //用户id 
    private Long ueserId  ; 
    //用户邮箱 
    private String email  ; 
    //用户手机号码 
    private String mobile  ; 
    //签名 1 签到，0 未签到 
    private Integer signIn  ; 
    //图片 
    private ImgURLDTO img  ; 
    //用户名称 
    private String userName  ; 
    //积分 
    private Integer integralTotals  ; 
    /**
    *设定用户id
    */
    public void setUeserId(Long ueserId) { 
        this.ueserId=ueserId;
     }
    /**
    *获取用户id
    */
    public Long getUeserId() { 
        return  this.ueserId;
     }
    /**
    *设定用户邮箱
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *获取用户邮箱
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *设定用户手机号码
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *获取用户手机号码
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *设定签名 1 签到，0 未签到
    */
    public void setSignIn(Integer signIn) { 
        this.signIn=signIn;
     }
    /**
    *获取签名 1 签到，0 未签到
    */
    public Integer getSignIn() { 
        return  this.signIn;
     }
    /**
    *设定图片
    */
    public void setImg(ImgURLDTO img) { 
        this.img=img;
     }
    /**
    *获取图片
    */
    public ImgURLDTO getImg() { 
        return  this.img;
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
    *设定积分
    */
    public void setIntegralTotals(Integer integralTotals) { 
        this.integralTotals=integralTotals;
     }
    /**
    *获取积分
    */
    public Integer getIntegralTotals() { 
        return  this.integralTotals;
     }

 } 

