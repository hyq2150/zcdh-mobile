/** 
*  JobEducationExperienceDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 下午2:24:52 
*/  
public class JobEducationExperienceDTO  implements Serializable { 
    //教育经历 
    private List<JobEducationListDTO> edus  ; 
    //培训经历 
    private List<JobTrainListDTO> trains  ; 
    /**
    *设定教育经历
    */
    public void setEdus(List<JobEducationListDTO> edus) { 
        this.edus=edus;
     }
    /**
    *获取教育经历
    */
    public List<JobEducationListDTO> getEdus() { 
        return  this.edus;
     }
    /**
    *设定培训经历
    */
    public void setTrains(List<JobTrainListDTO> trains) { 
        this.trains=trains;
     }
    /**
    *获取培训经历
    */
    public List<JobTrainListDTO> getTrains() { 
        return  this.trains;
     }

 } 

