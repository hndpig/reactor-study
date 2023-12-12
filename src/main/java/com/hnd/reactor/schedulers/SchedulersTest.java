package com.hnd.reactor.schedulers;

import org.w3c.dom.ls.LSOutput;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.sql.SQLOutput;
import java.time.Duration;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/8 17:11
 */
public class SchedulersTest {
    public static void main(String[] args) {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(1));
        interval.map(i->i)
                .subscribe(System.out::println);
        interval.blockLast();
    }

    private static void newElastic() {
        Scheduler hnd = Schedulers.newElastic("hnd", 2);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                hnd.schedule(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }).start();
        }
    }

    private static void parallel() {
        Scheduler parallel = Schedulers.parallel();//有个线程池
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                parallel.schedule(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }).start();
        }
    }

    private static void elastic() {
       
        Scheduler hnd = Schedulers.elastic();//弹性线程池，等待过长会废弃
//        Scheduler hnd = Schedulers.newElastic("hnd");
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                hnd.schedule(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }).start();
        }
    }

    private static void newSingle() {
        Scheduler myThread = Schedulers.newSingle("myThread");
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                myThread.schedule(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }).start();
        }
    }

    private static void immediate() throws InterruptedException {
        Scheduler immediate = Schedulers.immediate();
        for (int i = 0; i < 5; i++) {
            int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("***********************");
                    System.out.print(Thread.currentThread().getName());
                    immediate.schedule(() -> {//当前线程执行
                        System.out.print(Thread.currentThread().getName());
                    });
                    System.out.println("***********************");
                }
            }).start();
        }
        Thread.sleep(1000);
    }
}
