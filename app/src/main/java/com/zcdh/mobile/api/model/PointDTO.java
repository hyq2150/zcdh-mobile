/** 
*  PointDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 地图上点实体 
*  @author focus, 2014-6-18 下午3:02:32 
*/  
public class PointDTO  implements Serializable { 
    //聘 
    private EmployPointDTO employPoint  ; 
    //我的投放的简历的坐标点 
    private List<MyResumePointDTO> myResumePoint  ; 
    //岗位的点 
    private List<PostPointDTO> postPoints  ; 
    //1 只显示岗位的点 ，2 显示岗位的点和 聘， 3 显示岗位的点和 我的简历的点， 4 全部显示 
    private Integer isShowPointType  ; 
    //岗位的总数 
    private Integer totalPosts  ; 
    /**
    *设定聘
    */
    public void setEmployPoint(EmployPointDTO employPoint) { 
        this.employPoint=employPoint;
     }
    /**
    *获取聘
    */
    public EmployPointDTO getEmployPoint() { 
        return  this.employPoint;
     }
    /**
    *设定我的投放的简历的坐标点
    */
    public void setMyResumePoint(List<MyResumePointDTO> myResumePoint) { 
        this.myResumePoint=myResumePoint;
     }
    /**
    *获取我的投放的简历的坐标点
    */
    public List<MyResumePointDTO> getMyResumePoint() { 
        return  this.myResumePoint;
     }
    /**
    *设定岗位的点
    */
    public void setPostPoints(List<PostPointDTO> postPoints) { 
        this.postPoints=postPoints;
     }
    /**
    *获取岗位的点
    */
    public List<PostPointDTO> getPostPoints() { 
        return  this.postPoints;
     }
    /**
    *设定1 只显示岗位的点 ，2 显示岗位的点和 聘， 3 显示岗位的点和 我的简历的点， 4 全部显示
    */
    public void setIsShowPointType(Integer isShowPointType) { 
        this.isShowPointType=isShowPointType;
     }
    /**
    *获取1 只显示岗位的点 ，2 显示岗位的点和 聘， 3 显示岗位的点和 我的简历的点， 4 全部显示
    */
    public Integer getIsShowPointType() { 
        return  this.isShowPointType;
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

