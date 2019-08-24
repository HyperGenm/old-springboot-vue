package com.weiziplus.springboot.utils;

import com.weiziplus.springboot.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author wanglongwei
 * @data 2019/6/20 15:45
 */
@Slf4j
public class FileUtil {

    /**
     * 文件上传
     *
     * @param file
     * @param mkdir 如果分文件夹存放，传入文件夹
     * @return 成功返回路径，失败返回null
     */
    public static String upFile(MultipartFile file, String mkdir) {
        if (file.isEmpty()) {
            return null;
        }
        String resultPath = "";
        if (StringUtil.notBlank(mkdir)) {
            resultPath = mkdir + File.separatorChar;
        }
        // 获取原始名字
        String fileName = file.getOriginalFilename();
        if ("01.jpg".equals(fileName)) {
            return null;
        }
        // 如果原始名字为null获取当前名字
        if (StringUtil.isBlank(fileName)) {
            fileName = file.getName();
        }
        //  获取文件后缀类型
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 生成新文件名
        fileName = StringUtil.createUUID() + suffixName;
        resultPath = resultPath + fileName;
        File dest = new File(GlobalConfig.BASE_FILE_PATH + resultPath);
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
     * 文件上传
     *
     * @param file
     * @return
     */
    public static String upFile(MultipartFile file) {
        return upFile(file, null);
    }
}
