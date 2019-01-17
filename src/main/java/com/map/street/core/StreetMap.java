package com.map.street.core;

import com.map.street.util.HttpUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author: hua
 * @date: 2019/1/17 16:44
 */
public class StreetMap {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
    private static final String REQUEST_URL = "https://maps.googleapis.com/maps/api/streetview";
    private static final String LOCAL_ADDRESS = "C:\\Users\\admin\\Desktop\\GPS\\GPS\\";
    private static final String KEY = "AIzaSyCnNtqbkGPTsqwD4PuS7O7sKA-QKj2Qbxs";
    private static final String READ_FILE_URL = LOCAL_ADDRESS + "paris.txt";

    public void readFileAndSaveMapPic(String readFileUrl) throws Exception {
        List<String> lines = FileUtils.readLines(new File(readFileUrl), "utf-8");
        lines.forEach(line -> {
            System.out.println(line);
            String[] arrs = line.split("_");
            if (arrs.length < 3) {
                throw new RuntimeException(line + ":该行数据格式有误(︶︹︺)");
            }
            String jin = arrs[0];
            String wei = arrs[1];
            String heading = arrs[2];
            System.out.println("经度：" + jin + " 纬度：" + wei + " 高度：" + heading);
            String writeFileUrl = LOCAL_ADDRESS + "paris_sub\\" + jin + "_" + wei + "_" + heading + ".jpg";
            String url = REQUEST_URL + "?size=200x200&location=" + jin + "," + wei + "&heading=" + heading + "&pitch=-004&key=" + KEY;
            System.out.println("requestUrl:" + url);
            try {
                this.saveMapPic(url, writeFileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("请求或保存有误(︶︹︺)");
            }
        });
    }

    public void saveMapPic(String url, String fileWritenUrl) throws Exception {
        // GET请求获取返回流
        InputStream inputStream = HttpUtils.getInputStreamByGet(url, USER_AGENT);
        // 将流数据保存到文件
        FileUtils.copyToFile(inputStream, new File(fileWritenUrl));
        // 关闭流
        inputStream.close();
        System.out.println("Pic Save!");
    }

    public static void main(String[] args) throws Exception {
        StreetMap streetMap = new StreetMap();
        streetMap.readFileAndSaveMapPic(READ_FILE_URL);
    }


}
