package com.jib.wwyt.service.impl;

import com.jib.wwyt.mapper.CzkWarnMapper;
import com.jib.wwyt.model.CzkWarn;
import com.jib.wwyt.service.CzkWarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 16:56
 * @description:车载卡报警
 */
@Service("CzkWarnService")
public class CzkWarnServiceImpl  implements CzkWarnService {
    @Autowired
    private com.jib.wwyt.mapper.CzkWarnMapper CzkWarnMapper;
    public  int  insert(CzkWarn CzkWarn){
        return CzkWarnMapper.insert(CzkWarn);
    }
    //根据公司和卡号进行报警查询
    public List<Map> findbycompany(Map map){
        return CzkWarnMapper.findbycompany(map);
    }
}
