/** 
*  PostPointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 地图中岗位的标点的实体 
*  @author focus, 2014-6-11 下午5:00:28 
*/  
public class PostPointDTO  implements Serializable { 
    //一个点一个岗位的DTO 
    private SinglePostPointDTO singlePostPoint  ; 
    //一个点有多个岗位的DTO 
    private MultiPostPointDTO multiPostPoint  ; 
    //是否多个岗位 : 1 是, 0 不是 
    private Integer isMutiplePost  ; 
    /**
    *设定一个点一个岗位的DTO
    */
    public void setSinglePostPoint(SinglePostPointDTO singlePostPoint) { 
        this.singlePostPoint=singlePostPoint;
     }
    /**
    *获取一个点一个岗位的DTO
    */
    public SinglePostPointDTO getSinglePostPoint() { 
        return  this.singlePostPoint;
     }
    /**
    *设定一个点有多个岗位的DTO
    */
    public void setMultiPostPoint(MultiPostPointDTO multiPostPoint) { 
        this.multiPostPoint=multiPostPoint;
     }
    /**
    *获取一个点有多个岗位的DTO
    */
    public MultiPostPointDTO getMultiPostPoint() { 
        return  this.multiPostPoint;
     }
    /**
    *设定是否多个岗位 : 1 是, 0 不是
    */
    public void setIsMutiplePost(Integer isMutiplePost) { 
        this.isMutiplePost=isMutiplePost;
     }
    /**
    *获取是否多个岗位 : 1 是, 0 不是
    */
    public Integer getIsMutiplePost() { 
        return  this.isMutiplePost;
     }

 } 

