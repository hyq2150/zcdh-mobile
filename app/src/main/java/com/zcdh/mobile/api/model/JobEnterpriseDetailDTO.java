/** 
*  JobEnterpriseDetailDTO 
* 
*  Created Date: 2015-08-12 17:07:58 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* @author focus, 2014-4-8 上午11:25:17 
*/  
public class JobEnterpriseDetailDTO  implements Serializable { 
    private Long entId  ; 
    private String entName  ; 
    private String fullEntName  ; 
    //企业性质 
    private String propertyName  ; 
    //行业 
    private String industry  ; 
    //地址 
    private String address  ; 
    //标签名称 
    private List<String> tagNames  ; 
    //简介 
    private String introduction  ; 
    //联系人姓名 
    private String contactName  ; 
    //手机号码 
    private String mobile  ; 
    //是否公开手机号码，0：不公开，1：公开 
    private Integer isPublicMobile  ; 
    //移动电话 
    private String phone  ; 
    //是否公开联系电话，0：不公开，1：公开 
    private Integer isPublicPhone  ; 
    //邮箱 
    private String email  ; 
    //是否公开邮箱，0：不公开，1：公开 
    private Integer is_public_email  ; 
    //是否认证，0：没认证，1：认证 
    private Integer isLegalize  ; 
    //图片 
    private List<ImgURLDTO> photos  ; 
    //企业规模 
    private String employNum  ; 
    //纬度 
    private Double lat  ; 
    //经度 
    private Double lon  ; 
    private String logoFileCode  ; 
    private Integer isAttention  ; 
    //企业logo 
    private ImgURLDTO logo  ; 
    //产品总数 
    private Integer productTotals  ; 
    //公司产品 
    private EntProductDTO product  ; 
    //评论列表 
    private List<CommentDTO> commentDTO  ; 
    //评论总数 
    private Integer commentTotals  ; 
    /**
    *无功能描述
    */
    public void setEntId(Long entId) { 
        this.entId=entId;
     }
    /**
    *无功能描述
    */
    public Long getEntId() { 
        return  this.entId;
     }
    /**
    *无功能描述
    */
    public void setEntName(String entName) { 
        this.entName=entName;
     }
    /**
    *无功能描述
    */
    public String getEntName() { 
        return  this.entName;
     }
    /**
    *无功能描述
    */
    public void setFullEntName(String fullEntName) { 
        this.fullEntName=fullEntName;
     }
    /**
    *无功能描述
    */
    public String getFullEntName() { 
        return  this.fullEntName;
     }
    /**
    *设定企业性质
    */
    public void setPropertyName(String propertyName) { 
        this.propertyName=propertyName;
     }
    /**
    *获取企业性质
    */
    public String getPropertyName() { 
        return  this.propertyName;
     }
    /**
    *设定行业
    */
    public void setIndustry(String industry) { 
        this.industry=industry;
     }
    /**
    *获取行业
    */
    public String getIndustry() { 
        return  this.industry;
     }
    /**
    *设定地址
    */
    public void setAddress(String address) { 
        this.address=address;
     }
    /**
    *获取地址
    */
    public String getAddress() { 
        return  this.address;
     }
    /**
    *设定标签名称
    */
    public void setTagNames(List<String> tagNames) { 
        this.tagNames=tagNames;
     }
    /**
    *获取标签名称
    */
    public List<String> getTagNames() { 
        return  this.tagNames;
     }
    /**
    *设定简介
    */
    public void setIntroduction(String introduction) { 
        this.introduction=introduction;
     }
    /**
    *获取简介
    */
    public String getIntroduction() { 
        return  this.introduction;
     }
    /**
    *设定联系人姓名
    */
    public void setContactName(String contactName) { 
        this.contactName=contactName;
     }
    /**
    *获取联系人姓名
    */
    public String getContactName() { 
        return  this.contactName;
     }
    /**
    *设定手机号码
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *获取手机号码
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *设定是否公开手机号码，0：不公开，1：公开
    */
    public void setIsPublicMobile(Integer isPublicMobile) { 
        this.isPublicMobile=isPublicMobile;
     }
    /**
    *获取是否公开手机号码，0：不公开，1：公开
    */
    public Integer getIsPublicMobile() { 
        return  this.isPublicMobile;
     }
    /**
    *设定移动电话
    */
    public void setPhone(String phone) { 
        this.phone=phone;
     }
    /**
    *获取移动电话
    */
    public String getPhone() { 
        return  this.phone;
     }
    /**
    *设定是否公开联系电话，0：不公开，1：公开
    */
    public void setIsPublicPhone(Integer isPublicPhone) { 
        this.isPublicPhone=isPublicPhone;
     }
    /**
    *获取是否公开联系电话，0：不公开，1：公开
    */
    public Integer getIsPublicPhone() { 
        return  this.isPublicPhone;
     }
    /**
    *设定邮箱
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *获取邮箱
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *设定是否公开邮箱，0：不公开，1：公开
    */
    public void setIs_public_email(Integer is_public_email) { 
        this.is_public_email=is_public_email;
     }
    /**
    *获取是否公开邮箱，0：不公开，1：公开
    */
    public Integer getIs_public_email() { 
        return  this.is_public_email;
     }
    /**
    *设定是否认证，0：没认证，1：认证
    */
    public void setIsLegalize(Integer isLegalize) { 
        this.isLegalize=isLegalize;
     }
    /**
    *获取是否认证，0：没认证，1：认证
    */
    public Integer getIsLegalize() { 
        return  this.isLegalize;
     }
    /**
    *设定图片
    */
    public void setPhotos(List<ImgURLDTO> photos) { 
        this.photos=photos;
     }
    /**
    *获取图片
    */
    public List<ImgURLDTO> getPhotos() { 
        return  this.photos;
     }
    /**
    *设定企业规模
    */
    public void setEmployNum(String employNum) { 
        this.employNum=employNum;
     }
    /**
    *获取企业规模
    */
    public String getEmployNum() { 
        return  this.employNum;
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
    *无功能描述
    */
    public void setLogoFileCode(String logoFileCode) { 
        this.logoFileCode=logoFileCode;
     }
    /**
    *无功能描述
    */
    public String getLogoFileCode() { 
        return  this.logoFileCode;
     }
    /**
    *无功能描述
    */
    public void setIsAttention(Integer isAttention) { 
        this.isAttention=isAttention;
     }
    /**
    *无功能描述
    */
    public Integer getIsAttention() { 
        return  this.isAttention;
     }
    /**
    *设定企业logo
    */
    public void setLogo(ImgURLDTO logo) { 
        this.logo=logo;
     }
    /**
    *获取企业logo
    */
    public ImgURLDTO getLogo() { 
        return  this.logo;
     }
    /**
    *设定产品总数
    */
    public void setProductTotals(Integer productTotals) { 
        this.productTotals=productTotals;
     }
    /**
    *获取产品总数
    */
    public Integer getProductTotals() { 
        return  this.productTotals;
     }
    /**
    *设定公司产品
    */
    public void setProduct(EntProductDTO product) { 
        this.product=product;
     }
    /**
    *获取公司产品
    */
    public EntProductDTO getProduct() { 
        return  this.product;
     }
    /**
    *设定评论列表
    */
    public void setCommentDTO(List<CommentDTO> commentDTO) { 
        this.commentDTO=commentDTO;
     }
    /**
    *获取评论列表
    */
    public List<CommentDTO> getCommentDTO() { 
        return  this.commentDTO;
     }
    /**
    *设定评论总数
    */
    public void setCommentTotals(Integer commentTotals) { 
        this.commentTotals=commentTotals;
     }
    /**
    *获取评论总数
    */
    public Integer getCommentTotals() { 
        return  this.commentTotals;
     }

 } 

