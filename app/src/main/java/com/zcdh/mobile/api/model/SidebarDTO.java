/** 
*  SidebarDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 侧边栏信息 
*  @author focus, 2014-6-11 下午3:11:16 
*/  
public class SidebarDTO  implements Serializable { 
    //用户id 
    private Long userId  ; 
    //用户名称 
    private String userName  ; 
    //头像 
    private ImgURLDTO imgUrl  ; 
    //积分 
    private Integer integralTotals  ; 
    //是否签到 : 1 签到，0 未签到 
    private Integer isSign  ; 
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
    *设定头像
    */
    public void setImgUrl(ImgURLDTO imgUrl) { 
        this.imgUrl=imgUrl;
     }
    /**
    *获取头像
    */
    public ImgURLDTO getImgUrl() { 
        return  this.imgUrl;
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
    /**
    *设定是否签到 : 1 签到，0 未签到
    */
    public void setIsSign(Integer isSign) { 
        this.isSign=isSign;
     }
    /**
    *获取是否签到 : 1 签到，0 未签到
    */
    public Integer getIsSign() { 
        return  this.isSign;
     }

 } 

