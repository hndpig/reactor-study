package com.hnd.reactor.generate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/6 9:53
 */
public class GenerateTest {
    public static void main(String[] args) {
        /*
         *这是一种同步地，逐个地产生值的方法
        */
        Flux.generate(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hnd";
            }
        }, new BiFunction<String, SynchronousSink<String>, String>() {
            @Override
            public String apply(String s, SynchronousSink<String> sink) {
                sink.next(s + "=" + s);//提供给消费者的值
                if (s.equals(s.toUpperCase())) {
                    sink.complete();//终止序列
                }
                return s.toUpperCase();//下回合的初始值
            }
        }, new Consumer<String>() {
            /*序列终止后会被执行
             *使用了数据库连接或者其他需要最终进行清理的资源，这个 Consumer lambda 可以用来在最后关闭连接或完成相关的其他清理任务
            **/
            @Override
            public void accept(String s) {
                System.out.println("Consumer:" + s);
            }
        }).subscribe(s -> {
            System.out.println(s);
        }, Throwable::printStackTrace, () -> {
            System.out.println("subscribe completed");
        });
    }
}
