package com.sky.utils;


/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/08 21:31
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.BaiduMapProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 百度地图操作工具类
 */
@Component
public class BaiduMapUtils {


    @Resource
    private BaiduMapProperties baiduMapProperties;

    /**
     * 调用百度地图地理编码服务接口，根据地址获取坐标（经度、纬度）
     *
     * @param address
     * @return
     */
    public String getCoordinate(String address) {
        String httpUrl = "https://api.map.baidu.com/geocoding/v3/?address=" + address + "&output=json&ak=" + baiduMapProperties.getAk();
        String json = loadJSON(httpUrl);
        Map map = JSON.parseObject(json, Map.class);

        String status = map.get("status").toString();
        if (status.equals("0")) {
            //返回结果成功，能够正常解析地址信息
            Map result = (Map) map.get("result");
            Map location = (Map) result.get("location");
            String lng = location.get("lng").toString();
            String lat = location.get("lat").toString();

            DecimalFormat df = new DecimalFormat("#.######");
            String lngStr = df.format(Double.parseDouble(lng));
            String latStr = df.format(Double.parseDouble(lat));
            String r = latStr + "," + lngStr;
            return r;
        }

        return null;
    }

    /**
     * 调用百度地图驾车路线规划服务接口，根据寄件人地址和收件人地址坐标计算订单距离
     *
     * @param origin
     * @param destination
     * @return
     */
    public Double getDistance(String origin, String destination) {
        String httpUrl = "http://api.map.baidu.com/directionlite/v1/driving?origin="
                + origin + "&destination="
                + destination + "&ak=" + baiduMapProperties.getAk();

        String json = loadJSON(httpUrl);

        Map map = JSON.parseObject(json, Map.class);
        if ("0".equals(map.getOrDefault("status", "500").toString())) {
            Map childMap = (Map) map.get("result");
            JSONArray jsonArray = (JSONArray) childMap.get("routes");
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            double distance = Double.parseDouble(jsonObject.get("distance") == null ? "0" : jsonObject.get("distance").toString());
            return distance;
        }

        return null;
    }

    /**
     * 调用百度地图驾车路线规划服务接口，根据寄件人地址和收件人地址坐标计算订单距离
     *
     * @param origin
     * @param destination
     * @return
     */
    public Integer getTime(String origin, String destination) {
        String httpUrl = "http://api.map.baidu.com/directionlite/v1/driving?origin="
                + origin + "&destination="
                + destination + "&ak=" + baiduMapProperties.getAk();

        String json = loadJSON(httpUrl);

        Map map = JSON.parseObject(json, Map.class);
        if ("0".equals(map.getOrDefault("status", "500").toString())) {
            Map childMap = (Map) map.get("result");
            JSONArray jsonArray = (JSONArray) childMap.get("routes");
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            int time = Integer.parseInt(jsonObject.get("duration") == null ? "0" : jsonObject.get("duration").toString());
            return time;
        }

        return null;
    }

    /**
     * 调用服务接口，返回百度地图服务端的结果
     *
     * @param httpUrl
     * @return
     */
    public String loadJSON(String httpUrl) {
        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        System.out.println(json.toString());
        return json.toString();
    }
}

