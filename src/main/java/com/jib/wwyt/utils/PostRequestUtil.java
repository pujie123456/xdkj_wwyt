package com.jib.wwyt.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author: Administrator
 * @date: 2020/6/19 9:40
 * @description:
 */
public class PostRequestUtil {
    private static Logger logger = LoggerFactory.getLogger(PostRequestUtil.class);
    public Map postSend(String sjscurl, Map map, String cookie){
        //请求路径
//        String sjscurl="http://dw.yz-cloud.com/position/public/embeded.smd";
        HttpPost post = new HttpPost(sjscurl);
        System.out.println("post开始请求:");
        String result="";
//        Map map=new HashMap<>();
//        Map map2=new HashMap<>();
//        map2.put("tenantId","sc19120011");
//        map2.put("userId","xhny");
//        map.put("jsonrpc","2.0");
//        map.put("params",map2);
//        map.put("method","getCode");
//        map.put("id","1");
        String data= JSONObject.toJSONString(map);
        System.out.println("postdata:"+data);
        Map resultmap=new HashMap();
        CloseableHttpClient httpClient=null;
        httpClient = HttpClients.createDefault();
        try {
            post.setHeader("Content-Type","application/json;charset=utf-8");
            StringEntity postingString = new StringEntity(data,"utf-8");
            postingString.setContentType("application/x-www-form-urlencoded");
            if(cookie!=null &&cookie.length()>0){
                post.addHeader("Cookie", cookie);
            }
            post.setEntity(postingString);
            HttpResponse response1 = httpClient.execute(post);
            Header[] ssoResponseHeader = response1.getHeaders("Set-Cookie");
            for(int i=0;i<ssoResponseHeader.length;i++){
                String a=ssoResponseHeader[i].toString();
                if(a.contains("Set-Cookie")){
                    resultmap.put("cookie",a.substring(0,a.indexOf(";")).replace("Set-Cookie:",""));
                }
            }
            InputStream in = response1.getEntity().getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber= new StringBuilder();
            String line = null;
            while((line = br.readLine())!=null){
                strber.append(line+'\n');
            }
            br.close();
            in.close();

            result = strber.toString();
            if(response1.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                result = "服务器异常";
            }
        }catch (Exception e){
            System.out.println("请求异常");
            throw new RuntimeException(e);
        } finally{
            post.abort();
            try {
                httpClient.close();
                post.releaseConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.print("postresul:"+result);
        resultmap.put("result",result);
        return  resultmap;
    }

    public String  postSendNoCookie(String sjscurl, Map map){
        // 创建连接池
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        // 声明呀一个字符串用来存储response
        String result;
        String data= JSONObject.toJSONString(map);
        // 创建httppost对象
        HttpPost httpPost = new HttpPost(sjscurl);
        // 给httppost对象设置json格式的参数
        StringEntity httpEntity = new StringEntity(data,"utf-8");
        // 设置请求格式
        httpPost.setHeader("Content-type","application/json");
        // 传参
        httpPost.setEntity(httpEntity);

        // 发送请求，并获取返回值
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            // 将response转成String并存储在result中
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "error";
    }

    public static String doGet(String url) {
        String strResult = "";
        // 1. 创建一个默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            // 2. 创建一个httpget对象
            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing GET request " + httpGet.getURI());

            // 3. 执行GET请求并获取响应对象
            CloseableHttpResponse resp = client.execute(httpGet);
            try {
                // 6. 打印响应长度和响应内容
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // 4. 获取响应体
                    HttpEntity entity = resp.getEntity();
                    System.out.println("Response content length = "
                            + entity.getContentLength());
                    System.out.println("------");
                    strResult = EntityUtils.toString(resp.getEntity());
                }
            } finally {
                //无论请求成功与否都要关闭resp
                resp.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("get请求失败:", e);
            // e.printStackTrace();
        } catch (ParseException e) {
            logger.error("get请求解析出错:", e);
            // e.printStackTrace();
        } catch (IOException e) {
            logger.error("get请求IO出错:", e);
            // e.printStackTrace();
        } finally {
            // 8. 最终要关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                logger.error("get请求完毕关闭连接出错:", e);
                // e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * 普通POST提交
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map<String, Object> map) {
        String strResult = "";
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        // 3. 创建参数队列（键值对列表）
        List<NameValuePair> paramPairs = new ArrayList<>();
        Set<String> keySet = map.keySet();


        for (String key : keySet) {
            Object val = map.get(key);
            paramPairs.add(new BasicNameValuePair(key, val.toString()));
        }
        UrlEncodedFormEntity entity;
        try {
            // 4. 将参数设置到entity对象中
            entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            // 5. 将entity对象设置到httppost对象中
            httpPost.setEntity(entity);
            // 6. 发送请求并回去响应
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                // 9. 关闭响应对象
                resp.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("post请求失败:", e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            logger.error("post请求解析出错:", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("get请求IO出错:", e);
            e.printStackTrace();
        } finally {
            // 10. 关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * json参数方式POST提交
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, JSONObject params){
        String strResult = "";
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8"); //添加请求头
        try {
            httpPost.setEntity(new StringEntity(params.toJSONString(),"utf-8"));
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                resp.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

//    //get请求
//    public String doGet(String urlString){
//        try {
//            URL url = new URL(urlString);// 字符串转成请求地址
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
//            connection.connect();// 连接会话
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 响应结果为输入流
//            String line;
//            StringBuilder sb = new StringBuilder();// 输出的结果
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            br.close();
//            connection.disconnect();// 断开连接
////            System.out.println(sb.toString());
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("请求失败 :" + e.getMessage());
//        }
//        return  "请求失败";
//    }
}
