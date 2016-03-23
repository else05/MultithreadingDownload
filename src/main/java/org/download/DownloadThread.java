package org.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Else05 on 2016/3/22.
 */
public class DownloadThread implements Runnable {
    private File file ;
    private long start ;
    private long end ;
    private URL url ;

    public DownloadThread(File file, long start, long end, URL url) {
        this.start = start;
        this.file = file;
        this.end = end;
        this.url = url;
    }

    public void run() {
        System.out.println("\n ======" + Thread.currentThread().getName() + " boot======");
        HttpURLConnection conn = null;
        long len = 0L ;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range" , "bytes=" + start + "-" + end);
            len = conn.getContentLength();


            if (conn.getResponseCode() == 206) {
                long s = System.currentTimeMillis() ;
                InputStream inputStream = conn.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");

                randomAccessFile.setLength(len);
                randomAccessFile.seek(start);

                byte[] bytes = new byte[1024];
                int lenTem ;
                while ((lenTem = bufferedInputStream.read(bytes)) != -1) {
                    randomAccessFile.write(bytes, 0, lenTem);
                }
                long e = System.currentTimeMillis() ;
                System.out.println("====" + Thread.currentThread().getName() + "====>" + (e - s) );
                App.time += (e - s) ;
                System.out.println("\n" + App.time);
                bufferedInputStream.close();
                randomAccessFile.close();
                inputStream.close();
            } else {
                System.out.println("\nerror :"+conn.getResponseCode());
            }
            System.out.println("线程" + Thread.currentThread().getName() + "下载完成 : " + start + "--->" + end);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
