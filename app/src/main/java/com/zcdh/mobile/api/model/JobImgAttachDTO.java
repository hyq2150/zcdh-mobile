/** 
*  JobImgAttachDTO 
* 
*  Created Date: 2014-05-08 12:04:27 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
/** 
 * @author focus, 2014-5-8 上午11:39:16
 */
public class JobImgAttachDTO  implements Serializable { 
    /** 
     * 文件二进制流
     */
    private byte[] fileBytes  ; 
    /** 
     * 文件名
     */
    private String fileName  ; 
    /** 
     * 文件路径
     */
    private String filePath  ; 
    /** 
     * 文件大小
     */
    private Long fileSize  ; 
    /**
    *设定文件二进制流
    */
    public void setFileBytes(byte[] fileBytes) { 
        this.fileBytes=fileBytes;
     }
    /**
    *获取设定文件二进制流
    */
    public byte[] getFileBytes() { 
        return  this.fileBytes;
     }
    /**
    *设定文件名
    */
    public void setFileName(String fileName) { 
        this.fileName=fileName;
     }
    /**
    *获取设定文件名
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
    *获取设定文件路径
    */
    public String getFilePath() { 
        return  this.filePath;
     }
    /**
    *设定文件大小
    */
    public void setFileSize(Long fileSize) { 
        this.fileSize=fileSize;
     }
    /**
    *获取设定文件大小
    */
    public Long getFileSize() { 
        return  this.fileSize;
     }

 } 

