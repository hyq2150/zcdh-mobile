/** 
*  PayForDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 工作申请- 支付 
*  @author focus, 2014-4-19 下午5:16:13 
*/  
public class PayForDTO  implements Serializable { 
    private Long userId  ; 
    private Long postId  ; 
    private String entName  ; 
    private String postName  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    private String areaName  ; 
    private BigDecimal earnestMoney  ; 
    private Integer postCount  ; 
    private Boolean hasVoucher  ; 
    private BigDecimal voucher  ; 
    private String orderNum  ; 
    private Integer isUserMobileConfirm  ; 
    /**
    *无功能描述
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *无功能描述
    */
    public void setPostId(Long postId) { 
        this.postId=postId;
     }
    /**
    *无功能描述
    */
    public Long getPostId() { 
        return  this.postId;
     }
    /**
    *无功能描述
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *无功能描述
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *无功能描述
    */
    public void setPostName(String postName) { 
        this.postName=postName;
     }
    /**
    *无功能描述
    */
    public String getPostName() { 
        return  this.postName;
     }
    /**
    *无功能描述
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *无功能描述
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *无功能描述
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *无功能描述
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *无功能描述
    */
    public void setAreaName(String areaName) { 
        this.areaName=areaName;
     }
    /**
    *无功能描述
    */
    public String getAreaName() { 
        return  this.areaName;
     }
    /**
    *无功能描述
    */
    public void setEarnestMoney(BigDecimal earnestMoney) { 
        this.earnestMoney=earnestMoney;
     }
    /**
    *无功能描述
    */
    public BigDecimal getEarnestMoney() { 
        return  this.earnestMoney;
     }
    /**
    *无功能描述
    */
    public void setPostCount(Integer postCount) { 
        this.postCount=postCount;
     }
    /**
    *无功能描述
    */
    public Integer getPostCount() { 
        return  this.postCount;
     }
    /**
    *无功能描述
    */
    public void setHasVoucher(Boolean hasVoucher) { 
        this.hasVoucher=hasVoucher;
     }
    /**
    *无功能描述
    */
    public Boolean getHasVoucher() { 
        return  this.hasVoucher;
     }
    /**
    *无功能描述
    */
    public void setVoucher(BigDecimal voucher) { 
        this.voucher=voucher;
     }
    /**
    *无功能描述
    */
    public BigDecimal getVoucher() { 
        return  this.voucher;
     }
    /**
    *无功能描述
    */
    public void setOrderNum(String orderNum) { 
        this.orderNum=orderNum;
     }
    /**
    *无功能描述
    */
    public String getOrderNum() { 
        return  this.orderNum;
     }
    /**
    *无功能描述
    */
    public void setIsUserMobileConfirm(Integer isUserMobileConfirm) { 
        this.isUserMobileConfirm=isUserMobileConfirm;
     }
    /**
    *无功能描述
    */
    public Integer getIsUserMobileConfirm() { 
        return  this.isUserMobileConfirm;
     }

 } 

