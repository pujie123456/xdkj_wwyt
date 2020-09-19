package com.jib.wwyt.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ZuoZhuoPei
 * @version 13:05 2018/11/28
 */
@Component
public class StateInvariant {



    public static String PCURL;
    @Value("${web.pcurl}")
    public void setPCURL(String pcurl) {
        PCURL = pcurl;
    }

    public static final String SESSION_USER = "sessionUser";



    /**
     *
     */
    public static final Boolean SUCCESS = true;

    public static final Boolean FAIL = false;


    /**
     * 返回状态码
     */
    public static final String HTTP_SUCCESS = "200";
    //参数不正确
    public static final String HTTP_PARAM_FALSNESS = "204";
    //参数不能为空
    public static final String HTTP_PARAM_NONULL = "202";

    public static final String HTTP_LOGIN_FAIL = "203";

    public static final String HTTP_NONEXISTENT = "201";
    //用户已禁用
    public static final String HTTP_UNAVAILABLE = "205";



    /**
     * 返回描述
     */
    public static final String RET_SUCCESS = "请求成功";

    public static final String RET_FAIL = "请求失败";

    public static final String RET_PARAM_FAIL = "未查询到数据";

    public static final String RET_PARAM_NONULL = "必须参数不能为空";

    public static final String RET_LOGIN_FAIL = "登录失效，请重新登录";


}
