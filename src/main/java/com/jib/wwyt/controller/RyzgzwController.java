package com.jib.wwyt.controller;

import com.alibaba.fastjson.JSONObject;
import com.jib.wwyt.utils.PostRequestUtil;
import com.jib.wwyt.utils.Sha256;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2020/8/26 9:41
 * @description:
 */
/*新人员在岗在位controller
 *author pujie
 *2020-04-17
 * sjscurl 请求路径
 *
 * */
@Controller
@RequestMapping("bis")
public class RyzgzwController {
    public static String  cookie="";
    public  static Map rydwMap;
    @RequestMapping("xryzgzw")
    @ResponseBody
    public void getssxx(HttpServletRequest request, HttpServletResponse response, Model model, @RequestBody Map map){
//        System.out.print("收到请求");
        this.rydwMap=map;
    }




    @RequestMapping("xryzgzw/xryzgzwtest")
    @ResponseBody
    public String xryzgzwtest(HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            String sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
            Map map=new HashMap<>();
            Map map2=new HashMap<>();
            map2.put("tenantId","sc19120011");
            map2.put("userId","xhny");
            map.put("jsonrpc","2.0");
            map.put("params",map2);
            map.put("method","getCode");
            map.put("id","1");
            Map resultMap=PostRequestUtil.postSend(sjscurl,map,"");
            String result=resultMap.get("result")+"";
            System.out.println("result:"+result);
            JSONObject  resultjson = JSONObject.parseObject(result);
            String dwvscode=resultjson.get("result").toString();
            System.out.println("dwvscode:"+dwvscode);
            //密码 xhny201912100950
            String key= Sha256.getSHA256("sc19120011"+"xhny"+"xhny201912100950"+dwvscode);
            System.out.println("key:"+key);
            map.put("method","login");
            map2.put("key",key);
            resultMap=PostRequestUtil.postSend("http://dw.yz-cloud.com/position/public/embeded.smd",map,this.cookie);
            if(this.cookie.length()<1){
                this.cookie=resultMap.get("cookie").toString();
            }

            System.out.print("this.cookie:"+this.cookie);
            map.put("method","getUsers");
            resultMap=PostRequestUtil.postSend("http://dw.yz-cloud.com/position/service/embeded.smd",map,this.cookie);
            System.out.print("resultmap:"+resultMap.get("result"));
            Map map3=new HashMap<>();
//         map3.put("entityType","staff");
//        map.put("params",map3);
//        map.put("method","getEntities");id
            map3.put("topic","State.Beacon");
            map3.put("interval",1000);
            map3.put("overtime",30000);
//            map3.put("url","http://58.214.244.98:8098/bis/xryzgzw");
            map.put("params",map3);
            map.put("method","getTopics");
            resultMap=PostRequestUtil.postSend("http://dw.yz-cloud.com/position/service/embeded.smd",map,cookie);
            System.out.print("resultmap:"+resultMap.get("result"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "this.cookie:"+this.cookie;
    }






    /**
     *获取系统实体类型
     * author pujie
     *2020-04-22
     *  tenantId 租户id
     *   userId 用户id
     *   passwd 用户密码
     *   sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * */
    @RequestMapping("xryzgzw/hqxtstlx")
    @ResponseBody
    public String hqxtstlx(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        Map resultMap=new HashMap();
        try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            //先判断是否登录
            if(this.cookie==null||this.cookie.length()<1){
//                sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
                Map map=new HashMap<>();
                Map map2=new HashMap<>();
                map2.put("tenantId",tenantId);
                map2.put("userId",userId);
                map.put("jsonrpc","2.0");
                map.put("params",map2);
                map.put("method","getCode");
                map.put("id","1");
                resultMap=PostRequestUtil.postSend(publicsjscurl,map,"");
                String result=resultMap.get("result")+"";
                System.out.println("result:"+result);
                JSONObject  resultjson = JSONObject.parseObject(result);
                String dwvscode=resultjson.get("result").toString();
                System.out.println("dwvscode:"+dwvscode);
                //密码 xhny201912100950
                String key= Sha256.getSHA256(tenantId+userId+passwd+dwvscode);
                System.out.println("key:"+key);
                map.put("method","login");
                map2.put("key",key);
                resultMap=PostRequestUtil.postSend(publicsjscurl,map,this.cookie);
                if(this.cookie.length()<1){
                    this.cookie=resultMap.get("cookie").toString();
                }
                System.out.println("人员用户重新登录成功");
            }
            Map csmap=new HashMap<>();

//            csmap.put("entityType",entityType);
            Map map=reMap(csmap,"getEntityTypes");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
//        System.out.print(resultMap.get("result").toString());
        return resultMap.get("result").toString();
    }


    /**
     * 获取实体列表
     * pujie
     * 2020-04-21
     *tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * entityType 实体类型
     * */
    @RequestMapping("xryzgzw/stList")
    @ResponseBody
    public String stList(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String entityType=request.getParameter("entityType");
        String sjscurl=request.getParameter("sjscurl");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        Map resultMap=new HashMap();
        try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            //先判断是否登录
            if(this.cookie==null||this.cookie.length()<1){
//                sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
                Map map=new HashMap<>();
                Map map2=new HashMap<>();
                map2.put("tenantId",tenantId);
                map2.put("userId",userId);
                map.put("jsonrpc","2.0");
                map.put("params",map2);
                map.put("method","getCode");
                map.put("id","1");
                resultMap=PostRequestUtil.postSend(publicsjscurl,map,"");
                String result=resultMap.get("result")+"";
                System.out.println("result:"+result);
                JSONObject  resultjson = JSONObject.parseObject(result);
                String dwvscode=resultjson.get("result").toString();
                System.out.println("dwvscode:"+dwvscode);
                //密码 xhny201912100950
                String key= Sha256.getSHA256(tenantId+userId+passwd+dwvscode);
                System.out.println("key:"+key);
                map.put("method","login");
                map2.put("key",key);
                resultMap=PostRequestUtil.postSend(publicsjscurl,map,this.cookie);
                if(this.cookie.length()<1){
                    this.cookie=resultMap.get("cookie").toString();
                }
                System.out.println("人员用户重新登录成功");
            }
            Map csmap=new HashMap<>();
            csmap.put("entityType",entityType);
            Map map=reMap(csmap,"getEntities");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap.get("result").toString();
    }

    //getRootAreas
    /**
     * 获取根地图列表
     * pujie
     * 2020-04-21
     *tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * entityType 实体类型
     * */
    @RequestMapping("xryzgzw/hqgdtlb")
    @ResponseBody
    public String hqgdtlb(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        String  engineId=request.getParameter("engineId");
        Map resultMap=new HashMap();
        try {
//            String sjscurl="http://dw.yz-cloud.com/position/public/service.smd";
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            //先判断是否登录
            if(this.cookie==null||this.cookie.length()<1){
//                sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
                Map map=new HashMap<>();
                Map map2=new HashMap<>();
                map2.put("tenantId",tenantId);
                map2.put("userId",userId);
                map.put("jsonrpc","2.0");
                map.put("params",map2);
                map.put("method","getCode");
                map.put("id","1");
                resultMap=PostRequestUtil.postSend(publicsjscurl,map,"");
                String result=resultMap.get("result")+"";
                System.out.println("result:"+result);
                JSONObject  resultjson = JSONObject.parseObject(result);
                String dwvscode=resultjson.get("result").toString();
                System.out.println("dwvscode:"+dwvscode);
                //密码 xhny201912100950
                String key= Sha256.getSHA256(tenantId+userId+passwd+dwvscode);
                System.out.println("key:"+key);
                map.put("method","login");
                map2.put("key",key);
                resultMap=PostRequestUtil.postSend(publicsjscurl,map,this.cookie);
                if(this.cookie.length()<1){
                    this.cookie=resultMap.get("cookie").toString();
                }
                System.out.println("人员用户重新登录成功");
            }

            Map csmap=new HashMap<>();
            csmap.put("engineId",engineId);
            Map map=reMap(csmap,"getRootAreas");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap.get("result").toString();
    }

    //getRootAreas
    /**
     * 获取定位区块列表
     * pujie
     * 2020-04-22
     *tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * areaId:  所属地图Id
     * */
    @RequestMapping("xryzgzw/dwqklist")
    @ResponseBody
    public String dwqklist(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        String areaIdString=request.getParameter("areaId");
        int areaId=Integer.parseInt(areaIdString);
        Map resultMap=new HashMap();
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
            Map csmap=new HashMap<>();
            csmap.put("areaId",areaId);
            Map map=reMap(csmap,"getLocationBlocks");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "resultmap:\n"+resultMap.get("result");
    }




    /**
     * 实时在线人数及区域统计
     *  author pujie
     * 2020-04-22
     * tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * areaId:  所属地图Id
     * */
    @RequestMapping("xryzgzw/sszxrsjqytj")
    @ResponseBody
    public String sszxrsjqytj(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        String areaIdString=request.getParameter("areaId");
        if(areaIdString==null||"".equals(areaIdString)){
            areaIdString="1";
        }
        int areaId=Integer.parseInt(areaIdString);

        Map resultMap=new HashMap();
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
            Map csmap=new HashMap<>();
            csmap.put("areaId",areaId);
            Map map=reMap(csmap,"summaryOnlineEntity");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap.get("result").toString();
    }


    /**
     * 获取电子围栏列表
     *author pujie
     * 2020-04-22
     * tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * areaId:  所属地图Id
     * type: 围栏类型(1~巡检区域 2~危险区域 3~普通区域)
     * */
    @RequestMapping("xryzgzw/hqdzwllb")
    @ResponseBody
    public String hqdzwllb(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        String areaIdString=request.getParameter("areaId");
        String type=request.getParameter("type");

        if(areaIdString==null||"".equals(areaIdString)){
            areaIdString="1";
        }
        int areaId=Integer.parseInt(areaIdString);

        Map resultMap=new HashMap();
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
            Map csmap=new HashMap<>();
            csmap.put("areaId",areaId);
            csmap.put("type",type);
            Map map=reMap(csmap,"getEnclosures");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap.get("result").toString();
    }

    /**
     * 获取电子围栏
     *getEnclosure
     *author pujie
     * 2020-04-22
     * tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * dzwlid 电子围栏ID
     * */
    @RequestMapping("xryzgzw/hqdzwl")
    @ResponseBody
    public String hqdzwl(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
        String dzwlid=request.getParameter("dzwlid");
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        Map resultMap=new HashMap();
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
            Map csmap=new HashMap<>();
            csmap.put("dzwlid",dzwlid);
            Map map=reMap(csmap,"getEnclosure");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap.get("result").toString();
    }

    /**
     *获取引擎定位配置信息（所有的地图信息）
     *getEngineConfig
     * author pujie
     * 2020-04-24
     * tenantId 租户id
     * userId 用户id
     * passwd 用户密码
     * sjscurl 请求路径 一般为http://dw.yz-cloud.com/position/service/embeded.smd
     * */
    @RequestMapping("xryzgzw/sydtxx")
    @ResponseBody
    public String sydtxx(HttpServletRequest request, HttpServletResponse response, Model model){
        String tenantId=request.getParameter("tenantId");
        String userId=request.getParameter("userId");
        String passwd=request.getParameter("passwd");
        String sjscurl=request.getParameter("sjscurl");
//        String areaIdString=request.getParameter("areaId");
//        int areaId=Integer.parseInt(areaIdString);
        String engineIdString=request.getParameter("engineId");
        int engineId=Integer.parseInt(engineIdString);
        String engineTypeMaskString =request.getParameter("engineTypeMask");
//        int engineTypeMask=Integer.parseInt(engineTypeMaskString);
        String publicsjscurl=sjscurl.replace("/service/","/public/");
        Map resultMap=new HashMap();
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
            Map csmap=new HashMap<>();
//            csmap.put("dzwlid",dzwlid);
//            csmap.put("areaId",areaId);
            csmap.put("engineId",engineId);
//            csmap.put("engineTypeMask",engineTypeMask);
            Map map=reMap(csmap,"getEngineConfig");
            resultMap= PostRequestUtil.postSend(sjscurl,map,this.cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap.get("result").toString();
    }








    public static  void main(String args[]){
        try {
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            String sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
            Map map=new HashMap<>();
            Map map2=new HashMap<>();
            map2.put("tenantId","sc19120011");
            map2.put("userId","xhny");
            map.put("jsonrpc","2.0");
            map.put("params",map2);
            map.put("method","getCode");
            map.put("id","1");
            Map resultMap=PostRequestUtil.postSend(sjscurl,map,"");
            String result=resultMap.get("result")+"";
            System.out.println("result:"+result);
            JSONObject resultjson = JSONObject.parseObject(result);
            String dwvscode=resultjson.get("result").toString();
            System.out.println("dwvscode:"+dwvscode);
            //密码 xhny201912100950
            String key= Sha256.getSHA256("sc19120011"+"xhny"+"xhny"+dwvscode);
            System.out.println("key:"+key);
            map.put("method","login");
            map2.put("key",key);
            resultMap=PostRequestUtil.postSend("http://dw.yz-cloud.com/position/public/embeded.smd",map,cookie);
            cookie=resultMap.get("cookie").toString();
            map.put("method","getUsers");
            resultMap=PostRequestUtil.postSend("http://dw.yz-cloud.com/position/service/embeded.smd",map,cookie);
            System.out.print("resultmap:"+resultMap.get("result"));
            Map map3=new HashMap<>();
//         map3.put("entityType","staff");
//        map.put("params",map3);
//        map.put("method","getEntities");id
//            map3.put("topic","Location");
//            map3.put("interval",3000);
//            map3.put("overtime",30000);
//            map3.put("id",2);
//            map3.put("url","http://58.214.244.98:8098/bis/xryzgzw");
//            map3.put("engineId",2);
//            map3.put("engineId",0);
            map3.put("engineId","a1");
            map.put("params",map3);

            map.put("method","getRootAreas");
//            map.put("method","subscribe");
            resultMap=PostRequestUtil.postSend("http://dw.yz-cloud.com/position/service/embeded.smd",map,cookie);
            System.out.print("resultmap:"+resultMap.get("result"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 返回传入map
     * */
    public Map reMap(Map map2,String method){
        Map map=new HashMap<>();
        map.put("jsonrpc","2.0");
        map.put("params",map2);
        map.put("method",method);
        map.put("id","1");
        return map;
    }
}

