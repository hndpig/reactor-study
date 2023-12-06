package com.hnd.reactor.simple;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 基本创建
 * @author hnd
 * @date 2023/12/4 17:21
 * @version 1.0
 */
public class Create {
    public static void main(String[] args) {
        Flux<String> strFlux = Flux.just("a", "b", "c", "d");
        strFlux.subscribe(System.out::print);

        System.out.println();

        List<String> strings = Arrays.asList("e", "f", "g", "h");
        Flux<String>fromIterable = Flux.fromIterable(strings);
        fromIterable.subscribe(System.out::print);

        System.out.println();

        Flux<String> fromStream = Flux.fromStream(strings.stream());
        fromStream.subscribe(System.out::print);

        System.out.println();

        Flux<Integer> range = Flux.range(1, 5);
        range.subscribe(System.out::print);

        System.out.println();

        Mono<Object> empty = Mono.empty();

        empty.subscribe(System.out::print);

        System.out.println();

        Mono<String> mono = Mono.just("a");
        mono.subscribe(System.out::print);

    }
}
