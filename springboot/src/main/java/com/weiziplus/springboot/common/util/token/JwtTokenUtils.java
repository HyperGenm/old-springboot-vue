package com.weiziplus.springboot.common.util.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.Base64Utils;
import com.weiziplus.springboot.common.util.HttpRequestUtils;
import com.weiziplus.springboot.common.util.Md5Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JwtToken
 *
 * @author wanglongwei
 * @date 2019/5/7 9:50
 */
public class JwtTokenUtils {

    /**
     * 加密方式
     */
    private static final SignatureAlgorithm HS512 = SignatureAlgorithm.HS512;
    /**
     * 秘钥
     */
    private static final String SECRET = "weiziplus";
    /**
     * 过期时间--30天过期
     */
    private static final long EXPIRATION = 1000L * 60 * 60 * 24 * 30;

    /**
     * 根据用户id和用户类型创建token
     *
     * @param userId
     * @return
     */
    protected static String createToken(String userId, String audience, HttpServletRequest request, ExpandModel expand) {
        return Jwts.builder()
                //用户id
                .setId(Base64Utils.encode(userId))
                //获取ip地址和USER_AGENT，某种程度上防止token被利用
                .setIssuer(createIssuer(request))
                //用户类型，admin还是web
                .setAudience(Base64Utils.encode(audience))
                //签名算法
                .signWith(HS512, SECRET)
                //存放自定义内容
                .setSubject(Base64Utils.encode(JSON.toJSONString(expand)))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .compact();
    }

    /**
     * 创建issuer
     *
     * @param request
     * @return
     */
    public static String createIssuer(HttpServletRequest request) {
        return Base64Utils.encode(Md5Utils.encode(
                HttpRequestUtils.getIpAddress(request) +
                        request.getHeader(HttpHeaders.USER_AGENT)));
    }

    /**
     * 获取token中的issuer---目前存放的是ip地址
     *
     * @param token
     * @return
     */
    public static String getIssuer(String token) {
        return getTokenBody(token).getIssuer();
    }

    /**
     * 根据token判断是否失效
     *
     * @param token
     * @return
     */
    public static Boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 根据token获取token中的信息
     *
     * @param token
     * @return
     */
    protected static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 根据token获取用户Audience
     *
     * @param token
     * @return
     */
    public static String getUserAudienceByToken(String token) {
        return Base64Utils.decode(getTokenBody(token).getAudience());
    }

    /**
     * 获取自定义内容
     *
     * @param token
     * @return
     */
    public static ExpandModel getExpandModel(String token) {
        return JSONObject.parseObject(Base64Utils.decode(getTokenBody(token).getSubject()), ExpandModel.class);
    }

    /**
     * 获取自定义内容
     *
     * @param request
     * @return
     */
    public static ExpandModel getExpandModel(HttpServletRequest request) {
        String token = request.getHeader(GlobalConfig.TOKEN);
        return JSONObject.parseObject(Base64Utils.decode(getTokenBody(token).getSubject()), ExpandModel.class);
    }

}
