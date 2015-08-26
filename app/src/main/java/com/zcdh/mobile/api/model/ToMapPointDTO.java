/** 
*  ToMapPointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 到地图的DTO 
*  @author focus, 2014-6-23 下午5:05:19 
*/  
public class ToMapPointDTO  implements Serializable { 
    //地图上的点 
    private PointDTO point  ; 
    //经纬度的极限值 
    private List<LbsParam> lbs  ; 
    //岗位的总数 
    private Integer totalPosts  ; 
    /**
    *设定地图上的点
    */
    public void setPoint(PointDTO point) { 
        this.point=point;
     }
    /**
    *获取地图上的点
    */
    public PointDTO getPoint() { 
        return  this.point;
     }
    /**
    *设定经纬度的极限值
    */
    public void setLbs(List<LbsParam> lbs) { 
        this.lbs=lbs;
     }
    /**
    *获取经纬度的极限值
    */
    public List<LbsParam> getLbs() { 
        return  this.lbs;
     }
    /**
    *设定岗位的总数
    */
    public void setTotalPosts(Integer totalPosts) { 
        this.totalPosts=totalPosts;
     }
    /**
    *获取岗位的总数
    */
    public Integer getTotalPosts() { 
        return  this.totalPosts;
     }

 } 

