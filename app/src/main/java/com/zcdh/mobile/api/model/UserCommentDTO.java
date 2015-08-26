/** 
*  UserCommentDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 用户已选择的标签、用户填写的评价 
*  @author focus, 2014-5-8 下午3:04:23 
*/  
public class UserCommentDTO  implements Serializable { 
    //用户填写的自我评价 
    private String comment  ; 
    //用户已选标签列表 
    private List<CommentTagDTO> tagList  ; 
    /**
    *设定用户填写的自我评价
    */
    public void setComment(String comment) { 
        this.comment=comment;
     }
    /**
    *获取用户填写的自我评价
    */
    public String getComment() { 
        return  this.comment;
     }
    /**
    *设定用户已选标签列表
    */
    public void setTagList(List<CommentTagDTO> tagList) { 
        this.tagList=tagList;
     }
    /**
    *获取用户已选标签列表
    */
    public List<CommentTagDTO> getTagList() { 
        return  this.tagList;
     }

 } 

