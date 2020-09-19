package com.jib.wwyt.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2020/7/23 17:28
 * @description:
 */
@Component
public interface EdmMapper {
    List<Map>  findEdmBytime(Map map);
    List<Map>  findEdmByid(Map map);
}
