/** 
*  UserObjectiveDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-29 上午1:16:15 
*/  
public class UserObjectiveDTO  implements Serializable { 
    private List<SearchMajorDTO> majors  ; 
    private List<SearchIndustryDTO> industies  ; 
    private List<SearchPostDTO> posts  ; 
    private Date startTime  ; 
    private Date endTime  ; 
    private String workPropertyCode  ; 
    /**
    *无功能描述
    */
    public void setMajors(List<SearchMajorDTO> majors) { 
        this.majors=majors;
     }
    /**
    *无功能描述
    */
    public List<SearchMajorDTO> getMajors() { 
        return  this.majors;
     }
    /**
    *无功能描述
    */
    public void setIndusties(List<SearchIndustryDTO> industies) { 
        this.industies=industies;
     }
    /**
    *无功能描述
    */
    public List<SearchIndustryDTO> getIndusties() { 
        return  this.industies;
     }
    /**
    *无功能描述
    */
    public void setPosts(List<SearchPostDTO> posts) { 
        this.posts=posts;
     }
    /**
    *无功能描述
    */
    public List<SearchPostDTO> getPosts() { 
        return  this.posts;
     }
    /**
    *无功能描述
    */
    public void setStartTime(Date startTime) { 
        this.startTime=startTime;
     }
    /**
    *无功能描述
    */
    public Date getStartTime() { 
        return  this.startTime;
     }
    /**
    *无功能描述
    */
    public void setEndTime(Date endTime) { 
        this.endTime=endTime;
     }
    /**
    *无功能描述
    */
    public Date getEndTime() { 
        return  this.endTime;
     }
    /**
    *无功能描述
    */
    public void setWorkPropertyCode(String workPropertyCode) { 
        this.workPropertyCode=workPropertyCode;
     }
    /**
    *无功能描述
    */
    public String getWorkPropertyCode() { 
        return  this.workPropertyCode;
     }

 } 

