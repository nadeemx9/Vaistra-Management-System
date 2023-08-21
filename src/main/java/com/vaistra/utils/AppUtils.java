package com.vaistra.utils;

public class AppUtils {

    public static String getEmailMessage(String name, String host, String token)
    {
        return "Hello, "+name+",\n\n Your new account has been created. Please click the link below to verify your account. \n\n"
                +getVerificationURL(host, token)+"\n\n Team VaistraTech.";
    }

    public static String getVerificationURL(String host, String token)
    {
        return host+"/user/verify?token="+token;
    }
}
