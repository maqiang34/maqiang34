package com.smartcold.manage.cold.dao;

import com.smartcold.manage.cold.entity.comm.FileDateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileDataMapper {

    public void del(@Param("id") int id);

    public void dels(@Param("ids") String ids);

    public void delByMt(@Param("mt") int mt, @Param("oid") int oid);

    /**
     * @param mt
     * @param oid
     * @return
     */
    public List<FileDateEntity> getList(@Param("mt") int mt, @Param("oid") int oid);

    public void add(FileDateEntity data);

    public void insert(List<FileDateEntity> list);

}
