package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbParam;
import com.yufan.service.param.IParamCodeService;
import com.yufan.utils.*;
import org.apache.log4j.Logger;
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

    private Logger LOG = Logger.getLogger(ImageController.class);

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
        System.out.printf("--文件上传--");
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
//            if(1==1){
//                out = CommonMethod.packagMsg("12");
//                out.put("imgfdfsUrl", "/1/1/"+Math.random());
//                out.put("imgWebUrl", Constants.IMG_URL + "" + Math.random());//图片访问地址
//                writer.print(out);
//                writer.close();
//                return;
//            }
            String path = "";


            //保存到本地
            String root = Constants.IMG_SAVE_ROOT_PATH;//本地根目录
            LOG.info("----保存本地路径----" + path);
            // 文件按 年/月/日    目录保存
            String savePath = DatetimeUtil.getNow("yyyy/MM/dd");
            String filename = DatetimeUtil.getNow("yyyyMMdd") + System.currentTimeMillis() + ".jpg";
            String localSavePath = root + "\\" + savePath;//本地完整路径
            localSavePath = localSavePath.replace("\\", "/").replace("\\\\", "/").replace("//","/");
            LOG.info("----localSavePath---->" + localSavePath);
            LOG.info("----filename---->" + filename);
            boolean flag = ImageUtil.getInstance().saveFile(file.getInputStream(), localSavePath, filename);//保存到本地
            if (flag) {
                path = savePath + "/" + filename;
            }
            //String path = fastdfsClient.uploadFile(file);//保存到hdfs中
            if (!StringUtils.isEmpty(path)) {
                path = path.replace("\\", "/");
                out = CommonMethod.packagMsg("12");
                out.put("imgfdfsUrl", path);
                out.put("imgWebUrl", Constants.IMG_WEB_URL + "" + path);//图片访问地址
                LOG.info("--------------响应结果" + out);
            }

            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
