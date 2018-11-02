package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartcold.manage.cold.dao.FileDataMapper;
import com.smartcold.manage.cold.dao.GoodGrowMapper;
import com.smartcold.manage.cold.entity.comm.FileDateEntity;
import com.smartcold.manage.cold.entity.comm.GoodGrow;
import com.smartcold.manage.cold.entity.comm.UploadFileEntity;
import com.smartcold.manage.cold.entity.comm.UserEntity;
import com.smartcold.manage.cold.service.FtpService;
import com.smartcold.manage.cold.util.ResponseData;
import com.smartcold.manage.cold.util.SetUtil;
import com.smartcold.manage.cold.util.StringUtil;
import com.smartcold.manage.cold.util.TableData;
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
 * 基地管理
 */
@RestController
@RequestMapping(value="/i/grow")
public class Grow_Controller {

    @Resource
    private GoodGrowMapper goodGrowMapper;
    @Resource
    private FtpService ftpService;
    @Resource
    private FileDataMapper fileDataMapper;

    @RequestMapping(value = "/allList", method = RequestMethod.POST)
    @ResponseBody
    public Object allList(HttpSession session ,Integer cid) {
            if(cid==null){return null;}
            return this.goodGrowMapper.allList(cid);
    }
    /**
     *
     * @param session
     * @param page
     * @param rows
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Object list(HttpSession session, Integer page,Integer rows,String keyword,Integer compid,String coleam,  String  colval) {
       UserEntity userEntity= (UserEntity) session.getAttribute("user");
       if(userEntity!=null){
           page = page == null? 1:page;  rows = rows==null? 10:rows;
           PageHelper.startPage(page, rows);
           if("compName".equals(coleam)){coleam="c.name" ;}else {   coleam=String.format("g.`%s`",coleam); }
           Page<GoodGrow>     complist =this.goodGrowMapper.list(userEntity.getType()==0?userEntity.getId():null,compid,coleam,colval);
           return TableData.newSuccess(complist);
       }else{
           return TableData.newSuccess();
       }
    }


    @RequestMapping(value = "/deleteByids", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteByUid(HttpSession session,Integer compid,  int  [] ids) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                this.goodGrowMapper.del(userEntity.getType()==0?userEntity.getId():null,compid,StringUtil.getIdS(ids));
                return  ResponseData.newSuccess();
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }


    @RequestMapping(value = "/findById")
    @ResponseBody
    public Object findUserById(HttpSession session,int id) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                return  ResponseData.newSuccess( this.goodGrowMapper.get(id));
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }
    @Transient
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(HttpServletRequest request, HttpSession session,GoodGrow data, int []fileids,String []fileurls,  MultipartFile file0, MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, MultipartFile file9) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                if(null==data.getId()||data.getId()==0){
                    this.goodGrowMapper.add(data);
                }else{
                   this.goodGrowMapper.update(data);
                }
               int   oid=data.getId();
                String dir="smartcode/grow/"+oid+"/";
                MultipartFile[] files = { file0,file1,file2,file3,file4,file5,file6,file7,file8,file9 };

                String name="";MultipartFile fil=null;
                List<FileDateEntity> filedataList = new ArrayList<FileDateEntity>();
                List<UploadFileEntity> uploadFileEntityList=new ArrayList<>();
                for (int i = 0; i <files.length ; i++) {
                    fil=  files[i];
                    if(fil!=null){
                        name=System.currentTimeMillis()+i+".jpg";
                        uploadFileEntityList.add(new UploadFileEntity(name,fil,dir));
                        filedataList.add(new FileDateEntity(oid,2,1,dir+name));//
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
