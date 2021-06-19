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
import java.util.List;

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
     *                 闲菜：from=xiancai
     * @param imgType  图片类型 用于控制不同图片大小和尺寸
     *                 闲菜：xiancaiGoodsImg(闲菜商品主图)  xiancaiGoodsInfoImg(闲菜商品介绍)
     * @param file     上传的文件名称
     */
    @RequestMapping("uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response, String from, String imgType, MultipartFile file) {
        LOG.info("------文件上传------");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg(13);
            out.put("from", from);
            out.put("imgType", imgType);
            if (null == file) {
                writer.print(out);
                writer.close();
                return;
            }
            out = HelpCommon.packagMsg(14);

            //测试
//            if(1==1){
//                out = HelpCommon.packagMsg("12");
//                out.put("imgfdfsUrl", "/1/1/"+Math.random());
//                out.put("imgWebUrl", Constants.IMG_URL + "" + Math.random());//图片访问地址
//                writer.print(out);
//                writer.close();
//                return;
//            }

            //控制上传大小
//            if ("xiancai".equals(from)) {
            //检查文件大小
   /*         LOG.info("--------检查文件大小-------");
            int size = 300;
            String unit = "K";
            boolean flagImg = checkFileSize(file.getSize(), size, unit);
            if (!flagImg) {
                out = HelpCommon.packagMsg("23");
                out.put("msg", out.getString("msg") + ";文件大小不能超过" + size + unit + "B");
                out.put("msg", "图片大小不能超过" + size + unit + "B");
                out.put("from", from);
                out.put("imgType", imgType);
                writer.print(out);
                writer.close();
                return;
            }*/
//            }

            String path = "";
            //img_save_local

            boolean flagLocal = false;
            List<TbParam> list = CacheData.PARAMLIST;
            for (int i = 0; i < list.size(); i++) {
                TbParam param = list.get(i);
                String code = param.getParamCode();
                String key = param.getParamKey();
                String value = param.getParamValue();
                if (!"sys_code".equals(code)) {
                    continue;
                }
                if ("img_save_local".equals(key) && "1".equals(value)) {
                    flagLocal = true;
                    break;
                }
            }

            if (flagLocal) {
                //保存到本地
                String root = Constants.IMG_SAVE_ROOT_PATH;//本地根目录
                LOG.info("----保存本地路径----" + path);
                // 文件按 年/月/日    目录保存
                String savePath = DatetimeUtil.getNow("yyyy/MM/dd");
                String filename = DatetimeUtil.getNow("yyyyMMdd") + System.currentTimeMillis() + ".jpg";
                String localSavePath = root + "\\" + savePath;//本地完整路径
                localSavePath = localSavePath.replace("\\", "/").replace("\\\\", "/").replace("//", "/");
                LOG.info("----localSavePath---->" + localSavePath);
                LOG.info("----filename---->" + filename);
                boolean flag = ImageUtil.getInstance().saveFile(file.getInputStream(), localSavePath, filename);//保存到本地
                if (flag) {
                    path = savePath + "/" + filename;
                }
            } else {
                path = fastdfsClient.uploadFile(file);//保存到hdfs中
            }

            if (!StringUtils.isEmpty(path)) {
                path = path.replace("\\", "/");
                out = HelpCommon.packagMsg(12);
                out.put("imgfdfsUrl", path);
                out.put("imgWebUrl", Constants.IMG_WEB_URL + "" + path);//图片访问地址
                LOG.info("--------------响应结果" + out);
            }
            out.put("from", from);
            out.put("imgType", imgType);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
//        long len = file.length();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

}
