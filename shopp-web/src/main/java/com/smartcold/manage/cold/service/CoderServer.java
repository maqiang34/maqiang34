package com.smartcold.manage.cold.service;

import com.google.zxing.common.BitMatrix;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

public interface CoderServer {


    public String  createPdf(HttpRequest request, HttpResponse response, ModelMap model);

    public  String creatRrCode(String contents, int width, int height);


}
