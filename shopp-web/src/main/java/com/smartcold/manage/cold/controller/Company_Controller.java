package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smartcold.manage.cold.dao.CompanyMapper;
import com.smartcold.manage.cold.dao.FileDataMapper;
import com.smartcold.manage.cold.dao.UserMapper;
import com.smartcold.manage.cold.entity.comm.*;
import com.smartcold.manage.cold.service.FtpService;
import com.smartcold.manage.cold.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 * 追溯
 */
@RestController
@RequestMapping(value="/i/company")
public class Company_Controller {

    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private FtpService ftpService;
    @Resource
    private FileDataMapper fileDataMapper;

    @RequestMapping(value = "/allList", method = RequestMethod.POST)
    @ResponseBody
    public Object allList(HttpSession session ) {
        UserEntity userEntity= (UserEntity) session.getAttribute("user");
        if(userEntity!=null) {
            return this.companyMapper.allList(userEntity.getType()==0?userEntity.getId():null);
        }
        return null;
    }
    /**
     *
     * @param session
     * @param page
     * @param rows
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/findCompList", method = RequestMethod.POST)
    @ResponseBody
    public Object findCompList(HttpSession session, Integer page,Integer rows,String keyword,String coleam,  String  colval) {
       UserEntity userEntity= (UserEntity) session.getAttribute("user");
       if(userEntity!=null){
           page = page == null? 1:page;  rows = rows==null? 10:rows;
           PageHelper.startPage(page, rows);
           Page<CompanyEntity>     complist =this.companyMapper.list(userEntity.getType()==0?userEntity.getId():null,keyword,coleam,colval);
           return TableData.newSuccess(complist);
       }else{
           return TableData.newSuccess();
       }
    }

    @RequestMapping(value = "/deleteByCid", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteByUid(HttpSession session,int  [] ids) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                this.companyMapper.del(userEntity.getType()==0?userEntity.getId():null,StringUtil.getIdS(ids));
                return  ResponseData.newSuccess();
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }


    @RequestMapping(value = "/findCompById")
    @ResponseBody
    public Object findCompById(HttpSession session,int id) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                return  ResponseData.newSuccess( this.companyMapper.get(id));
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }
    @Transient
    @RequestMapping(value = "/addComp", method = RequestMethod.POST)
    @ResponseBody
    public Object addComp(HttpServletRequest request, HttpSession session,CompanyEntity companyEntity, int []fileids,String []fileurls, MultipartFile logoimg,  MultipartFile file0, MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, MultipartFile file9) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                if(null==companyEntity.getId()||companyEntity.getId()==0){
                    companyEntity.setUid(userEntity.getId());
                    this.companyMapper.add(companyEntity);
                }else{
                   this.companyMapper.update(companyEntity);
                }
               int   oid=companyEntity.getId();
                String dir="smartcode/company/"+oid+"/";
                MultipartFile[] files = { file0,file1,file2,file3,file4,file5,file6,file7,file8,file9 };
                if(logoimg!=null){
                    ftpService.uploadFile(new UploadFileEntity("logo.jpg", logoimg, dir));
                    CompanyEntity logocomp = new CompanyEntity();
                    logocomp.setId(oid);
                    logocomp.setLogo(dir+"logo.jpg");
                    this.companyMapper.update(logocomp);
                }
                String name="";
                MultipartFile fil=null;
                List<FileDateEntity> filedataList = new ArrayList<FileDateEntity>();
                List<UploadFileEntity> uploadFileEntityList=new ArrayList<>();
                for (int i = 0; i <files.length ; i++) {
                     fil=  files[i];
                    if(fil!=null){
                        name=System.currentTimeMillis()+i+".jpg";
                        uploadFileEntityList.add(new UploadFileEntity(name,fil,dir));
                        filedataList.add(new FileDateEntity(oid,1,1,dir+name));//
                    }
                }
                if(fileids!=null&&fileids.length>0){
                    this.fileDataMapper.dels(StringUtil.getIdS(fileids));
                    this.ftpService.deleteByLocations(Arrays.asList(fileurls) );
                }
                if(SetUtil.isnotNullList(filedataList)){
                    this.ftpService.uploadFileList(uploadFileEntityList);
                    this.fileDataMapper.insert(filedataList);
                }
                return  ResponseData.newSuccess( );
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }


}
