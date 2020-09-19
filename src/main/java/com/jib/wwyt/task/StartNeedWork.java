package com.jib.wwyt.task;

/**
 * @author: Administrator
 * @date: 2020/8/26 9:14
 * @description:
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jib.wwyt.utils.PostRequestUtil;
import com.jib.wwyt.utils.PropertyUtil;
import com.jib.wwyt.utils.Sha256;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动tomcat服务实现实时数据订阅
 * */
@Component
public class StartNeedWork implements HttpSessionListener {
    //    #人员定位tenantId
    static String  tenantId= PropertyUtil.getProperty("tenantId");
    //#人员定位最高权限userId
    static String userId= PropertyUtil.getProperty("userId");
    //#人员定位sjscurl
    static String rydwurl= PropertyUtil.getProperty("rydwurl");
    //#人员定位最高权限passwd
    static String passwd= PropertyUtil.getProperty("passwd");
    static String cookie_first="";
    //#人员定位的接口路径 （只要改ip地址和端口号）
    static String rydyjklj= PropertyUtil.getProperty("rydyjklj");
    static Map mapemps_name=new HashMap(); //毛东方人员人员定位姓名对应
    static Map mapemps_pid=new HashMap();
    static Map mapemps_map=new HashMap();

    static {
            //毛东方的人员定位
            String publicsjscurl=rydwurl.replace("/service/","/public/");
            String cookie="";
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            Map map=new HashMap<>();
            Map map2=new HashMap<>();
            map2.put("tenantId",tenantId);
            map2.put("userId",userId);
            map.put("jsonrpc","2.0");
            map.put("params",map2);
            map.put("method","getCode");
            map.put("id","1");
            Map resultMap=PostRequestUtil.postSend(publicsjscurl,map,"");
            String result=resultMap.get("result")+"";
            System.out.println("result:"+result);
            JSONObject resultjson = JSONObject.parseObject(result);
            String dwvscode=resultjson.get("result").toString();
            System.out.println("dwvscode:"+dwvscode);
            //登录
            String key= Sha256.getSHA256(tenantId+userId+passwd+dwvscode);
            System.out.println("key:"+key);
            map.put("method","login");
            map2.put("key",key);
            resultMap=PostRequestUtil.postSend(publicsjscurl,map,cookie);
            if(resultMap.get("cookie")!=null){


                cookie=resultMap.get("cookie").toString();
                cookie_first=cookie;

                //判断是否已经订阅
                Map map3=new HashMap<>();
//        map3.put("topic","State.Tag");
//        map3.put("interval",3000);
//        map3.put("overtime",30000);
//        map.put("params",map3);
                map.put("method","getSubscribeURLs");
                map.put("params",map3);
                resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                if(!resultMap.get("result").toString().contains("http")){
                    System.out.println("----------未配置定位url-------------");
                    map3.put("url",rydyjklj);
                    map.put("method","addSubscribeURL");
                    resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                    System.out.println("------定位url配置完毕-------");
                    System.out.println("------开始配置定位主题-------");
//            map3.put("topic","State.Tag");
//            map3.put("interval",3000);
//            map3.put("overtime",30000);
//            map.put("method","subscribe");
//            resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                    map3.put("topic","Location");
                    map3.put("interval",3000);
                    map3.put("overtime",30000);
                    map.put("method","subscribe");
                    map.put("params",map3);
                    resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                    map3.put("topic","Warn");
                    map3.put("interval",3000);
                    map3.put("overtime",30000);
                    map.put("method","subscribe");
                    map.put("params",map3);
                    resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                    System.out.println("------定位主题配置完毕-------");
                }else {
                    System.out.println("----------已配置定位url-------------");
                }

                System.out.print("--------开始获取毛东方人员列表-------");

                map3.put("entityType","staff");
                map.put("method","getEntities");
                map.put("params",map3);
                resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                JSONObject json= JSONObject.parseObject(resultMap.get("result").toString());
                JSONArray jsonarr=json.getJSONArray("result");
                JSONObject json2=null;
                for(int i=0;i<jsonarr.size();i++){
                    json2=jsonarr.getJSONObject(i);
                    mapemps_name.put(json2.getString("userId"),json2.getString("name"));
                    mapemps_pid.put(json2.getString("userId"),json2.getString("pid"));
                }

                map3.put("entityType","car");
                map.put("method","getEntities");
                map.put("params",map3);
                resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                json= JSONObject.parseObject(resultMap.get("result").toString());
                jsonarr=json.getJSONArray("result");
                json2=null;
                for(int i=0;i<jsonarr.size();i++){
                    json2=jsonarr.getJSONObject(i);
                    mapemps_name.put(json2.getString("userId"),json2.getString("name"));
                    mapemps_pid.put(json2.getString("userId"),json2.getString("pid"));
                }

                map3.put("entityType","equipment");
                map.put("method","getEntities");
                map.put("params",map3);
                resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                json= JSONObject.parseObject(resultMap.get("result").toString());
                jsonarr=json.getJSONArray("result");
                json2=null;
                for(int i=0;i<jsonarr.size();i++){
                    json2=jsonarr.getJSONObject(i);
                    mapemps_name.put(json2.getString("userId"),json2.getString("name"));
                    mapemps_pid.put(json2.getString("userId"),json2.getString("pid"));
                }
                map3.put("entityType","other");
                map.put("method","getEntities");
                map.put("params",map3);
                resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                json= JSONObject.parseObject(resultMap.get("result").toString());
                jsonarr=json.getJSONArray("result");
                json2=null;
                for(int i=0;i<jsonarr.size();i++){
                    json2=jsonarr.getJSONObject(i);
                    mapemps_name.put(json2.getString("userId"),json2.getString("name"));
                    mapemps_pid.put(json2.getString("userId"),json2.getString("pid"));
                }
                map3.put("engineId","a1");
                map.put("method","getRootAreas");
                map.put("params",map3);
                resultMap=PostRequestUtil.postSend(rydwurl,map,cookie);
                json= JSONObject.parseObject(resultMap.get("result").toString());
                jsonarr=json.getJSONArray("result");
                json2=null;
                for(int i=0;i<jsonarr.size();i++){
                    json2=jsonarr.getJSONObject(i);
                    mapemps_map.put(json2.getString("id"),json2.getString("floor"));
                }
            }
    }
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {

    }
    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {

    }
    /**
     * 返回传入map
     * */
    public Map reMap_name(){
        return mapemps_name;
    }
    public Map reMap_pid(){
        return mapemps_pid;
    }
    public Map reMap_map(){
        return mapemps_map;
    }
    public String recookie(){
        return cookie_first;
    }
}

