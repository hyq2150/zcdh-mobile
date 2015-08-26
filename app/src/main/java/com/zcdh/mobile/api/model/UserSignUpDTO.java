/** 
*  UserSignUpDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 查询招聘会报名人员 
*  @author kyz, 2015-4-18 下午5:49:27 
*/  
public class UserSignUpDTO  implements Serializable { 
    private Long user_id  ; 
    private String user_name  ; 
    private String timestr  ; 
    private ImgURLDTO imgURLDTO  ; 
    private String school_name  ; 
    /**
    *无功能描述
    */
    public void setUser_id(Long user_id) { 
        this.user_id=user_id;
     }
    /**
    *无功能描述
    */
    public Long getUser_id() { 
        return  this.user_id;
     }
    /**
    *无功能描述
    */
    public void setUser_name(String user_name) { 
        this.user_name=user_name;
     }
    /**
    *无功能描述
    */
    public String getUser_name() { 
        return  this.user_name;
     }
    /**
    *无功能描述
    */
    public void setTimestr(String timestr) { 
        this.timestr=timestr;
     }
    /**
    *无功能描述
    */
    public String getTimestr() { 
        return  this.timestr;
     }
    /**
    *无功能描述
    */
    public void setImgURLDTO(ImgURLDTO imgURLDTO) { 
        this.imgURLDTO=imgURLDTO;
     }
    /**
    *无功能描述
    */
    public ImgURLDTO getImgURLDTO() { 
        return  this.imgURLDTO;
     }
    /**
    *无功能描述
    */
    public void setSchool_name(String school_name) { 
        this.school_name=school_name;
     }
    /**
    *无功能描述
    */
    public String getSchool_name() { 
        return  this.school_name;
     }

 } 

