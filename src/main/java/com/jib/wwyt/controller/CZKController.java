package com.jib.wwyt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jib.wwyt.model.CzkCard;
import com.jib.wwyt.model.CzkLsjl;
import com.jib.wwyt.model.CzkUser;
import com.jib.wwyt.service.CzkCardService;
import com.jib.wwyt.service.CzkLsjlService;
import com.jib.wwyt.service.CzkUserService;
import com.jib.wwyt.service.CzkWarnService;
import com.jib.wwyt.utils.Dateutil;
import com.jib.wwyt.utils.PostRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author: pujie
 * @date: 2020/6/19 9:43
 * @description: 车载卡controller
 */
@RestController
@RequestMapping("/czk")
public class CZKController {

    @Autowired
    private CzkCardService CzkCardService;
    @Autowired
    private CzkLsjlService CzkLsjlService;
    @Autowired
    private CzkWarnService CzkWarnService;
    @Autowired
    private CzkUserService CzkUserService;


    //根据公司编码返回车辆实时定位(可加卡号)
    @RequestMapping("/findbycompanynum")
    @ResponseBody
    public String selectcardbycompanynum(HttpServletRequest request, HttpServletResponse response, Model model){
        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap.put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        String devicenum=request.getParameter("devicenum");
        Map map=new HashMap();
        map.put("companynum",companynum);
        map.put("devicenum",devicenum);
        List<CzkCard>  list  =CzkCardService.selectbydevicenum(map);
        PostRequestUtil PostRequestUtil = new PostRequestUtil();
        List listresult=new ArrayList();
        //循环遍历卡号
        for(CzkCard CzkCard:list){
            String devicenumCzkCard=CzkCard.getDevicenum();//获取卡号
            int speedlimit=CzkCard.getSpeedlimit();
            String url=CzkCard.getUrlandport()+"?VNO="+devicenumCzkCard;//获取url
            String cldwString=PostRequestUtil.doGet(url);
            cldwString=cldwString.substring(cldwString.indexOf("["),cldwString.indexOf("]")+1);
            JSONArray cldwarr=JSONArray.parseArray(cldwString);
            JSONObject cldw=null;
            for(int i=0;i<cldwarr.size();i++){
                CzkLsjl CzkLsjl=new CzkLsjl();
                cldw=cldwarr.getJSONObject(i);
                //判断是否超速
                if( cldw.getDouble("Speed")>speedlimit){
                    System.out.print("超速");
                    cldw.put("alarm","超速报警");
                    CzkLsjl.setAlarm(cldw.getString("alarm"));
                }
                CzkLsjl.setDevicenum(devicenumCzkCard);
//                CzkLsjl.setDirection(cldw.getString("direction"));
//                CzkLsjl.setElectricity(cldw.getString("electricity"));
                CzkLsjl.setElectricity("");
//                CzkLsjl.setHorizontalfactor(cldw.getString("horizontalFactor"));
                Dateutil Dateutil=new Dateutil();
                CzkLsjl.setSendtime(Dateutil.dateToTimestamp(cldw.getString("GPSREV_TIME").replace("/","-")));
                CzkLsjl.setVelocity(cldw.getString("Speed"));
//                CzkLsjl.setVoltage(cldw.getString("voltage"));
//                CzkLsjl.setXCross(cldw.getString("Longitude"));
//                CzkLsjl.setYCross(cldw.getString("Latitude"));
                CzkLsjl.setXCross(cldw.getString("Longitude"));
                CzkLsjl.setYCross(cldw.getString("Latitude"));
                CzkLsjl.setCarname(CzkCard.getCarname());
                listresult.add(CzkLsjl);
            }
        }
        Map resultmap=new HashMap();
        resultmap.put("data",listresult);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 查询车辆历史接口
     *
     * */
    @RequestMapping("/findHistory")
    @ResponseBody
    public String findHistory(HttpServletRequest request, HttpServletResponse response, Model model){
        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap   .put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        String starttime=request.getParameter("starttime");
        if(starttime==null){
                Map resultmap=new HashMap();
                resultmap.put("retCode","401");
                resultmap.put("msg","请求失败,未获取到开始时间");
                resultmap.put("success","false");
              return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        String endtime=request.getParameter("endtime");
        String devicenum=request.getParameter("devicenum");

        Map conditionMap=new HashMap();
        conditionMap.put("starttime",starttime);
        conditionMap.put("endtime",endtime);
        conditionMap.put("companynum",companynum);
        conditionMap.put("devicenum",devicenum);
        List<Map> resultlist=CzkLsjlService.findhistorybytime(conditionMap);


        Map resultmap=new HashMap();
        resultmap.put("data",resultlist);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);

    }


    /**
     * 查询公司下所有卡号
     *author pujie
     * 2020-06-22
     * */
    @RequestMapping("/findcardbycompanynum")
    @ResponseBody
    public String findcardbycompanynum(HttpServletRequest request, HttpServletResponse response, Model model){
        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap   .put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        String devicenum=request.getParameter("devicenum");
        Map map=new HashMap();
        map.put("companynum",companynum);
        map.put("devicenum",devicenum);
        List<Map> resultList=CzkCardService.findcardbycompanynum(map);
        Map resultmap=new HashMap();
        resultmap.put("data",resultList);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }


    /**
     * 最新报警接口
     * author pujie
     * 2020-06-22
     * */
    @RequestMapping("/findwarn")
    @ResponseBody
    public String findwarn(HttpServletRequest request, HttpServletResponse response, Model model){
        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap.put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }

         Map map=new HashMap();
        map.put("companynum",companynum);
        String devicenum=request.getParameter("devicenum");
        map.put("devicenum",devicenum);

        List<Map> resultList=CzkWarnService.findbycompany(map);
        Map resultmap=new HashMap();
        resultmap.put("data",resultList);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 修改车辆卡信息
     * author pujie
     * 2020-06-28
     * */
    @RequestMapping("/updatecard")
    @ResponseBody
    public String updatecard(HttpServletRequest request, HttpServletResponse response, Model model){
        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap   .put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        Map map=new HashMap();
        map.put("companynum",companynum);
        String devicenum=request.getParameter("devicenum");
        map.put("devicenum",devicenum);
        String phonenum=request.getParameter("phonenum");//获取手机号
        map.put("phonenum",phonenum);
        String carname=request.getParameter("carname");//获取手机号
        map.put("carname",carname);//获取车辆名称
        String speedlimit=request.getParameter("speedlimit");//获取手机号
        map.put("speedlimit",speedlimit);//获取超速限制
        CzkCardService.updatecard(map);
        Map resultmap=new HashMap();

        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 根据企业编号获取车载卡详细信息
     * author pujie
     * 2020-06-28
     * */
    @RequestMapping("/findallcardlist")
    @ResponseBody
    public String findallcardlist(HttpServletRequest request, HttpServletResponse response, Model model){
        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap   .put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        Map map=new HashMap();
        map.put("companynum",companynum);
        String devicenum=request.getParameter("devicenum");
        map.put("devicenum",devicenum);
        List<Map> resultList=CzkCardService.findallcardlist(map);
        Map resultmap=new HashMap();
        resultmap.put("data",resultList);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 根据来源的历史记录进行查看
     * author pujie
     * 2020-06-28
     * */
    @RequestMapping("/findhistorybyly")
    @ResponseBody
    public String findhistorybyly(HttpServletRequest request, HttpServletResponse response, Model model){

        String companynum=request.getParameter("companynum");
        if(companynum==null){
            Map resultmap=new HashMap();
            resultmap.put("retCode","401");
            resultmap.put("msg","请求失败,未获取到公司编码");
            resultmap.put("success","false");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        String devicenum=request.getParameter("devicenum");
        String starttime=request.getParameter("starttime");
        String endtime=request.getParameter("endtime");
        Map map=new HashMap();
        map.put("companynum",companynum);
        map.put("devicenum",devicenum);
        List<CzkCard>  list  =CzkCardService.selectbydevicenum(map);
        PostRequestUtil PostRequestUtil = new PostRequestUtil();
        List listresult=new ArrayList();
        //循环遍历卡号
        for(CzkCard CzkCard:list) {
            String devicenumCzkCard = CzkCard.getDevicenum();//获取卡号
//            String url = CzkCard.getUrlandport().replace("DataGpsAPI","DataHistoryAPI") + "?No=" + devicenumCzkCard+"|"+starttime+"|"+endtime;//获取url
            String url = CzkCard.getUrlandport().replace("DataGpsAPI","DataHistoryAPI")   ;//获取url
            Map cmap=new HashMap();
            cmap.put("NO",devicenumCzkCard+"|"+starttime+"|"+endtime);
            String cldwString = PostRequestUtil.doPost(url,cmap);
            cldwString = cldwString.substring(cldwString.indexOf("["), cldwString.indexOf("]") + 1);
            JSONArray cldwarr = JSONArray.parseArray(cldwString);
            JSONObject cldw = null;
            for(int i=0;i<cldwarr.size();i++){
                cldw=cldwarr.getJSONObject(i);
                Map lmap=new HashMap();
                lmap.put("xCross",cldw.getString("Longitude"));
                lmap.put("yCross",cldw.getString("Latitude"));
                listresult.add(lmap);
            }

        }
        Map resultmap=new HashMap();
        resultmap.put("data",listresult);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 用户登录
     * author pujie
     * 2020-06-28
     * */
    @RequestMapping("/userlogin")
    @ResponseBody
    public String userlogin(HttpServletRequest request, HttpServletResponse response, Model model){
        String username=request.getParameter("username");
        String passwd=request.getParameter("passwd");
       String passwdmd5=DigestUtils.md5DigestAsHex(passwd.getBytes());
        Map cmap=new HashMap();
        cmap.put("passwd",passwdmd5);
        cmap.put("username",username);
        List<Map> list=CzkUserService.userlogin(cmap);
        if(list==null||list.size()==0){
            Map resultmap=new HashMap();
            resultmap.put("data","用户名或密码不正确");
            resultmap.put("retCode","200");
            resultmap.put("msg","请求成功");
            resultmap.put("success","true");
            return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
        }
        Map resultmap=new HashMap();
        resultmap.put("data",list);
        resultmap.put("retCode","200");
        resultmap.put("msg","请求成功");
        resultmap.put("success","true");
        return JSONObject.toJSONString(resultmap,SerializerFeature.WriteNullStringAsEmpty);
    }




    //测试用main函数
    public static  void main(String args[]){
        PostRequestUtil PostRequestUtil = new PostRequestUtil();

        Map map =new HashMap();
      map.put("appid","testdemo");
      map.put("time", Calendar.getInstance().getTimeInMillis());
      map.put("signature",md5Digest("rHW3agj3$uc#8vPB7qPkl8K#exdaf&bY"+ Calendar.getInstance().getTimeInMillis()));

        String aaa=PostRequestUtil.doPost("http://open.gumigps.com/api/auth",map);

        System.out.println(aaa);
    }


    public static String md5Digest(String paramString){
        MessageDigest localMessageDigest;
        StringBuilder localStringBuilder = new StringBuilder();
        byte[] arrayOfByte =null;
        try {
            localMessageDigest = MessageDigest.getInstance("MD5");
            arrayOfByte = localMessageDigest.digest(paramString.getBytes("utf-8"));
            int j = arrayOfByte.length;
            for (int i = 0; i < j; i++)
            {
                String k = Integer.toHexString(arrayOfByte[i] & 0xFF);
                if (k.length() == 1) {
                    localStringBuilder.append("0");
                }
                localStringBuilder.append(k);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return localStringBuilder.toString();
    }

}
