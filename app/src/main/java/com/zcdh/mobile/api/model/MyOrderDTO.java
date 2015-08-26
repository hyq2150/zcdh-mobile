/** 
*  MyOrderDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 我的订单DTO 
*  @author focus, 2014-4-21 下午9:09:38 
*/  
public class MyOrderDTO  implements Serializable { 
    private Long userId  ; 
    private Long postId  ; 
    private String orderNum  ; 
    private String postName  ; 
    private String entName  ; 
    private Date orderDate  ; 
    private BigDecimal payMoney  ; 
    private Integer status  ; 
    private Integer postCount  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    private String areaName  ; 
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
    public void setOrderDate(Date orderDate) { 
        this.orderDate=orderDate;
     }
    /**
    *无功能描述
    */
    public Date getOrderDate() { 
        return  this.orderDate;
     }
    /**
    *无功能描述
    */
    public void setPayMoney(BigDecimal payMoney) { 
        this.payMoney=payMoney;
     }
    /**
    *无功能描述
    */
    public BigDecimal getPayMoney() { 
        return  this.payMoney;
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

 } 

