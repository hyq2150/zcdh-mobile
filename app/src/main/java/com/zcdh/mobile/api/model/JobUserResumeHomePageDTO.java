/** 
*  JobUserResumeHomePageDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-14 下午4:41:09 
*/  
public class JobUserResumeHomePageDTO  implements Serializable { 
    //中间内容 
    private JobUserResumeMiddleDTO middle  ; 
    //头部 
    private JobUserResumeTitleDTO title  ; 
    /**
    *设定中间内容
    */
    public void setMiddle(JobUserResumeMiddleDTO middle) { 
        this.middle=middle;
     }
    /**
    *获取中间内容
    */
    public JobUserResumeMiddleDTO getMiddle() { 
        return  this.middle;
     }
    /**
    *设定头部
    */
    public void setTitle(JobUserResumeTitleDTO title) { 
        this.title=title;
     }
    /**
    *获取头部
    */
    public JobUserResumeTitleDTO getTitle() { 
        return  this.title;
     }

 } 

