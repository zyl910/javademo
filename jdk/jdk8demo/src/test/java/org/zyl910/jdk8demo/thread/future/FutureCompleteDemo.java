package org.zyl910.jdk8demo.thread.future;

import java.util.concurrent.CompletableFuture;

/** Java8新特性8--使用CompletableFuture构建异步应用. https://www.jianshu.com/p/4897ccdcb278
 *
 */
public class FutureCompleteDemo {

    public static void test1() throws Exception{
        CompletableFuture<String> completableFuture=new CompletableFuture();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //模拟执行耗时任务
                System.out.println("task doing...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //告诉completableFuture任务已经完成
                completableFuture.complete("result");
            }
        }).start();
        //获取任务结果，如果没有完成会一直阻塞等待
        String result=completableFuture.get();
        System.out.println("计算结果:"+result);
    }

    public static void main(String[] args) {
        try {
            test1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
