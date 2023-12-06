package com.hnd.reactor.simple;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * @author hnd
 * @description: 自定义BaseSubscriber
 * @date 2023/12/5 9:55
 */
public class BaseSubscriberTest {
    public static void main(String[] args) {
        testRequest();
    }
/**
 * @Author hnd
 * @Date 9:46 2023/12/6
 *       自定义BaseSubscriber
**/
    private static void extracted() {
        Flux<Integer> intFlux = Flux.range(1, 10);
        intFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                request(Long.MAX_VALUE);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(value);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("complete");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

    /*
 * @Author hnd
 * @Date 9:43 2023/12/6
 *    request 方法的测试
**/
    private static void testRequest() {
        Flux<Integer> intFlux = Flux.range(1, 10);
        BaseSubscriber baseSubscriber =  new BaseSubscriber<Integer>() {
             @Override
             protected void hookOnSubscribe(Subscription subscription) {
                 request(20);//应该是个指针作用，
             }

             @Override
             protected void hookOnNext(Integer value) {
                 System.out.println(value);
                 if (value == 1) {
                     request(2);
                 }

             }

             @Override
             protected void hookOnComplete() {
                 System.out.println("completed");
             }
         };
        intFlux.subscribe(baseSubscriber);
    }
}
