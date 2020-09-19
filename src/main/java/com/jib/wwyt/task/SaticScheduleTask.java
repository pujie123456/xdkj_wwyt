package com.jib.wwyt.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jib.wwyt.controller.XdkjRydwController;
import com.jib.wwyt.model.CzkCard;
import com.jib.wwyt.model.CzkLsjl;
import com.jib.wwyt.model.CzkWarn;
import com.jib.wwyt.service.CzkCardService;
import com.jib.wwyt.service.CzkLsjlService;
import com.jib.wwyt.service.CzkWarnService;
import com.jib.wwyt.service.edmService;
import com.jib.wwyt.utils.*;
import com.jib.wwyt.websocket.WebSocketUtil;
import com.jib.wwyt.websocket.WebSocketUtil2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;

import javax.xml.crypto.Data;

/**
 * @author: pujie
 * @date: 2020/6/19 15:50
 * @description:毛东方人员定位
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask{
//public class SaticScheduleTask  implements ApplicationContextAware {
    private static final Logger LOGGER = LogManager.getLogger(SaticScheduleTask.class);
    public static String cookie = "";
    public static Date dateflag = null;
    public static String id_flag = "";

    Dateutil Dateutil=new Dateutil();
    FileTool FileTool=new FileTool();
    //毛东方人员定位tenantId
    static String  tenantId_mdf= PropertyUtil.getProperty("tenantId");
    //毛东方人员定位url
    static String rydwurl_mdf= PropertyUtil.getProperty("rydwurl");
    //毛东方人员定位id
    static String userId_mdf= PropertyUtil.getProperty("userId");
    //毛东方人员定位密码
    static String passwd_mdf= PropertyUtil.getProperty("passwd");
    PostRequestUtil PostRequestUtil=new PostRequestUtil();
    Calendar c = new GregorianCalendar();
    @Scheduled(cron = "*/3 * * * * ?") //每3秒执行一次
//	@Scheduled(cron = "0 0/3 * * * ?")  //每3分钟执行一次
    public void job1() {
        ConcurrentHashMap<String, WebSocketUtil2> map = WebSocketUtil2.getWebSocketMap();
        if (map.size() != 0) {
            List resultList = new ArrayList();
            resultList = XdkjRydwController.list_mdf;
            for (Map.Entry<String, WebSocketUtil2> entry : map.entrySet()) {
                WebSocketUtil2 webSocketServer = entry.getValue();
                try {
                    //向客户端推送消息
//					System.out.print("count:"+count);
//					map_result.add(map_2);
//					int a=6-count;
//					String aaa="[{\"areaID\":2,\"dataTime\":1594619734517,\"empName\":\"杨进\",\"empNum\":\"41\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000041\",\"out\":false,\"xCross\":163.661,\"yCross\":193.741},{\"areaID\":23,\"dataTime\":1594619734519,\"empName\":\"吕明霞\",\"empNum\":\"49\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000049\",\"out\":false,\"xCross\":81.331,\"yCross\":287.236},{\"areaID\":4,\"dataTime\":1185850144,\"empName\":\"张道刚\",\"empNum\":\"10\",\"empType\":\"staff\",\"macAddress\":\"BTT20000010\",\"out\":false,\"xCross\":390.742,\"yCross\":302.963},{\"areaID\":37,\"dataTime\":1594619734517,\"empName\":\"承包商2\",\"empNum\":\"190\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000190\",\"out\":false,\"xCross\":58.677,\"yCross\":201.799},{\"areaID\":2,\"dataTime\":1186861540,\"empName\":\"宗建新\",\"empNum\":\"164\",\"empType\":\"staff\",\"macAddress\":\"BTT20000164\",\"out\":false,\"xCross\":89.428,\"yCross\":132.481},{\"areaID\":41,\"dataTime\":1186859606,\"empName\":\"龚益东\",\"empNum\":\"171\",\"empType\":\"staff\",\"macAddress\":\"BTT20000171\",\"out\":false,\"xCross\":333.441,\"yCross\":231.233},{\"areaID\":10,\"dataTime\":1594619734996,\"empName\":\"吴宗德\",\"empNum\":\"115\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000115\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":10,\"dataTime\":1594619734516,\"empName\":\"沙志豪\",\"empNum\":\"112\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000112\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":28,\"dataTime\":1594619734518,\"empName\":\"承包商12\",\"empNum\":\"200\",\"empType\":\"staff\",\"layer\":3,\"macAddress\":\"BTT20000200\",\"out\":false,\"xCross\":125.413,\"yCross\":324.944},{\"areaID\":4,\"dataTime\":1186759458,\"empName\":\"承包商4\",\"empNum\":\"192\",\"empType\":\"staff\",\"macAddress\":\"BTT20000192\",\"out\":false,\"xCross\":390.742,\"yCross\":302.963},{\"areaID\":41,\"dataTime\":1185632408,\"empName\":\"朱强\",\"empNum\":\"172\",\"empType\":\"staff\",\"macAddress\":\"BTT20000172\",\"out\":false,\"xCross\":333.441,\"yCross\":231.233},{\"areaID\":24,\"dataTime\":1185811624,\"empName\":\"俞伟\",\"empNum\":\"4\",\"empType\":\"staff\",\"macAddress\":\"BTT20000004\",\"out\":false,\"xCross\":128.338,\"yCross\":276.507},{\"areaID\":6,\"dataTime\":1594619734519,\"empName\":\"朱琴华\",\"empNum\":\"157\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000157\",\"out\":false,\"xCross\":358.441,\"yCross\":262.444},{\"areaID\":10,\"dataTime\":1594619734516,\"empName\":\"孙士财\",\"empNum\":\"114\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000114\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":10,\"dataTime\":1594619734516,\"empName\":\"丁利刚\",\"empNum\":\"113\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000113\",\"out\":false,\"xCross\":155.244,\"yCross\":213.173},{\"areaID\":10,\"dataTime\":1594619734996,\"empName\":\"平军利\",\"empNum\":\"99\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000099\",\"out\":false,\"xCross\":187.957,\"yCross\":215.861},{\"areaID\":6,\"dataTime\":1186777694,\"empName\":\"周美琴\",\"empNum\":\"145\",\"empType\":\"staff\",\"macAddress\":\"BTT20000145\",\"out\":false,\"xCross\":344.459,\"yCross\":261.883},{\"areaID\":23,\"dataTime\":1594619734519,\"empName\":\"王孝远\",\"empNum\":\"74\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000074\",\"out\":false,\"xCross\":81.173,\"yCross\":293.319},{\"areaID\":2,\"dataTime\":1594619734517,\"empName\":\"曹叶东\",\"empNum\":\"84\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000084\",\"out\":false,\"xCross\":46.266,\"yCross\":379.02},{\"areaID\":4,\"dataTime\":1186700132,\"empName\":\"顾璟达\",\"empNum\":\"35\",\"empType\":\"staff\",\"macAddress\":\"BTT20000035\",\"out\":false,\"xCross\":380.225,\"yCross\":291.647},{\"areaID\":10,\"dataTime\":1186861883,\"empName\":\"肖岭峰\",\"empNum\":\"98\",\"empType\":\"staff\",\"macAddress\":\"BTT20000098\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":4,\"dataTime\":1594619734518,\"empName\":\"承包商5\",\"empNum\":\"193\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000193\",\"out\":false,\"xCross\":385.309,\"yCross\":303.43},{\"areaID\":10,\"dataTime\":1594619734996,\"empName\":\"丁国健\",\"empNum\":\"104\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000104\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":10,\"dataTime\":1594619734520,\"empName\":\"金健\",\"empNum\":\"103\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000103\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":43,\"dataTime\":1594619734519,\"empName\":\"陆英\",\"empNum\":\"131\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000131\",\"out\":false,\"xCross\":154.429,\"yCross\":39.771},{\"areaID\":37,\"dataTime\":1594619734518,\"empName\":\"承包商1\",\"empNum\":\"189\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000189\",\"out\":false,\"xCross\":57.812,\"yCross\":201.8},{\"areaID\":28,\"dataTime\":1594619734519,\"empName\":\"承包商11\",\"empNum\":\"199\",\"empType\":\"staff\",\"layer\":3,\"macAddress\":\"BTT20000199\",\"out\":false,\"xCross\":125.413,\"yCross\":324.944},{\"areaID\":7,\"dataTime\":1186843368,\"empName\":\"戴飞\",\"empNum\":\"88\",\"empType\":\"staff\",\"macAddress\":\"BTT20000088\",\"out\":false,\"xCross\":281.351,\"yCross\":271.019},{\"areaID\":11,\"dataTime\":1594619734521,\"empName\":\"吴俊刚\",\"empNum\":\"101\",\"empType\":\"staff\",\"layer\":3,\"macAddress\":\"BTT20000101\",\"out\":false,\"xCross\":175.709,\"yCross\":214.246},{\"areaID\":10,\"dataTime\":1185727740,\"empName\":\"陆进强\",\"empNum\":\"106\",\"empType\":\"staff\",\"macAddress\":\"BTT20000106\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":2,\"dataTime\":1594619734517,\"empName\":\"蔡子川\",\"empNum\":\"94\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000094\",\"out\":false,\"xCross\":180.773,\"yCross\":241.224},{\"areaID\":10,\"dataTime\":1186802050,\"empName\":\"陈东明\",\"empNum\":\"108\",\"empType\":\"staff\",\"macAddress\":\"BTT20000108\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":10,\"dataTime\":1186810959,\"empName\":\"杨山林\",\"empNum\":\"107\",\"empType\":\"staff\",\"macAddress\":\"BTT20000107\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":10,\"dataTime\":1594619734518,\"empName\":\"唐龙标\",\"empNum\":\"100\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000100\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":37,\"dataTime\":1594619734519,\"empName\":\"李卢敏\",\"empNum\":\"92\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000092\",\"out\":false,\"xCross\":54.921,\"yCross\":201.802},{\"areaID\":2,\"dataTime\":1594619734519,\"empName\":\"赵友桃\",\"empNum\":\"95\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000095\",\"out\":false,\"xCross\":160.168,\"yCross\":195.545},{\"areaID\":2,\"dataTime\":1594619734518,\"empName\":\"施鸣东\",\"empNum\":\"65\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000065\",\"out\":false,\"xCross\":99.02,\"yCross\":324.98},{\"areaID\":23,\"dataTime\":1594619734520,\"empName\":\"王文清\",\"empNum\":\"51\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000051\",\"out\":false,\"xCross\":118.482,\"yCross\":308.754},{\"areaID\":4,\"dataTime\":1186797974,\"empName\":\"邵建江\",\"empNum\":\"55\",\"empType\":\"staff\",\"macAddress\":\"BTT20000055\",\"out\":false,\"xCross\":402.113,\"yCross\":310.73},{\"areaID\":10,\"dataTime\":1186747280,\"empName\":\"陶俊杰\",\"empNum\":\"44\",\"empType\":\"staff\",\"macAddress\":\"BTT20000044\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":2,\"dataTime\":1594619734520,\"empName\":\"缪雪峰\",\"empNum\":\"13\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000013\",\"out\":false,\"xCross\":441.266,\"yCross\":348.037},{\"areaID\":2,\"dataTime\":1186337043,\"empName\":\"孙连云\",\"empNum\":\"14\",\"empType\":\"staff\",\"macAddress\":\"BTT20000014\",\"out\":false,\"xCross\":414.373,\"yCross\":348.035},{\"areaID\":24,\"dataTime\":1186637461,\"empName\":\"李科\",\"empNum\":\"23\",\"empType\":\"staff\",\"macAddress\":\"BTT20000023\",\"out\":false,\"xCross\":115.356,\"yCross\":276.231},{\"areaID\":10,\"dataTime\":1186860942,\"empName\":\"沈东\",\"empNum\":\"118\",\"empType\":\"staff\",\"macAddress\":\"BTT20000118\",\"out\":false,\"xCross\":164.367,\"yCross\":216.653},{\"areaID\":10,\"dataTime\":1594619734518,\"empName\":\"顾景锋\",\"empNum\":\"117\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000117\",\"out\":false,\"xCross\":230.988,\"yCross\":195.21},{\"areaID\":10,\"dataTime\":1186781239,\"empName\":\"章奇\",\"empNum\":\"110\",\"empType\":\"staff\",\"macAddress\":\"BTT20000110\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":24,\"dataTime\":1594619734517,\"empName\":\"支叶锋\",\"empNum\":\"21\",\"empType\":\"staff\",\"layer\":3,\"macAddress\":\"BTT20000021\",\"out\":false,\"xCross\":126.957,\"yCross\":295.219},{\"areaID\":10,\"dataTime\":1186442661,\"empName\":\"陈和全\",\"empNum\":\"29\",\"empType\":\"staff\",\"macAddress\":\"BTT20000029\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":41,\"dataTime\":1185965716,\"empName\":\"钱君明\",\"empNum\":\"168\",\"empType\":\"staff\",\"macAddress\":\"BTT20000168\",\"out\":false,\"xCross\":333.441,\"yCross\":231.233},{\"areaID\":7,\"dataTime\":1594619734520,\"empName\":\"周月花\",\"empNum\":\"147\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000147\",\"out\":false,\"xCross\":287.394,\"yCross\":285.6},{\"areaID\":6,\"dataTime\":1186777008,\"empName\":\"陈梅琴\",\"empNum\":\"160\",\"empType\":\"staff\",\"macAddress\":\"BTT20000160\",\"out\":false,\"xCross\":358.441,\"yCross\":262.444},{\"areaID\":23,\"dataTime\":1594619734518,\"empName\":\"邵进艳\",\"empNum\":\"72\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000072\",\"out\":false,\"xCross\":81.217,\"yCross\":289.517},{\"areaID\":36,\"dataTime\":1186688309,\"empName\":\"徐丽忠\",\"empNum\":\"78\",\"empType\":\"staff\",\"macAddress\":\"BTT20000078\",\"out\":false,\"xCross\":79.317,\"yCross\":327.94},{\"areaID\":24,\"dataTime\":1186694433,\"empName\":\"杨凯峰\",\"empNum\":\"77\",\"empType\":\"staff\",\"macAddress\":\"BTT20000077\",\"out\":false,\"xCross\":126.957,\"yCross\":295.219},{\"areaID\":4,\"dataTime\":1186661886,\"empName\":\"陆连妹\",\"empNum\":\"144\",\"empType\":\"staff\",\"macAddress\":\"BTT20000144\",\"out\":false,\"xCross\":390.742,\"yCross\":302.963},{\"areaID\":23,\"dataTime\":1594619734520,\"empName\":\"顾进良\",\"empNum\":\"143\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000143\",\"out\":false,\"xCross\":118.12,\"yCross\":305.985},{\"areaID\":4,\"dataTime\":1186851386,\"empName\":\"金丹\",\"empNum\":\"25\",\"empType\":\"staff\",\"macAddress\":\"BTT20000025\",\"out\":false,\"xCross\":380.225,\"yCross\":291.647},{\"areaID\":40,\"dataTime\":1186853509,\"empName\":\"张慧芬\",\"empNum\":\"146\",\"empType\":\"staff\",\"macAddress\":\"BTT20000146\",\"out\":false,\"xCross\":280.45,\"yCross\":108.006},{\"areaID\":10,\"dataTime\":1594619734516,\"empName\":\"赵国兴\",\"empNum\":\"30\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000030\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":7,\"dataTime\":1186853465,\"empName\":\"徐麟\",\"empNum\":\"122\",\"empType\":\"staff\",\"macAddress\":\"BTT20000122\",\"out\":false,\"xCross\":287.288,\"yCross\":283.647},{\"areaID\":23,\"dataTime\":1594619734519,\"empName\":\"朱琳\",\"empNum\":\"85\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000085\",\"out\":false,\"xCross\":97.729,\"yCross\":272.836},{\"areaID\":7,\"dataTime\":1186827177,\"empName\":\"姚作炬\",\"empNum\":\"1\",\"empType\":\"staff\",\"macAddress\":\"BTT20000001\",\"out\":false,\"xCross\":287.288,\"yCross\":283.647},{\"areaID\":4,\"dataTime\":1186860763,\"empName\":\"俞苏\",\"empNum\":\"9\",\"empType\":\"staff\",\"macAddress\":\"BTT20000009\",\"out\":false,\"xCross\":380.225,\"yCross\":291.647},{\"areaID\":7,\"dataTime\":1186450803,\"empName\":\"张伟冯\",\"empNum\":\"42\",\"empType\":\"staff\",\"macAddress\":\"BTT20000042\",\"out\":false,\"xCross\":287.288,\"yCross\":283.647},{\"areaID\":4,\"dataTime\":1186849429,\"empName\":\"朱怡君\",\"empNum\":\"39\",\"empType\":\"staff\",\"macAddress\":\"BTT20000039\",\"out\":false,\"xCross\":380.225,\"yCross\":291.647},{\"areaID\":7,\"dataTime\":1186839314,\"empName\":\"钟富光\",\"empNum\":\"36\",\"empType\":\"staff\",\"macAddress\":\"BTT20000036\",\"out\":false,\"xCross\":287.288,\"yCross\":283.647},{\"areaID\":2,\"dataTime\":1594619734518,\"empName\":\"李欣峰\",\"empNum\":\"89\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000089\",\"out\":false,\"xCross\":154.173,\"yCross\":191.315},{\"areaID\":23,\"dataTime\":1186680211,\"empName\":\"顾宝萍\",\"empNum\":\"50\",\"empType\":\"staff\",\"macAddress\":\"BTT20000050\",\"out\":false,\"xCross\":84.297,\"yCross\":292.477},{\"areaID\":10,\"dataTime\":1594619734518,\"empName\":\"钱冬华\",\"empNum\":\"119\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000119\",\"out\":false,\"xCross\":161.973,\"yCross\":208.501},{\"areaID\":10,\"dataTime\":1186208003,\"empName\":\"钱学文\",\"empNum\":\"116\",\"empType\":\"staff\",\"macAddress\":\"BTT20000116\",\"out\":false,\"xCross\":164.367,\"yCross\":216.653},{\"areaID\":10,\"dataTime\":1594619734520,\"empName\":\"吴科\",\"empNum\":\"111\",\"empType\":\"staff\",\"layer\":2,\"macAddress\":\"BTT20000111\",\"out\":false,\"xCross\":230.988,\"yCross\":195.21},{\"areaID\":7,\"dataTime\":1186865233,\"empName\":\"瞿桂锋\",\"empNum\":\"12\",\"empType\":\"staff\",\"macAddress\":\"BTT20000012\",\"out\":false,\"xCross\":287.288,\"yCross\":283.647},{\"areaID\":2,\"dataTime\":1594619734517,\"empName\":\"王莉\",\"empNum\":\"33\",\"empType\":\"staff\",\"layer\":1,\"macAddress\":\"BTT20000033\",\"out\":false,\"xCross\":398.449,\"yCross\":265.02}]\n";
//					webSocketServer.sendMessageAll(aaa);\

                    webSocketServer.sendMessageAll( JSONObject.toJSONString(resultList, SerializerFeature.WriteNullStringAsEmpty));
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    public void resubscribe2(){
        try {
            String tenantId2=PropertyUtil.getProperty("tenantId");
            String userId2=PropertyUtil.getProperty("userId");
            String passwd2=PropertyUtil.getProperty("passwd");
            String rydyjklj2=PropertyUtil.getProperty("rydwurl");
            String rydy=rydyjklj2.replace("service","public");
            PostRequestUtil PostRequestUtil=new PostRequestUtil();
            String sjscurl=rydy;
            Map map=new HashMap<>();
            Map map2=new HashMap<>();
            map2.put("tenantId",tenantId2);
            map2.put("userId",userId2);
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
            String key= Sha256.getSHA256(tenantId2+userId2+passwd2+dwvscode);
            System.out.println("key:"+key);
            map.put("method","login");
            map2.put("key",key);
            resultMap=PostRequestUtil.postSend(rydy,map,"");
            if(resultMap.get("cookie")==null){
                String cookie2=resultMap.get("cookie").toString();
                map.put("method","getUsers");
                resultMap=PostRequestUtil.postSend(rydyjklj2,map,cookie2);
                System.out.print("resultmap:"+resultMap.get("result"));
                Map map3=new HashMap<>();
//         map3.put("entityType","staff");
//        map.put("params",map3);
//        map.put("method","getEntities");id
//        int[] intarr={-1,1};
//        map3.put("triggerIds",intarr);
//        map3.put("startTime",1590681600000l);
//        map3.put("endTime",1591083852000l);
//       map3.put("id","2");

                map.put("method","unsubscribeAll");
                resultMap=PostRequestUtil.postSend(rydyjklj2,map,cookie2);
                map3.put("interval",3000);
                map3.put("overtime",30000);
                map3.put("id",2);
//            map3.put("warnType",1);
//            map3.put("areaIds",0);
                map3.put("topic","Location");
//            map3.put("engineId","a1");
                map.put("params",map3);
//        map.put("method","getTopics");
                map.put("method","subscribe");
//      map.put("method","getWarnTriggers");
//      map.put("method","subscribe");
                resultMap=PostRequestUtil.postSend(rydyjklj2,map,cookie2);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 毛东方人员定位每10分钟判断是否订阅
     *
     * */
    @Scheduled(cron = "0 0/5 * * * ?") //每5分钟执行一次
//	@Scheduled(cron = "0 0/3 * * * ?")  //每3分钟执行一次
    public void job5() {

            long timeflag=System.currentTimeMillis()-XdkjRydwController.timeflag_mdf;
            if(XdkjRydwController.timeflag_mdf==0||timeflag>600000){
                System.out.println("毛东方重新数据订阅定位");
                resubscribe2();
                System.out.println("毛东方重新数据订阅定位成功");
            }
    }
}






