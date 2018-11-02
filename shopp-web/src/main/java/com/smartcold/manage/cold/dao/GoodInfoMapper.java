package com.smartcold.manage.cold.dao;


import com.github.pagehelper.Page;
import com.smartcold.manage.cold.entity.comm.GoodInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodInfoMapper {

    public void add(GoodInfo goodGrow);

    /**
     * @param goodGrow
     */
    public void update(GoodInfo goodGrow);

    /**
     * @param id
     * @return
     */
    public GoodInfo get(@Param("id") int id);


    public Integer vserUUID(String uuid);
    /**
     *
     * @param uuid
     * @return
     */
    public GoodInfo getByUUID(@Param("uuid") String uuid);

    /**
     *
     * @param ids
     * @param compid
     * @param growid
     */
    public void del(@Param("ids") String ids,@Param("compid") Integer compid, @Param("growid") Integer growid);


    /**
     *
     * @param ids
     * @return
     */
    public List<GoodInfo> listByIds(@Param("ids") String ids);

    /**
     *
     * @param uid:用户id
     * @param compid:公司id
     * @param growid:基地id
     * @param goodtype:商品类型
     * @param grade：等级
     * @param coleam ：自动过滤key
     * @param colval:自动过滤value
     * @return
     */
    public Page<GoodInfo> list(@Param("uid") Integer uid, @Param("compid") Integer compid, @Param("growid") Integer growid, @Param("goodtype") Integer goodtype, @Param("grade") Integer grade, @Param("coleam") String coleam, @Param("colval") String  colval);


}
