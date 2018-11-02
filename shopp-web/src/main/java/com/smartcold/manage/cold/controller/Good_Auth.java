package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartcold.manage.cold.dao.GoodAuthMapper;
import com.smartcold.manage.cold.entity.comm.GoodAuth;
import com.smartcold.manage.cold.service.CoderServer;
import com.smartcold.manage.cold.util.*;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * 商品管理
 */
@Controller
@RequestMapping(value="/i/auth")
public class Good_Auth {

    @Resource
    private GoodAuthMapper goodAuthMapper;

    @Resource
    private CoderServer coderServer;

    /**
     *
     * @param session
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Object list(HttpSession session, Integer page,Integer rows,int token,int gid) {
        try {
            if( ! StringUtil.vserToken(token)){return TableData.newFailure(); }
            page = page == null? 1:page;
            rows = rows==null? 10:rows;
            PageHelper.startPage(page, rows);
            Page<GoodAuth>     objList =this.goodAuthMapper.list(gid);
            return TableData.newSuccess(objList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TableData.newFailure();
    }

    @RequestMapping(value = "/deleteByids", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteByUid(HttpSession session,int  token, int  [] ids) {
        try {
            if( ! StringUtil.vserToken(token)||ids==null||ids.length==0){return TableData.newFailure(); }
            this.goodAuthMapper.del(StringUtil.getIdS(ids));
            return  ResponseData.newSuccess( );
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseData.newFailure("操作失败！");
        }
    }


    @RequestMapping(value = "/findById")
    @ResponseBody
    public Object findUserById(HttpSession session,int id) {
        try {
                return  ResponseData.newSuccess( this.goodAuthMapper.get(id));
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }
    @Transient
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(int  token,int gid,int size ) {
        try {
            if( ! StringUtil.vserToken(token)){return TableData.newFailure(); }
            GoodAuth auth=null;
            String uuid="";
            List<GoodAuth> auths=new ArrayList<>();
            HashMap<String,Integer> s=new HashMap<String, Integer>();
            for (int i = 0; i <size ;) {
                uuid=  UUIDUtils.getRUUID(2);
                if(s.containsKey(uuid)){
                    continue;
                }else {
                    s.put(uuid, i);
                    auths.add(new GoodAuth(gid, uuid));
                    i++;
                }
            }
            this.goodAuthMapper.add(auths);
            return ResponseData.newSuccess(auths.size());
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseData.newFailure("操作失败！");
        }
    }


    @RequestMapping(value = "/adddAndPrint", method = RequestMethod.POST)
    public String print( ModelMap model, int  token, int gid, int size ) {
        try {
            if( ! StringUtil.vserToken(token)){return "index"; }
            GoodAuth auth=null;
            String uuid="";
            List<GoodAuth> auths=new ArrayList<>();
            HashMap<String,Integer> s=new HashMap<String, Integer>();
            for (int i = 0; i <size ;) {
                uuid=  UUIDUtils.getRUUID(2);
                if(s.containsKey(uuid)){
                    continue;
                }else {
                    s.put(uuid, i);
                    auths.add(new GoodAuth(gid, uuid));
                    i++;
                }
            }
            this.goodAuthMapper.add(auths);//提交
            for (GoodAuth goodAuth: auths) {
                goodAuth.setImg( coderServer.creatRrCode(String.format(SystemInfo.getWEBURL()+"/app.html#/tab/gooddeatil//%s",goodAuth.getUuid()),200,200));
            }
            ModelMap pdfmodel=new ModelMap();
            pdfmodel.put("list",auths);
//            HttpRequest request, HttpResponse response,
            model.put("path", this.coderServer.createPdf( null,  null,  pdfmodel));
            return "print";
        } catch (Exception e) {
            e.printStackTrace();
            return  "404";
        }
    }


}
