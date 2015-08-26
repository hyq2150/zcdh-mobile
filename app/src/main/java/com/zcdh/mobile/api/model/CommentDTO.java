/** 
*  CommentDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author hw, 2013-11-6 下午4:33:43 
*/  
public class CommentDTO  implements Serializable { 
    private Long id  ; 
    //评论的用户id 
    private Long userId  ; 
    //评论的用户名称 
    private String userName  ; 
    //评论内容 
    private String commContent  ; 
    //评论日期 
    private Date commDate  ; 
    //个人头像 
    private ImgURLDTO headPortrait  ; 
    //评论的岗位 
    private String postName  ; 
    //头像的文件编码：仅仅用户后台使用 
    private String fileCode  ; 
    //1 匿名， 0 实际名称 
    private Integer isNickName  ; 
    //评论标签 
    private List<CommentTagDTO> tagList  ; 
    //性别： 
    // 1 男， 
    // 0 女 
    private Integer gender  ; 
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
    *设定评论的用户id
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *获取评论的用户id
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定评论的用户名称
    */
    public void setUserName(String userName) { 
        this.userName=userName;
     }
    /**
    *获取评论的用户名称
    */
    public String getUserName() { 
        return  this.userName;
     }
    /**
    *设定评论内容
    */
    public void setCommContent(String commContent) { 
        this.commContent=commContent;
     }
    /**
    *获取评论内容
    */
    public String getCommContent() { 
        return  this.commContent;
     }
    /**
    *设定评论日期
    */
    public void setCommDate(Date commDate) { 
        this.commDate=commDate;
     }
    /**
    *获取评论日期
    */
    public Date getCommDate() { 
        return  this.commDate;
     }
    /**
    *设定个人头像
    */
    public void setHeadPortrait(ImgURLDTO headPortrait) { 
        this.headPortrait=headPortrait;
     }
    /**
    *获取个人头像
    */
    public ImgURLDTO getHeadPortrait() { 
        return  this.headPortrait;
     }
    /**
    *设定评论的岗位
    */
    public void setPostName(String postName) { 
        this.postName=postName;
     }
    /**
    *获取评论的岗位
    */
    public String getPostName() { 
        return  this.postName;
     }
    /**
    *设定头像的文件编码：仅仅用户后台使用
    */
    public void setFileCode(String fileCode) { 
        this.fileCode=fileCode;
     }
    /**
    *获取头像的文件编码：仅仅用户后台使用
    */
    public String getFileCode() { 
        return  this.fileCode;
     }
    /**
    *设定1 匿名， 0 实际名称
    */
    public void setIsNickName(Integer isNickName) { 
        this.isNickName=isNickName;
     }
    /**
    *获取1 匿名， 0 实际名称
    */
    public Integer getIsNickName() { 
        return  this.isNickName;
     }
    /**
    *设定评论标签
    */
    public void setTagList(List<CommentTagDTO> tagList) { 
        this.tagList=tagList;
     }
    /**
    *获取评论标签
    */
    public List<CommentTagDTO> getTagList() { 
        return  this.tagList;
     }
    /**
    *设定性别： 1 男， 0 女
    */
    public void setGender(Integer gender) { 
        this.gender=gender;
     }
    /**
    *获取性别： 1 男， 0 女
    */
    public Integer getGender() { 
        return  this.gender;
     }

 } 

