package com.smartcold.manage.cold.dao;

import com.github.pagehelper.Page;
import com.smartcold.manage.cold.entity.comm.GoodGrow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基地or 加工基地信息
 */
public interface GoodGrowMapper {

   public  List<Map> allList(@Param("cid") int cid);

    public GoodGrow get(@Param("id") int id);

    public void del(@Param("uid") Integer uid, @Param("compid") Integer compid, @Param("ids") String ids);

    public void add(GoodGrow goodGrow);

    public void update(GoodGrow goodGrow);

    public Page<GoodGrow> list(@Param("uid") Integer uid, @Param("compid") Integer compid, @Param("coleam") String coleam, @Param("colval") String  colval);



}
