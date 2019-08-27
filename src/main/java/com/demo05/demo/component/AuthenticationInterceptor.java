package com.demo05.demo.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.demo05.demo.annotation.PassToken;
import com.demo05.demo.annotation.UserLoginToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Value("${prop.user-wait}")
    private Long userTtl;

//    @Autowired
//    private UserService userService;

    @Autowired
    @Qualifier("redisTem")
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()){
                return true;
            }
        }

        if (method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()){
                if (token == null) {
                    //throw new RuntimeException("无token");
                    //System.out.println("无token");
                    System.out.println("无token");
                    response.sendError(401,"无token");
                    return false;
                }

                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j){
                    //throw new RuntimeException("401");
                    System.out.println("获取id失败");
                    response.sendError(401,"获取id失败");
                    return false;
                }

                //User user = userService.findUserById(Long.parseLong(userId));
                //Object uuid = redisTemplate.opsForHash().get("uuid", userId);
                String uuid = redisTemplate.opsForValue().get("api:user:" + userId);

                //if (user == null){
                if (uuid == null){
                    //throw new RuntimeException("用户不存在，请重新登录");
                    System.out.println("用户不存在");
                    response.sendError(401,"用户不存在");
                    return false;
                }

                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(uuid)).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException j){
                    //throw new RuntimeException("401");
                    System.out.println("验证失败");
                    response.sendError(401,"验证失败");
                    return false;
                }

                //如果验证通过，刷新过期时间
                redisTemplate.expire("api:user:" + userId, userTtl, TimeUnit.MINUTES);

                return true;

            }
        }
        return true;
    }
}
