package com.datech.zjfh.alarm.common.ivs;

import com.alibaba.fastjson.JSONObject;
import com.datech.zjfh.alarm.bean.IvsDeviceList;
import com.datech.zjfh.alarm.entity.BizCameraEntity;
import com.datech.zjfh.alarm.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.datech.zjfh.alarm.CreateSSLClientDefault.createSSLClientDefault;

/**
 *  查询子设备列表
 */
@Slf4j
public class GetSubDeviceList {
    /**
     * 通过httpget方式查询子设备列表
     */
    public static List<BizCameraEntity> getSubDeviceList(String host, String cookie) {

        // 请求路径
        String url = host + "/device/deviceList/v1.0";
        // 构建get请求参数，这里只填了必填项
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("deviceType", "2");
        paramsMap.put("fromIndex", "1");
        paramsMap.put("toIndex", "5000");
        url = HttpUtils.buildUrl(url, paramsMap);

        CloseableHttpClient httpclient = createSSLClientDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            //添加头文件
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Cache-Control", "no-cache");
            httpGet.addHeader("Cookie", cookie);

            //将请求发送给1800并获取返回结果
            response = httpclient.execute(httpGet);
            //获取返回结果并解析
            InputStream content = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
//            log.info("摄像头1800返回结果result:{}", result);
            IvsDeviceList ivsDeviceList = JSONObject.parseObject(result, IvsDeviceList.class);
            if (ivsDeviceList != null && ivsDeviceList.getCameraBriefInfos() != null) {
                List<BizCameraEntity> cameraList = ivsDeviceList.getCameraBriefInfos().getCameraBriefInfoList();
                return cameraList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                    httpclient.close();
                } catch (IOException e) {
                    System.err.println("释放连接错误");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /*public static String buildUrlAndParams(String host) {
        // 请求路径
        String getUrl = host + "/device/deviceList/v1.0";
        // 构建get请求参数，这里只填了必填项
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("deviceType", "2");
        paramsMap.put("fromIndex", "1");
        paramsMap.put("toIndex", "1000");
        getUrl = HttpUtils.buildUrl(getUrl, paramsMap);
        return getUrl;
    }*/

    /*public static void main(String[] args) {
        // 请求路径
        String url = Constants.IVS1800_SERVER_HOST + "/device/deviceList/v1.0";

        // 构建get请求参数，这里只填了必填项
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("deviceType", "2");
        paramsMap.put("fromIndex", "1");
        paramsMap.put("toIndex", "1000");
        url = HttpUtils.buildUrl(url, paramsMap);

        // 调用login接口获取
        String token = "JSESSIONID=EoL0UJRCkq8Mh3YF8LbOHQS5IAqtanMZ";
        // 发送请求
        String result = getSubDeviceList(url, token);
        System.out.println(result);
    }*/
}
