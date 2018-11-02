package com.smartcold.manage.cold.entity.comm;

public class FileDateEntity {

    private int   id;
    private int   oid;
    private int   mt;
    private int   type;
    private String    location;
    public FileDateEntity() {

    }


    public FileDateEntity(int oid, int mt, int type, String location) {
        this.oid = oid;
        this.mt = mt;
        this.type = type;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getMt() {
        return mt;
    }

    public void setMt(int mt) {
        this.mt = mt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
