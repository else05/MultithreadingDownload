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
    public static void main(String[] args) {

        int threadNum = 10 ;
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

            path = Paths.get("G:/new/video.mp4");

            block = len%threadNum == 0 ? len/threadNum : len/threadNum + 1 ;
            System.out.println("每个线程下载："+block);

            long start = 0L ;
            long end = 0L ;

//            start = 0 * block ;
//            end = start + (block - 1);
//            System.out.println("start:" + start + " --> end:" + end);
//            new App().down(path.toFile(), start, end, url);

            for (int i = 0; i < threadNum; i++) {
                start = i * block ;
                end = start + (block - 1);
                System.out.println("start:" + start + " --> end:" + end);
                new Thread(new DownloadThread(path.toFile(), start, end, url)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }

    public void down(File file , long start , long end , URL url) {
        System.out.println("\n ======" + Thread.currentThread().getName() + "======");
        HttpURLConnection conn = null;
        long len = 0L ;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range" , "bytes=" + start + "-" + end);
            len = conn.getContentLength();


            if(conn.getResponseCode() == 206 ){
                InputStream inputStream = conn.getInputStream();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(len);
                randomAccessFile.seek(start);

                byte[] bytes = new byte[1024];
                int lenTem = 0 ;
                while ((lenTem = inputStream.read(bytes)) != -1) {
                    randomAccessFile.write(bytes,0,lenTem);
                }
                randomAccessFile.close();
                inputStream.close();
            }
            System.out.println("线程" + Thread.currentThread().getName() + "下载完成 : " + start + "--->" + end);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }
}
