package com.smartcold.manage.cold.entity.comm;

public class UserEntity {
    private Integer id;
    private Integer type;
    private Integer available;
    private Integer lgcount;
    private String name;
    private String pwd;
    private String telephone;
    private String email;
    private String token;
    private String addtime;

    public UserEntity() {

    }

    public UserEntity(Integer id, Integer lgcount) {
        this.id = id;
        this.lgcount = lgcount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getLgcount() {
        return lgcount;
    }

    public void setLgcount(Integer lgcount) {
        this.lgcount = lgcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
