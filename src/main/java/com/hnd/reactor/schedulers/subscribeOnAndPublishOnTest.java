package com.hnd.reactor.schedulers;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.sql.SQLOutput;
import java.util.concurrent.Executors;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/12 16:45
 */
public class subscribeOnAndPublishOnTest {
    public static void main(String[] args) throws InterruptedException {

        Flux.range(0, 1000).publishOn(Schedulers.parallel()).subscribeOn(Schedulers.parallel()).subscribe(e -> {
            System.out.println(Thread.currentThread().getName() + Thread.currentThread().threadId() + ":" + e);
        });
        Thread.sleep(20000);

        Flux.range(0, 1000).publishOn(Schedulers.parallel()).subscribe(e->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(Thread.currentThread().getName()+Thread.currentThread().threadId()+":"+e);
        });
       Thread.sleep(20000);
    }
}
