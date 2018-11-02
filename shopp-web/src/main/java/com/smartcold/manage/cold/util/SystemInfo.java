package com.smartcold.manage.cold.util;

/*
 * Copyright (C) DCIS 版权所有
 * 功能描述: 系统配置，由spring 托管变量，直接修改无效，请在配置中修改
 * Create on MaQiang 2016-6-25 09:28:36
 * 
 */
public  class SystemInfo {
	//#系统版本-->data(system.version) 
    public  static String VERSION="v-0.0.1"; 
    //#运行模式-->data(system.debugger)  true:开发环境->不生成缓存
	public static   Boolean ISDEBUGGER=false; 
	//項目路徑 
	public static String SERVLETCONTEXTPATH="/";
    //域名
	public static String WEBURL="";


	public static String getVERSION() {
		return VERSION;
	}

	public static void setVERSION(String VERSION) {
		SystemInfo.VERSION = VERSION;
	}

	public static Boolean getISDEBUGGER() {
		return ISDEBUGGER;
	}

	public static void setISDEBUGGER(Boolean ISDEBUGGER) {
		SystemInfo.ISDEBUGGER = ISDEBUGGER;
	}

	public static String getSERVLETCONTEXTPATH() {
		return SERVLETCONTEXTPATH;
	}

	public static void setSERVLETCONTEXTPATH(String SERVLETCONTEXTPATH) {
		SystemInfo.SERVLETCONTEXTPATH = SERVLETCONTEXTPATH;
	}

	public static String getWEBURL() {
		return WEBURL;
	}

	public static void setWEBURL(String WEBURL) {
		SystemInfo.WEBURL = WEBURL;
	}
}