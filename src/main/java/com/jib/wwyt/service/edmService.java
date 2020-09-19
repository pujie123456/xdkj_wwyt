package com.jib.wwyt.service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/7/23 17:21
 * @description: 二道门数据上传
 */
public interface edmService {
    //获取二道门数据
    public List<Map> findEdmBytime(Map map);
    List<Map> findEdmByid(Map map);

}
