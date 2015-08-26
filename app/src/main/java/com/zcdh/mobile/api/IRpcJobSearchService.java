/** 
*  IRpcJobSearchService 
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
* 搜索的接口 
*  @author focus, 2014-3-28 下午5:45:48 
*/  
@RpcService("rpcJobSearchService")
public interface IRpcJobSearchService   { 
    /** 
    * 查找标签类型 
    *  @return 
    *  @author focus, 2014-4-7 上午10:26:37 
    */  
    @RpcMethod("findJobTagTypeDTO")
    public RequestChannel<List<JobTagTypeDTO>> findJobTagTypeDTO();
    /** 
    * 分页查询标签 
    *  @param tagType		标签类型 001:常用，002：应届毕业生，003：人力 
    *  @param currentPge	当前页 
    *  @param pageSize		一页的元素的数量 
    *  @return 
    *  @author focus, 2014-4-7 上午10:14:54 
    */  
    @RpcMethod("findJobSearchTagDTOByPage")
    public RequestChannel<Page<JobSearchTagDTO>> findJobSearchTagDTOByPage(@Param("tagType") String tagType, @Param("currentPge") Integer currentPge, @Param("pageSize") Integer pageSize);
    /** 
    * 高级搜索 
    *  @param highSearchDTO 
    *  @return 
    *  @author focus, 2014-4-1 下午5:25:16 
    */  
    @RpcMethod("findEntPostDTOByHighSearch")
    public RequestChannel<Page<JobEntPostDTO>> findEntPostDTOByHighSearch(@Param("highSearchDTO") SearchConditionDTO highSearchDTO, @Param("currentPge") Integer currentPge, @Param("pageSize") Integer pageSize);


 } 

