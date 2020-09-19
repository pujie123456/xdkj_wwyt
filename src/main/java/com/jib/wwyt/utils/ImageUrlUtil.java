package com.jib.wwyt.utils;


import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* *
 * 使用正则截取请求头
 * author
 * */
public class ImageUrlUtil {
    public String getImage(String requestURL) {
        Pattern pattern = Pattern.compile(".*\\:[0-9]{4}");
        Matcher m = pattern.matcher(requestURL);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            return m.group(0);
        } else {
            return "error";
        }
    }


    /**
     * base64转file
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }


    /**
     * 生成文件名
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}