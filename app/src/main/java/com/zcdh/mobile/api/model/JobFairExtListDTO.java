/** 
*  JobFairExtListDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 招聘会列表扩展 
*  @author liyuan, 2015-5-5 下午3:47:54 
*/  
public class JobFairExtListDTO  implements Serializable { 
    //招聘会场次 
    private Long bannerId  ; 
    //开始时间 
    private Date startTime  ; 
    //结束时间 
    private Date endTime  ; 
    //标题 
    private String title  ; 
    //描述 
    private String decription  ; 
    //招聘会状态 
    // 0: 不通过  ，1：生效，上线，-1：审核中,2:草稿中  3：失效，暂停，4：已关闭 5：已结束 
    private Integer status  ; 
    //是否允许报名 
    // 0:不允许报名，1：允许报名 
    private Integer sign_status  ; 
    //1:已结束，2：已暂停，3：进行中 
    private Integer jobfair_status  ; 
    //招聘会状态名称 
    private String status_name  ; 
    //时间范围字符串 
    private String timeRange  ; 
    /**
    *设定招聘会场次
    */
    public void setBannerId(Long bannerId) { 
        this.bannerId=bannerId;
     }
    /**
    *获取招聘会场次
    */
    public Long getBannerId() { 
        return  this.bannerId;
     }
    /**
    *设定开始时间
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *获取开始时间
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *设定结束时间
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *获取结束时间
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *设定标题
    */
    public void setTitle(String title) { 
        this.title=title;
     }
    /**
    *获取标题
    */
    public String getTitle() { 
        return  this.title;
     }
    /**
    *设定描述
    */
    public void setDecription(String decription) { 
        this.decription=decription;
     }
    /**
    *获取描述
    */
    public String getDecription() { 
        return  this.decription;
     }
    /**
    *设定招聘会状态 0: 不通过  ，1：生效，上线，-1：审核中,2:草稿中  3：失效，暂停，4：已关闭 5：已结束
    */
    public void setStatus(Integer status) { 
        this.status=status;
     }
    /**
    *获取招聘会状态 0: 不通过  ，1：生效，上线，-1：审核中,2:草稿中  3：失效，暂停，4：已关闭 5：已结束
    */
    public Integer getStatus() { 
        return  this.status;
     }
    /**
    *设定是否允许报名 0:不允许报名，1：允许报名
    */
    public void setSign_status(Integer sign_status) { 
        this.sign_status=sign_status;
     }
    /**
    *获取是否允许报名 0:不允许报名，1：允许报名
    */
    public Integer getSign_status() { 
        return  this.sign_status;
     }
    /**
    *设定1:已结束，2：已暂停，3：进行中
    */
    public void setJobfair_status(Integer jobfair_status) { 
        this.jobfair_status=jobfair_status;
     }
    /**
    *获取1:已结束，2：已暂停，3：进行中
    */
    public Integer getJobfair_status() { 
        return  this.jobfair_status;
     }
    /**
    *设定招聘会状态名称
    */
    public void setStatus_name(String status_name) { 
        this.status_name=status_name;
     }
    /**
    *获取招聘会状态名称
    */
    public String getStatus_name() { 
        return  this.status_name;
     }
    /**
    *设定时间范围字符串
    */
    public void setTimeRange(String timeRange) { 
        this.timeRange=timeRange;
     }
    /**
    *获取时间范围字符串
    */
    public String getTimeRange() { 
        return  this.timeRange;
     }

 } 

