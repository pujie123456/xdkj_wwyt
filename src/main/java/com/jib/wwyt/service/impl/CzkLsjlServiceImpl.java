package com.jib.wwyt.service.impl;

import com.jib.wwyt.mapper.CzkCardMapper;
import com.jib.wwyt.model.CzkLsjl;
import com.jib.wwyt.service.CzkLsjlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 16:34
 * @description:车载卡历史记录service
 */
@Service("CzkLsjlService")
public class CzkLsjlServiceImpl implements CzkLsjlService {
    @Autowired
    private com.jib.wwyt.mapper.CzkLsjlMapper CzkLsjlMapper;
    //插入
    public int insert(CzkLsjl CzkLsjl){
        return CzkLsjlMapper.insert( CzkLsjl);
    }
    //查询历史记录
    public  List<Map> findhistorybytime(Map map){
        return CzkLsjlMapper.findhistorybytime( map);
    }

    //获取最新x,y轴
    public List<Map> findbydevicenumnewest(Map map ){ return CzkLsjlMapper.findbydevicenumnewest( map);}
}
