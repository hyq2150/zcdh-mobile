/** 
*  ImgAttachDTO 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-5-8 上午11:39:16 
*/  
public class ImgAttachDTO  implements Serializable { 
    private Long userId  ; 
    //扩展名称 
    private String fileExtension  ; 
    //文件名 
    private String fileName  ; 
    //文件大小 
    private Long fileSize  ; 
    //文件二进制流 
    private byte[] fileBytes  ; 
    /**
    *无功能描述
    */
    public void setUserId(Long userId) { 
        this.userId=userId;
     }
    /**
    *无功能描述
    */
    public Long getUserId() { 
        return  this.userId;
     }
    /**
    *设定扩展名称
    */
    public void setFileExtension(String fileExtension) { 
        this.fileExtension=fileExtension;
     }
    /**
    *获取扩展名称
    */
    public String getFileExtension() { 
        return  this.fileExtension;
     }
    /**
    *设定文件名
    */
    public void setFileName(String fileName) { 
        this.fileName=fileName;
     }
    /**
    *获取文件名
    */
    public String getFileName() { 
        return  this.fileName;
     }
    /**
    *设定文件大小
    */
    public void setFileSize(Long fileSize) { 
        this.fileSize=fileSize;
     }
    /**
    *获取文件大小
    */
    public Long getFileSize() { 
        return  this.fileSize;
     }
    /**
    *设定文件二进制流
    */
    public void setFileBytes(byte[] fileBytes) { 
        this.fileBytes=fileBytes;
     }
    /**
    *获取文件二进制流
    */
    public byte[] getFileBytes() { 
        return  this.fileBytes;
     }

 } 

