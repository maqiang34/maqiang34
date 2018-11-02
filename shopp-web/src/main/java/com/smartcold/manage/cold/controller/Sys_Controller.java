package com.smartcold.manage.cold.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C) SmartCold 版权所有 
 * @author MaQiang34
 * @ClassName Cart_Controller.java 
 * @Description: MODEVIEW 目录
 * @createDate:2017-12-28 17:41:27
 * @version:V1.0
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartcold.manage.cold.util.SystemInfo;

@Controller
@RequestMapping(value = "/sys")
public class Sys_Controller {

    /**
     * 系统版本
     * @param request
     * @return
     */
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	@ResponseBody
	public String version(HttpServletRequest request) {
			return SystemInfo.VERSION;
	}




	
}
