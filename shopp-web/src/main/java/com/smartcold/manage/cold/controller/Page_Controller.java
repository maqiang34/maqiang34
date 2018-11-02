package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartcold.manage.cold.dao.*;
import com.smartcold.manage.cold.entity.comm.FileDateEntity;
import com.smartcold.manage.cold.entity.comm.GoodAuth;
import com.smartcold.manage.cold.entity.comm.GoodInfo;
import com.smartcold.manage.cold.entity.comm.UserEntity;
import com.smartcold.manage.cold.service.CoderServer;
import com.smartcold.manage.cold.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * PC
 */
@Controller
@RequestMapping
public class Page_Controller {

    @Resource
    private GoodInfoMapper goodInfoMapper;
    @Resource
    private GoodAuthMapper goodAuthMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private GoodGrowMapper goodGrowMapper;
    @Resource
    private FileDataMapper fileDataMapper;

    @Resource
    private GoodTraceMapper goodTraceMapper;

    @Resource
    private CoderServer coderServer;

    /**
     * PC
     * 追溯页的默认显示商品数量
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("trace")
    public String person(HttpServletRequest request, ModelMap model) {
        //int compid,int growid,int goodtype,int grade,String name
        PageHelper.startPage(1, 10);
        model.put("sg",goodInfoMapper.list(null,null,null,1,null,null,null));//水果
        PageHelper.startPage(1, 10);
        model.put("sc",goodInfoMapper.list(null,null,null,2,null,null,null));//蔬菜
        return "trace";
    }



    /**
     * 详细详细
     * @param request
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("gooddeatil/{id}")
    public String gooddeatil(HttpServletRequest request, ModelMap model,@PathVariable int id) {
        GoodInfo goodInfo=  this.goodInfoMapper.get(id);
        if(goodInfo==null){
            return "404";
        }
        if(null!=goodInfo.getCompid()){  model.put("company", companyMapper.get(goodInfo.getCompid())); }//公司信息
        if(null!=goodInfo.getGrowid()){  model.put("grow", goodGrowMapper.get(goodInfo.getGrowid())); }//基地信息
        model.put("imgs", this.fileDataMapper.getList(3,goodInfo.getId())); //商品图片信息
        model.put("traces",  goodTraceMapper.list(goodInfo.getId(),null,null,null,null)); //追溯记录
        model.put("goodinfo",  goodInfo); //
        return "gooddeatil";
    }

    @RequestMapping(value = "/print", method = RequestMethod.POST)
    public String print( ModelMap model,int  token,int type,int []ids ) {
        try {
          if(!StringUtil.vserToken(token)){ return "404";}
            ModelMap pdfmodel=new ModelMap();
          if(type==1){
              List<GoodInfo>    goodInfoList=   this.goodInfoMapper.listByIds(StringUtil.getIdS(ids));
              for (GoodInfo goodInfo:   goodInfoList) {
                  goodInfo.setImg(coderServer.creatRrCode(String.format(SystemInfo.getWEBURL()+"/app.html?v=3.7.4#/tab/gooddeatil//%s",goodInfo.getUuid()),300,300));
              }
              pdfmodel.put("list",goodInfoList);
          }else{
              List<GoodAuth> goodAuths = this.goodAuthMapper.listbyIds(StringUtil.getIdS(ids));
              for (GoodAuth goodAuth: goodAuths) {
                  goodAuth.setImg( coderServer.creatRrCode(String.format(SystemInfo.getWEBURL()+"/app.html?v=3.7.4#/tab/gooddeatil//%s",goodAuth.getUuid()),300,300));
              }
              pdfmodel.put("list",goodAuths);
          }
            model.put("path", this.coderServer.createPdf( null,  null,  pdfmodel));
            return "print";
        } catch (Exception e) {
            e.printStackTrace();
            return  "500";
        }
    }

    //==================================================================================================================================================================================================================
    /**
     * 记录详细
     * @param request
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("tracesImgs/{oid}")
    @ResponseBody
    public List<FileDateEntity>  tracesImgs(HttpServletRequest request, ModelMap model, @PathVariable int oid) {
      return   this.fileDataMapper.getList(4,oid);
    }


    /**
     *
     * @param page
     * @param rows
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/goodlist", method = RequestMethod.POST)
    @ResponseBody
    public Object list(int token, Integer page, Integer rows, String keyword, Integer compid, Integer growid, Integer goodtype, Integer grade, String coleam, String  colval) {
      if( ! StringUtil.vserToken(token)){return null; }
        page = page == null? 1:page;  rows = rows==null? 10:rows;
        if(StringUtil.isnotNull(coleam)&&coleam.indexOf("name")>-1){  if("name".equals(coleam)){coleam="g.`name`"; }else if("compname".equals(coleam)){coleam="c.`name`";}else{ coleam="w.`name`";} }
        PageHelper.startPage(page, rows);
        Page<GoodInfo> complist =this.goodInfoMapper.list(null,compid,growid,goodtype,grade,coleam,colval);
        return TableData.newSuccess(complist);
    }


    /**
     *company
     * @param token
     * @param id
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/vserUUID", method = RequestMethod.POST)
    @ResponseBody
    public boolean vserUUID(int token,  String uuid) {
        if( ! StringUtil.vserToken(token)){return false; }
        return null!=this.goodInfoMapper.vserUUID(uuid);
    }
    @RequestMapping(value = "/vserURID", method = RequestMethod.POST)
    @ResponseBody
    public Integer vserURID(int token,  String uuid) {
        if( ! StringUtil.vserToken(token)||!UUIDUtils.verUUID(uuid)){return null; }
        int type = UUIDUtils.getType(uuid);
        if (type == 1) {
            return this.goodInfoMapper.vserUUID(uuid);
        } else {
            GoodAuth goodAuth = this.goodAuthMapper.getBuyUUID(uuid);
            if (goodAuth == null) {
                return null;
            }
            return goodAuth.getGid();
        }

    }
    /**
     *company
     * @param token
     * @param id
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/gooddeatil", method = RequestMethod.POST)
    @ResponseBody
    public Object gooddeatil(int token, Integer id, String uuid) {
        if( ! StringUtil.vserToken(token)){return ResponseData.newFailure("口令校验失败！"); }
        GoodInfo goodInfo =null;
        if(id!=null){
             goodInfo = this.goodInfoMapper.get(id);
             if(goodInfo==null){
                 return ResponseData.newFailure("商品不存在！谨慎假冒产品！");
             }
        }
        HashMap<String, Object> model = new HashMap<>();
        if(UUIDUtils.verUUID(uuid)) {

            int type = UUIDUtils.getType(uuid);
            if (type == 1) {
                goodInfo = this.goodInfoMapper.getByUUID(uuid);
                if (goodInfo == null) {
                    return ResponseData.newFailure("商品不存在！谨慎假冒产品！");
                }
            } else {
                GoodAuth goodAuth = this.goodAuthMapper.getBuyUUID(uuid);
                if (goodAuth == null) {
                    return ResponseData.newFailure("商品不存在！谨慎假冒产品！");
                }
                goodInfo = this.goodInfoMapper.get(goodAuth.getGid());
                if (goodInfo == null) {
                    return ResponseData.newFailure("商品不存在！谨慎假冒产品！");
                }
                this.goodAuthMapper.update(goodAuth);
                goodAuth.upCount();
                model.put("goodAuth", goodAuth); //公司图片信息
            }

        }

        if (goodInfo != null) {
            //商品
            model.put("goodinfo", goodInfo); //追溯记录
            model.put("imgs", this.fileDataMapper.getList(3, goodInfo.getId())); //商品图片信息
            //公司信息
            if (null != goodInfo.getCompid()) {
                model.put("company", companyMapper.get(goodInfo.getCompid()));
                model.put("copimgs", this.fileDataMapper.getList(1, goodInfo.getCompid())); //公司图片信息
            }
            //基地信息
            if (null != goodInfo.getGrowid()) {
                model.put("grow", goodGrowMapper.get(goodInfo.getGrowid()));
                model.put("growimgs", this.fileDataMapper.getList(2, goodInfo.getGrowid())); //基地图片信息
            }
            model.put("traces", goodTraceMapper.list(goodInfo.getId(), null, null, null, null)); //追溯记录
            return ResponseData.newSuccess(model);
        }
        return ResponseData.newFailure("不存在该类商品，谨慎假冒！");
    }
}
