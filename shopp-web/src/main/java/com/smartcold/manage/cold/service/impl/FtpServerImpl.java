package com.smartcold.manage.cold.service.impl;


import com.smartcold.manage.cold.dao.FileDataMapper;
import com.smartcold.manage.cold.entity.comm.UploadFileEntity;
import com.smartcold.manage.cold.service.FtpService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;


/**
 * @author jiangkaiqiang
 * @date 2016-6-7 下午8:16:03
 * @Description: implement Ftp Service
 */
@Service
public class FtpServerImpl implements FtpService {

    private FTPClient ftp;
    @Resource
    private FileDataMapper fileDataMapper;

//    private ThreadPoolExecutor tpe = new ThreadPoolExecutor(9, 100, 300,TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

    private static final Logger log = LoggerFactory.getLogger(FtpServerImpl.class);



    /**
     * ftp 已经连接登录
     * @param uploadFileEntity
     * @return
     * @throws IOException
     */


    private boolean upload(UploadFileEntity uploadFileEntity) throws IOException{
        boolean result = false;
        if (!ftp.changeWorkingDirectory(BASEDIR)) {
            throw new IOException("change base working dir error!");
        }

        //创建目录
        String[] dirs = uploadFileEntity.getRemoteNewDir().split("/");
        for (String dir : dirs) {
            boolean isExist =  ftp.changeWorkingDirectory(dir);
            if (!isExist) {
                if (!ftp.makeDirectory(dir)) {
                    log.error("ftp current working directory:"+ftp.printWorkingDirectory()+
                            " ftp create "+dir+" directory failed, reply code:"+ftp.getReplyCode());
                }
                ftp.changeWorkingDirectory(dir);
            }
        }
        InputStream inputStream = uploadFileEntity.getMultipartFile().getInputStream();

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).scale(1f).outputQuality(0.25f).toOutputStream(bs);
        InputStream zipimg=new ByteArrayInputStream(bs.toByteArray());
        inputStream.close();
        result = ftp.storeFile(uploadFileEntity.getName(),zipimg );
        zipimg.close();
        if (!result) {
            log.error("File upload failed, upload dir:"+uploadFileEntity.getRemoteNewDir()+
                    ", file name:"+uploadFileEntity.getName()+
                    ", reply code "+ftp.getReplyCode()+" "+ftp.getReplyString());
        }
        return result;
    }


    private void connectFtp() throws IOException{
        ftp = new FTPClient();
        ftp.connect(PUB_HOST, PORT);// 连接FTP服务器
        // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
        ftp.login(USER_NAME, PASSWORD);// 登录
        if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            log.error("login ftp error, "+ftp.getReplyString());
            ftp.disconnect();
        }
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }
    // 多文件上传
    @Override
    public boolean uploadFileList(List<UploadFileEntity> uploadFileList) {

        try {
            ExecutorService tpe = Executors.newFixedThreadPool(uploadFileList.size());
            List<Future<Boolean>> results = new LinkedList<Future<Boolean>>();
            for (UploadFileEntity uploadFile : uploadFileList) {
                results.add( tpe.submit( new updateFile(uploadFile)));
            }
            tpe.shutdown();
            for (Future<Boolean> result : results) {
                result.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }




    private void closeFtp() throws IOException{
        ftp.logout();
    }

    // 单个文件上传
    @Override
    public boolean uploadFile(UploadFileEntity uploadFileEntity) {
        boolean result = false;

        try {
            connectFtp();
            result = upload(uploadFileEntity);
            closeFtp();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public boolean deleteByLocation(String location){
        boolean deleted = false;
        try {
            connectFtp();
            deleted = ftp.deleteFile(BASEDIR+"/"+location);
            closeFtp();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return deleted;
    }

    @Override
    public boolean deleteByLocations(List<String> list) {
        try {
            connectFtp();
            for (String url:  list ) {
                ftp.deleteFile(BASEDIR+"/"+url);
            }
            closeFtp();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return  true;
    }


    class updateFile implements Callable<Boolean>{


        UploadFileEntity uploadFileEntity=null;

        public updateFile(UploadFileEntity uploadFileEntity){
            this.uploadFileEntity=uploadFileEntity;
        }



        @Override
        public Boolean call() throws Exception {
            long l = System.currentTimeMillis();
            boolean result = false;
            try {
                FTPClient     ftp = new FTPClient();
                ftp.connect(PUB_HOST, PORT);// 连接FTP服务器
                // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
                ftp.login(USER_NAME, PASSWORD);// 登录
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    log.error("login ftp error, "+ftp.getReplyString());
                    ftp.disconnect();
                }
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                long l1 = System.currentTimeMillis();
                log.info(  " 连接用时用时："+(l1-l));


                if (!ftp.changeWorkingDirectory(BASEDIR)) {
                    throw new IOException("change base working dir error!");
                }

                //创建目录
                String[] dirs = uploadFileEntity.getRemoteNewDir().split("/");
                for (String dir : dirs) {
                    boolean isExist =  ftp.changeWorkingDirectory(dir);
                    if (!isExist) {
                        if (!ftp.makeDirectory(dir)) {
                            log.error("ftp current working directory:"+ftp.printWorkingDirectory()+
                                    " ftp create "+dir+" directory failed, reply code:"+ftp.getReplyCode());
                        }
                        ftp.changeWorkingDirectory(dir);
                    }
                }
                InputStream inputStream = uploadFileEntity.getMultipartFile().getInputStream();
//                ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                Thumbnails.of(inputStream).scale(1f).outputQuality(0.25f).toOutputStream(bs);

                BufferedImage bufferedImage = Thumbnails.of(inputStream).scale(1f).outputQuality(0.25f).asBufferedImage();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
                ImageIO.write(bufferedImage, "jpg", imageOutput);
                InputStream zipinputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                inputStream.close();
                bufferedImage=null;
                imageOutput.close();
                result = ftp.storeFile(uploadFileEntity.getName(),zipinputStream );
                zipinputStream.close();
                ftp.logout();
                if (!result) {
                    log.error("File upload failed, upload dir:"+uploadFileEntity.getRemoteNewDir()+
                            ", file name:"+uploadFileEntity.getName()+
                            ", reply code "+ftp.getReplyCode()+" "+ftp.getReplyString());
                }

                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (Exception ioe) {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info(Thread.currentThread().getName()+   "用时："+(System.currentTimeMillis()-l));
            return result;
        }
    }


}
