package com.jib.wwyt.service.impl;

import com.jib.wwyt.mapper.CzkUserMapper;
import com.jib.wwyt.service.CzkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/28 15:01
 * @description: 车载卡人员信息
 */
@Service("CzkUserService")
public class CzkUserServiceImpl  implements CzkUserService {
    @Autowired
    private CzkUserMapper CzkUserMapper;
    //<!--查询用户名是否存在-->
   public  int finduserbyname(Map map){
       return  CzkUserMapper.finduserbyname(map);
   }

    // <!--查询密码是否正确-->
    public List<Map> userlogin(Map map){
        return  CzkUserMapper.userlogin(map);
    }
    //更新密码
    public int updateuser(Map map){
        return  CzkUserMapper.updateuser(map);
    }

}
