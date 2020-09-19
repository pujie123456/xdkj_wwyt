//package com.jib.wwyt.service;
//
//
//import com.jib.wwyt.model.Bis_monitor_point_maintain;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 重大危险源储罐信息
// *  浦杰
// * */
//public interface ZdwxyCgService {
//    //分页展示储罐监控
//    public List<Map<String,Object>> getCgjkList(int qyid, String pageSize, String pageIndex);
//    //储罐条数
//    public int getCgjkListCount(int qyid);
//
//    //重大危险储罐报警列表
//    public List<Map<String,Object>>  cgxxBjList(int qyid);
//    //重大危险源报警条数
//    public int cgxxBjCount(int qyid);
//
//    //重大危险源储罐信息-带条件
//    public List<Map<String,Object>> cgxxTjcxList(int qyid, String pageSize, String pageIndex,String zt,String cgname);
//    public int cgxxTjcxCount(int qyid,String zt,String cgname);
//    //根据id查找储罐详细信息
//    public List<Map<String,Object>> idfindcg(String id);
//
//    //气体监测列表
//    public  List<Map<String,Object>> qtjcList(int qyid,String pageSize,String pageIndex);
//    //气体监测统计
//    public  int qtjkCount(int qyid);
//    //气体监测带条件
//    public  List<Map<String,Object>> qtjkTjcxList(int qyid,String pageSize,String pageIndex,String ztdm,String qtname);
//    //气体监测带条件统计
//    public int  qtjcTjCount(int qyid,String ztdm,String qtname);
//
//    //根据id查询气体
//    public  List<Map<String,Object>> idFindqt(String id);
//    //重大危险源-高危工艺列表展示
//    public  List<Map<String,Object>> gwgyFindAllList(int qyid,String pageSize,String pageIndex);
//    //重大危险源-高危工艺统计
//    public int gwgyFindAllCount(int qyid);
//    //重大危险源-高危工艺条件查找
//    public List<Map<String,Object>> gwgyFindTjList(int qyid,String pageSize,String pageIndex,String ztdm,String gwgyname);
//    //重大危险源-高危工艺条件查找统计
//    public int gwgyFindTjCount(int qyid,String ztdm,String gwgyname);
//    //根据id查找高危工艺信息
//    public  List<Map<String,Object>> gwgyFindById(String id);
//}
