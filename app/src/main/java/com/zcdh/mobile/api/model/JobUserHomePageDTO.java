/** 
*  JobUserHomePageDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-14 下午4:29:48 
*/  
public class JobUserHomePageDTO  implements Serializable { 
    //中间的信息 
    private JobUserHomePageMiddleDTO middle  ; 
    //头部的信息 
    private JobUserHomePageTitleDTO title  ; 
    /**
    *设定中间的信息
    */
    public void setMiddle(JobUserHomePageMiddleDTO middle) { 
        this.middle=middle;
     }
    /**
    *获取中间的信息
    */
    public JobUserHomePageMiddleDTO getMiddle() { 
        return  this.middle;
     }
    /**
    *设定头部的信息
    */
    public void setTitle(JobUserHomePageTitleDTO title) { 
        this.title=title;
     }
    /**
    *获取头部的信息
    */
    public JobUserHomePageTitleDTO getTitle() { 
        return  this.title;
     }

 } 

