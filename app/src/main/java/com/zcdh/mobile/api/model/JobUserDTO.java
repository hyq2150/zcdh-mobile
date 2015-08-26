/** 
*  JobUserDTO 
* 
*  Created Date: 2015-08-12 17:07:56 
*  
*/  
package com.zcdh.mobile.api.model;  
import java.io.Serializable; 
import java.util.*; 
import java.math.*; 
/** 
* 求职用户DTO 
*  @author liyuan, 2015-8-12 下午1:27:34 
*/  
public class JobUserDTO  implements Serializable { 
    private Long user_id  ; 
    private String name  ; 
    private Integer category  ; 
    private String nation  ; 
    private Date birth  ; 
    private String mobile  ; 
    private String email  ; 
    private String zcdh_code  ; 
    private Integer is_login  ; 
    private String credentials  ; 
    private String duty_time  ; 
    private Boolean is_credentials  ; 
    private Date create_date  ; 
    private Date update_date  ; 
    private String miei  ; 
    private String pservice_year  ; 
    private String peducation  ; 
    private String pgender  ; 
    private String pis_married  ; 
    private String pcred  ; 
    private String panmelden  ; 
    private String pnative_place  ; 
    private String paddress  ; 
    private String file_code  ; 
    private String talent_type  ; 
    private String self_comment  ; 
    private String position  ; 
    private String city  ; 
    private Double lon  ; 
    private Double lat  ; 
    private String school_job  ; 
    private String specialty  ; 
    private String en_level_code  ; 
    private String ots_languages  ; 
    private String huaixin_username  ; 
    private String huaixin_password  ; 
    /**
    *无功能描述
    */
    public void setUser_id(Long user_id) { 
        this.user_id=user_id;
     }
    /**
    *无功能描述
    */
    public Long getUser_id() { 
        return  this.user_id;
     }
    /**
    *无功能描述
    */
    public void setName(String name) { 
        this.name=name;
     }
    /**
    *无功能描述
    */
    public String getName() { 
        return  this.name;
     }
    /**
    *无功能描述
    */
    public void setCategory(Integer category) { 
        this.category=category;
     }
    /**
    *无功能描述
    */
    public Integer getCategory() { 
        return  this.category;
     }
    /**
    *无功能描述
    */
    public void setNation(String nation) { 
        this.nation=nation;
     }
    /**
    *无功能描述
    */
    public String getNation() { 
        return  this.nation;
     }
    /**
    *无功能描述
    */
    public void setBirth(Date birth) { 
        this.birth=birth;
     }
    /**
    *无功能描述
    */
    public Date getBirth() { 
        return  this.birth;
     }
    /**
    *无功能描述
    */
    public void setMobile(String mobile) { 
        this.mobile=mobile;
     }
    /**
    *无功能描述
    */
    public String getMobile() { 
        return  this.mobile;
     }
    /**
    *无功能描述
    */
    public void setEmail(String email) { 
        this.email=email;
     }
    /**
    *无功能描述
    */
    public String getEmail() { 
        return  this.email;
     }
    /**
    *无功能描述
    */
    public void setZcdh_code(String zcdh_code) { 
        this.zcdh_code=zcdh_code;
     }
    /**
    *无功能描述
    */
    public String getZcdh_code() { 
        return  this.zcdh_code;
     }
    /**
    *无功能描述
    */
    public void setIs_login(Integer is_login) { 
        this.is_login=is_login;
     }
    /**
    *无功能描述
    */
    public Integer getIs_login() { 
        return  this.is_login;
     }
    /**
    *无功能描述
    */
    public void setCredentials(String credentials) { 
        this.credentials=credentials;
     }
    /**
    *无功能描述
    */
    public String getCredentials() { 
        return  this.credentials;
     }
    /**
    *无功能描述
    */
    public void setDuty_time(String duty_time) { 
        this.duty_time=duty_time;
     }
    /**
    *无功能描述
    */
    public String getDuty_time() { 
        return  this.duty_time;
     }
    /**
    *无功能描述
    */
    public void setIs_credentials(Boolean is_credentials) { 
        this.is_credentials=is_credentials;
     }
    /**
    *无功能描述
    */
    public Boolean getIs_credentials() { 
        return  this.is_credentials;
     }
    /**
    *无功能描述
    */
    public void setCreate_date(Date create_date) { 
        this.create_date=create_date;
     }
    /**
    *无功能描述
    */
    public Date getCreate_date() { 
        return  this.create_date;
     }
    /**
    *无功能描述
    */
    public void setUpdate_date(Date update_date) { 
        this.update_date=update_date;
     }
    /**
    *无功能描述
    */
    public Date getUpdate_date() { 
        return  this.update_date;
     }
    /**
    *无功能描述
    */
    public void setMiei(String miei) { 
        this.miei=miei;
     }
    /**
    *无功能描述
    */
    public String getMiei() { 
        return  this.miei;
     }
    /**
    *无功能描述
    */
    public void setPservice_year(String pservice_year) { 
        this.pservice_year=pservice_year;
     }
    /**
    *无功能描述
    */
    public String getPservice_year() { 
        return  this.pservice_year;
     }
    /**
    *无功能描述
    */
    public void setPeducation(String peducation) { 
        this.peducation=peducation;
     }
    /**
    *无功能描述
    */
    public String getPeducation() { 
        return  this.peducation;
     }
    /**
    *无功能描述
    */
    public void setPgender(String pgender) { 
        this.pgender=pgender;
     }
    /**
    *无功能描述
    */
    public String getPgender() { 
        return  this.pgender;
     }
    /**
    *无功能描述
    */
    public void setPis_married(String pis_married) { 
        this.pis_married=pis_married;
     }
    /**
    *无功能描述
    */
    public String getPis_married() { 
        return  this.pis_married;
     }
    /**
    *无功能描述
    */
    public void setPcred(String pcred) { 
        this.pcred=pcred;
     }
    /**
    *无功能描述
    */
    public String getPcred() { 
        return  this.pcred;
     }
    /**
    *无功能描述
    */
    public void setPanmelden(String panmelden) { 
        this.panmelden=panmelden;
     }
    /**
    *无功能描述
    */
    public String getPanmelden() { 
        return  this.panmelden;
     }
    /**
    *无功能描述
    */
    public void setPnative_place(String pnative_place) { 
        this.pnative_place=pnative_place;
     }
    /**
    *无功能描述
    */
    public String getPnative_place() { 
        return  this.pnative_place;
     }
    /**
    *无功能描述
    */
    public void setPaddress(String paddress) { 
        this.paddress=paddress;
     }
    /**
    *无功能描述
    */
    public String getPaddress() { 
        return  this.paddress;
     }
    /**
    *无功能描述
    */
    public void setFile_code(String file_code) { 
        this.file_code=file_code;
     }
    /**
    *无功能描述
    */
    public String getFile_code() { 
        return  this.file_code;
     }
    /**
    *无功能描述
    */
    public void setTalent_type(String talent_type) { 
        this.talent_type=talent_type;
     }
    /**
    *无功能描述
    */
    public String getTalent_type() { 
        return  this.talent_type;
     }
    /**
    *无功能描述
    */
    public void setSelf_comment(String self_comment) { 
        this.self_comment=self_comment;
     }
    /**
    *无功能描述
    */
    public String getSelf_comment() { 
        return  this.self_comment;
     }
    /**
    *无功能描述
    */
    public void setPosition(String position) { 
        this.position=position;
     }
    /**
    *无功能描述
    */
    public String getPosition() { 
        return  this.position;
     }
    /**
    *无功能描述
    */
    public void setCity(String city) { 
        this.city=city;
     }
    /**
    *无功能描述
    */
    public String getCity() { 
        return  this.city;
     }
    /**
    *无功能描述
    */
    public void setLon(Double lon) { 
        this.lon=lon;
     }
    /**
    *无功能描述
    */
    public Double getLon() { 
        return  this.lon;
     }
    /**
    *无功能描述
    */
    public void setLat(Double lat) { 
        this.lat=lat;
     }
    /**
    *无功能描述
    */
    public Double getLat() { 
        return  this.lat;
     }
    /**
    *无功能描述
    */
    public void setSchool_job(String school_job) { 
        this.school_job=school_job;
     }
    /**
    *无功能描述
    */
    public String getSchool_job() { 
        return  this.school_job;
     }
    /**
    *无功能描述
    */
    public void setSpecialty(String specialty) { 
        this.specialty=specialty;
     }
    /**
    *无功能描述
    */
    public String getSpecialty() { 
        return  this.specialty;
     }
    /**
    *无功能描述
    */
    public void setEn_level_code(String en_level_code) { 
        this.en_level_code=en_level_code;
     }
    /**
    *无功能描述
    */
    public String getEn_level_code() { 
        return  this.en_level_code;
     }
    /**
    *无功能描述
    */
    public void setOts_languages(String ots_languages) { 
        this.ots_languages=ots_languages;
     }
    /**
    *无功能描述
    */
    public String getOts_languages() { 
        return  this.ots_languages;
     }
    /**
    *无功能描述
    */
    public void setHuaixin_username(String huaixin_username) { 
        this.huaixin_username=huaixin_username;
     }
    /**
    *无功能描述
    */
    public String getHuaixin_username() { 
        return  this.huaixin_username;
     }
    /**
    *无功能描述
    */
    public void setHuaixin_password(String huaixin_password) { 
        this.huaixin_password=huaixin_password;
     }
    /**
    *无功能描述
    */
    public String getHuaixin_password() { 
        return  this.huaixin_password;
     }

 } 

