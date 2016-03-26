package org.download;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Else05 on 2016/3/22.
 */
public class MultithreadingDownload {

    /**
     * 存放线程返回的值
     */
    private static List<Future<String>> futureList = new ArrayList<>();
    public static AtomicInteger threadPerformCount = new AtomicInteger(0);

    public void download(File file, long len, int threadNum, URL url) {
        long start = 0L;
        long end = 0L;
        long block = len % threadNum == 0 ? len / threadNum : len / threadNum + 1;

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        for (int i = 0; i < threadNum; i++) {
            start = i * block;
            end = start + (block - 1);
            System.out.println("start:" + start + " --> end:" + end);
            Future<String> future = fixedThreadPool.submit(new DownloadCallable(file, len, start, end, url));
            futureList.add(future);
        }
        while (true) {
            try {
                Thread.sleep(5000);
                if (MultithreadingDownload.threadPerformCount.get() >= 20) {
                    for (Future fu :
                            futureList) {
                        System.out.println(fu.get());
                    }
                    fixedThreadPool.shutdown();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
