package com.smartcold.manage.cold.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.smartcold.manage.cold.entity.comm.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {


   public int vistUserName(@Param("name") String name);
    /**
     * @param user
     * @return
     */
  public UserEntity login(UserEntity user);

    /**
     *
     * @param id
     * @return
     */
  public UserEntity finedUserById(@Param("id") Integer id);

  /**
   *
   * @param ids
   */
  public void delUserByIds(@Param("ids") String ids);



    /**
     * @param user
     */
  public void addUser(UserEntity user);


    /**
     *
     * @param user
     */
  public void updateUser( UserEntity user);


  public Page<UserEntity> findUserList(@Param("available") Integer available, @Param("keyword") String keyword, @Param("coleam") String coleam, @Param("colval") String  colval);


}
