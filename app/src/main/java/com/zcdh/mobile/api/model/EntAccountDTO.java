/** 
*  EntAccountDTO 
* 
*  Created Date: 2014-06-06 10:16:51 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业账号 
*  @author hw, 2014-5-27 上午11:00:12 
*/  
public class EntAccountDTO  implements Serializable { 
    private Long accountId  ; 
    private Long entId  ; 
    private String account  ; 
    private String pwd  ; 
    private Integer status  ; 
    private Date createDate  ; 
    private String remark  ; 
    /**
    *无功能描述
    */
    public void setAccountId(Long accountId) { 
        this.accountId=accountId;
     }
    /**
    *无功能描述
    */
    public Long getAccountId() { 
        return  this.accountId;
     }
    /**
    *无功能描述
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *无功能描述
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *无功能描述
    */
    public void setAccount(String account) { 
        this.account=account;
     }
    /**
    *无功能描述
    */
    public String getAccount() { 
        return  this.account;
     }
    /**
    *无功能描述
    */
    public void setPwd(String pwd) { 
        this.pwd=pwd;
     }
    /**
    *无功能描述
    */
    public String getPwd() { 
        return  this.pwd;
     }
    /**
    *无功能描述
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *无功能描述
    */
    public Integer getStatus() { 
        return  this.status;
     }
    /**
    *无功能描述
    */
    public void setCreateDate(Date createDate) { 
        this.createDate=createDate;
     }
    /**
    *无功能描述
    */
    public Date getCreateDate() { 
        return  this.createDate;
     }
    /**
    *无功能描述
    */
    public void setRemark(String remark) { 
        this.remark=remark;
     }
    /**
    *无功能描述
    */
    public String getRemark() { 
        return  this.remark;
     }

 } 

