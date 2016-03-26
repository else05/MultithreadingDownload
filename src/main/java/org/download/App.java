package org.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 */
public class App {
    public static long time = 0L ;
    public static void main(String[] args) {

        int threadNum = 30 ;
        long block = 0L ;
        System.out.println("Hello World!");
        URL url = null ;
        int len = 0 ;
        Path path = null ;
        try {
            url = new URL("http://dlsw.baidu.com/sw-search-sp/soft/3a/12350/QQ_8.1.17283.0_setup.1458109312.exe");
//            url = new URL("http://cv4.jikexueyuan.com/21961266f6cab8d6bef2d7ec17c086e0/201603222320/course/1801-1900/1847/video/5036_b_h264_sd_960_540.mp4");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            len = conn.getContentLength();
            conn.disconnect();

            path = Paths.get("G:/new/QQ.exe");

            new MultithreadingDownload().download(path.toFile() , len , threadNum , url);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }

}
