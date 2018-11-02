package com.smartcold.manage.cold.init;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;

import com.smartcold.manage.cold.service.FtpService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.smartcold.manage.cold.util.SystemInfo;

import java.io.File;


/**
 * Copyright (C) SmartCold 版权所有 
 * @author MaQiang34
 * @ClassName InitIndexPage.java 
 * @Description: 初始化商城配置及缓存
 * @createDate:2017-12-29 14:19:57
 * @version:V1.0
 */
@Component
public class InitData {
	    
	    @PostConstruct  
	    public void  init(){  
				WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
				ServletContext servletContext = webApplicationContext.getServletContext();  
				SystemInfo.SERVLETCONTEXTPATH = servletContext.getRealPath("/");



				//
				//webApplicationContext.getBean(arg0) 程序启动生成index 页面
				// FreeMarkerUtil.createHtml(model,freeMarkerConfig,null,"index.html","index.html");//根据模板生成静态页面
				
				
				// TODO   -->init conf
				
				// TODO   -->init cache
		    	this.iniImgCachDir();
	    }


	    private void iniImgCachDir(){
           File file=new File(FtpService.CACHE_DIR);
           if(!file.exists()){
           	file.mkdirs();
		   }

		}
	     
	    /**
	     *销毁时 
	     */
	    @PreDestroy  
	    public void  dostory(){  
	        System.out.println("I'm  destory method  using  @PreDestroy.....");  
	    }  
	    
	    
	    
	    
	
}

