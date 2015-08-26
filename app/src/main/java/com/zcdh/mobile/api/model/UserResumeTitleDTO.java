/** 
*  UserResumeTitleDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午2:20:15 
*/  
public class UserResumeTitleDTO  implements Serializable { 
    //用户id 
    private Long userId  ; 
    //求职状态编码 
    private String jobStatusCode  ; 
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
    *设定求职状态编码
    */
    public void setJobStatusCode(String jobStatusCode) { 
        this.jobStatusCode=jobStatusCode;
     }
    /**
    *获取求职状态编码
    */
    public String getJobStatusCode() { 
        return  this.jobStatusCode;
     }

 } 

