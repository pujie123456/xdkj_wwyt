//package com.jib.wwyt.service.impl;
//
//import com.jib.wwyt.mapper.Bis_monitor_point_maintainMapper;
//import com.jib.wwyt.model.Bis_monitor_point_maintain;
//import com.jib.wwyt.service.ZdwxyCgService;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 重大危险源储罐service
// * */
//@Service("ZdwxyCgService")
//public class ZdwyxCgServriceImpl implements ZdwxyCgService {
//    @Autowired
//    private Bis_monitor_point_maintainMapper Bis_monitor_point_maintainMapper;
//
//    //重大危险源储罐-列表
//    public List<Map<String,Object>> getCgjkList(int qyid, String pageSize, String pageIndex){
//        return  Bis_monitor_point_maintainMapper.getCgjkList(qyid,pageSize,pageIndex);
//    }
//    //重大危险源储罐条数
//    public int getCgjkListCount(int qyid){
//        return  Bis_monitor_point_maintainMapper.getCgjkListCount(qyid);
//    }
//    //重大危险源报警列表
//    public List<Map<String,Object>> cgxxBjList(int qyid){
//        return  Bis_monitor_point_maintainMapper.cgxxBjList(qyid);
//    }
//    //重大危险源报警条数
//    public int  cgxxBjCount(int qyid){
//        return  Bis_monitor_point_maintainMapper.cgxxBjCount(qyid);
//    }
//    //带条件查询重大危险源储罐
//    public List<Map<String,Object>> cgxxTjcxList(int qyid, String pageSize, String pageIndex,String zt,String cgname){
//        return  Bis_monitor_point_maintainMapper.cgxxTjcxList(qyid,pageSize,pageIndex,zt,cgname);
//    }
//    public int cgxxTjcxCount(int qyid,String zt,String cgname){
//        return  Bis_monitor_point_maintainMapper.cgxxTjcxCount(qyid,zt,cgname);
//    }
//    //根据id查找储罐详细信息
//    public List<Map<String,Object>> idfindcg(String id){
//        return  Bis_monitor_point_maintainMapper.idfindcg(id);
//    }
//
//    //气体监测列表
//    public  List<Map<String,Object>> qtjcList(int qyid,String pageSize,String pageIndex){
//        return Bis_monitor_point_maintainMapper.qtjcList(qyid,pageSize,pageIndex);
//    }
//    //气体监测统计
//    public  int qtjkCount(int qyid){
//        return Bis_monitor_point_maintainMapper.qtjkCount(qyid);
//    }
//
//    //气体带条件查询
//    public  List<Map<String,Object>> qtjkTjcxList(int qyid,String pageSize,String pageIndex,String ztdm,String qtname){
//        return Bis_monitor_point_maintainMapper.qtjkTjcxList(qyid,pageSize,pageIndex,ztdm,qtname);
//    }
//    //气体带条件查询统计
//    public int  qtjcTjCount(int qyid,String ztdm,String qtname){
//        return Bis_monitor_point_maintainMapper.qtjcTjCount(qyid,ztdm,qtname);
//    }
//    //气体根据id查询
//    public  List<Map<String,Object>> idFindqt(String id){
//        return Bis_monitor_point_maintainMapper.idFindqt(id);
//    }
//
//    //重大危险源-高危工艺列表展示
//    public  List<Map<String,Object>> gwgyFindAllList(int qyid,String pageSize,String pageIndex){
//        return Bis_monitor_point_maintainMapper.gwgyFindAllList(qyid,pageSize,pageIndex);
//    }
//    //重大危险源-高危工艺统计
//    public int gwgyFindAllCount(int qyid){
//        return Bis_monitor_point_maintainMapper.gwgyFindAllCount(qyid);
//    }
//    //重大危险源-高危工艺条件展示
//    public List<Map<String,Object>> gwgyFindTjList(int qyid,String pageSize,String pageIndex,String ztdm,String gwgyname){
//        return Bis_monitor_point_maintainMapper.gwgyFindTjList(qyid,pageSize,pageIndex,ztdm,gwgyname);
//    }
//    //重大危险源-高危工艺条件查找统计
//    public int gwgyFindTjCount(int qyid,String ztdm,String gwgyname){
//        return Bis_monitor_point_maintainMapper.gwgyFindTjCount(qyid,ztdm,gwgyname);
//    }
//
//    //根据id获取高危工艺信息
//    public  List<Map<String,Object>> gwgyFindById(String id){
//        return Bis_monitor_point_maintainMapper.gwgyFindById(id);
//    }
//}
