package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartcold.manage.cold.dao.CompanyMapper;
import com.smartcold.manage.cold.dao.FileDataMapper;
import com.smartcold.manage.cold.entity.comm.CompanyEntity;
import com.smartcold.manage.cold.entity.comm.UserEntity;
import com.smartcold.manage.cold.util.ResponseData;
import com.smartcold.manage.cold.util.StringUtil;
import com.smartcold.manage.cold.util.TableData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/*
 * wenj
 */
@RestController
@RequestMapping(value="/i/file")
public class File_Controller {

    @Resource
    private FileDataMapper fileDataMapper;

    /**
     *
     * @param mt
     * @param oid
     * @return
     */
    @RequestMapping(value = "/fileList", method = RequestMethod.POST)
    @ResponseBody
    public Object findCompList(int mt,int oid,int token) {
        if(StringUtil.vserToken(token)){
            return  ResponseData.newSuccess( this.fileDataMapper.getList(mt,oid));
        }
        return  ResponseData.newFailure( );
    }

}
