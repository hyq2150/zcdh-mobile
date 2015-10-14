/** 
*  LeaveMessageDTO 
* 
*  Created Date: 2015-10-12 17:31:53 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-14 下午4:27:41 
*/  
public class LeaveMessageDTO  implements Serializable { 
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
    private String postName  ; 
    //企业logo 
    private ImgURLDTO entLogo  ; 
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
    *设定企业logo
    */
    public void setEntLogo(ImgURLDTO entLogo) { 
        this.entLogo=entLogo;
     }
    /**
    *获取企业logo
    */
    public ImgURLDTO getEntLogo() { 
        return  this.entLogo;
     }

 } 

