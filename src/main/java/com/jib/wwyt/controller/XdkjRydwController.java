package com.jib.wwyt.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jib.wwyt.task.StartNeedWork;
import com.jib.wwyt.utils.PostRequestUtil;
import com.jib.wwyt.utils.PropertyUtil;
import com.jib.wwyt.utils.Sha256;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author: pujie
 * @date: 2020/8/26 9:43
 * @description:人员定位
 */
@Controller
@RequestMapping("xdkj/rydw")
public class XdkjRydwController {
    com.jib.wwyt.task.StartNeedWork StartNeedWork=new StartNeedWork();
    //毛东方人员定位tenantId
    static String  tenantId_mdf= PropertyUtil.getProperty("tenantId");
    //毛东方人员定位url
    static String rydwurl_mdf= PropertyUtil.getProperty("rydwurl");
    //#人员定位的接口路径 （只要改ip地址和端口号）
    static String rydyjklj_mdf= PropertyUtil.getProperty("rydyjklj");


    //毛东方人员定位id
    static String userId_mdf= PropertyUtil.getProperty("userId");

    //毛东方人员定位密码
    static String passwd_mdf= PropertyUtil.getProperty("passwd");

    //获取毛东方快照
    static JSONObject jsonkz=null;
    public static List list_mdf = new ArrayList();//毛东方实时人员定位列表

    Map mapemps_name = StartNeedWork.reMap_name();

    Map mapemps_pid = StartNeedWork.reMap_pid();

    Map mapemps_map = StartNeedWork.reMap_map();
    public String cookie_first = StartNeedWork.recookie();
    //毛东方人员定位cookie
    public static String cookie = "";
    //时间戳为了判断毛东方数据是否过期
    public static long timeflag_mdf = 0;

    @RequestMapping("xryzgzwtest2")
    @ResponseBody
//    public void getssxx(HttpServletRequest request, HttpServletResponse response, Model model){
    public void xryzgzwtest2(HttpServletRequest request, HttpServletResponse response, Model model, @RequestBody Map map) {
        if(map.get("Location")==null){
            System.out.println("Location为null");
            return;
        }
        List<LinkedHashMap> list = (ArrayList) map.get("Location");
        list_mdf.clear();
//      String s = JSONObject.toJSONString(map);
//      System.out.println(s);
        //获取毛东方快照
        if (this.jsonkz == null) {
            this.jsonkz = getTagStateMap();
        }
        //获取闸机位置
        JSONObject jsonZJ = getZJ();

        Map ssmap = new HashMap();//实时数据
        Map ssgxmap = new HashMap();//实时更新数据
        for (LinkedHashMap map2 : list) {
            if (map2.get("userId") == null || "".equals(map2.get("userId").toString())) {
                continue;
            }
            if (map2.get("warnType") != null && !"".equals(map2.get("warnType").toString())) {
                System.out.println(map2.get("warnType") + "");
                System.out.println(map2.get("warnType") + "");
            }

//            if("true".equals(map2.get("out").toString())){
//                map2.put("x",-100);
//                map2.put("y",-100);
//            }
            Map map3 = new HashMap();
            String aaa = "";
            String bbb = "";
//            System.out.print("userId"+map2.get("userId").toString());
            if (mapemps_name.get(map2.get("userId").toString()) == null) {
//                    continue;
            } else {
                aaa = mapemps_name.get(map2.get("userId")).toString();
                bbb = mapemps_pid.get(map2.get("userId")).toString();
            }

            map3.put("empName", aaa);
            map3.put("empNum", bbb);
            map3.put("out", (boolean) map2.get("out"));
//            map3.put("out", false);
            map3.put("macAddress", map2.get("tagId"));
            map3.put("dataTime", map2.get("locationTime")); //时间
            timeflag_mdf = System.currentTimeMillis();

            if (map2.get("x") == null || map2.get("x").toString().equals("")) {
//                continue;
                if(jsonZJ==null||jsonZJ.get("x")==null){
                    map2.put("x", 0);
                    map2.put("y", 0);
                }else {
                    map2.put("x", jsonZJ.getDouble("x"));
                    map2.put("y", jsonZJ.getDouble("y"));
                }

            }

            map3.put("xCross", map2.get("x")); //x
            map3.put("yCross", map2.get("y")); //y
            if (map2.get("areaId") != null&&mapemps_map!=null&&mapemps_map.get(map2.get("areaId"))!=null) {
                map3.put("layer", Integer.parseInt(mapemps_map.get(map2.get("areaId").toString()) + "") + 1); //
            } else {
                map3.put("layer", "0");
            }
            map3.put("empType", map2.get("entityType")); //区域idstatic\model\images\xfss\
            map3.put("areaID", map2.get("areaId")); //区域idc4
            ssmap.put(map2.get("tagId"), map3);
            ssgxmap.put(map2.get("tagId"), map2);
        }

        for (Map.Entry<String, Object> entry : jsonkz.entrySet()) {
            //循环遍历毛东方快照数据
            JSONObject jsondataresult2 = JSONObject.parseObject(entry.getValue().toString());
            if (!jsondataresult2.getString("entityType").equals("staff")) { //如果不是员工就跳过
                continue;
            }
            Map map3 = new HashMap();
            //判断有实时数据
            if (ssmap.get(entry.getKey()) != null) {
                //有实时数据
                list_mdf.add(ssmap.get(entry.getKey()));
                String jsongx = JSONObject.toJSONString(ssgxmap.get(entry.getKey()), SerializerFeature.WriteNullStringAsEmpty);
                JSONObject gxJson = JSONObject.parseObject(jsongx);
                jsonkz.put(entry.getKey(), gxJson);
            } else {
                //无实时数据
                String aaa = "";
                String bbb = "";
                if (mapemps_name.get(jsondataresult2.getString("userId")) == null) {
                    continue;
                }
                aaa = mapemps_name.get(jsondataresult2.getString("userId")).toString(); //获取用户名  empName
                bbb = mapemps_pid.get(jsondataresult2.getString("userId")).toString();//获取员工工号    empNum
                map3.put("empName", aaa);
                map3.put("empNum", bbb);
                map3.put("macAddress", entry.getKey());  //tagid
                map3.put("dataTime", jsondataresult2.getLong("locationTime")); //时间
                timeflag_mdf = System.currentTimeMillis();
                map3.put("xCross", jsondataresult2.getDouble("x")); //x
                map3.put("yCross", jsondataresult2.getDouble("y")); //y
                map3.put("out", false); //out标志
                if (jsondataresult2.getInteger("areaId") != null) {
                    map3.put("layer", mapemps_map.get(jsondataresult2.getInteger("areaId") + 1));
                } else {
                    map3.put("layer", 0);
                }
                map3.put("empType", "staff"); //区域idstatic\model\images\xfss\
                map3.put("areaID", jsondataresult2.getInteger("areaId")); //区域idc4
                list_mdf.add(map3);
            }
        }
        timeflag_mdf= System.currentTimeMillis();
    }

    //获取毛东方标签快照
    public JSONObject getTagStateMap() {
        //fastjson解析方法
//        Map<String,JSONObject> mapresult=new HashMap<String,JSONObject>();
        JSONObject jsondataresult = null;
        String result = "";

            String tenantId = tenantId_mdf;
            String userId = userId_mdf;
            String passwd = passwd_mdf;
            String sjscurl = rydwurl_mdf;
            String publicsjscurl = sjscurl.replace("/service/", "/public/");
            Map resultMap = new HashMap();
            try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
                PostRequestUtil PostRequestUtil = new PostRequestUtil();
                //先判断是否登录
                if (this.cookie == null || this.cookie.length() < 1) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                }
                Map csmap = new HashMap<>();
                csmap.put("areaId", 1);
                Map map = reMap(csmap, "getTagStateMap");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                JSONObject jsondata = JSONObject.parseObject(resultMap.get("result").toString());
                jsondataresult = jsondata.getJSONObject("result");
//                    for (Map.Entry<String, Object> entry : jsondataresult.entrySet()) {
//                        JSONObject jsondataresult2=JSONObject.parseObject(entry.getValue().toString());
////                        mapresult.put(entry.getKey(),jsondataresult2);
//                    }
            } catch (Exception e) {
                e.printStackTrace();
            }

        return jsondataresult;
    }

    /**
     * 重新登录方法
     * *tenantId 租户id
     * * userId 用户id
     * * passwd 用户密码
     * * sjscurl 请求路径 一般为http://dw2.yz-cloud.com/position/service/embeded.smd
     */
    public void reLosgin(String tenantId, String userId, String passwd, String sjscurl) {
        Map resultMap = new HashMap();
        String publicsjscurl = sjscurl.replace("/service/", "/public/");
        PostRequestUtil PostRequestUtil = new PostRequestUtil();
        Map map = new HashMap<>();
        Map map2 = new HashMap<>();
        map2.put("tenantId", tenantId);
        map2.put("userId", userId);
        map.put("jsonrpc", "2.0");
        map.put("params", map2);
        map.put("method", "getCode");
        map.put("id", "1");
        resultMap = PostRequestUtil.postSend(publicsjscurl, map, "");
        String result = resultMap.get("result") + "";
        System.out.println("result:" + result);
        JSONObject resultjson = JSONObject.parseObject(result);
        String dwvscode = resultjson.get("result").toString();
        System.out.println("dwvscode:" + dwvscode);
        //密码 xhny201912100950
        String key = Sha256.getSHA256(tenantId + userId + passwd + dwvscode);
        System.out.println("key:" + key);
        map.put("method", "login");
        map2.put("key", key);
        resultMap = PostRequestUtil.postSend(publicsjscurl, map, this.cookie);
        if (resultMap.get("cookie") == null) {
            this.cookie = StartNeedWork.recookie();
        } else {
            this.cookie = resultMap.get("cookie").toString();
        }
        System.out.println("session过期，人员用户重新登录成功");
    }
    /**
     * 返回传入map
     */
    static public Map reMap(Map map2, String method) {
        Map map = new HashMap<>();
        map.put("jsonrpc", "2.0");
        map.put("params", map2);
        map.put("method", method);
        map.put("id", "1");
        return map;
    }
    //获取毛东方闸机位置
    public JSONObject getZJ() {
        //fastjson解析方法
//        Map<String,JSONObject> mapresult=new HashMap<String,JSONObject>();
        JSONObject jsondataresult = null;
        String result = "";

            String tenantId = tenantId_mdf;
            String userId = userId_mdf;
            String passwd = passwd_mdf;
            String sjscurl = rydwurl_mdf;
            String publicsjscurl = sjscurl.replace("/service/", "/public/");
            Map resultMap = new HashMap();
            try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
                PostRequestUtil PostRequestUtil = new PostRequestUtil();
                //先判断是否登录
                if (this.cookie == null || this.cookie.length() < 1) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                }
                Map csmap = new HashMap<>();
                csmap.put("areaId", 0);
                csmap.put("deviceType", "GAT");
                csmap.put("pageIndex", 0);
                csmap.put("maxCount", 1);
                Map map = reMap(csmap, "getLocationPoints");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                JSONObject jsonJson = JSONObject.parseObject(resultMap.get("result").toString());
                JSONArray jsondataArr = jsonJson.getJSONArray("result");
                JSONObject jsondata = new JSONObject();
                if (jsondataArr.size() > 0) {
                    jsondata = jsondataArr.getJSONObject(0);
                } else {
                    jsondata.put("x", 0.0);
                    jsondata.put("y", 0.0);
                }
                jsondataresult = jsondata.getJSONObject("result");
//                    for (Map.Entry<String, Object> entry : jsondataresult.entrySet()) {
//                        JSONObject jsondataresult2=JSONObject.parseObject(entry.getValue().toString());
////                        mapresult.put(entry.getKey(),jsondataresult2);
//                    }
            } catch (Exception e) {
                e.printStackTrace();
            }

        return jsondataresult;
    }
    //毛东方获取快照触发
    public void mdfkzcf() {
        this.jsonkz = getTagStateMap();
        for (Map.Entry<String, Object> entry : jsonkz.entrySet()) {
            //循环遍历毛东方快照数据
            JSONObject jsondataresult2 = JSONObject.parseObject(entry.getValue().toString());
            if (!jsondataresult2.getString("entityType").equals("staff")) { //如果不是员工就跳过
                continue;
            }
            Map map3 = new HashMap();
            //判断有实时数据

            //无实时数据
            String aaa = "";
            String bbb = "";
            if (mapemps_name.get(jsondataresult2.getString("userId")) == null) {
                continue;
            }
            aaa = mapemps_name.get(jsondataresult2.getString("userId")).toString(); //获取用户名  empName
            bbb = mapemps_pid.get(jsondataresult2.getString("userId")).toString();//获取员工工号    empNum
            map3.put("empName", aaa);
            map3.put("empNum", bbb);
            map3.put("macAddress", entry.getKey());  //tagid
            map3.put("dataTime", jsondataresult2.getInteger("locationTime")); //时间
            timeflag_mdf = System.currentTimeMillis();
            map3.put("xCross", jsondataresult2.getDouble("x")); //x
            map3.put("yCross", jsondataresult2.getDouble("y")); //y
            map3.put("out", false); //out标志
            if (jsondataresult2.getInteger("areaId") != null) {
                map3.put("layer", mapemps_map.get(jsondataresult2.getInteger("areaId") + 1));
            } else {
                map3.put("layer", 0);
            }
            map3.put("empType", "staff"); //区域idstatic\model\images\xfss\
            map3.put("areaID", jsondataresult2.getInteger("areaId")); //区域idc4
            list_mdf.add(map3);
        }
    }
    /**
     * 毛东方重新订阅数据接口
     * author pujie
     * 2020-06-15
     */
    @RequestMapping(value = "resubscribe")
    @ResponseBody
    public String resubscribe(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map mapresult = new HashMap();

            String tenantId = tenantId_mdf;
            String userId = request.getParameter("username");
            String passwd = request.getParameter("password");
            String sjscurl = rydwurl_mdf;
            String publicsjscurl = sjscurl.replace("/service/", "/public/");
            Map resultMap = new HashMap();
            try {
                PostRequestUtil PostRequestUtil = new PostRequestUtil();
                //先判断是否登录
                if (this.cookie == null || this.cookie.length() < 1) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                }
                Map csmap = new HashMap<>();
                String areaidString = request.getParameter("areaId");

                //删除订阅
                Map map = reMap(csmap, "unsubscribeAll");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                csmap.put("interval", 3000);
                csmap.put("overtime", 30000);
                csmap.put("topic", "Location");
                map = reMap(csmap, "subscribe");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
//                csmap.put("interval",3000);
//                csmap.put("overtime",30000);
//                csmap.put("topic","Warn");
//                map = reMap(csmap, "subscribe");
//                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                return "毛东方人员订阅重新订阅成功";
            } catch (Exception e) {
                e.printStackTrace();
            }

        return "订阅失败";
    }


    /**
     * 毛东方重新配置订阅地址数据接口
     * author pujie
     * 2020-06-15
     */
    @RequestMapping(value = "readdSubscribeURL")
    @ResponseBody
    public String readdSubscribeURL(HttpServletRequest request, HttpServletResponse response, Model model) {
            Map mapresult = new HashMap();
            String rydyjklj = rydyjklj_mdf;
            String tenantId = tenantId_mdf;
            String userId = request.getParameter("username");
            String passwd = request.getParameter("password");
            String sjscurl = rydwurl_mdf;
            String publicsjscurl = sjscurl.replace("/service/", "/public/");
            Map resultMap = new HashMap();
            try {
                PostRequestUtil PostRequestUtil = new PostRequestUtil();
                //先判断是否登录
                if (this.cookie == null || this.cookie.length() < 1) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                }
                Map csmap = new HashMap<>();
                String areaidString = request.getParameter("areaId");
                csmap.put("id", 2);
                //删除原本的url
                Map map = reMap(csmap, "deleteSubscribeURL");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                System.out.println("删除原本的url");
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }

                csmap.put("url", rydyjklj_mdf);

                map = reMap(csmap, "addSubscribeURL");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                System.out.println("重新订阅url地址");
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                return "毛东方人员地址订阅重新订阅成功";
            } catch (Exception e) {
                e.printStackTrace();
            }

        return "订阅失败";
    }
    /**
     * 区域数据部分接口-返回区域id   区域名称  区域当前人数
     * author pujie
     * 2020-05-25
     */
    @RequestMapping(value = "AllAreaList")
    @ResponseBody
    public String AllAreaList(HttpServletRequest request, HttpServletResponse response, Model model) {
        //拼接成都的请求路径
        Map mapresult = new HashMap();

            String tenantId = tenantId_mdf;
            String userId = userId_mdf;
            String passwd = passwd_mdf;
            String sjscurl = rydwurl_mdf;
            String publicsjscurl = sjscurl.replace("/service/", "/public/");
            String areaIdString = request.getParameter("areaId");
            if (areaIdString == null || "".equals(areaIdString)) {
                areaIdString = "1";
            }
            int areaId = Integer.parseInt(areaIdString);
//              int areaId=Integer.parseInt(areaIdString);
            Map resultMap = new HashMap();
            try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
                PostRequestUtil PostRequestUtil = new PostRequestUtil();
                //先判断是否登录
                if (this.cookie == null || this.cookie.length() < 1) {
//                sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
                    Map map = new HashMap<>();
                    Map map2 = new HashMap<>();
                    map2.put("tenantId", tenantId);
                    map2.put("userId", userId);
                    map.put("jsonrpc", "2.0");
                    map.put("params", map2);
                    map.put("method", "getCode");
                    map.put("id", "1");
                    resultMap = PostRequestUtil.postSend(publicsjscurl, map, "");
                    String result = resultMap.get("result") + "";
                    System.out.println("result:" + result);
                    JSONObject resultjson = JSONObject.parseObject(result);
                    String dwvscode = resultjson.get("result").toString();
                    System.out.println("dwvscode:" + dwvscode);
                    //密码 xhny201912100950
                    String key = Sha256.getSHA256(tenantId + userId + passwd + dwvscode);
                    System.out.println("key:" + key);
                    map.put("method", "login");
                    map2.put("key", key);
                    resultMap = PostRequestUtil.postSend(publicsjscurl, map, this.cookie);
                    if (this.cookie.length() < 1) {
                        this.cookie = resultMap.get("cookie").toString();
                    }
                    System.out.println("人员用户重新登录成功");
                }
                Map csmap = new HashMap<>();
                csmap.put("areaId", areaId);
                Map map = reMap(csmap, "summaryOnlineEntity");
                resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                JSONObject jsondata1 = JSONObject.parseObject(resultMap.get("result").toString());
                JSONObject jsondata2 = jsondata1.getJSONObject("result");
                JSONArray jsonarr = jsondata2.getJSONArray("children");
                JSONObject jsondata3 = new JSONObject();
                JSONObject jsondata4 = new JSONObject();
                JSONObject jsondata5 = new JSONObject();
                List resultlist = new ArrayList();
                for (int i = 0; i < jsonarr.size(); i++) {
                    jsondata3 = jsonarr.getJSONObject(i);
                    jsondata4 = jsondata3.getJSONObject("summary");
                    jsondata5 = jsondata4.getJSONObject("online");
                    Map mapdata = new HashMap();
                    mapdata.put("areaid", jsondata3.getInteger("id"));
                    mapdata.put("areaname", jsondata3.getString("name"));
                    mapdata.put("personcount", jsondata5.getInteger("total"));
                    resultlist.add(mapdata);
                }
                mapresult.put("data", resultlist);
                mapresult.put("retCode", "200");
                mapresult.put("retInfo", "请求成功");
                mapresult.put("success", "true");
            } catch (Exception e) {
                e.printStackTrace();
                mapresult.put("retCode", "500");
                mapresult.put("retInfo", "请求失败");
                mapresult.put("success", "false");
                return JSONObject.toJSONString(mapresult, SerializerFeature.WriteNullStringAsEmpty);
            }

        return JSONObject.toJSONString(mapresult, SerializerFeature.WriteNullStringAsEmpty);
    }
    /**
     * 返回人员信息列表
     * author pujie
     * 2020-05-26
     */
    @RequestMapping(value = "getEmpList")
    @ResponseBody
    public String getEmpList(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map mapresult = new HashMap();

            String tenantId = tenantId_mdf;
            String userId = request.getParameter("username");
            String passwd = request.getParameter("password");
            String sjscurl = rydwurl_mdf;
            String publicsjscurl = sjscurl.replace("/service/", "/public/");
            Map resultMap = new HashMap();
            List resultlist = new ArrayList();
            try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
                PostRequestUtil PostRequestUtil = new PostRequestUtil();
                //先判断是否登录
                if (this.cookie == null || this.cookie.length() < 1) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                }
                Map csmap = new HashMap<>();

                //----------------人员---------------
                csmap.put("entityType", "staff");
                Map map = reMap(csmap, "getEntities");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                JSONObject jsondata = JSONObject.parseObject(resultMap.get("result").toString());

                JSONArray jsonarr = jsondata.getJSONArray("result");
                JSONObject json2 = null;

                for (int i = 0; i < jsonarr.size(); i++) {
                    json2 = jsonarr.getJSONObject(i);
                    Map Mapdata = new HashMap<>();
                    Mapdata.put("empName", json2.getString("name"));
                    Mapdata.put("empNum", json2.getString("pid"));
                    Mapdata.put("macAddress", json2.getString("tagId"));
                    resultlist.add(Mapdata);
                }
                //-------------车辆----------------
                csmap.put("entityType", "car");
                map = reMap(csmap, "getEntities");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                jsondata = JSONObject.parseObject(resultMap.get("result").toString());

                jsonarr = jsondata.getJSONArray("result");
                json2 = null;

                for (int i = 0; i < jsonarr.size(); i++) {
                    json2 = jsonarr.getJSONObject(i);
                    Map Mapdata = new HashMap<>();
                    Mapdata.put("empName", json2.getString("name"));
                    Mapdata.put("empNum", json2.getString("pid"));
                    Mapdata.put("macAddress", json2.getString("tagId"));
                    resultlist.add(Mapdata);
                }

                //-------------其他----------------
                csmap.put("entityType", "other");
                map = reMap(csmap, "getEntities");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                jsondata = JSONObject.parseObject(resultMap.get("result").toString());

                jsonarr = jsondata.getJSONArray("result");
                json2 = null;

                for (int i = 0; i < jsonarr.size(); i++) {
                    json2 = jsonarr.getJSONObject(i);
                    Map Mapdata = new HashMap<>();
                    Mapdata.put("empName", json2.getString("name"));
                    Mapdata.put("empNum", json2.getString("pid"));
                    Mapdata.put("macAddress", json2.getString("tagId"));
                    resultlist.add(Mapdata);
                }


                //--------------资产装备---------------
                csmap.put("entityType", "equipment");
                map = reMap(csmap, "getEntities");
                resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
                if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                    reLosgin(tenantId, userId, passwd, sjscurl);
                    resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
                }
                jsondata = JSONObject.parseObject(resultMap.get("result").toString());

                jsonarr = jsondata.getJSONArray("result");
                json2 = null;

                for (int i = 0; i < jsonarr.size(); i++) {
                    json2 = jsonarr.getJSONObject(i);
                    Map Mapdata = new HashMap<>();
                    Mapdata.put("empName", json2.getString("name"));
                    Mapdata.put("empNum", json2.getString("pid"));
                    Mapdata.put("macAddress", json2.getString("tagId"));
                    resultlist.add(Mapdata);
                }
                mapresult.put("data", resultlist);
                mapresult.put("retCode", "200");
                mapresult.put("retInfo", "请求成功");
                mapresult.put("success", "true");
            } catch (Exception e) {
                mapresult.put("retCode", "500");
                mapresult.put("retInfo", "请求失败");
                mapresult.put("success", "false");
                return JSONObject.toJSONString(mapresult, SerializerFeature.WriteNullStringAsEmpty);
            }

        return JSONObject.toJSONString(mapresult, SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     *  人员数据统计
     * author pujie
     * 2020-08-27
     */
    @RequestMapping(value = "totalstaff")
    @ResponseBody
    public String totalstaff(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map mapresult = new HashMap();
        String tenantId = tenantId_mdf;
        String userId = userId_mdf;
        String passwd = passwd_mdf;
        String sjscurl = rydwurl_mdf;
        String publicsjscurl = sjscurl.replace("/service/", "/public/");
        String areaIdString = request.getParameter("areaId");
        if (areaIdString == null || "".equals(areaIdString)) {
            areaIdString = "1";
        }
        int areaId = Integer.parseInt(areaIdString);
//              int areaId=Integer.parseInt(areaIdString);
        Map resultMap = new HashMap();
        try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
            PostRequestUtil PostRequestUtil = new PostRequestUtil();
            //先判断是否登录
            if (this.cookie == null || this.cookie.length() < 1) {
//                sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
                Map map = new HashMap<>();
                Map map2 = new HashMap<>();
                map2.put("tenantId", tenantId);
                map2.put("userId", userId);
                map.put("jsonrpc", "2.0");
                map.put("params", map2);
                map.put("method", "getCode");
                map.put("id", "1");
                resultMap = PostRequestUtil.postSend(publicsjscurl, map, "");
                String result = resultMap.get("result") + "";
                System.out.println("result:" + result);
                JSONObject resultjson = JSONObject.parseObject(result);
                String dwvscode = resultjson.get("result").toString();
                System.out.println("dwvscode:" + dwvscode);
                //密码 xhny201912100950
                String key = Sha256.getSHA256(tenantId + userId + passwd + dwvscode);
                System.out.println("key:" + key);
                map.put("method", "login");
                map2.put("key", key);
                resultMap = PostRequestUtil.postSend(publicsjscurl, map, this.cookie);
                if (this.cookie.length() < 1) {
                    this.cookie = resultMap.get("cookie").toString();
                }
                System.out.println("人员用户重新登录成功");
            }
            Map csmap = new HashMap<>();
            csmap.put("areaId", areaId);
            Map map = reMap(csmap, "summaryOnlineEntity");
            resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
            if (resultMap.get("result").equals("密码未填写") || resultMap.get("result").toString().contains("服务器异常")) {
                reLosgin(tenantId, userId, passwd, sjscurl);
                resultMap = PostRequestUtil.postSend(sjscurl, map, this.cookie);
            }
            JSONObject jsondata1 = JSONObject.parseObject(resultMap.get("result").toString());
            JSONObject jsondata2 = jsondata1.getJSONObject("result");
            JSONObject summaryjson = jsondata2.getJSONObject("summary");
            JSONObject onlinejson = summaryjson.getJSONObject("online");
            int person=onlinejson.getInteger("staff");
            mapresult.put("data", person);
            mapresult.put("retCode", "200");
            mapresult.put("retInfo", "请求成功");
            mapresult.put("success", "true");
        } catch (Exception e) {
            e.printStackTrace();
            mapresult.put("retCode", "500");
            mapresult.put("retInfo", "请求失败");
            mapresult.put("success", "false");
            return JSONObject.toJSONString(mapresult, SerializerFeature.WriteNullStringAsEmpty);
        }

        return JSONObject.toJSONString(mapresult, SerializerFeature.WriteNullStringAsEmpty);
    }
}
