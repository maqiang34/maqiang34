package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartcold.manage.cold.dao.FileDataMapper;
import com.smartcold.manage.cold.dao.GoodInfoMapper;
import com.smartcold.manage.cold.dao.GoodTraceMapper;
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
@RequestMapping(value="/i/trace")
public class Trace_Controller {

    @Resource
    private GoodTraceMapper goodTraceMapper;
    @Resource
    private FtpService ftpService;
    @Resource
    private FileDataMapper fileDataMapper;

    /**
     *
     * @param session
     * @param page
     * @param rows
     * @return
     * @Param("gid") Integer gid, @Param("record") Integer record, @Param("stage") Integer stage, @Param("startTime") String startTime, @Param("endTime") String endTime
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Object list(HttpSession session, Integer page,Integer rows,int gid, Integer record, Integer stage, String coleam,  String  colval) {
       UserEntity userEntity= (UserEntity) session.getAttribute("user");
       if(userEntity!=null){
           page = page == null? 1:page;  rows = rows==null? 10:rows;
           PageHelper.startPage(page, rows);
           Page<GoodTrace>     complist =this.goodTraceMapper.list(gid,record,stage,null,null);
           return TableData.newSuccess(complist);
       }else{
           return TableData.newSuccess();
       }
    }

    @RequestMapping(value = "/deleteByids", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteByUid(HttpSession session, int  [] ids) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null&&ids.length>0){
                this.goodTraceMapper.del(StringUtil.getIdS(ids));
                return  ResponseData.newSuccess();
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseData.newFailure("操作失败！");
        }
    }


    @RequestMapping(value = "/findById")
    @ResponseBody
    public Object findUserById(HttpSession session,int id) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                return  ResponseData.newSuccess( this.goodTraceMapper.get(id));
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
    public Object add(HttpServletRequest request, HttpSession session,GoodTrace data, int []fileids,String []fileurls,MultipartFile logoimg,  MultipartFile file0, MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, MultipartFile file9) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                if(null==data.getId()||data.getId()==0){
                    this.goodTraceMapper.add(data);
                }else{
                   this.goodTraceMapper.update(data);
                }
               int   oid=data.getId();
                String dir="smartcode/good/"+oid+"/";
                MultipartFile[] files = { file0,file1,file2,file3,file4,file5,file6,file7,file8,file9 };
                if(logoimg!=null){
                    ftpService.uploadFile(new UploadFileEntity("logo.jpg", logoimg, dir));
                    GoodTrace logocomp = new GoodTrace();
                    logocomp.setId(oid);
                    logocomp.setImg(dir+"logo.jpg");
                    this.goodTraceMapper.update(logocomp);
                }
                String name="";MultipartFile fil=null;
                List<FileDateEntity> filedataList = new ArrayList<FileDateEntity>();
                List<UploadFileEntity> uploadFileEntityList=new ArrayList<>();
                for (int i = 0; i <files.length ; i++) {
                    fil=  files[i];
                    if(fil!=null){
                        name=System.currentTimeMillis()+i+".jpg";
                        uploadFileEntityList.add(new UploadFileEntity(name,fil,dir));
                        filedataList.add(new FileDateEntity(oid,4,1,dir+name));//
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
            e.printStackTrace();
            return  ResponseData.newFailure("操作失败！");
        }
    }


}
