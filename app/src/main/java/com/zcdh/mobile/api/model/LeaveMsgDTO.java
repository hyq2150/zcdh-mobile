/** 
*  LeaveMsgDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-7-22 下午5:15:54 
*/  
public class LeaveMsgDTO  implements Serializable { 
    //企业id 
    private Long entId  ; 
    //用户id 
    private Long userId  ; 
    //内容 
    private String content  ; 
    //留言类型	001：咨询或意见，002：求职咨询，003：纠错意见 
    private String type  ; 
    private Long postId  ; 
    //是否是用户 
    // 0 不是用户提交的信息, 1 为用户提交的信息 
    private Integer isUserSubmit  ; 
    //留言的时间 
    private Date leaveDate  ; 
    //岗位名字 
    private String postName  ; 
    /**
    *设定企业id
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *获取企业id
    */
    public Long getEntId() { 
        return  this.entId;
     }
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
    *设定内容
    */
    public void setContent(String content) { 
        this.content=content;
     }
    /**
    *获取内容
    */
    public String getContent() { 
        return  this.content;
     }
    /**
    *设定留言类型	001：咨询或意见，002：求职咨询，003：纠错意见
    */
    public void setType(String type) { 
        this.type=type;
     }
    /**
    *获取留言类型	001：咨询或意见，002：求职咨询，003：纠错意见
    */
    public String getType() { 
        return  this.type;
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
    *设定是否是用户 0 不是用户提交的信息, 1 为用户提交的信息
    */
    public void setIsUserSubmit(Integer isUserSubmit) { 
        this.isUserSubmit=isUserSubmit;
     }
    /**
    *获取是否是用户 0 不是用户提交的信息, 1 为用户提交的信息
    */
    public Integer getIsUserSubmit() { 
        return  this.isUserSubmit;
     }
    /**
    *设定留言的时间
    */
    public void setLeaveDate(Date leaveDate) { 
        this.leaveDate=leaveDate;
     }
    /**
    *获取留言的时间
    */
    public Date getLeaveDate() { 
        return  this.leaveDate;
     }
    /**
    *设定岗位名字
    */
    public void setPostName(String postName) { 
        this.postName=postName;
     }
    /**
    *获取岗位名字
    */
    public String getPostName() { 
        return  this.postName;
     }

 } 

