package com.smartcold.manage.cold.dao;

import com.github.pagehelper.Page;
import com.smartcold.manage.cold.entity.comm.GoodAuth;
import com.smartcold.manage.cold.entity.comm.GoodGrow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基地or 加工基地信息
 */
public interface GoodAuthMapper {

    public  GoodAuth get(@Param("id") int id);

    public  GoodAuth getBuyUUID(@Param("uuid") String uuid);

    public  Page<GoodAuth> list(@Param("gid") int gid);

    public  List<GoodAuth> listbyIds(@Param("ids") String ids);

   public void del(@Param("idS") String idS);

   public void add(List<GoodAuth> list);

   public void update(GoodAuth goodAuth);

}
