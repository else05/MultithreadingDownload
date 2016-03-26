package org.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;

/**
 * 有返回值的线程，方便判断是否下载完成
 * Created by Else05 on 2016/3/26.
 */
public class DownloadCallable implements Callable<String> {
    /**
     * 保存的文件路径
     */
    private File file ;
    /**
     * 开始下载位置
     */
    private long start ;
    /**
     * 结束下载位置
     */
    private long end ;
    /**
     * 下载地址
     */
    private URL url ;
    /**
     * 该文件总长度
     */
    private long totalLength ;

    public DownloadCallable(File file, long totalLength, long start, long end, URL url) {
        this.file = file;
        this.start = start;
        this.end = end;
        this.url = url;
        this.totalLength = totalLength;
    }

    @Override
    public String call() throws Exception {
        HttpURLConnection conn = null;
        String returnSrc = null ;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range" , "bytes=" + start + "-" + end);

            // 状态码为206才支持多线程下载
            if (conn.getResponseCode() == 206) {
                InputStream inputStream = conn.getInputStream();

                ReadableByteChannel inChannel = Channels.newChannel(inputStream);

                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(totalLength);
                randomAccessFile.seek(start);
                FileChannel outChannel = randomAccessFile.getChannel();

                long writeCount = outChannel.transferFrom(inChannel, start, (end - start));

                MultithreadingDownload.threadPerformCount.getAndIncrement() ;

                returnSrc = "线程 " + Thread.currentThread().getName() + " 下载: " + start + "--->" + end + " 预计下载： " + (end - start) + ", 实际下载：" + writeCount ;
                inChannel.close();
                outChannel.close();
                randomAccessFile.close();
                inputStream.close();
            } else {
                returnSrc = "\n线程 " + Thread.currentThread().getName() + " 返回网络状态码（下载失败）：" +conn.getResponseCode() ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnSrc ;
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

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }
}
