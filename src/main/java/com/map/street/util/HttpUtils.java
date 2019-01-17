package com.map.street.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;

/**
 * @author: hua
 * @date: 2019/1/17 16:48
 */
public class HttpUtils {

    /**
     * GET请求获取返回流
     *
     * @param url
     * @param userAgent
     * @return
     * @throws Exception
     */
    public static InputStream getInputStreamByGet(String url, String userAgent) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            //创建httpclient实例
            httpClient = HttpClients.createDefault();
            // 创建httpget实例
            HttpGet httpGet = new HttpGet(url);
            // 添加head信息
            if (StringUtils.isNotBlank(userAgent)) {
                httpGet.setHeader("User-Agent", userAgent);
            }
            //执行httpget请求
            response = httpClient.execute(httpGet);
            //获取返回状态(200 正常 400未找到页面 500服务器错误 403拒绝)
            System.out.println("status:" + response.getStatusLine());
            //返回实体
            return response.getEntity().getContent();
        } catch (Exception e) {
            throw new Exception("请求有误(︶︹︺)");
        }
    }

}
