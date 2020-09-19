package com.jib.wwyt.service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/28 15:01
 * @description: 车载卡人员信息
 */
public interface CzkUserService {
    //<!--查询用户名是否存在-->
    int finduserbyname(Map map);
    // <!--查询密码是否正确-->
    List<Map> userlogin(Map map);
    //更新密码
    int updateuser(Map map);
}
