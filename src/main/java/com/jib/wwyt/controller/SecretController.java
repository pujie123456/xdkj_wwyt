package com.jib.wwyt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;


/**
 * @author: pujie
 * @date: 2020/6/19 9:43
 * @description: 密码转换
 */
@RestController
@RequestMapping("/xdkj")
public class SecretController {public static void main(String[] args) {
    //密钥


}
    public static byte[] decryptBASE64(String key) { return Base64.decodeBase64(key); }
    public static String encryptBASE64(byte[] key) { return Base64.encodeBase64String(key); }

    //根据公司编码返回车辆实时定位(可加卡号)
    @RequestMapping("/getsecret")
    @ResponseBody
    public String selectcardbycompanynum(HttpServletRequest request, HttpServletResponse response, Model model) {
        String src=request.getParameter("src");
        String key=request.getParameter("key");

        try{

            // 获取HTTP请求的输入流
            // 已HTTP请求输入流建立一个BufferedReader对象
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), "UTF-8"));
            String buffer = null;
            // 存放请求内容
            StringBuffer xml = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                // 在页面中显示读取到的请求参数
                xml.append(buffer);
            }

            String data = new String(xml);
            JSONObject json= JSONObject.parseObject(data);
            src=json.getString("src");
            key=json.getString("key");

        }catch (Exception e){
            e.printStackTrace();
        }
//        String key =
//                "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCDx6YYtYKaQfSfCykj89F6akwVk+BjGCS6MY7Afcjv2Lg==";
//        String src=map.get("src").toString();
//
//        String key=map.get("key").toString();

        byte[] keyByte = decryptBASE64(key);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyByte);

        KeyFactory keyFactory = null;
        String result="加密失败";
        try {
            keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] res = signature.sign();
//            System.out.println("签名：" + encryptBASE64(res));
            result=encryptBASE64(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
