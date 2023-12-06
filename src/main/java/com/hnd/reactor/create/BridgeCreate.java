package com.hnd.reactor.create;

import com.hnd.reactor.listener.MyEventProcessor;
import com.hnd.reactor.listener.MyListener;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author hnd
 * @description: create 异步生产序列
 * @date 2023/12/6 10:50
 */
public class BridgeCreate {
    public static void main(String[] args) {
        // syncCreate();
        asyncBridgeCreate();
    }

    private static void syncCreate() {
        //同步？
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ints.add(i);
        }
        Flux.create(sink -> {
            System.out.println("Thread id:" + Thread.currentThread().threadId());
            ints.forEach(s -> sink.next(s));
        }).subscribe(s -> {
            System.out.println(" subscribe Thread id:" + Thread.currentThread().threadId() + " value=" + s);
        });
    }

    /*
     * @Author hnd
     * @Date 11:50 2023/12/6
     * 异步
        IGNORE： 完全忽略下游背压请求，这可能会在下游队列积满的时候导致 IllegalStateException。
        ERROR： 当下游跟不上节奏的时候发出一个 IllegalStateException 的错误信号。
        DROP：当下游没有准备好接收新的元素的时候抛弃这个元素。
        LATEST：让下游只得到上游最新的元素。
        BUFFER：（默认的）缓存所有下游没有来得及处理的元素（这个不限大小的缓存可能导致 OutOfMemoryError）
     **/
    private static void asyncBridgeCreate() {
        MyEventProcessor myEventProcessor = new MyEventProcessor();
        System.out.println("主线程：" + Thread.currentThread().threadId());
        Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> sink) {
                myEventProcessor.register(new MyListener<String>() {
                    @Override
                    public void onDataChunk(List<String> data) {
                        System.out.println("序列生产线程ID :" + Thread.currentThread().threadId());
                        data.forEach(s -> sink.next(s));
                       // processComplete();
                    }

                    @Override
                    public void processComplete() {
                        sink.complete();
                    }

                    @Override
                    public void processError(Exception e) {
                        sink.error(e);
                    }
                });

            }
        }).subscribe(new BaseSubscriber<String>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            @Override
            protected void hookOnNext(String value) {
                System.out.println("消费线程：" + Thread.currentThread().threadId() + "  " + value);
                request(1);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("消费完成");
            }
        });
        List<String> strs = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            strs.add(i + "");
            if (i > 9 && i % 10 == 0) {
                List<String> finalStrs = strs;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> collect = finalStrs.stream().map(s -> s + ":" + Thread.currentThread().getName()).collect(Collectors.toList());
                        myEventProcessor.publish(collect);
                    }
                }).start();
                strs = new ArrayList<>();
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
