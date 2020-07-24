package com.weiziplus.springboot.common.util;

import com.weiziplus.springboot.common.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wanglongwei
 * @date 2019/6/20 15:45
 */
@Slf4j
@Component
public class FileUtils {

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public static String upFile(MultipartFile file) {
        return upFile(file, null);
    }

    /**
     * 文件上传
     *
     * @param file
     * @param mkdir 如果分文件夹存放，传入文件夹
     * @return 成功返回路径，失败返回null
     */
    public static String upFile(MultipartFile file, String mkdir) {
        return upFile(file, mkdir, null);
    }

    /**
     * 文件上传
     *
     * @param file
     * @param mkdir   如果分文件夹存放，传入文件夹
     * @param builder
     * @return 成功返回路径，失败返回null
     */
    public static String upFile(MultipartFile file, String mkdir, Thumbnails.Builder<? extends InputStream> builder) {
        if (null == file || file.isEmpty()) {
            return null;
        }
        // 获取原始名字
        String fileName = file.getOriginalFilename();
        // 如果原始名字为null获取当前名字
        if (ToolUtils.isBlank(fileName)) {
            fileName = file.getName();
        }
        //如果没有设置目录，存放到临时目录
        String resultPath = ToolUtils.isBlank(mkdir) ? "/temp/" : ("/" + mkdir + "/");
        //分日期存放
        resultPath += DateUtils.getNowDateNum() + "/";
        //  获取文件后缀类型
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 生成新文件名
        fileName = ToolUtils.createUUID() + suffixName;
        resultPath = resultPath + fileName;
        File dest = new File(GlobalConfig.getBaseFilePath() + resultPath);
        if (!dest.getParentFile().exists()) {
            boolean flag = dest.getParentFile().mkdirs();
            if (!flag) {
                return null;
            }
        }
        try {
            //如果没有自定义，使用默认
            if (null == builder) {
                //图片质量
                float outputQuality = 1F;
                long size = file.getSize() / 1024;
                if (3072 <= size) {
                    outputQuality = 0.2F;
                } else if (1024 <= size) {
                    outputQuality = 0.3F;
                } else if (512 <= size) {
                    outputQuality = 0.4F;
                } else if (64 <= size) {
                    outputQuality = 0.5F;
                }
                builder = Thumbnails.of(file.getInputStream())
                        .scale(1F)
                        .outputQuality(outputQuality);
            }
            builder.toFile(dest);
        } catch (IOException e) {
            log.warn("文件上传失败" + e);
            return null;
        }
        return resultPath;
    }

    /**
     * 如果返回null代表文件不是图片
     *
     * @param file
     * @return
     */
    public static BufferedImage getImage(MultipartFile file) {
        if (null == file) {
            return null;
        }
        try {
            return ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            log.warn("上传的文件不是图片,详情:" + e);
            return null;
        }
    }

    /**
     * 文件下载
     * tip:必须在yml配置文件中指定global.base-file-path
     *
     * @param response
     * @param path
     * @throws IOException
     */
    public static void downFile(HttpServletResponse response, String path) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("charset", "utf-8");
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Content-Disposition", "attachment");
        File file = new File(GlobalConfig.getBaseFilePath() + path);
        InputStream inputStream = new FileInputStream(file);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        IOUtils.copy(inputStream, servletOutputStream);
        response.flushBuffer();
        if (null != servletOutputStream) {
            servletOutputStream.close();
        }
        inputStream.close();
    }

}
