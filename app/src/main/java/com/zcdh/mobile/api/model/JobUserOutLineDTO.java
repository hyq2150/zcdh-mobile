/** 
*  JobUserOutLineDTO 
* 
*  Created Date: 2014-05-14 16:07:26 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-5-8 上午10:54:28
 */
public class JobUserOutLineDTO  implements Serializable { 
    /** 
     * 用户邮箱
     */
    private String email  ; 
    /** 
     * 图片
     */
    private ImgModelDTO img  ; 
    /** 
     * 用户手机号码
     */
    private String mobile  ; 
    /** 
     * 签名 1 签到，0 未签到
     */
    private Integer signIn  ; 
    /** 
     * 用户id
     */
    private Long ueserId  ; 
    /** 
     * 用户名称
     */
    private String userName  ; 
    /**
    *设定用户邮箱
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *获取设定用户邮箱
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *设定图片
    */
    public void setImg(ImgModelDTO img) { 
        this.img=img;
     }
    /**
    *获取设定图片
    */
    public ImgModelDTO getImg() { 
        return  this.img;
     }
    /**
    *设定用户手机号码
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *获取设定用户手机号码
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
    *获取设定签名 1 签到，0 未签到
    */
    public Integer getSignIn() { 
        return  this.signIn;
     }
    /**
    *设定用户id
    */
    public void setUeserId(Long ueserId) { 
        this.ueserId=ueserId;
     }
    /**
    *获取设定用户id
    */
    public Long getUeserId() { 
        return  this.ueserId;
     }
    /**
    *设定用户名称
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *获取设定用户名称
    */
    public String getUserName() { 
        return  this.userName;
     }

 } 

