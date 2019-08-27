package com.weiziplus.springboot.util.token;

import com.weiziplus.springboot.util.ToolUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JwtToken
 *
 * @author wanglongwei
 * @data 2019/5/7 9:50
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
    public static String createToken(Long userId, String audience) {
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setAudience(audience)
                .signWith(HS512, SECRET)
                .setSubject(ToolUtils.valueOfString(userId))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .compact();
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
        return getTokenBody(token).getAudience();
    }

    /**
     * 根据token获取用户id
     *
     * @param token
     * @return
     */
    public static Long getUserIdByToken(String token) {
        return Long.valueOf(getTokenBody(token).getSubject());
    }

    /**
     * 根据request获取用户id
     *
     * @param request
     * @return
     */
    public static Long getUserIdByHttpServletRequest(HttpServletRequest request) {
        return getUserIdByToken(request.getHeader("token"));
    }
}
