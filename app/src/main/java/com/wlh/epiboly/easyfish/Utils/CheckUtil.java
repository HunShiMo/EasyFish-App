package com.wlh.epiboly.easyfish.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
    public static boolean checkEmail(String email){
         String RULE_EMAIL ="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
         //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }
}
