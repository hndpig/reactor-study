package com.hnd.reactor;

import reactor.core.publisher.Flux;

/**
 * @author hnd
 * @description: subscribe 方法示例
 * @date 2023/12/4 17:53
 */
public class Subscribe {
    public static void main(String[] args) {
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
