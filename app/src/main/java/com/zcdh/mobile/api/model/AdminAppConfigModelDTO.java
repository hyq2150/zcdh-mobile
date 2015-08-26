/** 
*  AdminAppConfigModelDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
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
    //配置类别,取值: 
    // page-普通页面或菜单 
    // message-消息 
    // information-资讯 
    private String conf_type  ; 
    //模块项编码 
    private String model_code  ; 
    //模块项名称 
    private String model_name  ; 
    //模块项父类编码 
    private String parent_model_code  ; 
    //模块项层次级别 
    private Integer model_level  ; 
    //android文件编码 
    private String android_file_code  ; 
    //android跳转url 
    private String android_url  ; 
    //ios文件编码 
    private String ios_file_code  ; 
    //ios跳转url 
    private String ios_url  ; 
    //wp文件编码 
    private String wp_file_code  ; 
    //wp跳转url 
    private String wp_url  ; 
    //自定义参数,格式如:key1=value1,key2=value2 
    private String custom_param  ; 
    //打开类型,取值:1-URL 2-应用跳转 
    private String open_type  ; 
    //显示顺序 
    private Integer orders  ; 
    //引用记录id(用于关联旧的控制表的相应记录,以兼容旧版本) 
    private Long reference_id  ; 
    //是否限制区域,取值:0-不限 1-限制 
    private Integer is_restricted_area  ; 
    //是否可见,取值:0-不可见 1-可见 
    private Integer is_visible  ; 
    //是否可用,取值:0-不可用 1-可用 
    private Integer is_enabled  ; 
    //备注 
    private String memo  ; 
    //android 图片 (图片对象) 
    private ImgURLDTO andoridImg  ; 
    //IOS 图片 
    private ImgURLDTO iosImg  ; 
    //WP 图片 
    private ImgURLDTO wpImg  ; 
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
    *设定配置类别,取值: page-普通页面或菜单 message-消息 information-资讯
    */
    public void setConf_type(String conf_type) { 
        this.conf_type=conf_type;
     }
    /**
    *获取配置类别,取值: page-普通页面或菜单 message-消息 information-资讯
    */
    public String getConf_type() { 
        return  this.conf_type;
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
    *设定模块项父类编码
    */
    public void setParent_model_code(String parent_model_code) { 
        this.parent_model_code=parent_model_code;
     }
    /**
    *获取模块项父类编码
    */
    public String getParent_model_code() { 
        return  this.parent_model_code;
     }
    /**
    *设定模块项层次级别
    */
    public void setModel_level(Integer model_level) { 
        this.model_level=model_level;
     }
    /**
    *获取模块项层次级别
    */
    public Integer getModel_level() { 
        return  this.model_level;
     }
    /**
    *设定android文件编码
    */
    public void setAndroid_file_code(String android_file_code) { 
        this.android_file_code=android_file_code;
     }
    /**
    *获取android文件编码
    */
    public String getAndroid_file_code() { 
        return  this.android_file_code;
     }
    /**
    *设定android跳转url
    */
    public void setAndroid_url(String android_url) { 
        this.android_url=android_url;
     }
    /**
    *获取android跳转url
    */
    public String getAndroid_url() { 
        return  this.android_url;
     }
    /**
    *设定ios文件编码
    */
    public void setIos_file_code(String ios_file_code) { 
        this.ios_file_code=ios_file_code;
     }
    /**
    *获取ios文件编码
    */
    public String getIos_file_code() { 
        return  this.ios_file_code;
     }
    /**
    *设定ios跳转url
    */
    public void setIos_url(String ios_url) { 
        this.ios_url=ios_url;
     }
    /**
    *获取ios跳转url
    */
    public String getIos_url() { 
        return  this.ios_url;
     }
    /**
    *设定wp文件编码
    */
    public void setWp_file_code(String wp_file_code) { 
        this.wp_file_code=wp_file_code;
     }
    /**
    *获取wp文件编码
    */
    public String getWp_file_code() { 
        return  this.wp_file_code;
     }
    /**
    *设定wp跳转url
    */
    public void setWp_url(String wp_url) { 
        this.wp_url=wp_url;
     }
    /**
    *获取wp跳转url
    */
    public String getWp_url() { 
        return  this.wp_url;
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
    *设定是否限制区域,取值:0-不限 1-限制
    */
    public void setIs_restricted_area(Integer is_restricted_area) { 
        this.is_restricted_area=is_restricted_area;
     }
    /**
    *获取是否限制区域,取值:0-不限 1-限制
    */
    public Integer getIs_restricted_area() { 
        return  this.is_restricted_area;
     }
    /**
    *设定是否可见,取值:0-不可见 1-可见
    */
    public void setIs_visible(Integer is_visible) { 
        this.is_visible=is_visible;
     }
    /**
    *获取是否可见,取值:0-不可见 1-可见
    */
    public Integer getIs_visible() { 
        return  this.is_visible;
     }
    /**
    *设定是否可用,取值:0-不可用 1-可用
    */
    public void setIs_enabled(Integer is_enabled) { 
        this.is_enabled=is_enabled;
     }
    /**
    *获取是否可用,取值:0-不可用 1-可用
    */
    public Integer getIs_enabled() { 
        return  this.is_enabled;
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
    *设定android 图片 (图片对象)
    */
    public void setAndoridImg(ImgURLDTO andoridImg) { 
        this.andoridImg=andoridImg;
     }
    /**
    *获取android 图片 (图片对象)
    */
    public ImgURLDTO getAndoridImg() { 
        return  this.andoridImg;
     }
    /**
    *设定IOS 图片
    */
    public void setIosImg(ImgURLDTO iosImg) { 
        this.iosImg=iosImg;
     }
    /**
    *获取IOS 图片
    */
    public ImgURLDTO getIosImg() { 
        return  this.iosImg;
     }
    /**
    *设定WP 图片
    */
    public void setWpImg(ImgURLDTO wpImg) { 
        this.wpImg=wpImg;
     }
    /**
    *获取WP 图片
    */
    public ImgURLDTO getWpImg() { 
        return  this.wpImg;
     }

 } 

