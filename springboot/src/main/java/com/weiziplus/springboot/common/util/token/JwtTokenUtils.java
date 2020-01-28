package com.weiziplus.springboot.common.util.token;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.Base64Utils;
import com.weiziplus.springboot.common.util.ToolUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
     * 发行人
     */
    private static final String ISSUER = "WeiziPlus";
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
    protected static String createToken(Long userId, String audience, String ipAddress, Long roleId) {
        return Jwts.builder()
                //用户id
                .setId(Base64Utils.encode(ToolUtils.valueOfString(userId)))
                .setIssuer(Base64Utils.encode(ipAddress))
                //用户类型，admin还是web
                .setAudience(Base64Utils.encode(audience))
                .signWith(HS512, SECRET)
                //admin的用户角色，
                .setSubject(Base64Utils.encode(ToolUtils.valueOfString(roleId)))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .compact();
    }

    /**
     * 获取token中的issuer---目前存放的是ip地址
     *
     * @param token
     * @return
     */
    public static String getIssuer(String token) {
        return Base64Utils.decode(getTokenBody(token).getIssuer());
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
    private static Claims getTokenBody(String token) {
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
     * 根据token获取用户id
     *
     * @param token
     * @return
     */
    public static Long getUserIdByToken(String token) {
        return Long.valueOf(Base64Utils.decode(getTokenBody(token).getId()));
    }

    /**
     * 根据request获取用户id
     *
     * @param request
     * @return
     */
    public static Long getUserIdByHttpServletRequest(HttpServletRequest request) {
        return Long.valueOf(Base64Utils.decode(getTokenBody(request.getHeader(GlobalConfig.TOKEN)).getId()));
    }

    /**
     * 根据token获取用户角色
     *
     * @param token
     * @return
     */
    public static Long getRoleIdByToken(String token) {
        return ToolUtils.valueOfLong(Base64Utils.decode(getTokenBody(token).getSubject()));
    }

}
