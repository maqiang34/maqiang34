package com.smartcold.manage.cold.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;



/*
 * Copyright (C) DCIS 版权所有
 * 功能描述: Utils 工具类,具类, 提供静态工具方法 操作字符串
 * Create on MaQiang 2016-6-25 09:28:36
 */
public class StringUtil
{

	public static final String	EMPTY_STRING	= "";
	
	
	/**
	 * 判断字符串是否为null或者空字符串(即长度为0的字符串)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(final String str)
	{
		return (str == null || str.isEmpty());
	}
	/**
	 * 判断字符串是否为null或者空字符串(即长度为0的字符串)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isnotNull(final String str)
	{
		return !isNull(str);
	}

	/**
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNullString(final String str)
	{
		return (str == null || str.isEmpty());
	}

	public static boolean vserToken(final Integer token){
		return token==null? false:(TimeUtil.getDateHour()%4+3)==token;
	}



	public static String getIdS(int [] oids)
	{
		if(oids!=null){
			String str= Arrays.toString(oids);
			return str.substring(1,str.length()-1);
		}
		return "";
	}
	public static int [] getIdS(String oids)
	{
		if(isnotNull(oids)){
			String[] ids = oids.split(",");
			int [] newids=new int[ids.length];
			for (int i = 0; i <ids.length; i++) {
				newids[i]=Integer.parseInt(ids[i]);
			}
			return newids;
		}
		return null;
	}

	/**
	 *
	 * @param pwd
	 * @param systoken
	 * @return
	 */
	 public synchronized static String getUUIDMD5pwd(String pwd,String systoken){
		   if(isnotNull(pwd)){
			    String toknen=	UUID.randomUUID().toString();
				Object []uuid=toknen.split("-");uuid[1]=pwd.substring(0, 18);uuid[3]=pwd.substring(18);
			   return String.format("%s-%s-%s-%s-%s", uuid);
		   }
		   if(isnotNull(systoken)){
			   Object []token= systoken.split("-");
			   return  String.format("%s%s", token[1],token[3]);
		   }
		   return "";
	 }


    //public static String[] splitString(final String strs)
//	{
//		return splitStringWithDelimiter(";", strs);
//	}


	/**
	 * 生成16位UUID
	 */
//	public static String getUUID(){
//		UUID id=UUID.randomUUID();
//		String[] idd=id.toString().split("-");
//		String uuid=idd[0]+idd[1]+idd[2];
//		return uuid;
//	}


}
