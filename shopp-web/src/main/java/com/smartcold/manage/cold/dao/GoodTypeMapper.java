package com.smartcold.manage.cold.dao;

import com.smartcold.manage.cold.entity.comm.GoodType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodTypeMapper {

    public List<GoodType> get();

    public void del( int id);

    public void add(GoodType goodType);
}
