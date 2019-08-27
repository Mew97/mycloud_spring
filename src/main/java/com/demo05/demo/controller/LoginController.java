package com.demo05.demo.controller;

import com.demo05.demo.annotation.UserLoginToken;
import com.demo05.demo.dao.UserRepo;
import com.demo05.demo.model.User;
import com.demo05.demo.service.UserService;
import com.demo05.demo.util.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import static com.demo05.demo.util.TokenUtils.getToken;

@RestController
@RequestMapping("api/")
public class LoginController {
    @Value("${prop.user-wait}")
    private Long userTtl;

    @Resource
    private UserRepo userRepo;

    @Resource
    private UserService userService;

    @Resource(name = "redisTem")
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("login")
    public ArrayList login(User user, String uuid){
        ArrayList<Object> rs = new ArrayList<>(Arrays.asList(false,"用户名或密码错误"));

        User user1 = userService.updateUuid(uuid,user);

        if (user1 != null){
            //redisTemplate.opsForHash().put("uuid",user1.getId()+"",uuid);
            redisTemplate.opsForValue().set("api:user:"+user1.getId(), uuid);
            redisTemplate.expire("api:user:"+user1.getId(),userTtl, TimeUnit.MINUTES);
            String token = getToken(user1);
            rs.set(0,true);
            rs.set(1,"登陆成功");
            rs.add(user1);
            rs.add(token);
        }
        System.out.println(rs);
        return rs;
    }

    @PostMapping("registered")
    public ArrayList register(User user, String verify){
        ArrayList<Object> rs = new ArrayList<>(Arrays.asList(false, "注册失败"));
        if (userRepo.existsByName(user.getName())){
            rs.set(1,"用户名已存在，请换一个");
        } else {
            if (Sms.verify(verify, user.getPhoneNum())){
                userRepo.save(user);
                rs.set(0, true);
                rs.set(1,"注册成功");
            } else {
                rs.set(1, "验证码错误");
            }
        }
        System.out.println(rs);
        return rs;
    }

    @PostMapping("updatePwd")
    public boolean updatePwd(User user){
        userRepo.setPwdByName(user.getPwd(), user.getName());
        return true;
    }

    @UserLoginToken
    @GetMapping("logout")
    public boolean logout(Long userId){
        try {
            redisTemplate.delete("api:user:"+userId);
            System.out.println("api:user:"+userId+"退出登陆");
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @UserLoginToken
    @GetMapping("onlineCount")
    public int onlineCount(){
        Set<String> keys = redisTemplate.keys("api:user:*");
        if (keys == null){
            return 0;
        }else {
            return keys.size();
        }
    }

    @GetMapping("sendVerify")
    public ArrayList sendVerify(String phone,String option){
        ArrayList<Object> rs = new ArrayList<>(Arrays.asList(false, "发送失败"));
        if (option.equals("注册")){
            if (!userRepo.existsByPhoneNum(phone)){
                Sms.sendVerify(phone, option);
                rs.set(0,true);
                rs.set(1,"发送成功");
            }else {
                rs.set(1,"手机号已注册,请换一个");
            }

        } else if(option.equals("修改密码")) {
            if (userRepo.existsByPhoneNum(phone)){
                Sms.sendVerify(phone,option);
                rs.set(0,true);
                rs.set(1,"发送成功");
            } else {
                rs.set(1,"该手机号未注册");
            }
        }
        System.out.println(rs);
        return rs;
    }

    @GetMapping("verify")
    public ArrayList verify(String phone, String verify){
        ArrayList<Object> rs = new ArrayList<>(Arrays.asList(false,"验证失败"));
        if (Sms.verify(verify,phone)){
            rs.set(0,true);
            rs.set(1,"验证成功");
            rs.add(userRepo.getFirstByPhoneNum(phone));
        }
        return rs;
    }

}
