/** 
*  MultiPostPointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 多个岗位一个点的实体 
*  @author focus, 2014-6-18 下午3:16:12 
*/  
public class MultiPostPointDTO  implements Serializable { 
    //点的参数： 注意这是，在查询多个岗位的时候 返回给服务器的参数 
    private List<Long> postIds  ; 
    //经度 
    private Double lon  ; 
    //纬度 
    private Double lat  ; 
    //岗位数量 
    private Integer totals  ; 
    //是否显示岗位 
    //  
    // 1 的时候去查岗位列表 
    // 0 不查岗位列表直接散开 
    private Integer isShowPosts  ; 
    /**
    *设定点的参数： 注意这是，在查询多个岗位的时候 返回给服务器的参数
    */
    public void setPostIds(List<Long> postIds) { 
        this.postIds=postIds;
     }
    /**
    *获取点的参数： 注意这是，在查询多个岗位的时候 返回给服务器的参数
    */
    public List<Long> getPostIds() { 
        return  this.postIds;
     }
    /**
    *设定经度
    */
    public void setLon(Double lon) { 
        this.lon=lon;
     }
    /**
    *获取经度
    */
    public Double getLon() { 
        return  this.lon;
     }
    /**
    *设定纬度
    */
    public void setLat(Double lat) { 
        this.lat=lat;
     }
    /**
    *获取纬度
    */
    public Double getLat() { 
        return  this.lat;
     }
    /**
    *设定岗位数量
    */
    public void setTotals(Integer totals) { 
        this.totals=totals;
     }
    /**
    *获取岗位数量
    */
    public Integer getTotals() { 
        return  this.totals;
     }
    /**
    *设定是否显示岗位  1 的时候去查岗位列表 0 不查岗位列表直接散开
    */
    public void setIsShowPosts(Integer isShowPosts) { 
        this.isShowPosts=isShowPosts;
     }
    /**
    *获取是否显示岗位  1 的时候去查岗位列表 0 不查岗位列表直接散开
    */
    public Integer getIsShowPosts() { 
        return  this.isShowPosts;
     }

 } 

