package com.smartcold.manage.cold.dao;

import com.github.pagehelper.Page;
import com.smartcold.manage.cold.entity.comm.GoodTrace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodTraceMapper {

    public  GoodTrace get(@Param("id") int id);
    /**
     *
     * @param ids
     */
    public void del(@Param("ids") String ids);

    /**
     *
     * @param gid
     */
    public void delbygid(@Param("gid") int gid);

    /**
     *
     * @param goodTrace
     */
    public  void add(GoodTrace goodTrace);


    public void update(GoodTrace data);


    /**
     * @param gid
     * @param record
     * @param stage
     * @param startTime
     * @param endTime
     * @return
     */
    public Page<GoodTrace> list(@Param("gid") int gid, @Param("record") Integer record, @Param("stage") Integer stage, @Param("startTime") String startTime, @Param("endTime") String endTime);



}
