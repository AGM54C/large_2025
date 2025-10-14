package com.example.large_2025.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//jwt令牌解析工具
public class JWTUtil {
    //固定密钥形式为Planet
    private static final String KEY = "Planet";

    //生成Token
    public static String GenToken(Map<String, Object> claims) {
        //返回JWT代码
        var builder = JWT.create();

        // 遍历 claims，逐个添加
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                builder.withClaim(key, (String) value);
            } else if (value instanceof Integer) {
                builder.withClaim(key, (Integer) value);
            } else if (value instanceof Long) {
                builder.withClaim(key, (Long) value);
            } else if (value instanceof Boolean) {
                builder.withClaim(key, (Boolean) value);
            } else if (value instanceof Date) {
                builder.withClaim(key, (Date) value);
            } else if (value instanceof Double) {
                builder.withClaim(key, (Double) value);
            }
        }

        return builder
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24))//过期时间24小时
                .sign(Algorithm.HMAC256(KEY));
    }

    //验证Token
    public static Map<String, Object> ParseToken(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token);

        Map<String, Object> claims = new HashMap<>();

        // 获取所有的 claim
        jwt.getClaims().forEach((key, claim) -> {
            if (claim.asString() != null) {
                claims.put(key, claim.asString());
            } else if (claim.asInt() != null) {
                claims.put(key, claim.asInt());
            } else if (claim.asLong() != null) {
                claims.put(key, claim.asLong());
            } else if (claim.asBoolean() != null) {
                claims.put(key, claim.asBoolean());
            } else if (claim.asDate() != null) {
                claims.put(key, claim.asDate());
            } else if (claim.asDouble() != null) {
                claims.put(key, claim.asDouble());
            }
        });

        return claims;
    }
}