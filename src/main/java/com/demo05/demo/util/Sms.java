package com.demo05.demo.util;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;

public class Sms {
    static {
        AVOSCloud.initialize("HqdlfU5KVHiyKQKFPWSQHjtm-gzGzoHsz",
                "LAyrHCCtEUBXRG8CDlm9jUFg",
                "yiAdrdQ7Ewxpli2zP2RPGe0O");
    }

    public static void sendVerify(String phone, String option){
        try {
            AVOSCloud.requestSMSCode(phone, "Merapy", option, 10);
        } catch (AVException e) {
            System.out.println("短信发送失败");
        }
    }

    public static boolean verify(String verifyNum,String phone){
        try {
            AVOSCloud.verifyCode(verifyNum, phone);
            System.out.println("验证通过");
            return true;
        } catch (AVException e) {
            System.out.println("验证失败");
            return false;
        }
    }
}
