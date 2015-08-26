/** 
*  InvitationcodeUseDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 邀请码使用记录DTO 
*  @author liyuan, 2015-6-30 下午1:23:18 
*/  
public class InvitationcodeUseDTO  implements Serializable { 
    //主键 
    private Long id  ; 
    //邀请码 
    private String invitation_code  ; 
    //操作类别,取值: 
    // 1-安装 
    // 2-注册 
    private String operation_type  ; 
    //用户id 
    private Long user_id  ; 
    //用户账号 
    private String account  ; 
    //账号类别:取值 
    // 1-手机 
    // 2-第三方账号 
    private String account_type  ; 
    //手机的唯一串号 
    private String miei  ; 
    //建立时间 
    private Date create_time  ; 
    //备注 
    private String memo  ; 
    /**
    *设定主键
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取主键
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定邀请码
    */
    public void setInvitation_code(String invitation_code) { 
        this.invitation_code=invitation_code;
     }
    /**
    *获取邀请码
    */
    public String getInvitation_code() { 
        return  this.invitation_code;
     }
    /**
    *设定操作类别,取值: 1-安装 2-注册
    */
    public void setOperation_type(String operation_type) { 
        this.operation_type=operation_type;
     }
    /**
    *获取操作类别,取值: 1-安装 2-注册
    */
    public String getOperation_type() { 
        return  this.operation_type;
     }
    /**
    *设定用户id
    */
    public void setUser_id(Long user_id) { 
        this.user_id=user_id;
     }
    /**
    *获取用户id
    */
    public Long getUser_id() { 
        return  this.user_id;
     }
    /**
    *设定用户账号
    */
    public void setAccount(String account) { 
        this.account=account;
     }
    /**
    *获取用户账号
    */
    public String getAccount() { 
        return  this.account;
     }
    /**
    *设定账号类别:取值 1-手机 2-第三方账号
    */
    public void setAccount_type(String account_type) { 
        this.account_type=account_type;
     }
    /**
    *获取账号类别:取值 1-手机 2-第三方账号
    */
    public String getAccount_type() { 
        return  this.account_type;
     }
    /**
    *设定手机的唯一串号
    */
    public void setMiei(String miei) { 
        this.miei=miei;
     }
    /**
    *获取手机的唯一串号
    */
    public String getMiei() { 
        return  this.miei;
     }
    /**
    *设定建立时间
    */
    public void setCreate_time(Date create_time) { 
        this.create_time=create_time;
     }
    /**
    *获取建立时间
    */
    public Date getCreate_time() { 
        return  this.create_time;
     }
    /**
    *设定备注
    */
    public void setMemo(String memo) { 
        this.memo=memo;
     }
    /**
    *获取备注
    */
    public String getMemo() { 
        return  this.memo;
     }

 } 

