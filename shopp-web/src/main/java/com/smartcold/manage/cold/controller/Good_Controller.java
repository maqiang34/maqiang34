package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartcold.manage.cold.dao.FileDataMapper;
import com.smartcold.manage.cold.dao.GoodInfoMapper;
import com.smartcold.manage.cold.entity.comm.FileDateEntity;
import com.smartcold.manage.cold.entity.comm.GoodInfo;
import com.smartcold.manage.cold.entity.comm.UploadFileEntity;
import com.smartcold.manage.cold.entity.comm.UserEntity;
import com.smartcold.manage.cold.service.FtpService;
import com.smartcold.manage.cold.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
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
import java.util.concurrent.*;


/*
 * 商品管理
 */
@RestController
@RequestMapping(value="/i/good")
public class Good_Controller {

    @Resource
    private GoodInfoMapper goodInfoMapper;
    @Resource
    private FtpService ftpService;
    @Resource
    private FileDataMapper fileDataMapper;


    private static final Logger log = LoggerFactory.getLogger(Good_Controller.class);







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
    public Object list(HttpSession session, Integer page,Integer rows,String keyword,Integer compid,Integer growid, Integer goodtype, Integer grade, String coleam,  String  colval) {
       UserEntity userEntity= (UserEntity) session.getAttribute("user");
       if(userEntity!=null){
           page = page == null? 1:page;  rows = rows==null? 10:rows;
           if(StringUtil.isnotNull(coleam)&&coleam.indexOf("name")>-1){  if("name".equals(coleam)){coleam="g.`name`"; }else if("compname".equals(coleam)){coleam="c.`name`";}else{ coleam="w.`name`";} }
           PageHelper.startPage(page, rows);
           Page<GoodInfo>     complist =this.goodInfoMapper.list(userEntity.getType()==0?userEntity.getId():null,compid,growid,goodtype,grade,coleam,colval);
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
            if(userEntity!=null&&ids.length>0){
                this.goodInfoMapper.del(StringUtil.getIdS(ids),null,null);
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
                return  ResponseData.newSuccess( this.goodInfoMapper.get(id));
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
    public Object add(HttpServletRequest request, HttpSession session,GoodInfo data, int []fileids,String []fileurls,MultipartFile logoimg,  MultipartFile file0, MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, MultipartFile file9) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null){
                if(null==data.getId()||data.getId()==0){
                    data.setUuid(UUIDUtils.getRUUID(1));
                    this.goodInfoMapper.add(data);
                }else{
                   this.goodInfoMapper.update(data);
                }
               int   oid=data.getId();
                String dir="smartcode/good/"+oid+"/";
                MultipartFile[] files = { file0,file1,file2,file3,file4,file5,file6,file7,file8,file9 };
                if(logoimg!=null){
                    ftpService.uploadFile(new UploadFileEntity("logo.jpg", logoimg, dir));
                    GoodInfo logocomp = new GoodInfo();
                    logocomp.setId(oid);
                    logocomp.setImg(dir+"logo.jpg");
                    this.goodInfoMapper.update(logocomp);
                }
                String name="";
                long l = System.currentTimeMillis();
                MultipartFile fil=null;
                List<FileDateEntity> filedataList = new ArrayList<FileDateEntity>();
                List<UploadFileEntity> uploadFileEntityList=new ArrayList<>();
                for (int i = 0; i <files.length ; i++) {
                    fil=  files[i];
                    if(fil!=null){
                        name=(l+i)+".jpg";
                        uploadFileEntityList.add(new UploadFileEntity(name,fil,dir));
                        filedataList.add(new FileDateEntity(oid,3,1,dir+name));//
                    }
                }
                if(fileids!=null&&fileids.length>0){
                    this.fileDataMapper.dels(StringUtil.getIdS(fileids));
                }
                if(fileurls!=null&&fileurls.length>0){
                    this.ftpService.deleteByLocations(Arrays.asList(fileurls) );
                }
                if(SetUtil.isnotNullList(filedataList)){
                    long starttime = System.currentTimeMillis();
                    this.ftpService.uploadFileList(uploadFileEntityList);
                    this.fileDataMapper.insert(filedataList);
                    log.info("用时:"+(System.currentTimeMillis()-starttime));
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
