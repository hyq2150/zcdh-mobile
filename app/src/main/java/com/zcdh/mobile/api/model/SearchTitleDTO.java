/** 
*  SearchTitleDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 搜索的头部 
*  @author focus, 2014-4-22 上午10:53:29 
*/  
public class SearchTitleDTO  implements Serializable { 
    private List<SearchAreaDTO> areas  ; 
    private List<SearchMajorDTO> majors  ; 
    private List<SearchIndustryDTO> industies  ; 
    private Integer totalPostCount  ; 
    private Integer totalsByArea  ; 
    private Integer totalsByMajors  ; 
    private Integer totalsIndusties  ; 
    /**
    *无功能描述
    */
    public void setAreas(List<SearchAreaDTO> areas) { 
        this.areas=areas;
     }
    /**
    *无功能描述
    */
    public List<SearchAreaDTO> getAreas() { 
        return  this.areas;
     }
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
    public void setTotalPostCount(Integer totalPostCount) { 
        this.totalPostCount=totalPostCount;
     }
    /**
    *无功能描述
    */
    public Integer getTotalPostCount() { 
        return  this.totalPostCount;
     }
    /**
    *无功能描述
    */
    public void setTotalsByArea(Integer totalsByArea) { 
        this.totalsByArea=totalsByArea;
     }
    /**
    *无功能描述
    */
    public Integer getTotalsByArea() { 
        return  this.totalsByArea;
     }
    /**
    *无功能描述
    */
    public void setTotalsByMajors(Integer totalsByMajors) { 
        this.totalsByMajors=totalsByMajors;
     }
    /**
    *无功能描述
    */
    public Integer getTotalsByMajors() { 
        return  this.totalsByMajors;
     }
    /**
    *无功能描述
    */
    public void setTotalsIndusties(Integer totalsIndusties) { 
        this.totalsIndusties=totalsIndusties;
     }
    /**
    *无功能描述
    */
    public Integer getTotalsIndusties() { 
        return  this.totalsIndusties;
     }

 } 

