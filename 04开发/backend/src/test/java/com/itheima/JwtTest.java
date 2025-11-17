package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGenerateJwt() {
        //构建 JWT 令牌
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "aXRoZWltYQ==")//指定加密算法和密钥
                .addClaims(dataMap)//添加自定义属性
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))//设置过期时间1小时
                .compact();//构建令牌
        System.out.println(jwt);
    }

    /**
     * 解析 JWT 令牌
     */
    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc1ODI3MDUxM30.aq5b7Be4HqwYwCpd68texM_B0hyaOwsYICQJPbXll7w";
        Claims claims = Jwts.parser().setSigningKey("aXRoZWltYQ==")
                .parseClaimsJws(token)//校验
                .getBody();
        System.out.println(claims);
    }
}
