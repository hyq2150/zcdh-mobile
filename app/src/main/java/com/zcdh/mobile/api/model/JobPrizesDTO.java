/** 
*  JobPrizesDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 获奖经历 
*  @author liyuan, 2015-7-15 下午6:05:42 
*/  
public class JobPrizesDTO  implements Serializable { 
    //主键id 
    private Long prizes_id  ; 
    //用户id 
    private Long user_id  ; 
    //奖项名称 
    private String prizes_name  ; 
    //奖项描述 
    private String prizes_description  ; 
    //获奖时间 
    private Date time  ; 
    /**
    *设定主键id
    */
    public void setPrizes_id(Long prizes_id) { 
        this.prizes_id=prizes_id;
     }
    /**
    *获取主键id
    */
    public Long getPrizes_id() { 
        return  this.prizes_id;
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
    *设定奖项名称
    */
    public void setPrizes_name(String prizes_name) { 
        this.prizes_name=prizes_name;
     }
    /**
    *获取奖项名称
    */
    public String getPrizes_name() { 
        return  this.prizes_name;
     }
    /**
    *设定奖项描述
    */
    public void setPrizes_description(String prizes_description) { 
        this.prizes_description=prizes_description;
     }
    /**
    *获取奖项描述
    */
    public String getPrizes_description() { 
        return  this.prizes_description;
     }
    /**
    *设定获奖时间
    */
    public void setTime(Date time) { 
        this.time=time;
     }
    /**
    *获取获奖时间
    */
    public Date getTime() { 
        return  this.time;
     }

 } 

