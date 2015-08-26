/** 
*  ToEntStatusDTO 
* 
*  Created Date: 2014-04-17 09:53:21 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-4-17 上午9:43:23
 */
public class ToEntStatusDTO  implements Serializable { 
    private Integer isAttention  ; 
    /**
    *无功能描述
    */
    public void setIsAttention(Integer isAttention) { 
        this.isAttention=isAttention;
     }
    /**
    *无功能描述
    */
    public Integer getIsAttention() { 
        return  this.isAttention;
     }

 } 

