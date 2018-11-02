package com.smartcold.manage.cold.service;

import com.smartcold.manage.cold.entity.comm.FileDateEntity;
import com.smartcold.manage.cold.entity.comm.UploadFileEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

public interface FtpService  {


        public final static String PUB_HOST = "123.206.197.208";

        public final static String USER_NAME = "pwftp";

        public final static String PASSWORD = "MaQiang34MQ";

        public final static int PORT = 2021;

        public final static String BASEDIR = "/data";

        public final static String CACHE_DIR =BASEDIR+ File.separator+ "cache/";//用于压缩图片

//        public final static int READPORT = 8089;

        public    boolean uploadFile(UploadFileEntity uploadFile);

        public   boolean uploadFileList(List<UploadFileEntity> uploadFileList);

//        public   List<FileDateEntity> uploadFileList(HttpServletRequest request,int mt,int oid);

        public   boolean deleteByLocations(List<String>  list );

        public  boolean deleteByLocation(String location);

}
