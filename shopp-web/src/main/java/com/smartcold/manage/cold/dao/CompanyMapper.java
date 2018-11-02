package com.smartcold.manage.cold.dao;

import com.github.pagehelper.Page;
import com.smartcold.manage.cold.entity.comm.CompanyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CompanyMapper {

    public List<HashMap> allList(@Param("uid") Integer uid);

    public CompanyEntity get(@Param("id") int id);

    public void add(CompanyEntity companyEntity);

    public void update(CompanyEntity companyEntity);

    public void del(@Param("uid") Integer uid, @Param("ids") String ids);

    public Page<CompanyEntity> list(@Param("uid") Integer uid,@Param("keyword") String keyword, @Param("coleam") String coleam, @Param("colval") String  colval);


}
