package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "aXRoZWltYQ==";
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000; // 12小时

    /**
     * 生成 JWT 令牌
     *
     * @param claims 自定义的令牌载荷数据
     * @return 生成的 JWT 令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 校验并解析 JWT 令牌
     *
     * @param token JWT 令牌字符串
     * @return 解析后的 Claims 对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
