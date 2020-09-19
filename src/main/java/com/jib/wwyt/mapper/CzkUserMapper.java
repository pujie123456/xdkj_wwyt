package com.jib.wwyt.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2020/6/28 14:38
 * @description:
 */

@Component
public interface CzkUserMapper {
    //<!--查询用户名是否存在-->
    int finduserbyname(Map map);
    // <!--查询密码是否正确-->
    List<Map> userlogin(Map map);
    //更新密码
    int updateuser(Map map);
}
