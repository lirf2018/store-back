package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.FastdfsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 15:49
 * 功能介绍: 图片上传类
 */
@Controller
@RequestMapping(value = "/image/")
public class ImageController {

    @Autowired
    private FastdfsClient fastdfsClient;

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @param from     图片来源 用于控制不同图片大小和尺寸
     * @param file     上传的文件名称
     */
    @RequestMapping("uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response, String from, MultipartFile file) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = CommonMethod.packagMsg("13");
            if (null == file) {
                writer.print(out);
                writer.close();
                return;
            }
            out = CommonMethod.packagMsg("14");

            //测试
            if(1==1){
                out = CommonMethod.packagMsg("12");
                out.put("imgfdfsUrl", "/1/1/"+Math.random());
                out.put("imgWebUrl", Constants.IMG_URL + "" + Math.random());//图片访问地址
                writer.print(out);
                writer.close();
                return;
            }


            String fdfsPath = fastdfsClient.uploadFile(file);
            if (!StringUtils.isEmpty(fdfsPath)) {
                out = CommonMethod.packagMsg("12");
                out.put("imgfdfsUrl", fdfsPath);
                out.put("imgWebUrl", Constants.IMG_URL + "" + fdfsPath);//图片访问地址
            }
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
