package org.zyl910.jdk8demo.thread.future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/** Java8新特性8--使用CompletableFuture构建异步应用. https://www.jianshu.com/p/4897ccdcb278
 *
 */
public class FutureCompleteAsyncDemo {
    private Random random = new Random();

    public static Integer cale(Integer para) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para * para;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    /**
     * 同步计算商品价格的方法
     *
     * @param product 商品名称
     * @return 价格
     */
    private double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
    /**
     * 模拟计算,查询数据库等耗时
     */
    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 异步计算商品的价格.
     *
     * @param product 商品名称
     * @return 价格
     */
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();
        return futurePrice;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (true) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> cale(50));
            System.out.println(future.get());
        }
        // 流式调用.
        if (true) {
            CompletableFuture<Void> future = CompletableFuture
                    .supplyAsync(() -> cale(50))
                    .thenApply(i -> Integer.toString(i))
                    .thenApply(str -> "\"" + str + "\"")
                    .thenAccept(System.out::println);
            future.get();
        }
        // CompletableFuture中的异常处理.
        if (true) {
            CompletableFuture<Void> future3 = CompletableFuture
                    .supplyAsync(() -> cale(50))
                    .exceptionally(ex -> {
                        System.out.println("ex.toString() = " + ex.toString());
                        return 0;
                    })
                    .thenApply(i -> Integer.toString(i))
                    .thenApply(str -> "\"" + str + "\"")
                    .thenAccept(System.out::println);
            future3.get();
        }
        // 组合多个CompletableFuture.
        if (true) {
            CompletableFuture<Void> future4 = CompletableFuture
                    .supplyAsync(() -> cale(50))
                    .thenCompose(i -> CompletableFuture
                            .supplyAsync(() -> cale(i)))
                    .thenApply(i -> Integer.toString(i))
                    .thenApply(str -> "\"" + str + "\"")
                    .thenAccept(System.out::println);
            future4.get();
        }
        //public <U,V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn)
        /*方法thenCombine()首先完成当前CompletableFuture和other的执行,
        接着,将这两者的执行结果传递给BiFunction(该接口接受两个参数,并有一个返回值),
        并返回代表BiFuntion实例的CompletableFuture对象:*/
        if (true) {
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> cale(50));
            CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> cale(25));
            CompletableFuture<Void> fu = future1.thenCombine(future2, (i, j) -> (i + j))
                    .thenApply(str -> "\"" + str + "\"")
                    .thenAccept(System.out::println);
            fu.get();
        }
        // 实现异步API.
        if (true) {
            // 使用异步API 模拟客户端.
            FutureCompleteAsyncDemo shop = new FutureCompleteAsyncDemo(); //new Shop("BestShop");
            long start = System.nanoTime();
            Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
            long incocationTime = (System.nanoTime() - start) / 1_000_000;
            System.out.println("执行时间:" + incocationTime + " msecs");
            try {
                Double price = futurePrice.get();
                System.out.printf("Price is %.2f%n", price);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            long retrievalTime = (System.nanoTime() - start) / 1_000_000;
            System.out.println("retrievalTime:" + retrievalTime + " msecs");
        }
    }
}
