package com.hnd.reactor.simple;

import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

/**
 * @author hnd
 * @description: subscribe 简单方法示例
 * @date 2023/12/4 17:53
 */
public class Subscribe {
    public static void main(String[] args) {
        Flux<Integer> intFlux = Flux.range(1, 10);
        intFlux.subscribe(i -> {
            System.out.println(i);//1,2
        }, e -> {
            System.out.println(e);//终止操作
        },()->{
            System.out.println("not execute ");
        }, subscription -> {
            subscription.request(5);//定义消费数量
        });
    }

    private static void errorAndCompleted() {
        Flux<Integer> intFlux = Flux.range(1, 3);
        intFlux.subscribe(i -> {
            if (i == 3) {
                throw new RuntimeException("got 3");//异常会终止操作
            }
            System.out.println(i);//1,2
        }, e -> {
            System.out.println(e);//终止操作
        },()->{
            System.out.println("not execute ");
        });
    }

    private static void subscribeAndCompleted() {
        Flux<Integer> intFlux = Flux.range(1, 3);
        intFlux.subscribe(System.out::println, Throwable::printStackTrace,()->{
            System.out.println("completed");//序列消费完完成时会执行，终止操作
        });
    }

    private static void subscribeAndError() {
        Flux<Integer> intFlux1 = Flux.range(1, 3);
        intFlux1.subscribe();
        Flux<Integer> intFlux2 = Flux.range(1, 3);
        intFlux2.subscribe(System.out::println);//1,2,3
        Flux<Integer> intFlux3 = Flux.range(1, 3);
        intFlux3.subscribe(i -> {
            if (i == 3) {
                throw new RuntimeException("got 3");//异常会终止操作
            }
            System.out.println(i);//1,2
        }, throwable -> {
            System.out.println(throwable);
        });
    }
}
