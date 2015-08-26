/** 
*  IJobWoService 
* 
*  Created Date: 2015-08-12 17:07:57 
*  
*/  
package com.zcdh.mobile.api;  
import java.util.*; 
import java.math.*; 
import com.zcdh.core.annotation.*; 
import com.zcdh.mobile.api.model.*; 
import com.zcdh.comm.entity.*; 
import com.zcdh.mobile.framework.nio.RequestChannel;
/** 
* 沃职场 
*   
*  @author hw, 2013-11-1 下午4:27:05 
*/  
@RpcService("jobWoService")
public interface IJobWoService   { 
    /** 
    * 保存标记点信息 
    *  @param outletsCUCC 
    *  @author hw, 2015-1-7 上午11:16:06 
    */  
    @RpcMethod("addOutletsCUCC")
    public RequestChannel<Integer> addOutletsCUCC(@Param("outletsCUCC") OutletsCUCCDTO1 outletsCUCC);
    /** 
    * 查询联通网点列表 
    *  @return 
    *  @author hw, 2015-1-7 上午11:32:11 
    */  
    @RpcMethod("findUnicomList")
    public RequestChannel<List<OutletsCUCCDTO1>> findUnicomList();
    /** 
    * 查询标记点信息 
    *  @param id 
    *  @author hw, 2015-1-7 上午11:16:06 
    */  
    @RpcMethod("findOutletsCUCC")
    public RequestChannel<OutletsCUCCDTO1> findOutletsCUCC(@Param("cucc_id") Long cucc_id);
    /** 
    * 更新标记点信息 
    *  @param outletsCUCC 
    *  @author hw, 2015-1-7 上午11:16:06 
    */  
    @RpcMethod("updateOutletsCUCC")
    public RequestChannel<Integer> updateOutletsCUCC(@Param("outletsCUCC") OutletsCUCCDTO1 outletsCUCC);
    /** 
    * 保存标记点信息 
    *  @param outletsCUCC 
    *  @author hw, 2015-1-7 上午11:16:06 
    */  
    @RpcMethod("deleteOutletsCUCC")
    public RequestChannel<Integer> deleteOutletsCUCC(@Param("cucc_id") Long cucc_id);


 } 

