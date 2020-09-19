package com.jib.wwyt.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jib.wwyt.model.MDF;
import com.jib.wwyt.service.edmService;
import com.jib.wwyt.service.impl.edmServiceImpl;
import com.jib.wwyt.utils.Dateutil;
import com.jib.wwyt.utils.PostRequestUtil;
import com.jib.wwyt.utils.PropertyUtil;
import com.jib.wwyt.utils.Sha256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/7/22 17:56
 * @description:闸机同步
 */
@RestController
@RequestMapping("/zhaji")
public class ZjtbController {
    public static String cookie = "";
    //毛东方人员定位tenantId
    static String  tenantId_mdf= PropertyUtil.getProperty("tenantId");
    //毛东方人员定位url
    static String rydwurl_mdf= PropertyUtil.getProperty("rydwurl");
    //毛东方人员定位id
    static String userId_mdf= PropertyUtil.getProperty("userId");
    //毛东方人员定位密码
    static String passwd_mdf= PropertyUtil.getProperty("passwd");

    @Autowired
    private edmService edmService;


    public static void main(String[] args) {
        String tenantId = tenantId_mdf;
        String userId =userId_mdf;
        String passwd = passwd_mdf;
        String sjscurl = rydwurl_mdf;
        String publicsjscurl = sjscurl.replace("/service/", "/public/");
        Map resultMap = new HashMap();
        Map map = new HashMap<>();
        Map map2 = new HashMap<>();
        map2.put("tenantId", tenantId);
        map2.put("userId", userId);
        map.put("jsonrpc", "2.0");
        map.put("params", map2);
        map.put("method", "getCode");
        map.put("id", "1");
        PostRequestUtil PostRequestUtil=new PostRequestUtil();
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
        resultMap = PostRequestUtil.postSend(publicsjscurl, map, cookie);
        String cookie=resultMap.get("cookie").toString();
        //"areaId":1,
        Map csmap = new HashMap<>();
        csmap.put("entityType","staff");

        map = reMap(csmap, "getEntities");
        resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
        System.out.println(resultMap.get("result").toString());
        JSONObject jsonJson = JSONObject.parseObject(resultMap.get("result").toString());
        JSONArray jsondataArr = jsonJson.getJSONArray("result");
        JSONObject entityJson=null;
        //存放所有的员工实体,key值为员工姓名
        Map entityMap=new HashMap();
        for(int i=0;i<jsondataArr.size();i++){
            entityJson=jsondataArr.getJSONObject(i);
            Map jsonMap=new HashMap();
            jsonMap.put("entityId",entityJson.getInteger("id"));
            jsonMap.put("tagId",entityJson.getString("tagId"));
            jsonMap.put("pid",entityJson.getString("pid"));
            jsonMap.put("name",entityJson.getString("name"));
            entityMap.put(entityJson.getString("name"),jsonMap);
        }
        //查询数据库资料
        Map condition_map=new HashMap();
        edmServiceImpl edmServiceImpl=new edmServiceImpl();
        condition_map.put("starttime","2020-07-23");
        condition_map.put("endtime","2020-07-25");
        List<Map> dataList =edmServiceImpl.findEdmBytime(condition_map);
        String type="";
        String updatetime="";
        String ygcode="";
        String ygname="";
        String tagid="";
        Map tagidMap=null;
        //void pushTagIO(int areaId, string tagId, string direction,long raiseTime,string gateId,long now);
        //遍历新锐二道门数据库数据
        Dateutil Dateutil=new Dateutil();
        for(Map everymap:dataList){
            //(a > b) ? a : b;
            type=everymap.get("type").toString().equals("进")? "int":"out";//进出
            updatetime=everymap.get("updatetime").toString();//上传时间
            ygcode=everymap.get("ygcode").toString();//员工工号
            ygname=everymap.get("ygname").toString();//员工姓名
             tagidMap=(Map)entityMap.get(ygname);
             tagid=tagidMap.get("tagId").toString();
            Map csmap2 = new HashMap<>();
            csmap2.put("areaId",1);
            csmap2.put("tagId",tagid);
            csmap2.put("direction",type);
            csmap2.put("raiseTime", Dateutil.dateToTimestamp(updatetime).getTime());
            csmap2.put("gateId", "GAT0000");
            map = reMap(csmap, "getEntities");
            resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
            System.out.println(resultMap.get("result").toString());
        }





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

    /**
     * test测试端口
     * */
    @RequestMapping("/test")
    @ResponseBody
    public String findHistory(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId = tenantId_mdf;
        String userId =userId_mdf;
        String passwd = passwd_mdf;
        String sjscurl = rydwurl_mdf;
        String publicsjscurl = sjscurl.replace("/service/", "/public/");
        Map resultMap = new HashMap();
        Map map = new HashMap<>();
        Map map2 = new HashMap<>();
        map2.put("tenantId", tenantId);
        map2.put("userId", userId);
        map.put("jsonrpc", "2.0");
        map.put("params", map2);
        map.put("method", "getCode");
        map.put("id", "1");
        PostRequestUtil PostRequestUtil=new PostRequestUtil();
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
        resultMap = PostRequestUtil.postSend(publicsjscurl, map, cookie);
        String cookie=resultMap.get("cookie").toString();
        //"areaId":1,
        Map csmap = new HashMap<>();
        csmap.put("entityType","staff");

        map = reMap(csmap, "getEntities");
        resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
        System.out.println(resultMap.get("result").toString());
        JSONObject jsonJson = JSONObject.parseObject(resultMap.get("result").toString());
        JSONArray jsondataArr = jsonJson.getJSONArray("result");
        JSONObject entityJson=null;
        //存放所有的员工实体,key值为员工姓名
        Map entityMap=new HashMap();
        for(int i=0;i<jsondataArr.size();i++){
            entityJson=jsondataArr.getJSONObject(i);
            Map jsonMap=new HashMap();
            jsonMap.put("entityId",entityJson.getInteger("id"));
            jsonMap.put("tagId",entityJson.getString("tagId"));
            jsonMap.put("pid",entityJson.getString("pid"));
            jsonMap.put("name",entityJson.getString("name"));
            entityMap.put(entityJson.getString("name"),jsonMap);
        }
        //查询数据库资料
        Map condition_map=new HashMap();
        condition_map.put("starttime","2020-07-23");
        condition_map.put("endtime","2020-07-25");
        List<Map> dataList =edmService.findEdmBytime(condition_map);
        String type="";
        String updatetime="";
        String ygcode="";
        String ygname="";
        String tagid="";
        Map tagidMap=null;
        //void pushTagIO(int areaId, string tagId, string direction,long raiseTime,string gateId,long now);
        //遍历新锐二道门数据库数据
        Dateutil Dateutil=new Dateutil();
        for(Map everymap:dataList){
            //(a > b) ? a : b;
            type=everymap.get("type").toString().equals("进")? "in":"out";//进出
            updatetime=everymap.get("updatetime").toString();//上传时间
            ygcode=everymap.get("ygcode").toString();//员工工号
            ygname=everymap.get("ygname").toString();//员工姓名
            tagidMap=(Map)entityMap.get(ygname);
            tagid=tagidMap.get("tagId").toString();
            int entityId=Integer.parseInt(tagidMap.get("entityId").toString());
            Map csmap2 = new HashMap<>();
            csmap2.put("areaId",1);
            csmap2.put("tagId",tagid);
//            csmap2.put("tagId",tagId);
            csmap2.put("direction",type);
            csmap2.put("raiseTime", Dateutil.dateToTimestamp(updatetime).getTime());
            csmap2.put("gateId", "GAT0000");
            map = reMap(csmap2, "pushTagIO");
//            map = reMap(csmap2, "pushEntityIO");
//            System.out.println(JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue));
            //上传二道门数据
            resultMap = PostRequestUtil.postSend(sjscurl, map, cookie);
            System.out.println(resultMap.get("result").toString());
        }
        //获取表中所有用户






        return "测试结束";
    }
}
