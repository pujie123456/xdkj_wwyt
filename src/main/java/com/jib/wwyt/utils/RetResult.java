package com.jib.wwyt.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class RetResult<T> implements Serializable {
    //未知异常
    public static final RetResult  RETRESULT_UNKNOW =
            new RetResult(StateInvariant.FAIL,"399","未知异常");
    //密码不正确
    public static final RetResult RETRESULT_PASSWORD_FALSNESS =
            new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_PARAM_FALSNESS,"密码不正确");
    //参数返回失败
    public static final RetResult RETRESULT_PARAM_NONULL =
            new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_PARAM_NONULL,"参数不能为空");
    //参数不正确
    public static final RetResult RETRESULT_UNAVAILABLE =
            new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_UNAVAILABLE,"用户已禁用");
    //用户不存在
    public static final RetResult RETRESULT_NONEXISTENT =
            new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_NONEXISTENT,"用户不存在");



    //请求成功
    public static final RetResult RETRESULT_SUCCESS =
            new RetResult(StateInvariant.SUCCESS,StateInvariant.HTTP_SUCCESS,StateInvariant.RET_SUCCESS);
    //请求失败
    public static final RetResult RETRESULT_FAIL =
            new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_NONEXISTENT,StateInvariant.RET_FAIL);
    //用户未登录
    public static final RetResult RETRESULT_LOGIN_FAIL =
            new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_LOGIN_FAIL,StateInvariant.RET_LOGIN_FAIL);

    private Boolean success;

    private String retCode;

    private String retInfo;

    private T data;


    public RetResult() {
    }

    public RetResult(Boolean success, String retCode, String retInfo) {
        this.success = success;
        this.retCode = retCode;
        this.retInfo = retInfo;
    }
    public RetResult(RetResult r, T data) {
        this.success = r.success;
        this.retCode = r.retCode;
        this.retInfo = r.retInfo;
        this.data = data;
    }

    public RetResult(Boolean success, String retCode, String retInfo, T data) {
        this.success = success;
        this.retCode = retCode;
        this.retInfo = retInfo;
        this.data = data;
    }




    @Override
    public String toString() {
        return "RetResult{" +
                "success=" + success +
                ", retCode='" + retCode + '\'' +
                ", retInfo='" + retInfo + '\'' +
                ", data=" + data +
                '}';
    }

    public static RetResult  retSUCCESS(Object data){
        return new RetResult(StateInvariant.SUCCESS,StateInvariant.HTTP_SUCCESS,StateInvariant.RET_SUCCESS, data);
    }

    public static RetResult retFAIL(){
        return new RetResult(StateInvariant.FAIL,StateInvariant.HTTP_NONEXISTENT,StateInvariant.RET_PARAM_FAIL);
    }
}
