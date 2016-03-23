package org.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;

/**
 * Created by Else05 on 2016/3/22.
 */
public class MultithreadingDownload {
    public void download(File file , long len , int threadNum , URL url) {
        long start = 0L ;
        long end = 0L ;
        long block = len%threadNum == 0 ? len/threadNum : len/threadNum + 1 ;

        for (int i = 0; i < threadNum; i++) {
            start = i * block ;
            end = start + (block - 1);
            System.out.println("start:" + start + " --> end:" + end);
            new Thread(new DownloadThread(file, start, end, url)).start();
        }
    }
}
