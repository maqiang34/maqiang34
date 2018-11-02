package com.smartcold.manage.cold.entity.comm;

import java.util.List;

public class ACLAdminNode {

    int id;
    String url;
    String icon;
    String menuid;
    String menuname;
    private List<ACLAdminNode> child;

    public ACLAdminNode(String menuid,String icon,  String menuname) {
        super();
        this.icon = icon;
        this.menuid = menuid;
        this.menuname = menuname;
    }

    public ACLAdminNode( String menuid, String icon, String menuname,String url) {
        super();
        this.url = url;
        this.icon = icon;
        this.menuid = menuid;
        this.menuname = menuname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public List<ACLAdminNode> getChild() {
        return child;
    }

    public void setChild(List<ACLAdminNode> child) {
        this.child = child;
    }

}
