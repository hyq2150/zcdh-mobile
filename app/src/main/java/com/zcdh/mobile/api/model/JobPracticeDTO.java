/** 
*  JobPracticeDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 实践经历 
*  @author liyuan, 2015-7-15 下午5:57:15 
*/  
public class JobPracticeDTO  implements Serializable { 
    //主键id 
    private Long practice_id  ; 
    //用户id 
    private Long user_id  ; 
    //实践名称 
    private String practice_name  ; 
    //实践描述 
    private String practice_description  ; 
    //开始时间 
    private Date start_time  ; 
    //结束时间 
    private Date end_time  ; 
    /**
    *设定主键id
    */
    public void setPractice_id(Long practice_id) { 
        this.practice_id=practice_id;
     }
    /**
    *获取主键id
    */
    public Long getPractice_id() { 
        return  this.practice_id;
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
    *设定实践名称
    */
    public void setPractice_name(String practice_name) { 
        this.practice_name=practice_name;
     }
    /**
    *获取实践名称
    */
    public String getPractice_name() { 
        return  this.practice_name;
     }
    /**
    *设定实践描述
    */
    public void setPractice_description(String practice_description) { 
        this.practice_description=practice_description;
     }
    /**
    *获取实践描述
    */
    public String getPractice_description() { 
        return  this.practice_description;
     }
    /**
    *设定开始时间
    */
    public void setStart_time(Date start_time) { 
        this.start_time=start_time;
     }
    /**
    *获取开始时间
    */
    public Date getStart_time() { 
        return  this.start_time;
     }
    /**
    *设定结束时间
    */
    public void setEnd_time(Date end_time) { 
        this.end_time=end_time;
     }
    /**
    *获取结束时间
    */
    public Date getEnd_time() { 
        return  this.end_time;
     }

 } 

