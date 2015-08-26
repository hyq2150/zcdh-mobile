/** 
*  TrackOrder 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 订单跟踪 
*  @author focus, 2014-4-21 下午9:12:59 
*/  
public class TrackOrder  implements Serializable { 
    private Long id  ; 
    private String order_Num  ; 
    private Date track_order_time  ; 
    private String content  ; 
    /**
    *无功能描述
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *无功能描述
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *无功能描述
    */
    public void setOrder_Num(String order_Num) { 
        this.order_Num=order_Num;
     }
    /**
    *无功能描述
    */
    public String getOrder_Num() { 
        return  this.order_Num;
     }
    /**
    *无功能描述
    */
    public void setTrack_order_time(Date track_order_time) { 
        this.track_order_time=track_order_time;
     }
    /**
    *无功能描述
    */
    public Date getTrack_order_time() { 
        return  this.track_order_time;
     }
    /**
    *无功能描述
    */
    public void setContent(String content) { 
        this.content=content;
     }
    /**
    *无功能描述
    */
    public String getContent() { 
        return  this.content;
     }

 } 

