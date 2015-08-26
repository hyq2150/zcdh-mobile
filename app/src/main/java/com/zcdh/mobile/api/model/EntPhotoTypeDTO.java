/** 
*  EntPhotoTypeDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 企业照片类型的DTO 
*  @author focus, 2014-4-8 上午11:33:23 
*/  
public class EntPhotoTypeDTO  implements Serializable { 
    //文件名称 
    private String fileName  ; 
    //文件路径 
    private String filePath  ; 
    //文件类别 
    private String fileCategory  ; 
    /**
    *设定文件名称
    */
    public void setFileName(String fileName) { 
        this.fileName=fileName;
     }
    /**
    *获取文件名称
    */
    public String getFileName() { 
        return  this.fileName;
     }
    /**
    *设定文件路径
    */
    public void setFilePath(String filePath) { 
        this.filePath=filePath;
     }
    /**
    *获取文件路径
    */
    public String getFilePath() { 
        return  this.filePath;
     }
    /**
    *设定文件类别
    */
    public void setFileCategory(String fileCategory) { 
        this.fileCategory=fileCategory;
     }
    /**
    *获取文件类别
    */
    public String getFileCategory() { 
        return  this.fileCategory;
     }

 } 

