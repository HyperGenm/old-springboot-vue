package com.weiziplus.springboot.common.util;

import com.weiziplus.springboot.common.config.GlobalConfig;
import lombok.Cleanup;
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
 * 文件常用操作
 *
 * @author wanglongwei
 * @date 2020/05/27 15/07
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
            file.transferTo(dest);
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
            @Cleanup InputStream inputStream = file.getInputStream();
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            log.warn("上传的文件不是图片,详情:" + e);
            return null;
        }
    }

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    public static ResultUtils<String> upImage(MultipartFile file) {
        return upImage(file, null, null);
    }

    /**
     * 上传图片
     *
     * @param file
     * @param mkdir
     * @return
     */
    public static ResultUtils<String> upImage(MultipartFile file, String mkdir) {
        return upImage(file, mkdir, null);
    }

    /**
     * 上传图片
     *
     * @param file
     * @param mkdir
     * @param builder
     * @return
     */
    public static ResultUtils<String> upImage(MultipartFile file, String mkdir, Thumbnails.Builder<?> builder) {
        if (null == file || file.isEmpty()) {
            return ResultUtils.error("文件为空");
        }
        BufferedImage bufferedImage;
        try {
            @Cleanup InputStream inputStream = file.getInputStream();
            bufferedImage = ImageIO.read(inputStream);
        } catch (Exception e) {
            log.warn("图片上传失败，文件不是图片。详情:" + e);
            return ResultUtils.error("上传的文件不是图片。" + e);
        }
        return upImage(bufferedImage, mkdir, builder);
    }

    /**
     * 上传图片
     *
     * @param bufferedImage
     * @return
     */
    public static ResultUtils<String> upImage(BufferedImage bufferedImage) {
        return upImage(bufferedImage, null, null);
    }

    /**
     * 上传图片
     *
     * @param bufferedImage
     * @param mkdir
     * @return
     */
    public static ResultUtils<String> upImage(BufferedImage bufferedImage, String mkdir) {
        return upImage(bufferedImage, mkdir, null);
    }

    /**
     * 上传图片
     *
     * @param bufferedImage
     * @param mkdir
     * @param builder
     * @return
     */
    public static ResultUtils<String> upImage(BufferedImage bufferedImage, String mkdir, Thumbnails.Builder<?> builder) {
        if (null == bufferedImage) {
            return ResultUtils.error("图片不能为空");
        }
        //如果没有设置目录，存放到临时目录
        String resultPath = ToolUtils.isBlank(mkdir) ? "/tempImage/" : ("/" + mkdir + "/");
        //分日期存放
        resultPath += DateUtils.getNowDateNum() + "/";
        // 生成新文件名,默认为jpg格式，png格式可能会越压缩越大
        String fileName = ToolUtils.createUUID() + ".jpg";
        resultPath = resultPath + fileName;
        File dest = new File(GlobalConfig.getBaseFilePath() + resultPath);
        if (!dest.getParentFile().exists()) {
            boolean flag = dest.getParentFile().mkdirs();
            if (!flag) {
                return ResultUtils.error("创建文件失败，请重试");
            }
        }
        try {
            //如果没有自定义，使用默认
            if (null == builder) {
                builder = Thumbnails.of(bufferedImage)
                        .scale(1F)
                        .outputQuality(0.5F);
            }
            builder.toFile(dest);
        } catch (IOException e) {
            log.warn("图片上传失败，图片压缩出错。详情:" + e);
            return ResultUtils.error("图片上传失败。" + e);
        }
        return ResultUtils.success(resultPath);
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
        @Cleanup InputStream inputStream = new FileInputStream(file);
        @Cleanup ServletOutputStream servletOutputStream = response.getOutputStream();
        IOUtils.copy(inputStream, servletOutputStream);
        response.flushBuffer();
    }

}
