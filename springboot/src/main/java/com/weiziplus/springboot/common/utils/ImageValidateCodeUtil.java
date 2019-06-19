package com.weiziplus.springboot.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 图片验证码
 *
 * @author wanglongwei
 * @data 2019/5/24 8:24
 */
@Slf4j
public class ImageValidateCodeUtil {

    /**
     * 随机的字符串
     */
    private final static String[] RANDOM_STRING = new String[]{
            "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "k", "m", "n", "p", "k", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 图片的宽
     */
    private final static Integer WIDTH = 95;

    /**
     * 图片的高
     */
    private final static Integer HEIGHT = 25;

    /**
     * 干扰线数量
     */
    private final static Integer LINE_NUM = 40;

    /**
     * 产生随机数数量
     */
    public final static Integer RANDOM_NUM = 4;

    /**
     * 随机数
     */
    private static Random random = new Random();

    /**
     * 设置整体字体大小---干扰线
     */
    private static Font timesNewRomanFont = new Font("Times New Roman", Font.ROMAN_BASELINE, 16);

    /**
     * 设置验证码字体大小
     */
    private static Font fixedsysFont = new Font("Fixedsys", Font.CENTER_BASELINE, 20);

    public static Map<String, Object> getValidateCode() {
        Map<String, Object> result = new HashMap<>(2);
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
        // 产生bufferedImage对象的Graphics对象,该对象可以在图像上进行各种绘制操作
        Graphics graphics = bufferedImage.getGraphics();
        //图片大小
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        //设置整体字体大小---干扰线
        graphics.setFont(timesNewRomanFont);
        //设置整体字体颜色--干扰线
        graphics.setColor(getRandColor(110, 133));
        //绘制干扰线
        for (int i = 0; i <= LINE_NUM; i++) {
            drawLine(graphics);
        }
        //绘制随机字符
        String randomString = "";
        for (int i = 0; i < RANDOM_NUM; i++) {
            randomString = randomString.concat(drawString(graphics, i));
        }
        result.put("random", randomString);
        result.put("image", bufferedImage);
        return result;
    }

    /**
     * 生成随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        fc = fc > 255 ? 255 : fc;
        bc = bc > 255 ? 255 : bc;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 绘制干扰线
     */
    private static void drawLine(Graphics graphics) {
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        graphics.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 绘制字符串
     */
    private static String drawString(Graphics graphics, int i) {
        graphics.setFont(fixedsysFont);
        Color randomColor = new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121));
        graphics.setColor(randomColor);
        String rand = RANDOM_STRING[random.nextInt(RANDOM_STRING.length)];
        graphics.translate(random.nextInt(3), random.nextInt(3));
        graphics.drawString(rand, ((WIDTH - 20) / RANDOM_NUM * i) + 10, HEIGHT / 4 * 3);
        return rand;
    }
}
