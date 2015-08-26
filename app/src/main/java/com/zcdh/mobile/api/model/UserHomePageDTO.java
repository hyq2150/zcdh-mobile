/** 
*  UserHomePageDTO 
* 
*  Created Date: 2014-05-08 12:16:18 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-5-8 上午11:43:57
 */
public class UserHomePageDTO  implements Serializable { 
    /** 
     * 用户概要信息
     */
    private UserOutLineDTO userOutLineDTO  ; 
    /**
    *设定用户概要信息
    */
    public void setUserOutLineDTO(UserOutLineDTO userOutLineDTO) { 
        this.userOutLineDTO=userOutLineDTO;
     }
    /**
    *获取设定用户概要信息
    */
    public UserOutLineDTO getUserOutLineDTO() { 
        return  this.userOutLineDTO;
     }

 } 

