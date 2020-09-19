package com.jib.wwyt.service.impl;

import com.jib.wwyt.mapper.EdmMapper;
import com.jib.wwyt.service.edmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/7/23 17:20
 * @description: 二道门数据对接
 */
@Service("edmService")
public class edmServiceImpl  implements edmService{
    @Autowired
    private EdmMapper EdmMapper;
    public List<Map> findEdmBytime(Map map)  {
        return EdmMapper.findEdmBytime(map);
    }

    public List<Map>  findEdmByid(Map map)  {
        return EdmMapper.findEdmByid(map);
    }


}
