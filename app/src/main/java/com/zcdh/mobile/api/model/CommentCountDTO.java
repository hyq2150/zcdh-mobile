/** 
*  CommentCountDTO 
* 
*  Created Date: 2014-06-04 16:32:19 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-11 上午9:47:02 
*/  
public class CommentCountDTO  implements Serializable { 
    private Long ent_id  ; 
    //差评 
    private Integer negativeCount  ; 
    //好评 
    private Integer goodCount  ; 
    //中评 
    private Integer mediumCount  ; 
    //评论总数 
    private Integer allCount  ; 
    /**
    *无功能描述
    */
    public void setEnt_id(Long ent_id) { 
        this.ent_id=ent_id;
     }
    /**
    *无功能描述
    */
    public Long getEnt_id() { 
        return  this.ent_id;
     }
    /**
    *设定差评
    */
    public void setNegativeCount(Integer negativeCount) { 
        this.negativeCount=negativeCount;
     }
    /**
    *获取差评
    */
    public Integer getNegativeCount() { 
        return  this.negativeCount;
     }
    /**
    *设定好评
    */
    public void setGoodCount(Integer goodCount) { 
        this.goodCount=goodCount;
     }
    /**
    *获取好评
    */
    public Integer getGoodCount() { 
        return  this.goodCount;
     }
    /**
    *设定中评
    */
    public void setMediumCount(Integer mediumCount) { 
        this.mediumCount=mediumCount;
     }
    /**
    *获取中评
    */
    public Integer getMediumCount() { 
        return  this.mediumCount;
     }
    /**
    *设定评论总数
    */
    public void setAllCount(Integer allCount) { 
        this.allCount=allCount;
     }
    /**
    *获取评论总数
    */
    public Integer getAllCount() { 
        return  this.allCount;
     }

 } 

