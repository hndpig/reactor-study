package com.hnd.reactor.handler;

import reactor.core.publisher.Flux;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/8 17:00
 */
public class HandlerTest {
    public static void main(String[] args) {
        /*
         *map+filter的功能类似
        **/
        Flux.range(1, 50).handle((i, sink) -> {
            if (i > 20) {
                sink.next(i);
            }
        }).subscribe(System.out::println);
    }
}
