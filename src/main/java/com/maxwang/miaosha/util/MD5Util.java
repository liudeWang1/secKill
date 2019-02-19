package com.maxwang.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static  String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static final String salt="1a2b3c4d";

    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(4)+salt.charAt(5);
        return md5(str);
    }

    public static String formPassToDBPass(String inputPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(4)+salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String saltDB){
        String formPass=inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass,saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456", "123456"));
    }

}
