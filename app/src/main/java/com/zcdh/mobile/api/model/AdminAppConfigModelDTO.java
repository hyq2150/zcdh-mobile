/** 
*  AdminAppConfigModelDTO 
* 
*  Created Date: 2015-10-12 17:31:49 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author liyuan, 2015-7-29 下午2:14:01 
*  app框架配置模块对象 
*/  
public class AdminAppConfigModelDTO  implements Serializable { 
    //主键 
    private Long id  ; 
    //模块项编码 
    private String model_code  ; 
    //模块项名称 
    private String model_name  ; 
    //app模块父id 
    private Long parent_id  ; 
    //app的id 
    private Long app_id  ; 
    //框架模块类别编码 
    // cover - 封面 
    // guide - 引导页 
    // register - 注册 
    // login - 登录 
    // mainframe - 主框架 
    // businessmodule - 业务模块 
    private String framemodel_type_code  ; 
    //可配置项目代码 
    private String custom_item_code  ; 
    //自定义参数,格式如:key1=value1,key2=value2 
    private String custom_param  ; 
    //打开类型,取值:1-URL 2-应用跳转 
    private String open_type  ; 
    //显示顺序 
    private Integer orders  ; 
    //引用记录id(用于关联旧的控制表的相应记录,以兼容旧版本) 
    private Long reference_id  ; 
    //引用记录类型,取值: 
    // message - 消息 
    // information - 资讯 
    private String reference_type  ; 
    //备注 
    private String memo  ; 
    //图片url 
    private String imgUrl  ; 
    //模块url 
    private String modelUrl  ; 
    //是否可用(是否可点击,0-否,1-是) 
    private Integer isEnabled  ; 
    //是否已选择(0-未选择,1-已选择) 
    private Integer isSelected  ; 
    //是否热门,取值: 0-不是 1-是 
    private Integer is_hot  ; 
    //是否最新,取值: 0-不是 1-是 
    private Integer is_new  ; 
    //打开类型为url时是否在url地址尾部添加用户id参数,取值: 0-不是 1-是 
    private Integer is_url_autofilluserid  ; 
    //定制项类别编码 
    // 取值范围：text-文本 image - 图像 model-模块 modellist模块列表 
    // @author liyuan, 2015-10-12 上午11:12:15 
    private String custom_type_code  ; 
    /**
    *设定主键
    */
    public void setId(Long id) { 
        this.id=id;
     }
    /**
    *获取主键
    */
    public Long getId() { 
        return  this.id;
     }
    /**
    *设定模块项编码
    */
    public void setModel_code(String model_code) { 
        this.model_code=model_code;
     }
    /**
    *获取模块项编码
    */
    public String getModel_code() { 
        return  this.model_code;
     }
    /**
    *设定模块项名称
    */
    public void setModel_name(String model_name) { 
        this.model_name=model_name;
     }
    /**
    *获取模块项名称
    */
    public String getModel_name() { 
        return  this.model_name;
     }
    /**
    *设定app模块父id
    */
    public void setParent_id(Long parent_id) { 
        this.parent_id=parent_id;
     }
    /**
    *获取app模块父id
    */
    public Long getParent_id() { 
        return  this.parent_id;
     }
    /**
    *设定app的id
    */
    public void setApp_id(Long app_id) { 
        this.app_id=app_id;
     }
    /**
    *获取app的id
    */
    public Long getApp_id() { 
        return  this.app_id;
     }
    /**
    *设定框架模块类别编码 cover - 封面 guide - 引导页 register - 注册 login - 登录 mainframe - 主框架 businessmodule - 业务模块
    */
    public void setFramemodel_type_code(String framemodel_type_code) { 
        this.framemodel_type_code=framemodel_type_code;
     }
    /**
    *获取框架模块类别编码 cover - 封面 guide - 引导页 register - 注册 login - 登录 mainframe - 主框架 businessmodule - 业务模块
    */
    public String getFramemodel_type_code() { 
        return  this.framemodel_type_code;
     }
    /**
    *设定可配置项目代码
    */
    public void setCustom_item_code(String custom_item_code) { 
        this.custom_item_code=custom_item_code;
     }
    /**
    *获取可配置项目代码
    */
    public String getCustom_item_code() { 
        return  this.custom_item_code;
     }
    /**
    *设定自定义参数,格式如:key1=value1,key2=value2
    */
    public void setCustom_param(String custom_param) { 
        this.custom_param=custom_param;
     }
    /**
    *获取自定义参数,格式如:key1=value1,key2=value2
    */
    public String getCustom_param() { 
        return  this.custom_param;
     }
    /**
    *设定打开类型,取值:1-URL 2-应用跳转
    */
    public void setOpen_type(String open_type) { 
        this.open_type=open_type;
     }
    /**
    *获取打开类型,取值:1-URL 2-应用跳转
    */
    public String getOpen_type() { 
        return  this.open_type;
     }
    /**
    *设定显示顺序
    */
    public void setOrders(Integer orders) { 
        this.orders=orders;
     }
    /**
    *获取显示顺序
    */
    public Integer getOrders() { 
        return  this.orders;
     }
    /**
    *设定引用记录id(用于关联旧的控制表的相应记录,以兼容旧版本)
    */
    public void setReference_id(Long reference_id) { 
        this.reference_id=reference_id;
     }
    /**
    *获取引用记录id(用于关联旧的控制表的相应记录,以兼容旧版本)
    */
    public Long getReference_id() { 
        return  this.reference_id;
     }
    /**
    *设定引用记录类型,取值: message - 消息 information - 资讯
    */
    public void setReference_type(String reference_type) { 
        this.reference_type=reference_type;
     }
    /**
    *获取引用记录类型,取值: message - 消息 information - 资讯
    */
    public String getReference_type() { 
        return  this.reference_type;
     }
    /**
    *设定备注
    */
    public void setMemo(String memo) { 
        this.memo=memo;
     }
    /**
    *获取备注
    */
    public String getMemo() { 
        return  this.memo;
     }
    /**
    *设定图片url
    */
    public void setImgUrl(String imgUrl) { 
        this.imgUrl=imgUrl;
     }
    /**
    *获取图片url
    */
    public String getImgUrl() { 
        return  this.imgUrl;
     }
    /**
    *设定模块url
    */
    public void setModelUrl(String modelUrl) { 
        this.modelUrl=modelUrl;
     }
    /**
    *获取模块url
    */
    public String getModelUrl() { 
        return  this.modelUrl;
     }
    /**
    *设定是否可用(是否可点击,0-否,1-是)
    */
    public void setIsEnabled(Integer isEnabled) { 
        this.isEnabled=isEnabled;
     }
    /**
    *获取是否可用(是否可点击,0-否,1-是)
    */
    public Integer getIsEnabled() { 
        return  this.isEnabled;
     }
    /**
    *设定是否已选择(0-未选择,1-已选择)
    */
    public void setIsSelected(Integer isSelected) { 
        this.isSelected=isSelected;
     }
    /**
    *获取是否已选择(0-未选择,1-已选择)
    */
    public Integer getIsSelected() { 
        return  this.isSelected;
     }
    /**
    *设定是否热门,取值: 0-不是 1-是
    */
    public void setIs_hot(Integer is_hot) { 
        this.is_hot=is_hot;
     }
    /**
    *获取是否热门,取值: 0-不是 1-是
    */
    public Integer getIs_hot() { 
        return  this.is_hot;
     }
    /**
    *设定是否最新,取值: 0-不是 1-是
    */
    public void setIs_new(Integer is_new) { 
        this.is_new=is_new;
     }
    /**
    *获取是否最新,取值: 0-不是 1-是
    */
    public Integer getIs_new() { 
        return  this.is_new;
     }
    /**
    *设定打开类型为url时是否在url地址尾部添加用户id参数,取值: 0-不是 1-是
    */
    public void setIs_url_autofilluserid(Integer is_url_autofilluserid) { 
        this.is_url_autofilluserid=is_url_autofilluserid;
     }
    /**
    *获取打开类型为url时是否在url地址尾部添加用户id参数,取值: 0-不是 1-是
    */
    public Integer getIs_url_autofilluserid() { 
        return  this.is_url_autofilluserid;
     }
    /**
    *设定定制项类别编码 取值范围：text-文本 image - 图像 model-模块 modellist模块列表
    */
    public void setCustom_type_code(String custom_type_code) { 
        this.custom_type_code=custom_type_code;
     }
    /**
    *获取定制项类别编码 取值范围：text-文本 image - 图像 model-模块 modellist模块列表
    */
    public String getCustom_type_code() { 
        return  this.custom_type_code;
     }

 } 

