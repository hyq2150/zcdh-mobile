/** 
*  HomePageDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 假期工的首页 
*  @author focus, 2014-4-19 下午3:30:23 
*/  
public class HomePageDTO  implements Serializable { 
    private Integer entCount  ; 
    private Integer postCount  ; 
    private Date startTime  ; 
    private Boolean isStart  ; 
    private Integer fitPostsCount  ; 
    private Integer subscribedCount  ; 
    private BigDecimal voucherMoney  ; 
    private Boolean isSubscribe  ; 
    /**
    *无功能描述
    */
    public void setEntCount(Integer entCount) { 
        this.entCount=entCount;
     }
    /**
    *无功能描述
    */
    public Integer getEntCount() { 
        return  this.entCount;
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
    public void setIsStart(Boolean isStart) { 
        this.isStart=isStart;
     }
    /**
    *无功能描述
    */
    public Boolean getIsStart() { 
        return  this.isStart;
     }
    /**
    *无功能描述
    */
    public void setFitPostsCount(Integer fitPostsCount) { 
        this.fitPostsCount=fitPostsCount;
     }
    /**
    *无功能描述
    */
    public Integer getFitPostsCount() { 
        return  this.fitPostsCount;
     }
    /**
    *无功能描述
    */
    public void setSubscribedCount(Integer subscribedCount) { 
        this.subscribedCount=subscribedCount;
     }
    /**
    *无功能描述
    */
    public Integer getSubscribedCount() { 
        return  this.subscribedCount;
     }
    /**
    *无功能描述
    */
    public void setVoucherMoney(BigDecimal voucherMoney) { 
        this.voucherMoney=voucherMoney;
     }
    /**
    *无功能描述
    */
    public BigDecimal getVoucherMoney() { 
        return  this.voucherMoney;
     }
    /**
    *无功能描述
    */
    public void setIsSubscribe(Boolean isSubscribe) { 
        this.isSubscribe=isSubscribe;
     }
    /**
    *无功能描述
    */
    public Boolean getIsSubscribe() { 
        return  this.isSubscribe;
     }

 } 

