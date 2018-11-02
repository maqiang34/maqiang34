package com.smartcold.manage.cold.entity.comm;

public class GoodAuth {

   private int id;
    private Integer       gid;
    private String     uuid;
    private Integer          status;
    private Integer   count;
    private String           Invalidtime;
    private String           img;
    private String      addtime;

    public GoodAuth() {

    }

    public GoodAuth(Integer gid, String uuid) {
        this.gid = gid;
        this.uuid = uuid;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public void upCount() {
        this.count ++;
    }

    public String getInvalidtime() {
        return Invalidtime;
    }

    public void setInvalidtime(String invalidtime) {
        Invalidtime = invalidtime;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
