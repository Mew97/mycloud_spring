package com.demo05.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.demo05.demo.model.User;

public class TokenUtils {
    public static String getToken(User user){
        String token;
        token= JWT.create().withAudience(String.valueOf(user.getId()))
//                .withExpiresAt()
                .sign(Algorithm.HMAC256(user.getUuid()));
        return token;
    }
}
