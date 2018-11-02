package com.smartcold.manage.cold.service.impl;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.smartcold.manage.cold.service.CoderServer;
import com.smartcold.manage.cold.util.SystemInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Hashtable;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

@Service
public class CoderServerImpl implements CoderServer {


    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    @Override
    public String creatRrCode(String contents, int width, int height) {
        String binary = null;
        Hashtable hints= new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(  contents,BarcodeFormat.QR_CODE, width, height, hints);
            // 1、读取文件转换为字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage image = toBufferedImage(bitMatrix);
            //转换成png格式的IO流
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();


            String s ="data:image/png;base64," +encodeBase64String(bytes);
        return  s;
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return binary;
    }


    /**
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Override
    public String  createPdf(HttpRequest request, HttpResponse response, ModelMap model){
        String basePath = SystemInfo.getSERVLETCONTEXTPATH();
        Configuration cfg = new Configuration();
        try {
            cfg.setDefaultEncoding("utf-8");
            cfg.setDirectoryForTemplateLoading(new File(basePath + "WEB-INF" + File.separator + "pdfmodel"));
            Template template = cfg.getTemplate("codeprint.ftl");
            template.setEncoding("utf-8");

            String path = basePath + "WEB-INF" + File.separator + "pdfmodel" + File.separator + System.currentTimeMillis()+".html";
            FileOutputStream fos = new FileOutputStream(new File(path));
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            BufferedWriter bw = new BufferedWriter(osw);
            template.process(model, bw);
            bw.flush();
            String retunrpath="/temp/"+System.currentTimeMillis()+".pdf";
            String pdfpath=   basePath+retunrpath;
            OutputStream out1 = new FileOutputStream(pdfpath);
            ITextRenderer renderer = new ITextRenderer();
            String url = new File(path).toURI().toURL().toString();
            renderer.setDocument(url);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(basePath + "WEB-INF" + File.separator + "pdfmodel" + File.separator + "simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(out1);
            bw.close();
            osw.close();
            fos.close();

         return retunrpath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
