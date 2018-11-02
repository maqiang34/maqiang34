package com.smartcold.manage.cold.dao;

import com.smartcold.manage.cold.entity.comm.UserEntity;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {



    /**
     * @param user
     * @return
     */
    public UserEntity login(@Param("user") UserMapper user);

    /**
     *
     * @param id
     * @return
     */
    public UserEntity finedUserById(@Param("id") Integer id);

    /**
     * @param user
     */
    public void add(@Param("user") UserMapper user);


    /**
     *
     * @param user
     */
    public void update(@Param("user") UserEntity user);










}
